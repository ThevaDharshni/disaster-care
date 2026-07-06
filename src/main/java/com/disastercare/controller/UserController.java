package com.disastercare.controller;

import com.disastercare.model.*;
import com.disastercare.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired private UserService userService;
    @Autowired private ShelterService shelterService;
    @Autowired private DonationService donationService;
    @Autowired private MissingPersonService missingPersonService;

    // --- LOGIN ---
    @GetMapping("/login")
    public String loginPage() { return "user/login"; }

    @PostMapping("/login-process")
    public String processLogin(@RequestParam String name,
                                @RequestParam String password,
                                HttpSession session, Model model) {
        Optional<User> userOpt = userService.login(name, password);
        if (userOpt.isPresent()) {
            session.setAttribute("userId", userOpt.get().getUserId());
            session.setAttribute("userName", userOpt.get().getName());
            session.setAttribute("role", "USER");
            return "redirect:/user/map";
        }
        // Auto-register if new user
        User newUser = userService.register(name, password);
        session.setAttribute("userId", newUser.getUserId());
        session.setAttribute("userName", newUser.getName());
        session.setAttribute("role", "USER");
        session.setAttribute("newUserId", newUser.getUserId());
        return "redirect:/user/map";
    }

    // --- MAP ---
    @GetMapping("/map")
    public String mapPage(HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) return "redirect:/user/login";
        model.addAttribute("userId", session.getAttribute("userId"));
        model.addAttribute("userName", session.getAttribute("userName"));
        if (session.getAttribute("newUserId") != null) {
            model.addAttribute("newUserId", session.getAttribute("newUserId"));
            session.removeAttribute("newUserId");
        }
        return "user/map";
    }

    // --- SHELTERS BY DISTRICT ---
    @GetMapping("/shelters/{district}")
    public String sheltersByDistrict(@PathVariable String district, HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) return "redirect:/user/login";
        List<Shelter> shelters = shelterService.getSheltersByDistrict(district);
        model.addAttribute("shelters", shelters);
        model.addAttribute("district", district);
        model.addAttribute("userId", session.getAttribute("userId"));
        return "user/shelters";
    }

    // --- REGISTER FORM ---
    @GetMapping("/register/{shelterId}")
    public String registerForm(@PathVariable Long shelterId, HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) return "redirect:/user/login";
        Optional<Shelter> shelter = shelterService.getShelterById(shelterId);
        if (shelter.isEmpty() || shelter.get().getStatus() == Shelter.Status.FULL)
            return "redirect:/user/map";
        model.addAttribute("shelter", shelter.get());
        model.addAttribute("userId", session.getAttribute("userId"));
        return "user/register-form";
    }

    @PostMapping("/register/{shelterId}")
    public String processRegistration(@PathVariable Long shelterId,
                                       @RequestParam String name,
                                       @RequestParam int age,
                                       @RequestParam String gender,
                                       @RequestParam String bloodGroup,
                                       @RequestParam String identificationMark,
                                       HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) return "redirect:/user/login";
        shelterService.register(userId, shelterId, name, age,
                ShelterRegistration.Gender.valueOf(gender.toUpperCase()),
                bloodGroup, identificationMark);
        return "redirect:/user/shelters/" + shelterService.getShelterById(shelterId)
                .map(Shelter::getDistrict).orElse("Chennai") + "?registered=true";
    }

    // --- DONATION ---
    @GetMapping("/donation")
    public String donationPage(HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) return "redirect:/user/login";
        return "user/donation";
    }

    @PostMapping("/donation")
    public String submitDonation(@RequestParam String donorName,
                                  @RequestParam String itemType,
                                  @RequestParam String quantity,
                                  @RequestParam String address,
                                  @RequestParam String contactNumber,
                                  Model model) {
        Donation d = new Donation();
        d.setDonorName(donorName);
        d.setItemType(Donation.ItemType.valueOf(itemType.toUpperCase()));
        d.setQuantity(quantity);
        d.setAddress(address);
        d.setContactNumber(contactNumber);
        donationService.save(d);
        model.addAttribute("success", true);
        return "user/donation";
    }

    // --- MISSING PERSON ---
    @GetMapping("/missing-person")
    public String missingPersonPage(HttpSession session) {
        if (session.getAttribute("userId") == null) return "redirect:/user/login";
        return "user/missing-person";
    }

    @PostMapping("/missing-person")
    public String submitMissingPerson(@RequestParam String name,
                                       @RequestParam int age,
                                       @RequestParam String gender,
                                       @RequestParam String bloodGroup,
                                       @RequestParam String identificationMark,
                                       @RequestParam String lastSeenLocation,
                                       @RequestParam(required = false) MultipartFile photo,
                                       Model model) throws IOException {
        MissingPerson mp = new MissingPerson();
        mp.setName(name);
        mp.setAge(age);
        mp.setGender(MissingPerson.Gender.valueOf(gender.toUpperCase()));
        mp.setBloodGroup(bloodGroup);
        mp.setIdentificationMark(identificationMark);
        mp.setLastSeenLocation(lastSeenLocation);
        missingPersonService.report(mp, photo);
        model.addAttribute("success", true);
        return "user/missing-person";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}

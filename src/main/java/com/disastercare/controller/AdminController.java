package com.disastercare.controller;

import com.disastercare.model.*;
import com.disastercare.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private AdminService adminService;
    @Autowired private ShelterService shelterService;
    @Autowired private DonationService donationService;
    @Autowired private MissingPersonService missingPersonService;
    @Autowired private UserService userService;

    private boolean isAdmin(HttpSession session) {
        return "ADMIN".equals(session.getAttribute("role"));
    }

    // --- LOGIN ---
    @GetMapping("/login")
    public String loginPage() { return "admin/login"; }

    @PostMapping("/login-process")
    public String processLogin(@RequestParam String email,
                                @RequestParam String password,
                                HttpSession session, Model model) {
        Optional<Admin> adminOpt = adminService.login(email, password);
        if (adminOpt.isPresent()) {
            session.setAttribute("adminId", adminOpt.get().getAdminId());
            session.setAttribute("adminName", adminOpt.get().getName());
            session.setAttribute("role", "ADMIN");
            return "redirect:/admin/dashboard";
        }
        model.addAttribute("error", "Invalid credentials. Access denied.");
        return "admin/login";
    }

    // --- REGISTER ---
    @GetMapping("/register")
    public String registerPage() { return "admin/register"; }

    @PostMapping("/register")
    public String processRegister(@RequestParam String adminId,
                                   @RequestParam String name,
                                   @RequestParam String email,
                                   @RequestParam String password,
                                   Model model) {
        if (adminService.emailExists(email)) {
            model.addAttribute("error", "Email already registered.");
            return "admin/register";
        }
        adminService.register(adminId, name, email, password);
        model.addAttribute("success", "Account created! Please login.");
        return "admin/login";
    }

    // --- DASHBOARD ---
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/admin/login";
        model.addAttribute("totalShelters", shelterService.countTotal());
        model.addAttribute("activeShelters", shelterService.countActive());
        model.addAttribute("fullShelters", shelterService.countFull());
        model.addAttribute("totalUsers", userService.getTotalUsers());
        model.addAttribute("totalDonations", donationService.countTotal());
        model.addAttribute("missingPersons", missingPersonService.countByStatus(MissingPerson.MissingStatus.MISSING));
        model.addAttribute("adminName", session.getAttribute("adminName"));
        return "admin/dashboard";
    }

    // --- MANAGE SHELTERS ---
    @GetMapping("/shelters")
    public String manageShelters(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/admin/login";
        model.addAttribute("shelters", shelterService.getAllShelters());
        return "admin/shelters";
    }

    @PostMapping("/shelters/add")
    public String addShelter(@RequestParam String name, @RequestParam String location,
                              @RequestParam String district, @RequestParam int capacity,
                              HttpSession session) {
        if (!isAdmin(session)) return "redirect:/admin/login";
        Shelter s = new Shelter();
        s.setName(name); s.setLocation(location);
        s.setDistrict(district); s.setCapacity(capacity);
        shelterService.saveShelter(s);
        return "redirect:/admin/shelters?added=true";
    }

    @PostMapping("/shelters/delete/{id}")
    public String deleteShelter(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/admin/login";
        shelterService.deleteShelter(id);
        return "redirect:/admin/shelters";
    }

    @PostMapping("/shelters/update/{id}")
    public String updateShelter(@PathVariable Long id,
                                 @RequestParam String status, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/admin/login";
        shelterService.getShelterById(id).ifPresent(s -> {
            s.setStatus(Shelter.Status.valueOf(status.toUpperCase()));
            shelterService.saveShelter(s);
        });
        return "redirect:/admin/shelters";
    }

    // --- DONATIONS ---
    @GetMapping("/donations")
    public String donations(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/admin/login";
        model.addAttribute("donations", donationService.getAll());
        return "admin/donations";
    }

    @PostMapping("/donations/{id}/status")
    public String updateDonation(@PathVariable Long id, @RequestParam String status,
                                  HttpSession session) {
        if (!isAdmin(session)) return "redirect:/admin/login";
        donationService.updateStatus(id, Donation.DonationStatus.valueOf(status.toUpperCase()));
        return "redirect:/admin/donations";
    }

    // --- MISSING PERSONS ---
    @GetMapping("/missing")
    public String missingPersons(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/admin/login";
        model.addAttribute("missingPersons", missingPersonService.getAllMissing());
        return "admin/missing";
    }

    @GetMapping("/missing/{id}/match")
    public String matchPerson(@PathVariable Long id, HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/admin/login";
        List<Map<String, Object>> matches = missingPersonService.findMatches(id);
        model.addAttribute("matches", matches);
        model.addAttribute("personId", id);
        return "admin/match-results";
    }

    // --- REPORTS ---
    @GetMapping("/reports")
    public String reports(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/admin/login";
        model.addAttribute("totalShelters", shelterService.countTotal());
        model.addAttribute("activeShelters", shelterService.countActive());
        model.addAttribute("fullShelters", shelterService.countFull());
        model.addAttribute("totalUsers", userService.getTotalUsers());
        model.addAttribute("totalDonations", donationService.countTotal());
        model.addAttribute("pendingDonations", donationService.countPending());
        model.addAttribute("missingPersons", missingPersonService.countByStatus(MissingPerson.MissingStatus.MISSING));
        model.addAttribute("foundPersons", missingPersonService.countByStatus(MissingPerson.MissingStatus.FOUND));
        return "admin/reports";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}

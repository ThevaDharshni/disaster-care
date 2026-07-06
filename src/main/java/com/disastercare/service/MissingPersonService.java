package com.disastercare.service;

import com.disastercare.model.MissingPerson;
import com.disastercare.model.ShelterRegistration;
import com.disastercare.repository.MissingPersonRepository;
import com.disastercare.repository.ShelterRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@Service
public class MissingPersonService {

    @Autowired private MissingPersonRepository missingPersonRepository;
    @Autowired private ShelterRegistrationRepository registrationRepository;

    public MissingPerson report(MissingPerson mp, MultipartFile photo) throws IOException {
        if (photo != null && !photo.isEmpty()) {
            String uploadDir = "uploads/missing/";
            Files.createDirectories(Paths.get(uploadDir));
            String filename = System.currentTimeMillis() + "_" + photo.getOriginalFilename();
            Path path = Paths.get(uploadDir + filename);
            Files.write(path, photo.getBytes());
            mp.setPhotoPath(filename);
        }
        return missingPersonRepository.save(mp);
    }

    public List<MissingPerson> getAllMissing() { return missingPersonRepository.findAll(); }

    public long countByStatus(MissingPerson.MissingStatus status) {
        return missingPersonRepository.countByStatus(status);
    }

    // ===== AI MATCHING LOGIC =====
    public List<Map<String, Object>> findMatches(Long missingPersonId) {
        Optional<MissingPerson> mpOpt = missingPersonRepository.findById(missingPersonId);
        if (mpOpt.isEmpty()) return Collections.emptyList();
        MissingPerson mp = mpOpt.get();

        List<ShelterRegistration> allRegistrations = registrationRepository.findAll();
        List<Map<String, Object>> results = new ArrayList<>();

        for (ShelterRegistration reg : allRegistrations) {
            int score = 0;
            int maxScore = 100;
            List<String> matchDetails = new ArrayList<>();

            // Name match (40 points) — Levenshtein-based similarity
            double nameSim = nameSimilarity(mp.getName(), reg.getName());
            int nameScore = (int)(nameSim * 40);
            score += nameScore;
            if (nameSim > 0.7) matchDetails.add("Name match (" + (int)(nameSim*100) + "%)");

            // Age match (20 points) — within 5 years
            int ageDiff = Math.abs(mp.getAge() - reg.getAge());
            if (ageDiff == 0) { score += 20; matchDetails.add("Exact age match"); }
            else if (ageDiff <= 2) { score += 15; matchDetails.add("Near age match (±" + ageDiff + ")"); }
            else if (ageDiff <= 5) { score += 8; matchDetails.add("Age within 5 years"); }

            // Blood group match (20 points)
            if (mp.getBloodGroup() != null && mp.getBloodGroup().equalsIgnoreCase(reg.getBloodGroup())) {
                score += 20;
                matchDetails.add("Blood group match (" + mp.getBloodGroup() + ")");
            }

            // Identification mark (20 points) — keyword overlap
            if (mp.getIdentificationMark() != null && reg.getIdentificationMark() != null) {
                double markSim = keywordSimilarity(mp.getIdentificationMark(), reg.getIdentificationMark());
                int markScore = (int)(markSim * 20);
                score += markScore;
                if (markSim > 0.3) matchDetails.add("Identification mark overlap");
            }

            if (score >= 30) {
                Map<String, Object> result = new HashMap<>();
                result.put("registration", reg);
                result.put("shelter", reg.getShelter());
                result.put("accuracy", Math.min(score, 100));
                result.put("confidence", score >= 80 ? "HIGH" : score >= 50 ? "MEDIUM" : "LOW");
                result.put("matchDetails", matchDetails);
                results.add(result);
            }
        }

        results.sort((a, b) -> (int)b.get("accuracy") - (int)(int)a.get("accuracy"));
        return results.subList(0, Math.min(5, results.size()));
    }

    private double nameSimilarity(String a, String b) {
        if (a == null || b == null) return 0;
        a = a.toLowerCase().trim(); b = b.toLowerCase().trim();
        if (a.equals(b)) return 1.0;
        int dist = levenshtein(a, b);
        int maxLen = Math.max(a.length(), b.length());
        return maxLen == 0 ? 1.0 : 1.0 - (double) dist / maxLen;
    }

    private int levenshtein(String a, String b) {
        int[][] dp = new int[a.length()+1][b.length()+1];
        for (int i = 0; i <= a.length(); i++) dp[i][0] = i;
        for (int j = 0; j <= b.length(); j++) dp[0][j] = j;
        for (int i = 1; i <= a.length(); i++)
            for (int j = 1; j <= b.length(); j++)
                dp[i][j] = a.charAt(i-1) == b.charAt(j-1) ? dp[i-1][j-1]
                    : 1 + Math.min(dp[i-1][j-1], Math.min(dp[i-1][j], dp[i][j-1]));
        return dp[a.length()][b.length()];
    }

    private double keywordSimilarity(String a, String b) {
        Set<String> setA = new HashSet<>(Arrays.asList(a.toLowerCase().split("\\s+")));
        Set<String> setB = new HashSet<>(Arrays.asList(b.toLowerCase().split("\\s+")));
        Set<String> intersection = new HashSet<>(setA);
        intersection.retainAll(setB);
        Set<String> union = new HashSet<>(setA);
        union.addAll(setB);
        return union.isEmpty() ? 0 : (double) intersection.size() / union.size();
    }
}

package com.saesubam.dao;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saesubam.model.Profiles;
import com.saesubam.repositories.ProfileRepository;
import com.saesubam.service.ProfileService;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;

    @Autowired
    public ProfileServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public List<Profiles> getAllProfiles() {
        List<Profiles> all = profileRepository.findAll();
        List<Profiles> filtered = new ArrayList<>();
        for (Profiles p : all) {
            if (p.getUser() == null || (!"ADMIN".equalsIgnoreCase(p.getUser().getRole()) && p.getUser().isActive())) {
                filtered.add(p);
            }
        }
        filtered.sort((p1, p2) -> Long.compare(
            p2.getId() != null ? p2.getId() : 0L,
            p1.getId() != null ? p1.getId() : 0L
        ));
        return filtered;
    }

    @Override
    public Profiles getProfileById(Long id) {
        return profileRepository.findById(id).orElse(null);
    }

    @Override
    public Profiles getProfileByUserId(Long userId) {
        return profileRepository.findByUserId(userId).orElse(null);
    }

    @Override
    @Transactional
    public Profiles createProfile(Profiles profile) {
        return profileRepository.save(profile);
    }

    @Override
    @Transactional
    public Profiles updateProfile(Profiles profile) {
        return profileRepository.save(profile);
    }

    @Override
    @Transactional
    public void deleteProfile(Long id) {
        profileRepository.deleteById(id);
    }

    @Override
    public List<Profiles> searchProfiles(String gender, Integer minAge, Integer maxAge, String religion, String caste, String education, String city, String maritalStatus) {
        Specification<Profiles> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Exclude ADMIN role users from matrimony search results
            predicates.add(cb.or(
                cb.isNull(root.get("user")),
                cb.notEqual(cb.upper(root.get("user").get("role")), "ADMIN")
            ));

            if (gender != null && !gender.trim().isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("gender")), gender.toLowerCase().trim()));
            }

            if (minAge != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("age"), minAge));
            }

            if (maxAge != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("age"), maxAge));
            }

            if (religion != null && !religion.trim().isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("religion")), religion.toLowerCase().trim()));
            }

            if (caste != null && !caste.trim().isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("caste")), caste.toLowerCase().trim()));
            }

            if (education != null && !education.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("education")), "%" + education.toLowerCase().trim() + "%"));
            }

            if (city != null && !city.trim().isEmpty()) {
                String[] cities = city.split(",");
                List<Predicate> cityPredicates = new ArrayList<>();
                for (String c : cities) {
                    String trimmed = c.trim().toLowerCase();
                    if (!trimmed.isEmpty()) {
                        cityPredicates.add(cb.like(cb.lower(root.get("city")), "%" + trimmed + "%"));
                        cityPredicates.add(cb.like(cb.lower(root.get("nativePlace")), "%" + trimmed + "%"));
                    }
                }
                if (!cityPredicates.isEmpty()) {
                    predicates.add(cb.or(cityPredicates.toArray(new Predicate[0])));
                }
            }

            if (maritalStatus != null && !maritalStatus.trim().isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("maritalStatus")), maritalStatus.toLowerCase().trim()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        List<Profiles> results = profileRepository.findAll(spec);
        List<Profiles> filtered = new ArrayList<>();
        for (Profiles p : results) {
            if (p.getUser() == null || (!"ADMIN".equalsIgnoreCase(p.getUser().getRole()) && p.getUser().isActive())) {
                filtered.add(p);
            }
        }
        filtered.sort((p1, p2) -> Long.compare(
            p2.getId() != null ? p2.getId() : 0L,
            p1.getId() != null ? p1.getId() : 0L
        ));
        return filtered;
    }

    @Override
    public List<Profiles> getRecommendedMatches(Profiles userProfile) {
        if (userProfile == null) {
            return getAllProfiles();
        }

        String targetGender = "Female".equalsIgnoreCase(userProfile.getGender()) ? "Male" : "Female";
        
        List<Profiles> oppositeGenderProfiles = profileRepository.findByGender(targetGender);
        List<Profiles> sourceList = oppositeGenderProfiles.isEmpty() ? profileRepository.findAll() : oppositeGenderProfiles;
        List<Profiles> filtered = new ArrayList<>();
        for (Profiles p : sourceList) {
            if (p.getUser() == null || (!"ADMIN".equalsIgnoreCase(p.getUser().getRole()) && p.getUser().isActive())) {
                filtered.add(p);
            }
        }
        filtered.sort((p1, p2) -> Long.compare(
            p2.getId() != null ? p2.getId() : 0L,
            p1.getId() != null ? p1.getId() : 0L
        ));
        return filtered;
    }
}

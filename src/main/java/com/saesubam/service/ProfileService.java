package com.saesubam.service;

import java.util.List;
import com.saesubam.model.Profiles;

public interface ProfileService {

    List<Profiles> getAllProfiles();

    Profiles getProfileById(Long id);

    Profiles getProfileByUserId(Long userId);

    Profiles createProfile(Profiles profile);

    Profiles updateProfile(Profiles profile);

    void deleteProfile(Long id);

    List<Profiles> searchProfiles(String gender, Integer minAge, Integer maxAge, String religion, String caste, String education, String city, String maritalStatus);

    List<Profiles> getRecommendedMatches(Profiles userProfile);
}
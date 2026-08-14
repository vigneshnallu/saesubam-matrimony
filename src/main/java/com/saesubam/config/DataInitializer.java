/*
 * 
 */
package com.saesubam.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.saesubam.model.MembershipType;
import com.saesubam.model.Profiles;
import com.saesubam.model.Users;
import com.saesubam.repositories.ProfileRepository;
import com.saesubam.repositories.UserRepository;

/**
 * The Class DataInitializer.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    /** The user repository. */
    private final UserRepository userRepository;

    /** The profile repository. */
    private final ProfileRepository profileRepository;

    /**
     * Instantiates a new data initializer.
     *
     * @param userRepository the user repository
     * @param profileRepository the profile repository
     */
    @Autowired
    public DataInitializer(UserRepository userRepository, ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    /**
     * Run.
     *
     * @param args the args
     * @throws Exception the exception
     */
    @Override
    public void run(String... args) throws Exception {
        if (profileRepository.count() > 0) {
            return; // Seed data already exists
        }

        System.out.println("🌱 Initializing realistic matrimony seed profiles...");

        // Seed Users & Profiles
        createSeedUserAndProfile("Priya Sundaram", "priya@gmail.com", "Female", "Brahmin", "9876543210", "Self", 26,
            "5 ft 4 in / 163 cm", "Never Married", "Tamil", "Hindu", "Iyer", "Vadhula", "Uttiratadi", "No",
            "M.Tech Computer Science", "Private Sector", "Senior Software Engineer", "₹18 - ₹22 Lakhs", "Bengaluru",
            "Karnataka", "Chennai", "Upper Middle Class", "Nuclear Family", "Moderate",
            "Passionate software engineer with a love for music, travel, and classic South Indian cuisine. Looking for an understanding partner who values family and ambition.",
            "Seeking an educated, warm-hearted professional with strong values.",
            "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=600&auto=format&fit=crop",
            MembershipType.GOLD);

        createSeedUserAndProfile("Karthik Raja", "karthik@gmail.com", "Male", "Vanniyar", "9876543211", "Self", 29,
            "5 ft 10 in / 178 cm", "Never Married", "Tamil", "Hindu", "Vanniyar", "Gautama", "Rohini", "No",
            "B.E. Mechanical Engineering", "Private Sector", "Senior Business Analyst", "₹15 - ₹18 Lakhs", "Chennai",
            "Tamil Nadu", "Salem", "Upper Middle Class", "Joint Family", "Traditional",
            "Enthusiastic analyst working at a global consultancy. Enjoy fitness, badminton, and weekend drives. Looking for a caring life partner.",
            "Looking for a well-educated, respectful partner with open communication.",
            "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=600&auto=format&fit=crop",
            MembershipType.PREMIUM);

        createSeedUserAndProfile("Divya Ramachandran", "divya@gmail.com", "Female", "Mudaliyar", "9876543212",
            "Daughter", 25, "5 ft 5 in / 165 cm", "Never Married", "Tamil", "Hindu", "Mudaliyar", "Vishwamitra",
            "Krittika", "No", "MBBS, MD General Medicine", "Government / PSU", "Assistant Doctor", "₹12 - ₹15 Lakhs",
            "Coimbatore", "Tamil Nadu", "Madurai", "Upper Middle Class", "Nuclear Family", "Traditional",
            "Dedicated doctor working at a leading super-specialty hospital. Passionate about healthcare, reading, and classical dance.",
            "Seeking a professional groom with good character and mutual respect.",
            "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=600&auto=format&fit=crop",
            MembershipType.PLATINUM);

        createSeedUserAndProfile("Anand Viswanathan", "anand@gmail.com", "Male", "Brahmin", "9876543213", "Son", 30,
            "6 ft 0 in / 183 cm", "Never Married", "Tamil", "Hindu", "Iyengar", "Bharadwaja", "Hastham", "No",
            "MBA Finance (IIM Bangalore)", "Private Sector", "Investment Banker", "₹30 - ₹35 Lakhs", "Mumbai",
            "Maharashtra", "Trichy", "Rich / Affluent", "Nuclear Family", "Liberal",
            "Finance professional based in Mumbai. Fond of photography, culinary exploration, and international travel.",
            "Looking for an independent, cultured woman who shares mutual respect and career aspirations.",
            "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=600&auto=format&fit=crop",
            MembershipType.PLATINUM);

        createSeedUserAndProfile("Kavitha Nair", "kavitha@gmail.com", "Female", "Nair", "9876543214", "Sister", 27,
            "5 ft 6 in / 168 cm", "Never Married", "Malayalam", "Hindu", "Nair", "Kashyapa", "Anizham", "No",
            "M.Sc Biotechnology", "Private Sector", "Research Scientist", "₹14 - ₹16 Lakhs", "Hyderabad", "Telangana",
            "Kochi", "Upper Middle Class", "Nuclear Family", "Moderate",
            "Biotech scientist engaged in pharma research. Love nature walks, reading literature, and spending quality time with family.",
            "Seeking an educated, down-to-earth partner with good family background.",
            "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=600&auto=format&fit=crop",
            MembershipType.GOLD);

        createSeedUserAndProfile("Siddharth Menon", "siddharth@gmail.com", "Male", "Chettiar", "9876543215", "Self", 28,
            "5 ft 9 in / 175 cm", "Never Married", "Tamil", "Hindu", "Chettiar", "Agastya", "Aswini", "No",
            "B.Tech Computer Science", "Business / Self Employed", "Tech Entrepreneur", "₹25 - ₹30 Lakhs", "Bengaluru",
            "Karnataka", "Karaikudi", "Upper Middle Class", "Nuclear Family", "Moderate",
            "Founder of a growing software SaaS startup. Energetic, optimistic, and deeply rooted in family ethics.",
            "Looking for an ambitious, warm, and supportive life companion.",
            "https://images.unsplash.com/photo-1492562080023-ab3db95bfbce?w=600&auto=format&fit=crop",
            MembershipType.PREMIUM);

        createSeedUserAndProfile("Meenakshi Sundaram", "meenakshi@gmail.com", "Female", "Pillai", "9876543216", "Self",
            26, "5 ft 3 in / 160 cm", "Never Married", "Tamil", "Hindu", "Pillai", "Harita", "Magham", "No",
            "B.Arch Architecture", "Private Sector", "Architect & Interior Designer", "₹10 - ₹12 Lakhs", "Chennai",
            "Tamil Nadu", "Tirunelveli", "Middle Class", "Joint Family", "Traditional",
            "Creative architect working with top real estate developers. Passionate about design, sketching, and traditional arts.",
            "Seeking a caring, respectful partner with traditional values.",
            "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=600&auto=format&fit=crop",
            MembershipType.FREE);

        createSeedUserAndProfile("Vikramaditya Rao", "vikram@gmail.com", "Male", "Kamma", "9876543217", "Brother", 31,
            "5 ft 11 in / 180 cm", "Never Married", "Telugu", "Hindu", "Kamma", "Kaushika", "Swati", "No",
            "MS Data Science (USA)", "Private Sector", "Lead Data Scientist", "₹28 - ₹32 Lakhs", "Hyderabad",
            "Telangana", "Vijayawada", "Upper Middle Class", "Nuclear Family", "Moderate",
            "Data scientist working in AI tech. Enjoy chess, blogging, and long drives. Valuing honesty and genuine connections.",
            "Seeking a progressive, well-mannered bride from a good family background.",
            "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=600&auto=format&fit=crop",
            MembershipType.GOLD);

        createSeedUserAndProfile("Subash Pandian", "vignesh@gmail.com", "Male", "Devar", "9876543218", "Self", 28,
            "5 ft 11 in / 180 cm", "Never Married", "Tamil", "Hindu", "Maravar", "Siva", "Rohini", "No",
            "B.E. Computer Science", "Private Sector", "Software Engineering Lead", "₹22 - ₹25 Lakhs", "Madurai",
            "Tamil Nadu", "Madurai", "Upper Middle Class", "Joint Family", "Traditional",
            "Software lead working at a top MNC. Respectful towards traditional values and family culture. Enjoy sports and music.",
            "Looking for a cultured, well-educated bride from Devar community.",
            "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=600&auto=format&fit=crop",
            MembershipType.PLATINUM);

        createSeedUserAndProfile("Abirami Maravar", "abirami@gmail.com", "Female", "Devar", "9876543219", "Daughter",
            25, "5 ft 5 in / 165 cm", "Never Married", "Tamil", "Hindu", "Kallar", "Agastya", "Mirugaseerisham", "No",
            "B.Tech Civil Engineering", "Government / PSU", "Assistant Executive Engineer", "₹14 - ₹16 Lakhs", "Trichy",
            "Tamil Nadu", "Thanjavur", "Upper Middle Class", "Nuclear Family", "Traditional",
            "Assistant engineer in government sector. Values family traditions, spirituality, and modern education.",
            "Seeking an educated Devar groom with good family ethics.",
            "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=600&auto=format&fit=crop",
            MembershipType.GOLD);

        System.out.println("✅ Seed matrimony profiles initialized successfully!");
    }

    /**
     * Creates the seed user and profile.
     *
     * @param name the name
     * @param email the email
     * @param gender the gender
     * @param caste the caste
     * @param mobile the mobile
     * @param profileFor the profile for
     * @param age the age
     * @param height the height
     * @param maritalStatus the marital status
     * @param motherTongue the mother tongue
     * @param religion the religion
     * @param subCaste the sub caste
     * @param gothram the gothram
     * @param starRasi the star rasi
     * @param dosham the dosham
     * @param education the education
     * @param employedIn the employed in
     * @param occupation the occupation
     * @param annualIncome the annual income
     * @param city the city
     * @param state the state
     * @param nativePlace the native place
     * @param familyStatus the family status
     * @param familyType the family type
     * @param familyValues the family values
     * @param aboutMe the about me
     * @param partnerPreferences the partner preferences
     * @param photoUrl the photo url
     * @param membershipType the membership type
     */
    private void createSeedUserAndProfile(String name, String email, String gender, String caste, String mobile,
        String profileFor, int age, String height, String maritalStatus, String motherTongue, String religion,
        String subCaste, String gothram, String starRasi, String dosham, String education, String employedIn,
        String occupation, String annualIncome, String city, String state, String nativePlace, String familyStatus,
        String familyType, String familyValues, String aboutMe, String partnerPreferences, String photoUrl,
        MembershipType membershipType) {
        Users user = new Users();
        user.setName(name);
        user.setEmail(email);
        user.setGender(gender);
        user.setCaste(caste);
        user.setMobile(mobile);
        user.setProfileFor(profileFor);
        user.setPassword("password123");
        user.setMembershipType(membershipType);
        user.setMobileVerified(true);
        user.setEmailVerified(true);

        Users savedUser = userRepository.save(user);

        Profiles profile = new Profiles();
        profile.setUser(savedUser);
        profile.setFullName(name);
        profile.setGender(gender);
        profile.setAge(age);
        profile.setHeight(height);
        profile.setMaritalStatus(maritalStatus);
        profile.setMotherTongue(motherTongue);
        profile.setReligion(religion);
        profile.setCaste(caste);
        profile.setSubCaste(subCaste);
        profile.setGothram(gothram);
        profile.setStarRasi(starRasi);
        profile.setDosham(dosham);
        profile.setEducation(education);
        profile.setEmployedIn(employedIn);
        profile.setOccupation(occupation);
        profile.setAnnualIncome(annualIncome);
        profile.setCity(city);
        profile.setState(state);
        profile.setNativePlace(nativePlace);
        profile.setFamilyStatus(familyStatus);
        profile.setFamilyType(familyType);
        profile.setFamilyValues(familyValues);
        profile.setAboutMe(aboutMe);
        profile.setPartnerPreferences(partnerPreferences);
        profile.setContactMobile(mobile);
        profile.setContactPerson(profileFor);
        profile.setPhotoUrl(photoUrl);
        profile.setVerified(true);
        profile.setProfileCompleteness(90);

        profileRepository.save(profile);
        savedUser.setProfile(profile);
        userRepository.save(savedUser);
    }
}

const translations = {
    "en": {
        "dashboard": "Dashboard",
        "browse_profiles": "Browse Profiles",
        "match_interests": "Match Interests",
        "shortlisted_profiles": "Shortlisted Profiles",
        "my_profile": "My Profile",
        "membership_plan": "Membership Plan",
        "contact_us": "Contact Us",
        "logout": "Logout",
        "view_full_profile": "View Full Profile",
        "express_interest": "Express Interest",
        "interest_sent": "Interest Sent",
        "mutual_match": "Mutual Match",
        "verified": "Verified",
        "contact_details": "Contact Details",
        "jathagam_chart": "Horoscope / Jathagam Chart",
        "locked_notice": "Contact and Jathagam details are locked until mutual interest is accepted.",
        "upgrade_plan": "Upgrade Plan",
        "choose_package": "Choose Your Matrimony Package",
        "free_basic": "Free Basic",
        "gold_premium": "Gold Premium",
        "platinum_vip": "Platinum VIP",
        "unlimited_views": "Unlimited Profile Views",
        "views_remaining": "views remaining",
        "age": "Age",
        "height": "Height",
        "religion": "Religion",
        "caste": "Caste",
        "sub_caste": "Sub-Caste",
        "education": "Education",
        "occupation": "Occupation",
        "income": "Annual Income",
        "city": "City",
        "state": "State",
        "marital_status": "Marital Status",
        "star_rasi": "Star & Rasi",
        "gothram": "Gothram",
        "dosham": "Dosham",
        "about_me": "About Candidate",
        "partner_preference": "Partner Expectations",
        "family_details": "Family Background",
        "search_profiles": "Search Matrimony Profiles",
        "filter": "Filter Matches",
        "all_religions": "All Religions",
        "all_castes": "All Castes",
        "select_gender": "Select Gender",
        "male": "Male (ஆண்)",
        "female": "Female (பெண்)"
    },
    "ta": {
        "dashboard": "முகப்பு",
        "browse_profiles": "வரன்கள் / சுயவிவரங்கள்",
        "match_interests": "விருப்பங்கள்",
        "shortlisted_profiles": "விருப்பப்பட்டியல்கள்",
        "my_profile": "என் சுயவிவரம்",
        "membership_plan": "உறுப்பினர் திட்டம்",
        "contact_us": "தொடர்பு கொள்ள",
        "logout": "வெளியேறு",
        "view_full_profile": "முழு விவரம் பார்க்க",
        "express_interest": "விருப்பம் தெரிவிக்க",
        "interest_sent": "விருப்பம் அனுப்பப்பட்டது",
        "mutual_match": "பரஸ்பர பொருத்தம்",
        "verified": "சரிபார்க்கப்பட்டது",
        "contact_details": "தொடர்பு விவரங்கள்",
        "jathagam_chart": "ஜாதகக் கட்டம்",
        "locked_notice": "இருவீட்டு விருப்பம் உறுதி செய்யப்பட்ட பிறகே தொலைபேசி எண் மற்றும் ஜாதகக் கட்டம் திறக்கப்படும்.",
        "upgrade_plan": "சிறப்புத் திட்டங்கள்",
        "choose_package": "உங்களுக்கு ஏற்ற திட்டத்தை தேர்ந்தெடுக்கவும்",
        "free_basic": "இலவச திட்டம்",
        "gold_premium": "கோல்டு திட்டம்",
        "platinum_vip": "பிளாட்டினம் VIP",
        "unlimited_views": "வரம்பற்ற சுயவிவரப் பார்வை",
        "views_remaining": "பார்வைகள் பாக்கி",
        "age": "வயது",
        "height": "உயரம்",
        "religion": "மதம்",
        "caste": "ஜாதி",
        "sub_caste": "உட்பிரிவு",
        "education": "கல்வித் தகுதி",
        "occupation": "தொழில் / வேலை",
        "income": "ஆண்டு வருமானம்",
        "city": "நகரம்",
        "state": "மாநிலம்",
        "marital_status": "திருமண நிலை",
        "star_rasi": "நட்சத்திரம் & ராசி",
        "gothram": "கோத்திரம்",
        "dosham": "தோஷம்",
        "about_me": "சுயவிவரப் குறிப்பு",
        "partner_preference": "எதிர்பார்க்கும் வரன் விவரம்",
        "family_details": "குடும்ப விவரங்கள்",
        "search_profiles": "வரன்களைத் தேடுக",
        "filter": "தேடல் வடிகட்டி",
        "all_religions": "அனைத்து மதங்கள்",
        "all_castes": "அனைத்து ஜாதிகள்",
        "select_gender": "பாலினம் தேர்வு செய்ய",
        "male": "ஆண் வரன்கள்",
        "female": "பெண் வரன்கள்"
    }
};

// Precise text mapping engine for inline translation
const exactTextMap = {
    "Dashboard": "முகப்பு",
    "Browse Profiles": "வரன்கள் / சுயவிவரங்கள்",
    "Match Interests": "விருப்பங்கள்",
    "Shortlisted Profiles": "விருப்பப்பட்டியல்கள்",
    "My Profile": "என் சுயவிவரம்",
    "Membership Plan": "உறுப்பினர் திட்டம்",
    "Logout": "வெளியேறு",
    "View Full Profile": "முழு விவரம் பார்க்க",
    "Express Interest": "விருப்பம் தெரிவிக்க",
    "Interest Sent": "விருப்பம் அனுப்பப்பட்டது",
    "Mutual Interest Accepted": "பரஸ்பர விருப்பம் ஏற்றுக்கொள்ளப்பட்டது",
    "Verified": "சரிபார்க்கப்பட்டது",
    "Contact Details": "தொடர்பு விவரங்கள்",
    "Horoscope Chart": "ஜாதகக் கட்டம்",
    "Unlimited Profile Views": "வரம்பற்ற சுயவிவரப் பார்வை",
    "views remaining": "பார்வைகள் மீதம்",
    "Never Married": "முதல் திருமணம்",
    "Age:": "வயது:",
    "Height:": "உயரம்:",
    "Religion:": "மதம்:",
    "Caste:": "ஜாதி:",
    "Education:": "கல்வித் தகுதி:",
    "Occupation:": "தொழில் / வேலை:",
    "Location:": "இடம்:",
    "Annual Income:": "ஆண்டு வருமானம்:"
};

function getLanguagePreference() {
    return localStorage.getItem("saesubam_lang") || "en";
}

function setLanguagePreference(lang) {
    localStorage.setItem("saesubam_lang", lang);
    applyLanguage(lang);
}

function applyLanguage(lang) {
    let currentLang = lang || getLanguagePreference();
    
    // Update data-i18n elements
    document.querySelectorAll("[data-i18n]").forEach(el => {
        let key = el.getAttribute("data-i18n");
        if (translations[currentLang] && translations[currentLang][key]) {
            if (el.tagName === "INPUT" || el.tagName === "TEXTAREA") {
                el.placeholder = translations[currentLang][key];
            } else {
                el.textContent = translations[currentLang][key];
            }
        }
    });

    // Update exact matching text content if switching to Tamil
    if (currentLang === "ta") {
        document.querySelectorAll("span, a, button, h1, h2, h3, h4, h5, h6, label").forEach(el => {
            if (el.children.length === 0) { // Only direct text nodes
                let txt = el.textContent.trim();
                if (exactTextMap[txt]) {
                    el.setAttribute("data-orig-txt", txt);
                    el.textContent = exactTextMap[txt];
                }
            }
        });
    } else {
        // Restore English text
        document.querySelectorAll("[data-orig-txt]").forEach(el => {
            el.textContent = el.getAttribute("data-orig-txt");
            el.removeAttribute("data-orig-txt");
        });
    }

    // Update active UI toggle button states
    document.querySelectorAll(".lang-btn-en").forEach(btn => {
        if (currentLang === "en") {
            btn.className = "btn btn-sm rounded-pill px-3 fw-bold lang-btn-en border-0 bg-danger text-white shadow-sm";
        } else {
            btn.className = "btn btn-sm rounded-pill px-3 fw-bold lang-btn-en border-0 text-secondary bg-transparent";
        }
    });
    document.querySelectorAll(".lang-btn-ta").forEach(btn => {
        if (currentLang === "ta") {
            btn.className = "btn btn-sm rounded-pill px-3 fw-bold lang-btn-ta border-0 bg-danger text-white shadow-sm";
        } else {
            btn.className = "btn btn-sm rounded-pill px-3 fw-bold lang-btn-ta border-0 text-secondary bg-transparent";
        }
    });
}

document.addEventListener("DOMContentLoaded", function() {
    applyLanguage(getLanguagePreference());
});

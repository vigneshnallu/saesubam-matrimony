const rasiNatchathiramData = {
    "Mesham": [
        "Aswini (அஸ்வினி)",
        "Bharani (பரணி)",
        "Krittika / Karthigai - Pada 1 (கார்த்திகை - பாதம் 1)"
    ],
    "Rishabham": [
        "Krittika / Karthigai - Pada 2,3,4 (கார்த்திகை - பாதம் 2,3,4)",
        "Rohini (ரோகிணி)",
        "Mrigasira / Mirugaseerisham - Pada 1,2 (மிருகசீரிஷம் - பாதம் 1,2)"
    ],
    "Mithunam": [
        "Mrigasira / Mirugaseerisham - Pada 3,4 (மிருகசீரிஷம் - பாதம் 3,4)",
        "Thiruvathirai / Arudra (திருவாதிரை)",
        "Punarpoosam / Punarvasu - Pada 1,2,3 (புனர்பூசம் - பாதம் 1,2,3)"
    ],
    "Katakham": [
        "Punarpoosam / Punarvasu - Pada 4 (புனர்பூசம் - பாதம் 4)",
        "Poosam / Pushya (பூசம்)",
        "Ayilyam / Ashlesha (ஆயில்யம்)"
    ],
    "Simham": [
        "Magam / Makha (மகம்)",
        "Pooram / Poorva Phalguni (பூரம்)",
        "Uthiram / Uttara Phalguni - Pada 1 (உத்திரம் - பாதம் 1)"
    ],
    "Kanni": [
        "Uthiram / Uttara Phalguni - Pada 2,3,4 (உத்திரம் - பாதம் 2,3,4)",
        "Hastham / Hasta (ஹஸ்தம்)",
        "Chithirai / Chitra - Pada 1,2 (சித்திரை - பாதம் 1,2)"
    ],
    "Thulaam": [
        "Chithirai / Chitra - Pada 3,4 (சித்திரை - பாதம் 3,4)",
        "Swathi (சுவாதி)",
        "Visakam / Vishakha - Pada 1,2,3 (விசாகம் - பாதம் 1,2,3)"
    ],
    "Vrischikam": [
        "Visakam / Vishakha - Pada 4 (விசாகம் - பாதம் 4)",
        "Anusham / Anuradha (அனுஷம்)",
        "Kettai / Jyeshta (கேட்டை)"
    ],
    "Dhanusu": [
        "Moolam / Moola (மூலம்)",
        "Pooradam / Poorvashada (பூராடம்)",
        "Uthiradam / Uttarashada - Pada 1 (உத்திராடம் - பாதம் 1)"
    ],
    "Makaram": [
        "Uthiradam / Uttarashada - Pada 2,3,4 (உத்திராடம் - பாதம் 2,3,4)",
        "Thiruvonam / Shravana (திருவோணம்)",
        "Avittam / Dhanishta - Pada 1,2 (அவிட்டம் - பாதம் 1,2)"
    ],
    "Kumbham": [
        "Avittam / Dhanishta - Pada 3,4 (அவிட்டம் - பாதம் 3,4)",
        "Sathayam / Shatabhisha (சதயம்)",
        "Poorattathi / Poorvabhadra - Pada 1,2,3 (பூரட்டாதி - பாதம் 1,2,3)"
    ],
    "Meenam": [
        "Poorattathi / Poorvabhadra - Pada 4 (பூரட்டாதி - பாதம் 4)",
        "Uthirattathi / Uttarabhadra (உத்திரட்டாதி)",
        "Revathi (ரேவதி)"
    ]
};

function onRasiChange(rasiSelectId, natchathiramSelectId, currentNatchathiramValue) {
    let rasiSelect = document.getElementById(rasiSelectId);
    let natchathiramSelect = document.getElementById(natchathiramSelectId);
    if (!rasiSelect || !natchathiramSelect) return;

    let selectedRasi = rasiSelect.value;
    natchathiramSelect.innerHTML = '<option value="">-- Select Natchathiram (நட்சத்திரம்) --</option>';

    if (selectedRasi && rasiNatchathiramData[selectedRasi]) {
        let stars = rasiNatchathiramData[selectedRasi];
        stars.forEach(function(star) {
            let opt = document.createElement("option");
            opt.value = star;
            opt.textContent = star;
            if (currentNatchathiramValue && (currentNatchathiramValue === star || star.indexOf(currentNatchathiramValue) > -1)) {
                opt.selected = true;
            }
            natchathiramSelect.appendChild(opt);
        });
    } else {
        // If no Rasi selected, show all 27 stars
        Object.keys(rasiNatchathiramData).forEach(function(rKey) {
            rasiNatchathiramData[rKey].forEach(function(star) {
                let opt = document.createElement("option");
                opt.value = star;
                opt.textContent = star + " [" + rKey + "]";
                if (currentNatchathiramValue && currentNatchathiramValue === star) {
                    opt.selected = true;
                }
                natchathiramSelect.appendChild(opt);
            });
        });
    }
}

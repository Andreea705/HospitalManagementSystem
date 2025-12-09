package com.example.hospital.service;

import com.example.hospital.model.Hospital;
import com.example.hospital.repository.HospitalRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HospitalService {

    private final HospitalRepository hospitalRepository;

    @Autowired
    public HospitalService(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    public Hospital createHospital(Hospital hospital) {
        validateHospitalUniqueness(hospital, null); // Überprüfen beim Erstellen
        return hospitalRepository.save(hospital);
    }

    // ... (getAllHospitals, getHospitalById, deleteHospital, countHospitals sind unverändert)

    public List<Hospital> getAllHospitals() {
        return hospitalRepository.findAll();
    }

    public Hospital getHospitalById(Long id) {
        return hospitalRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Hospital not found with id: " + id)
        );
    }

    public Hospital updateHospital(Long id, Hospital updatedHospital) {
        Hospital existingHospital = getHospitalById(id);

        // Prüft die Uniqueness der Kombination aus Name/City und schließt das aktuelle Objekt aus
        validateHospitalUniqueness(updatedHospital, id);

        existingHospital.setName(updatedHospital.getName());
        existingHospital.setCity(updatedHospital.getCity());

        return hospitalRepository.save(existingHospital);
    }

    public void deleteHospital(Long id) {
        Hospital hospital = getHospitalById(id);
        hospitalRepository.delete(hospital);
    }

    // ...

    /**
     * Führt die Business-Validierung durch: Prüft auf Eindeutigkeit der Kombination (Name, City).
     * @param hospital Die zu validierende Hospital-Entität.
     * @param excludeId Die ID der Entität, die bei der Uniqueness-Prüfung ausgeschlossen werden soll (bei Updates).
     */
    private void validateHospitalUniqueness(Hospital hospital, Long excludeId) {
        if (hospital.getName() == null || hospital.getName().trim().isEmpty() ||
                hospital.getCity() == null || hospital.getCity().trim().isEmpty()) {
            // Die @NotBlank JPA-Annotationen sollten diese Fehler abfangen,
            // aber wir prüfen hier, um NullPointer zu vermeiden.
            return;
        }

        Hospital existingHospital = hospitalRepository.findByNameAndCity(
                hospital.getName(), hospital.getCity());

        if (existingHospital != null) {
            // Kombination (Name, City) existiert bereits

            // Bei Update: Prüfen, ob es sich um dieselbe Entität handelt, die gerade aktualisiert wird
            if (excludeId != null && existingHospital.getId().equals(excludeId)) {
                return;
            }


            throw new RuntimeException("A hospital named '" + hospital.getName() +
                    "' already exists in the city of '" + hospital.getCity() + "'.");
        }
    }
}
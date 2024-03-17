package com.patient.registration.services;

import com.patient.registration.mapper.PatientMapper;
import com.patient.registration.openapi.model.PatientDTO;
import com.patient.registration.repository.PatientRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public PatientService(final PatientRepository patientRepository,
                          final PatientMapper patientMapper) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
    }

    public Page<PatientDTO> getPatientByDate(String registrationDate,
                                             Integer limit,
                                             Integer offset) {
        return Optional.ofNullable(patientRepository.findByDate(registrationDate, limit, offset))
                .map(patients -> patients.map(patientMapper::mapToPatientDto))
                .orElse(null);
    }

    public PatientDTO insertPatient(PatientDTO patientDTO) {
        return Optional.of(patientDTO)
                .map(patientMapper::mapToPatient)
                .map(patientRepository::insertPatient)
                .map(patientMapper::mapToPatientDto)
                .orElse(null);
    }

    public void deletePatient(String patientId) {
        patientRepository.deletePatient(patientId);
    }
}

package com.patient.registration.mapper;

import com.patient.registration.document.Patient;
import com.patient.registration.openapi.model.PatientDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class PatientMapper {

    public Patient mapToPatient(PatientDTO patientDto) {
        var formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return Patient.builder()
                .firstName(patientDto.getFirstName())
                .lastName(patientDto.getLastName())
                .address(patientDto.getAddress())
                .dateOfBirth(patientDto.getDateOfBirth())
                .gender(patientDto.getGender())
                .zipCode(patientDto.getZipCode())
                .phone(patientDto.getPhone())
                .registrationDate(LocalDate.now().format(formatter))
                .build();
    }

    public PatientDTO mapToPatientDto(Patient patient) {
        var patientDTO = new PatientDTO();
        patientDTO.setFirstName(patient.getFirstName());
        patientDTO.setLastName(patient.getLastName());
        patientDTO.setDateOfBirth(patient.getDateOfBirth());
        patientDTO.setGender(patient.getGender());
        patientDTO.setAddress(patient.getAddress());
        patientDTO.setZipCode(patient.getZipCode());
        patientDTO.setPhone(patient.getPhone());
        return patientDTO;
    }
}

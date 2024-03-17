package com.patient.registration.repository;

import com.patient.registration.document.Patient;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PatientRepository {

    Page<Patient> findByDate(String registrationDate,
                             Integer limit,
                             Integer offset);

    Patient insertPatient(Patient patient);

    void deletePatient(String patientId);

    void deleteAll();

    List<Patient> findAll();
}

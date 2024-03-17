package com.patient.registration.repository;

import com.mongodb.client.result.DeleteResult;
import com.patient.registration.document.Patient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Component
public class PatientRepositoryImpl implements PatientRepository {

    private final MongoTemplate mongoTemplate;

    public PatientRepositoryImpl(final MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Page<Patient> findByDate(String registrationDate,
                                    Integer limit,
                                    Integer offset) {
        final var pageable = PageRequest.of(limit, offset);
        final var query = new Query(
                Criteria.where("registrationDate").is(registrationDate))
                .with(pageable);
        var patients = mongoTemplate.find(query, Patient.class);
        log.info("Successfully found {} patients", patients.size());
        return PageableExecutionUtils.getPage(patients, pageable,
                () -> mongoTemplate.count(Query.of(query).skip(-1).limit(-1), Patient.class));
    }

    @Override
    public Patient insertPatient(Patient patient) {
        var savedPatient = mongoTemplate.save(patient);
        log.info("Successfully inserted patient");
        return savedPatient;
    }

    @Override
    public void deletePatient(String patientId) {
        final var query = new Query(Criteria.where("$id").is(patientId));
        mongoTemplate.remove(query, Patient.class);
        log.info("Successfully deleted patient by id: {}", patientId);
    }

    @Override
    public void deleteAll() {
        mongoTemplate.remove(new Query(), Patient.class);
        log.info("Successfully deleted all patients ");
    }

    @Override
    public List<Patient> findAll() {
        var patients = mongoTemplate.findAll(Patient.class);
        log.info("Successfully found {} patients", patients.size());
        return patients;
    }
}

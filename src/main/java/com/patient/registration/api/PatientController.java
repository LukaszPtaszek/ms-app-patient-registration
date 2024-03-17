package com.patient.registration.api;

import com.patient.registration.openapi.api.PatientApi;
import com.patient.registration.openapi.model.PatientDTO;
import com.patient.registration.services.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static com.patient.registration.utils.Constants.X_RESULT_COUNT;
import static com.patient.registration.utils.Constants.X_TOTAL_COUNT;
import static java.lang.String.valueOf;

@Slf4j
@RestController
public class PatientController implements PatientApi {

    private final PatientService patientService;

    public PatientController(final PatientService patientService) {
        this.patientService = patientService;
    }

    @Override
    public ResponseEntity<List<PatientDTO>> getPatientsByDate(String date,
                                                              Integer offset,
                                                              Integer limit) {
        log.info("Trying to find patients by date: {}", date);
        return Optional.ofNullable(patientService.getPatientByDate(date, offset, limit))
                .filter(patientDTOS -> patientDTOS.getTotalElements() > 0)
                .map(patientDTOS -> ResponseEntity.ok()
                        .headers(addCountHeaders(patientDTOS.getTotalElements(), patientDTOS.getSize()))
                        .body(patientDTOS.getContent()))
                .orElse(ResponseEntity.notFound()
                        .build());
    }

    @Override
    public ResponseEntity<PatientDTO> insertPatient(PatientDTO patientDTO) {
        log.info("Trying to insert patient with firstName: {} and lastName: {}",
                patientDTO.getFirstName(),
                patientDTO.getLastName());
        return Optional.ofNullable(patientService.insertPatient(patientDTO))
                .map(patientDto -> new ResponseEntity<>(patientDto, HttpStatus.CREATED))
                .orElse(ResponseEntity.badRequest()
                        .build());
    }

    @Override
    public ResponseEntity<Void> deletePatientsById(String patientId) {
        log.info("Trying to delete patient by id: {}", patientId);
        patientService.deletePatient(patientId);
        return ResponseEntity
                .accepted()
                .build();
    }

    private HttpHeaders addCountHeaders(long totalElements, int listSize) {
        var headers = new HttpHeaders();
        headers.set(X_TOTAL_COUNT, valueOf(totalElements));
        headers.set(X_RESULT_COUNT, valueOf(listSize));
        return headers;
    }
}

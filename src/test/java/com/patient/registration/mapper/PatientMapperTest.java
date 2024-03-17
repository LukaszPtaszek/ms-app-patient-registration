package com.patient.registration.mapper;

import com.patient.registration.document.Patient;
import com.patient.registration.openapi.model.PatientDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Objects;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PatientMapperTest {

    PatientMapper patientMapper = new PatientMapper();

    @ParameterizedTest()
    @MethodSource("patientData")
    void shouldMapToPatientDto(Patient patient,
                               String expectedFirstName) {

        //when then
        assertThat(patientMapper.mapToPatientDto(patient))
                .extracting(
                        PatientDTO::getFirstName,
                        PatientDTO::getLastName,
                        PatientDTO::getAddress,
                        PatientDTO::getGender,
                        PatientDTO::getDateOfBirth,
                        PatientDTO::getPhone,
                        PatientDTO::getZipCode)
                .containsExactly(expectedFirstName,
                        "Test",
                        "25 Star Lane, Boston",
                        "Male",
                        "2020-10-10",
                        508181818,
                        "02108");
    }

    @ParameterizedTest()
    @MethodSource("patientDtoData")
    void shouldMapToPatient(PatientDTO patientDTO,
                            String expectedFirstName) {

        //when then
        assertThat(patientMapper.mapToPatient(patientDTO))
                .extracting(
                        Patient::getFirstName,
                        Patient::getLastName,
                        Patient::getAddress,
                        Patient::getGender,
                        Patient::getPatientId,
                        Patient::getDateOfBirth,
                        Patient::getPhone,
                        Patient::getZipCode)
                .containsExactly(expectedFirstName,
                        "Test",
                        "25 Star Lane, Boston",
                        "Male",
                        null,
                        "2020-10-10",
                        508181818,
                        "02108");
    }

    private static Stream<Arguments> patientData() {
        return Stream.of(
                Arguments.of(getPatient("Lukasz"), "Lukasz"),
                Arguments.of(getPatient("Adam"), "Adam")
        );
    }

    private static Patient getPatient(String firstName) {
        return Patient.builder()
                .patientId("1")
                .firstName(firstName)
                .lastName("Test")
                .dateOfBirth("2020-10-10")
                .gender("Male")
                .address("25 Star Lane, Boston")
                .zipCode("02108")
                .phone(508181818)
                .registrationDate("20240310")
                .build();
    }

    private static Stream<Arguments> patientDtoData() {
        return Stream.of(
                Arguments.of(getPatientDto("Marek"), "Marek"),
                Arguments.of(getPatientDto("Tomek"), "Tomek")
        );
    }

    private static PatientDTO getPatientDto(String firstName) {
        var patientDTO = new PatientDTO();
        patientDTO.setFirstName(firstName);
        patientDTO.setLastName("Test");
        patientDTO.setDateOfBirth("2020-10-10");
        patientDTO.setGender("Male");
        patientDTO.setAddress("25 Star Lane, Boston");
        patientDTO.setZipCode("02108");
        patientDTO.setPhone(508181818);
        return patientDTO;
    }
}
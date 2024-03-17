package com.patient.registration.services;

import com.patient.registration.document.Patient;
import com.patient.registration.mapper.PatientMapper;
import com.patient.registration.openapi.model.PatientDTO;
import com.patient.registration.repository.PatientRepository;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientMapper patientMapper;

    @InjectMocks
    private PatientService patientService;

    @Test
    void shouldGetPatientByDate() {

        //given
        var patient = getPatient();
        var patientDto = getPatientDto();
        var patients = List.of(patient);

        when(patientRepository.findByDate("20241010", 5 , 0))
                .thenReturn(new PageImpl<>(patients));
        when(patientMapper.mapToPatientDto(patient))
                .thenReturn(patientDto);

        //when
        var result = patientService.getPatientByDate("20241010", 5 , 0);

        //then
        assertThat(result)
                .isNotNull();

        assertThat(result.getTotalElements())
                .isEqualTo(1);

        assertThat(result.getContent())
                .hasSize(1)
                .hasOnlyElementsOfType(PatientDTO.class)
                .extracting(PatientDTO::getFirstName, PatientDTO::getLastName)
                .containsExactly(Tuple.tuple("Lukasz", "Test"));

        verify(patientRepository, times(1))
                .findByDate("20241010", 5 , 0);
        verify(patientMapper, times(1))
                .mapToPatientDto(patient);
    }

    @Test
    void shouldInsertPatient() {

        //given
        var patient = getPatient();
        var patientDto = getPatientDto();

        when(patientRepository.insertPatient(patient))
                .thenReturn(patient);
        when(patientMapper.mapToPatient(patientDto))
                .thenReturn(patient);
        when(patientMapper.mapToPatientDto(patient))
                .thenReturn(patientDto);

        //when
        var result = patientService.insertPatient(patientDto);

        //then
        assertThat(result)
                .isNotNull()
                .extracting(PatientDTO::getFirstName, PatientDTO::getLastName)
                .containsExactly("Lukasz", "Test");

        verify(patientRepository, times(1))
                .insertPatient(patient);
        verify(patientMapper, times(1))
                .mapToPatientDto(patient);
        verify(patientMapper, times(1))
                .mapToPatient(patientDto);
    }

    // TODO
    @Test
    void shouldDeletePatient() {
    }

    private Patient getPatient() {
        return Patient.builder()
                .firstName("Lukasz")
                .lastName("Test")
                .dateOfBirth("2020-10-10")
                .gender("Male")
                .address("25 Star Lane, Boston")
                .zipCode("02108")
                .phone(508181818)
                .registrationDate("20240310")
                .build();
    }

    private PatientDTO getPatientDto() {
        var patientDTO = new PatientDTO();
        patientDTO.setFirstName("Lukasz");
        patientDTO.setLastName("Test");
        patientDTO.setDateOfBirth("2020-10-10");
        patientDTO.setGender("Male");
        patientDTO.setAddress("25 Star Lane, Boston");
        patientDTO.setZipCode("02108");
        patientDTO.setPhone(508181818);

        return patientDTO;
    }
}
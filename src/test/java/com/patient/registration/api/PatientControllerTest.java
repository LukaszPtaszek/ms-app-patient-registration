package com.patient.registration.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patient.registration.document.Patient;
import com.patient.registration.openapi.model.PatientDTO;
import com.patient.registration.repository.PatientRepositoryImpl;
import com.patient.registration.services.PatientService;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Testcontainers
@AutoConfigureMockMvc
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientRepositoryImpl patientRepository;

    @Container
    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:latest"));

    @DynamicPropertySource
    static void setMongoDBProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    void tearDown() {
        patientRepository.deleteAll();
    }

    @Epic("Web interface")
    @Feature("Insert Patient into Database")
    @Story("shouldInsertPatient")
    @Test
    void shouldInsertPatient() throws Exception {

        //given

        var requestBodyAsJson = getPatientAsJsonRequestBody();

        //when then
        mockMvc.perform(post("/patient/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyAsJson))
                .andExpect(status().isCreated());

        var patients = patientRepository.findAll();

        assertThat(patients)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(patients.get(0))
                .isNotNull()
                .extracting(Patient::getFirstName, Patient::getLastName)
                .containsExactly("Lukasz", "Test");


    }

    @Epic("Web interface")
    @Feature("Get Patient from Database")
    @Story("shouldGetPatient")
    @SneakyThrows
    @Test
    void shouldGetPatient() {

        //given
        var patient = getPatient();
        patientRepository.insertPatient(patient);

        //when then
        mockMvc.perform(get("/patient/registration/20240310")
                .queryParam("offset", "0")
                .queryParam("limit", "5"))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    private String getPatientAsJsonRequestBody() {
        var patientDTO = new PatientDTO();
        patientDTO.setFirstName("Lukasz");
        patientDTO.setLastName("Test");
        patientDTO.setDateOfBirth("2020-10-10");
        patientDTO.setGender("Male");
        patientDTO.setAddress("25 Star Lane, Boston");
        patientDTO.setZipCode("02108");
        patientDTO.setPhone(508181818);

        return mapper.writeValueAsString(patientDTO);
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
}
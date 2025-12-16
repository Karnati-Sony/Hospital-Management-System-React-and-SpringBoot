package com.example.demo.patient;

import static org.assertj.core.api.Assertions.assertThat;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.demo.entity.Patient;
import com.example.demo.entity.enums.City;
import com.example.demo.service.PatientService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PatientControllerTest {

    @Autowired
    private PatientService patientService;

    private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void testGetAllPatientWithStatusOne() throws URISyntaxException {
        String baseUrl = "http://localhost:8185/api/patient";
        ResponseEntity<Object> result = restTemplate.getForEntity(baseUrl, Object.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void savePatientV1() {
        Patient p1 = new Patient("zipato", "zazula", new Date(), "Male",
                City.CHENNAI, "zipato@gmail.com", "9876543210", 1);

        String addURI = "http://localhost:8185/api/patient";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Patient> entity = new HttpEntity<>(p1, headers);

        ResponseEntity<Patient> response =
                restTemplate.postForEntity(addURI, entity, Patient.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void savePatientV2() {
        Patient p1 = new Patient("kamara", "tamara", new Date(), "Female",
                City.CHENNAI, "kamara.tamara@mynet.com", "9876543211", 1);

        Patient p2 = patientService.save(p1);
        assertThat(p2.getPatientid()).isNotNull();
    }
}

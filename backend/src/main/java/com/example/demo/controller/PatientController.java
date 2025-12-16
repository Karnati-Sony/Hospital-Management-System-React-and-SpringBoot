package com.example.demo.controller;

import com.example.demo.dto.PatientDto;
import com.example.demo.dto.PatientSingleDto;
import com.example.demo.entity.Patient;
import com.example.demo.entity.enums.City;
import com.example.demo.service.PatientService;
import com.example.demo.util.ApiPaths;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(ApiPaths.PatientCtrl.CTRL) // e.g. "/api/patients"
public class PatientController {

    private final PatientService patientService;

    // ✅ Constructor injection (better than field @Autowired)
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    // 🔹 Get all active patients (status = 1)
    @GetMapping
    public ResponseEntity<List<PatientDto>> getAll() throws Exception {
        return ResponseEntity.ok(patientService.findAll());
    }

    // 🔹 Get single patient by ID
    @GetMapping("/find-by-id/{patientid}")
    public ResponseEntity<PatientSingleDto> getPatientByPatientid(
            @PathVariable Long patientid) throws Exception {
        return ResponseEntity.ok(patientService.findByPatientId(patientid));
    }

    // 🔹 Get patient by email
    @GetMapping("/find-by-email/{email}")
    public ResponseEntity<Patient> getPatientByEmail(
            @PathVariable String email) throws Exception {
        return ResponseEntity.ok(patientService.findByEmail(email));
    }

    // 🔹 Get patient(s) by name
    @GetMapping("/find-by-name/{name}")
    public ResponseEntity<List<Patient>> getPatientByName(
            @PathVariable String name) throws Exception {
        return ResponseEntity.ok(patientService.findByName(name));
    }

    // 🔹 Save new patient
    @PostMapping
    public ResponseEntity<Patient> savePatient(@Valid @RequestBody Patient patient) {
        return ResponseEntity.ok(patientService.save(patient));
    }

    // 🔹 Update existing patient
    @PutMapping("/{patientid}")
    public ResponseEntity<Boolean> updatePatient(
            @PathVariable Long patientid,
            @Valid @RequestBody Patient patient) throws Exception {
        return ResponseEntity.ok(patientService.update(patientid, patient));
    }

    // 🔹 Soft delete patient (set status = 0)
    @DeleteMapping("/{patientid}")
    public ResponseEntity<Boolean> deletePatient(
            @PathVariable Long patientid) throws Exception {
        return ResponseEntity.ok(patientService.delete(patientid));
    }

    // 🔹 Get all deleted patients (status = 0)
    @GetMapping("/deleted-patient")
    public ResponseEntity<List<PatientDto>> getAllDeletedPatients() {
        return ResponseEntity.ok(patientService.findAllDeletedPatients());
    }

    // 🔹 Get list of all cities (enum)
    @GetMapping("/cities")
    public ResponseEntity<List<City>> getAllCities() {
        return ResponseEntity.ok(Arrays.asList(City.values()));
    }
}

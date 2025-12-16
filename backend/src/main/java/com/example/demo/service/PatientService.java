package com.example.demo.service;

import com.example.demo.dto.PatientDto;
import com.example.demo.dto.PatientSingleDto;
import com.example.demo.dto.ProblemDtoForPatientSingleDto;
import com.example.demo.entity.Patient;
import com.example.demo.entity.Problem;
import com.example.demo.exception.PatientNotFoundException;
import com.example.demo.repository.PatientRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final Logger logger;

    public PatientService(PatientRepository patientRepository, Logger logger) {
        this.patientRepository = patientRepository;
        this.logger = logger;
    }

        public List<PatientDto> findAll() {
        List<Patient> patients = patientRepository.findAllByStatusEquelsOne();

        if (patients.isEmpty()) {
            logger.warn("No active patients found");
            return List.of();
        }

        return patients.stream().map(p -> {
            PatientDto dto = new PatientDto();
            dto.setPatientid(p.getPatientid());
            dto.setName(p.getName());
            dto.setLastname(p.getLastname());
            dto.setEmail(p.getEmail());
            dto.setBornDate(p.getBornDate());
            dto.setCity(p.getCity());
            return dto;
        }).toList();
    }

    
    public List<PatientDto> findAllDeletedPatients() {
        List<Patient> patients = patientRepository.findAllByStatusEquelsZero();

        if (patients.isEmpty()) {
            throw new PatientNotFoundException("There is no deleted patient");
        }

        return patients.stream().map(p -> {
            PatientDto dto = new PatientDto();
            dto.setPatientid(p.getPatientid());
            dto.setName(p.getName());
            dto.setLastname(p.getLastname());
            dto.setEmail(p.getEmail());
            dto.setBornDate(p.getBornDate());
            dto.setCity(p.getCity());
            return dto;
        }).toList();
    }

    
    public Patient save(@Valid Patient patient) {
        patient.setStatus(1);
        Patient savedPatient = patientRepository.save(patient);

        if (savedPatient.getPatientid() == null) {
            throw new PatientNotFoundException("Error while saving patient");
        }
        return savedPatient;
    }

       public Boolean delete(Long patientid) {
        Patient patient = patientRepository.findById(patientid)
                .orElseThrow(() ->
                        new PatientNotFoundException("Patient does not exist with id " + patientid));

        patient.setStatus(0);

        if (patient.getProblems() != null) {
            patient.getProblems().forEach(p -> p.setStatus(0));
        }

        patientRepository.save(patient);
        return true;
    }

    
    public PatientSingleDto findByPatientId(Long patientid) {
        Patient patient = patientRepository.findById(patientid)
                .orElseThrow(() ->
                        new PatientNotFoundException("Patient does not exist with id " + patientid));

        PatientSingleDto dto = new PatientSingleDto();
        dto.setPatientid(patient.getPatientid());
        dto.setName(patient.getName());
        dto.setLastname(patient.getLastname());
        dto.setEmail(patient.getEmail());
        dto.setBornDate(patient.getBornDate());
        dto.setCity(patient.getCity());
        dto.setGender(patient.getGender());
        dto.setPhoneNo(patient.getPhoneNo());

        if (patient.getProblems() != null) {
            List<ProblemDtoForPatientSingleDto> problemDtos =
                    patient.getProblems().stream()
                            .filter(p -> p.getStatus() == 1)
                            .map(p -> mapProblemToDto(p, patient.getPatientid()))
                            .toList();

            dto.setProblems(problemDtos);
        }

        return dto;
    }

        private ProblemDtoForPatientSingleDto mapProblemToDto(Problem p, Long patientId) {
        ProblemDtoForPatientSingleDto dto = new ProblemDtoForPatientSingleDto();
        dto.setProblemid(p.getProblemid());
        dto.setProblemName(p.getProblemName());
        dto.setProblemDetail(p.getProblemDetail());
        dto.setProblemStatus(p.getProblemStatus());
        dto.setPId(patientId);
        dto.setStatus(p.getStatus());
        dto.setCreationDate(p.getCreationDate());
        return dto;
    }

        public Patient findByEmail(String email) {
        return patientRepository.findByEmail(email)
                .orElseThrow(() ->
                        new PatientNotFoundException("Patient does not exist with this email " + email));
    }

        public Boolean update(Long patientid, @Valid Patient patient) {
        patientRepository.findById(patientid)
                .orElseThrow(() ->
                        new PatientNotFoundException("Patient does not exist with id " + patientid));

        patient.setPatientid(patientid);
        patientRepository.save(patient);
        return true;
    }

        public List<Patient> findByName(String name) {
        List<Patient> patients = patientRepository.findByName(name);

        if (patients.isEmpty()) {
            throw new PatientNotFoundException("Patient does not exist with name " + name);
        }
        return patients;
    }
}

package com.example.demo.service;

import com.example.demo.dto.ProblemDto;
import com.example.demo.dto.ProblemDtoForPatientSingleDto;
import com.example.demo.entity.Patient;
import com.example.demo.entity.Problem;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.PatientRepository;
import com.example.demo.repository.ProblemRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;
    private final Logger logger;

    public ProblemService(
            ProblemRepository problemRepository,
            PatientRepository patientRepository,
            ModelMapper modelMapper,
            Logger logger) {

        this.problemRepository = problemRepository;
        this.patientRepository = patientRepository;
        this.modelMapper = modelMapper;
        this.logger = logger;
    }

    // ✅ SAVE (DTO → ENTITY)
    public ProblemDtoForPatientSingleDto save(ProblemDto dto) throws NotFoundException {

        if (dto.getPId() == null) {
            throw new IllegalArgumentException("Patient ID must not be null");
        }

        Patient patient = patientRepository.findById(dto.getPId())
                .orElseThrow(() ->
                        new NotFoundException("Patient not found with id " + dto.getPId()));

        Problem problem = modelMapper.map(dto, Problem.class);
        problem.setPatient(patient);
        problem.setPatientid(patient.getPatientid());
        problem.setStatus(1);

        problemRepository.save(problem);

        return modelMapper.map(problem, ProblemDtoForPatientSingleDto.class);
    }

    // ✅ DELETE (SOFT DELETE)
    public Boolean delete(Long problemid) throws NotFoundException {

        Problem problem = problemRepository.findById(problemid)
                .orElseThrow(() ->
                        new NotFoundException("Problem not found with id " + problemid));

        problem.setStatus(0);
        problemRepository.save(problem);
        return true;
    }

    // ✅ GET BY PROBLEM ID
    public ProblemDtoForPatientSingleDto findByProblemid(Long problemid)
            throws NotFoundException {

        Problem problem = problemRepository.findById(problemid)
                .orElseThrow(() ->
                        new NotFoundException("Problem not found with id " + problemid));

        return modelMapper.map(problem, ProblemDtoForPatientSingleDto.class);
    }

    // ✅ GET ALL BY PATIENT ID
    public List<ProblemDtoForPatientSingleDto> findAllByPatientid(Long patientid) {

        return problemRepository.findByPatientidWithStatusOne(patientid)
                .stream()
                .map(p -> modelMapper.map(p, ProblemDtoForPatientSingleDto.class))
                .collect(Collectors.toList());
    }
}

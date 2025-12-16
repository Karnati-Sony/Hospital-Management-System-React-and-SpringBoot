package com.example.demo.controller;

import com.example.demo.dto.ProblemDto;
import com.example.demo.dto.ProblemDtoForPatientSingleDto;
import com.example.demo.entity.enums.ProblemStatus;
import com.example.demo.exception.NotFoundException;
import com.example.demo.service.ProblemService;
import com.example.demo.util.ApiPaths;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(ApiPaths.ProblemCtrl.CTRL)
public class ProblemController {

    @Autowired
    private ProblemService problemService;

    // ✅ FIXED: Return type MATCHED with service
    @GetMapping("/find-by-problemid/{problemid}")
    public ResponseEntity<ProblemDtoForPatientSingleDto> getProblem(
            @PathVariable Long problemid) throws NotFoundException {

        return ResponseEntity.ok(problemService.findByProblemid(problemid));
    }

    @GetMapping("/find-all-by-patientid/{patientid}")
    public ResponseEntity<List<ProblemDtoForPatientSingleDto>> getAllProblem(
            @PathVariable Long patientid) {

        return ResponseEntity.ok(problemService.findAllByPatientid(patientid));
    }

    // ✅ SAVE
    @PostMapping
    public ResponseEntity<ProblemDtoForPatientSingleDto> saveProblem(
            @Valid @RequestBody ProblemDto dto) throws NotFoundException {

        return ResponseEntity.ok(problemService.save(dto));
    }

    @DeleteMapping("/{problemid}")
    public ResponseEntity<Boolean> deleteProblem(
            @PathVariable Long problemid) throws NotFoundException {

        return ResponseEntity.ok(problemService.delete(problemid));
    }

    @GetMapping("/status")
    public ResponseEntity<List<ProblemStatus>> getAllProblemStatus() {
        return ResponseEntity.ok(Arrays.asList(ProblemStatus.values()));
    }
}

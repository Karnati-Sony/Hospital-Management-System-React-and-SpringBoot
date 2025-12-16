package com.example.demo.service;

import com.example.demo.dto.ReceipeDto;
import com.example.demo.entity.Problem;
import com.example.demo.entity.Receipe;
import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.PatientNotFoundException;
import com.example.demo.repository.ProblemRepository;
import com.example.demo.repository.ReceipeRepository;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ReceipeService {

    private static final Logger logger = LoggerFactory.getLogger(ReceipeService.class);

    private final ReceipeRepository receipeRepository;
    private final ModelMapper modelMapper;
    private final ProblemRepository problemRepository;

    public ReceipeService(ReceipeRepository receipeRepository, ModelMapper modelMapper,
                          ProblemRepository problemRepository) {
        this.receipeRepository = receipeRepository;
        this.modelMapper = modelMapper;
        this.problemRepository = problemRepository;
    }

    public List<ReceipeDto> getAll() throws PatientNotFoundException {
        List<Receipe> list = receipeRepository.findAllByStatusEquals(1);
        if (!list.isEmpty()) {
            ReceipeDto[] dtos = modelMapper.map(list, ReceipeDto[].class);
            return Arrays.asList(dtos);
        } else {
            logger.error("There is no any receipe");
            throw new PatientNotFoundException("There is no any receipe");
        }
    }

    public ReceipeDto findByReceipeId(Long receipeId) throws NotFoundException {
        Optional<Receipe> opt = receipeRepository.findById(receipeId);
        if (opt.isPresent()) {
            return modelMapper.map(opt.get(), ReceipeDto.class);
        } else {
            logger.error("Receipe not found with id: " + receipeId);
            throw new NotFoundException("Receipe not found with id: " + receipeId);
        }
    }

    public List<ReceipeDto> findAllByProblemId(Long problemid) throws PatientNotFoundException {
        List<Receipe> list = receipeRepository.findAllByProblemId(problemid);
        if (!list.isEmpty()) {
            ReceipeDto[] dtos = modelMapper.map(list, ReceipeDto[].class);
            return Arrays.asList(dtos);
        } else {
            logger.info("This problem has no any receipe");
            throw new PatientNotFoundException("This problem has no any receipe");
        }
    }

    public ReceipeDto save(ReceipeDto dto) throws NotFoundException {
        Optional<Problem> opt = problemRepository.findById(dto.getProblemid());
        if (opt.isPresent()) {
            dto.setStatus(1);
            Receipe receipe = modelMapper.map(dto, Receipe.class);
            receipe.setProblem(opt.get());
            receipe.setProblemid(opt.get().getProblemid());
            receipe.setPatientid(opt.get().getPatientid());
            receipe = receipeRepository.save(receipe);
            if (receipe.getReceipeid() > -1) {
                dto.setReceipeid(receipe.getReceipeid());
                logger.info("Perfect.. Saving Receipe for related problem is ok");
                return dto;
            } else {
                logger.error("A problem occurred during saving receipe");
                throw new PatientNotFoundException("A problem occurred during saving receipe");
            }
        } else {
            logger.error("There is no such problem with problem id : " + dto.getProblemid());
            throw new NotFoundException("There is no such problem with problem id : " + dto.getProblemid());
        }
    }

    public boolean delete(Long receipeid) throws NotFoundException {
        Optional<Receipe> optional = receipeRepository.findById(receipeid);
        if (optional.isEmpty()) {
            logger.error("Receipe does not exist with receipeid : " + receipeid);
            throw new NotFoundException("Receipe does not exist with receipeid : " + receipeid);
        }
        optional.get().setStatus(0);
        receipeRepository.save(optional.get());
        logger.info("Receipe was deleted with receipeid : " + receipeid);
        return true;
    }
}

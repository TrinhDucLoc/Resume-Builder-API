package com.springboot.ecommerce.Resume.service.impl;


import com.springboot.ecommerce.Resume.entity.CV.CV;
import com.springboot.ecommerce.exception.ResourceNotFoundException;
import com.springboot.ecommerce.Resume.dto.CVDTO;
import com.springboot.ecommerce.Resume.repository.CVRepository;
import com.springboot.ecommerce.repository.UserRepository;
import com.springboot.ecommerce.Resume.service.CVService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CVImpl implements CVService {
    private final CVRepository cvRepository;

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;


    public CVImpl(CVRepository cvRepository, ModelMapper modelMapper,
                  UserRepository userRepository) {
        this.cvRepository = cvRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    @Override
    public CVDTO createCV(CVDTO cvdto) {
        CV cv = modelMapper.map(cvdto, CV.class);
        CV newCV = cvRepository.save(cv);
        return modelMapper.map(newCV, CVDTO.class);
    }

    @Override
    public List<CVDTO> getAllCV(){
        List<CV> cvs = cvRepository.findAll();
        return cvs.stream().map(cv -> modelMapper.map(cv, CVDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public CVDTO getCVById(Long id){
        CV cv = cvRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("CV", "id", id));
        return modelMapper.map(cv, CVDTO.class);
    }

//    @Override
//    public CVDTO updateCVById(Long id, CVDTO CVDTO){
//
//        CV cv = cvRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("CV", "id", id));
//
////        cv.s(categoryRequest.getName());
//        cv.
//
//        CV updateCv = cvRepository.save(cv);
//
//        return modelMapper.map(updateCv, CVDTO.class);
//    }


    @Override
    public void deleteCVById(Long id){
        CV cv = cvRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("CV", "id", id));
        cvRepository.delete(cv);
    }
}

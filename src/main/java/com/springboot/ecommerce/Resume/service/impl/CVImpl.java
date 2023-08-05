package com.springboot.ecommerce.Resume.service.impl;


import com.springboot.ecommerce.Resume.dto.UserDTO;
import com.springboot.ecommerce.Resume.entity.CV.CV;
import com.springboot.ecommerce.dto.OrderResponse;
import com.springboot.ecommerce.entity.Order;
import com.springboot.ecommerce.entity.User;
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
    public CVDTO createCV(CVDTO cvdto, Long userId) {
        CV cv = modelMapper.map(cvdto, CV.class);
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", userId)
        );
        cv.setUser(user);

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
    public List<CVDTO> getCVByUserID(Long userId){
        //        retrieved order by user id
        List<CV> cvs = cvRepository.findByUserId(userId);
//        return convert to list of order entity to order DTO response
        return cvs.stream().map(cv -> modelMapper.map(cv, CVDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public CVDTO getCVById(Long id){
        CV cv = cvRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("CV", "id", id));
        return modelMapper.map(cv, CVDTO.class);
    }

    @Override
    public CVDTO updateCVById(Long id, CVDTO CVDTO){

        CV cv = cvRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("CV", "id", id));

        CV cvInput = modelMapper.map(CVDTO, CV.class);
        cvInput.setId(cv.getId());
        CV newCV = cvRepository.save(cvInput);

        return modelMapper.map(newCV, CVDTO.class);
    }


    @Override
    public void deleteCVById(Long id){
        CV cv = cvRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("CV", "id", id));
        cvRepository.delete(cv);
    }

    @Override
    public List<UserDTO> getAllUser(){
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }
}

package com.springboot.ecommerce.ResumeBuilderAPI.service;

import com.springboot.ecommerce.ResumeBuilderAPI.dto.CVDTO;
import com.springboot.ecommerce.ResumeBuilderAPI.dto.UserDTO;

import java.util.List;


public interface CVService {
    CVDTO createCV(CVDTO cvdto, Long userId);

    List<CVDTO> getAllCV();

    List<CVDTO> getCVByUserID(Long userId);

    CVDTO getCVById(Long id);

    CVDTO updateCVById(Long id, CVDTO CVDTO, Long userId);

    void deleteCVById(Long id);

    List<UserDTO> getAllUser();

}

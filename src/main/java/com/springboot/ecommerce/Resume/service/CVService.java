package com.springboot.ecommerce.Resume.service;

import com.springboot.ecommerce.Resume.dto.CVDTO;

import java.util.List;


public interface CVService {
    CVDTO createCV(CVDTO cvdto, Long userId);

    List<CVDTO> getAllCV();

    List<CVDTO> getCVByUserID(Long userId);

    CVDTO getCVById(Long id);

    CVDTO updateCVById(Long id, CVDTO CVDTO);

    void deleteCVById(Long id);


}

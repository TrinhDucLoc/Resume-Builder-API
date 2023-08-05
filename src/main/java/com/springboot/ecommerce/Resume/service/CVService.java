package com.springboot.ecommerce.Resume.service;

import com.springboot.ecommerce.Resume.dto.CVDTO;

import java.util.List;


public interface CVService {
    CVDTO createCV(CVDTO cvdto);

    List<CVDTO> getAllCV();

    CVDTO getCVById(Long id);

    void deleteCVById(Long id);


}

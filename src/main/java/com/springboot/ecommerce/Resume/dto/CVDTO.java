package com.springboot.ecommerce.Resume.dto;


import com.springboot.ecommerce.Resume.entity.CV.CVEducation;
import com.springboot.ecommerce.Resume.entity.CV.CVExperience;
import com.springboot.ecommerce.Resume.entity.CV.CVInformation;
import com.springboot.ecommerce.entity.User;
import io.swagger.annotations.Api;
import lombok.*;


@Api(value = "Category model information")
@Data
@Getter
@Setter
public class CVDTO {
    private Long id;

    private CVInformation information;
    private String summary;
    private CVExperience experiences;
    private CVEducation education;
    private String skills;

    private Long userId;

//    private String education;
//    private String experience;
//    private String information;
//    private String skill;
//    private String summary;
}

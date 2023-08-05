package com.springboot.ecommerce.Resume.entity.CV;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class CVExperience {
//    Experiences: job title, company, period and job description.
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    private String jobTitle;
    private String company;
    private String period;
    private String jobDescription;


}

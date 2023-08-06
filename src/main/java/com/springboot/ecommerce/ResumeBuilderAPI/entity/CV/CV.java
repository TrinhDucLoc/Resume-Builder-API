package com.springboot.ecommerce.ResumeBuilderAPI.entity.CV;

import com.springboot.ecommerce.entity.User;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cv")
public class CV {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "information_id")
    private CVInformation information;
    private String summary;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "experiences_id")
    private CVExperience experiences;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "education_id")
    private CVEducation education;
    private String skills;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}

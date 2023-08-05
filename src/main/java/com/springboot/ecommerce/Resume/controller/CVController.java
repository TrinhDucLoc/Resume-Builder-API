package com.springboot.ecommerce.Resume.controller;

import com.springboot.ecommerce.Resume.dto.CVDTO;
import com.springboot.ecommerce.Resume.service.CVService;
import com.springboot.ecommerce.dto.CategoryResponse;
import com.springboot.ecommerce.dto.OrderResponse;
import com.springboot.ecommerce.entity.User;
import com.springboot.ecommerce.payload.CategoryRequest;
import com.springboot.ecommerce.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(value = "CRUD REST APIs for Comment Respource")
@RestController
@RequestMapping("/api/cv")
public class CVController {
    private final CVService cvService;
    private final UserRepository userRepository;

    public CVController(CVService cvService,
                        UserRepository userRepository) {
        this.cvService = cvService;
        this.userRepository = userRepository;
    }


    @ApiOperation("Create CV REST API")
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<CVDTO> createCV(@Valid @RequestBody CVDTO cvdto){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String username = userDetails.getUsername();

        User user = userRepository.findByUsername(username);

        Long userId = user.getId();
        return new ResponseEntity<>(cvService.createCV(cvdto, userId), HttpStatus.CREATED);
    }

    @ApiOperation("Get All CV REST API")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<CVDTO> getAllCV(){
        return cvService.getAllCV();
    }

    @ApiOperation(value = "Get order by user id REST API")
    @GetMapping("/user/")
    public List<CVDTO> getCVByUserID(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String username = userDetails.getUsername();

        User user = userRepository.findByUsername(username);

        Long userId = user.getId();
        return cvService.getCVByUserID(userId);
    }

    @ApiOperation("Get CV By Id REST API")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<CVDTO> getCVById(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(cvService.getCVById(id));
    }

    @ApiOperation("Update CV By Id REST API")
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}")
    public ResponseEntity<CVDTO> updateCVById(@PathVariable(value = "id") Long id,
                                                               @Valid @RequestBody CVDTO cvdto){
        return new ResponseEntity<>(cvService.updateCVById(id, cvdto), HttpStatus.CREATED);
    }


    @ApiOperation("Delete CV By Id REST API")
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCVById(@PathVariable(value = "id") Long id){
        cvService.deleteCVById(id);
        return new ResponseEntity<>("CV deleted successfully", HttpStatus.OK);
    }



}

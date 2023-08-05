package com.springboot.ecommerce.Resume.controller;

import com.springboot.ecommerce.Resume.dto.CVDTO;
import com.springboot.ecommerce.Resume.service.CVService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(value = "CRUD REST APIs for Comment Respource")
@RestController
@RequestMapping("/api/cv")
public class CVController {
    private final CVService cvService;

    public CVController(CVService cvService) {
        this.cvService = cvService;
    }


    //    Create category
    @ApiOperation("Create CV REST API")
//    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<CVDTO> createCV(@Valid @RequestBody CVDTO cvdto){
        return new ResponseEntity<>(cvService.createCV(cvdto), HttpStatus.CREATED);
    }

//    Get all category
    @ApiOperation("Get All CV REST API")
    @GetMapping
    public List<CVDTO> getAllCV(){
        return cvService.getAllCV();
    }

//    Get category by id
    @ApiOperation("Get CV By Id REST API")
    @GetMapping("/{id}")
    public ResponseEntity<CVDTO> getCVById(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(cvService.getCVById(id));
    }



//    Delete category by id
    @ApiOperation("Delete CV By Id REST API")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCVById(@PathVariable(value = "id") Long id){
        cvService.deleteCVById(id);
        return new ResponseEntity<>("CV deleted successfully", HttpStatus.OK);
    }

}

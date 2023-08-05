package com.springboot.ecommerce.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Api(value = "Product model information")
@Data
public class ProductResponse {
    @ApiModelProperty(value = "Product id")
    private Long id;

    @ApiModelProperty(value = "Product name")
    @NotEmpty
    private String name;

    @ApiModelProperty(value = "Product description")
    @NotEmpty
    private String description;

//    @ApiModelProperty(value = "Product quantity")
//    @NotEmpty
//    private Integer quantity;

    @ApiModelProperty(value = "Product price")
    @NotEmpty
    private Float price;

//    @ApiModelProperty(value = "Product discount_percent")
//    @NotEmpty
//    private Float discount_percent;

    @ApiModelProperty(value = "Product name")
    @NotEmpty
    private Boolean enable;
    private String image;

    @Column
    private Long countInStock;
//    private Date created_time;
//    private Date updated_time;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "category_id", nullable = false)
//    private Category category;
}

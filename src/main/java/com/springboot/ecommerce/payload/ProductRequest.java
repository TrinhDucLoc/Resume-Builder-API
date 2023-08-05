package com.springboot.ecommerce.payload;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;

@Api(value = "Product model information")
@Data
public class ProductRequest {

    @ApiModelProperty(value = "Product name")
    @NotEmpty
    private String name;

    @ApiModelProperty(value = "Product description")
    @NotEmpty
    private String description;

    @ApiModelProperty(value = "Product quantity")
    private Integer quantity;

    @ApiModelProperty(value = "Product price")
    private Float price;

    @Column
    private Long countInStock;

//    @ApiModelProperty(value = "Product discount_percent")
//    private Float discount_percent;

    @ApiModelProperty(value = "Product name")
    private Boolean enable;
//    private String main_image;
//    private Date created_time;
//    private Date updated_time;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "category_id", nullable = false)
//    private Category category;
}

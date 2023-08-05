package com.springboot.ecommerce.controller;

import com.springboot.ecommerce.dto.OrderResponse;
import com.springboot.ecommerce.entity.User;
import com.springboot.ecommerce.payload.OrderRequest;
import com.springboot.ecommerce.repository.UserRepository;
import com.springboot.ecommerce.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Api
@RestController
@RequestMapping("/api/order")
@Slf4j
public class OrderController {
    private final UserRepository userRepository;
    private final OrderService orderService;

    public OrderController(OrderService orderService,
                           UserRepository userRepository) {
        this.orderService = orderService;
        this.userRepository = userRepository;
    }

    //    create order
    @ApiOperation(value = "Create product REST API")
    @PostMapping("")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest orderRequest){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        try {

            String username = userDetails.getUsername();

            User user = userRepository.findByUsername(username);

            Long userId = user.getId();

            return new ResponseEntity<>(orderService.createOrder(orderRequest, userId), HttpStatus.CREATED);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

//    Get all order
    @ApiOperation("Get All Order REST API")
    @GetMapping
    public List<OrderResponse> getAllOrder(){
        return orderService.getAllOrder();
    }

////    get order by id
//    @ApiOperation(value = "Get order by id REST API")
//    @GetMapping("/{orderId}")
//    public ResponseEntity<OrderResponse> getOrderById(@PathVariable(name = "orderId") Long orderId){
//        return ResponseEntity.ok(orderService.getOrderById(orderId));
//    }


    //    get order by id
    @ApiOperation(value = "Get order by id REST API")
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderRequest> getOrderById(@PathVariable(name = "orderId") Long orderId){
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

//    get order by user id
    @ApiOperation(value = "Get order by user id REST API")
    @GetMapping("/user/{userId}")
    public List<OrderResponse> getOrderByUserId(@PathVariable(name = "userId") Long userId){
        return orderService.getOrderByUserId(userId);
    }

//    //    get order by accessToken
//    @ApiOperation(value = "Get order by id REST API")
//    @GetMapping("/accessToken/{accessToken}")
//    public ResponseEntity<List<OrderResponse>> getOrderByAccessToken(@PathVariable(name = "accessToken") String accessToken){
//        return ResponseEntity.ok(orderService.getOrderByAccessToken(accessToken));
//    }

//    Update order status by id #1
    @ApiOperation("Update Order by id REST API")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("comfirm/{idOrder}")
    public ResponseEntity<OrderResponse> comfirmOrderById(@PathVariable(name = "idOrder") Long id){
        return new ResponseEntity<>(orderService.comfirmOrderById(id), HttpStatus.CREATED);
    }

    //    Update order status by id #2
    @ApiOperation("Update Order by id REST API")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("cancel/{idOrder}")
    public ResponseEntity<OrderResponse> cancelOrderById(@PathVariable(name = "idOrder") Long id){
        return new ResponseEntity<>(orderService.cancelOrderById(id), HttpStatus.CREATED);
    }

    //    Update order status by id #3
    @ApiOperation("Update Order by id REST API")
    @PreAuthorize("hasAnyRole('ADMIN','SHIPPER')")
    @PatchMapping("delivering/{idOrder}")
    public ResponseEntity<OrderResponse> deliveringOrderById(@PathVariable(name = "idOrder") Long id){
        return new ResponseEntity<>(orderService.deliveringOrderById(id), HttpStatus.CREATED);
    }

    //    Update order status by id #4
    @ApiOperation("Update Order by id REST API")
//    @PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasAnyRole('ADMIN','SHIPPER')")
    @PatchMapping("done/{idOrder}")
    public ResponseEntity<OrderResponse> doneOrderById(@PathVariable(name = "idOrder") Long id) throws MessagingException, UnsupportedEncodingException {
        return new ResponseEntity<>(orderService.doneOrderById(id), HttpStatus.CREATED);
    }
}

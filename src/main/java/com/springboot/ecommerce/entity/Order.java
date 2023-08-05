package com.springboot.ecommerce.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Blob;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column
    @CreationTimestamp
    private Date orderTime;
    @Column
    private String address;
    @Column
    private String phoneNumber;
    @Column
    private Float productCost;
    @Column
    private Float shippingCost;
    @Column
    private String paymentMethod;
    @Column
    private String orderStatus;

    @Column
    private Float totalCost;

    @Column(columnDefinition = "TEXT")
//    @Column
//    private String qrCode;
    private String qrCode;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderDetail> orderDetails = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "addressId", referencedColumnName = "id")
    private ShippingAddress shippingAddress;
}

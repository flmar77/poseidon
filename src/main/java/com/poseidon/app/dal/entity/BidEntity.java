package com.poseidon.app.dal.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "bid")
public class BidEntity {

    public BidEntity() {
    }

    public BidEntity(String account, String type, Double bidQuantity) {
        this.account = account;
        this.type = type;
        this.bidQuantity = bidQuantity;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "account is mandatory")
    @Length(max = 255, message = "account must be 255 char max")
    private String account;

    @NotBlank(message = "type is mandatory")
    @Length(max = 255, message = "type must be 255 char max")
    private String type;

    @NotNull(message = "bidQuantity is mandatory")
    @Digits(integer = 8, fraction = 2, message = "bidQuantity must be double(10,2)")
    @DecimalMin(value = "0.00", message = "bidQuantity must >= 0.00")
    @DecimalMax(value = "99999999.99", message = "bidQuantity must be <= 99999999.99")
    private Double bidQuantity;

    private Double askQuantity;
    private Double bid;
    private Double ask;
    private String benchmark;
    private Timestamp bidListDate;
    private String commentary;
    private String security;
    private String status;
    private String trader;
    private String book;
    private String creationName;
    private Timestamp creationDate;
    private String revisionName;
    private Timestamp revisionDate;
    private String dealName;
    private String dealType;
    private String sourceListId;
    private String side;

}

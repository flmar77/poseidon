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
@Table(name = "trade")
public class TradeEntity {

    public TradeEntity() {
    }

    public TradeEntity(String account, String type, Double buyQuantity) {
        this.account = account;
        this.type = type;
        this.buyQuantity = buyQuantity;
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

    @NotNull(message = "buyQuantity is mandatory")
    @Digits(integer = 8, fraction = 2, message = "buyQuantity must be double(10,2)")
    @DecimalMin(value = "0.00", message = "buyQuantity must >= 0.00")
    @DecimalMax(value = "99999999.99", message = "buyQuantity must be <= 99999999.99")
    private Double buyQuantity;

    private Double sellQuantity;
    private Double buyPrice;
    private Double sellPrice;
    private Timestamp tradeDate;
    private String security;
    private String status;
    private String trader;
    private String benchmark;
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

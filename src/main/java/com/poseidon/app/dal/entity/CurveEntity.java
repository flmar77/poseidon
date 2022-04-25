package com.poseidon.app.dal.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;


@Getter
@Setter
@Entity
@Table(name = "curve")
public class CurveEntity {

    public CurveEntity() {
    }

    public CurveEntity(Integer curveId, Double term, Double value) {
        this.curveId = curveId;
        this.term = term;
        this.value = value;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "curveId is mandatory")
    @Digits(integer = 8, fraction = 0, message = "curveId must be integer(8)")
    @Min(value = 0, message = "curveId must be >= 0")
    @Max(value = 99999999, message = "curveId must be <= 99999999")
    private Integer curveId;

    @NotNull(message = "term is mandatory")
    @Digits(integer = 8, fraction = 2, message = "term must be double(10,2)")
    @DecimalMin(value = "-99999999.99", message = "term must >= -99999999.99")
    @DecimalMax(value = "99999999.99", message = "term must be <= 99999999.99")
    private Double term;

    @NotNull(message = "value is mandatory")
    @Digits(integer = 8, fraction = 2, message = "value must be double(10,2)")
    @DecimalMin(value = "-99999999.99", message = "value must >= -99999999.99")
    @DecimalMax(value = "99999999.99", message = "value must be <= 99999999.99")
    private Double value;

    private Timestamp asOfDate;
    private Timestamp creationDate;


}

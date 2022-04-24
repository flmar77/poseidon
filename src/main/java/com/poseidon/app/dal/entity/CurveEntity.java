package com.poseidon.app.dal.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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

    private Integer curveId;
    private Double term;
    private Double value;
    private Timestamp asOfDate;
    private Timestamp creationDate;


}
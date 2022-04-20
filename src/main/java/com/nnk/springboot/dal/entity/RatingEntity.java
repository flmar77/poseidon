package com.nnk.springboot.dal.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "rating")
public class RatingEntity {

    public RatingEntity() {
    }

    public RatingEntity(String moodysRating, String sandPrating, String fitchRating, Integer orderNumber) {
        this.moodysRating = moodysRating;
        this.sandPrating = sandPrating;
        this.fitchRating = fitchRating;
        this.orderNumber = orderNumber;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private String moodysRating;
    private String sandPrating;
    private String fitchRating;
    private Integer orderNumber;

}

package com.poseidon.app.dal.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.*;

@Getter
@Setter
@Entity
@Table(name = "rating")
public class RatingEntity {

    public RatingEntity() {
    }

    public RatingEntity(Integer orderNumber, String moodysRating, String sandPrating, String fitchRating) {
        this.orderNumber = orderNumber;
        this.moodysRating = moodysRating;
        this.sandPrating = sandPrating;
        this.fitchRating = fitchRating;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "orderNumber is mandatory")
    @Digits(integer = 8, fraction = 0, message = "orderNumber must be integer(8)")
    @Min(value = 0, message = "orderNumber must be >= 0")
    @Max(value = 99999999, message = "orderNumber must be <= 99999999")
    private Integer orderNumber;

    @NotBlank(message = "moodysRating is mandatory")
    @Length(max = 255, message = "moodysRating must be 255 char max")
    private String moodysRating;

    @NotBlank(message = "sandPrating is mandatory")
    @Length(max = 255, message = "sandPrating must be 255 char max")
    private String sandPrating;

    @NotBlank(message = "fitchRating is mandatory")
    @Length(max = 255, message = "fitchRating must be 255 char max")
    private String fitchRating;

}

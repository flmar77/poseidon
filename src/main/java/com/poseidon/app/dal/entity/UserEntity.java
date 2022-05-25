package com.poseidon.app.dal.entity;

import com.poseidon.app.configuration.ValidPassword;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;


@Getter
@Setter
@Entity
@Table(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "userName is mandatory")
    @Length(max = 255, message = "userName must be 255 char max")
    private String userName;

    @NotBlank(message = "password is mandatory")
    @Length(max = 255, message = "password must be 255 char max")
    @ValidPassword
    private String password;

    @NotBlank(message = "fullName is mandatory")
    @Length(max = 255, message = "fullName must be 255 char max")
    private String fullName;

    @NotBlank(message = "role is mandatory")
    @Length(max = 255, message = "role must be 255 char max")
    private String role;

}

package com.poseidon.app.dal.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Entity
@Table(name = "rule")
public class RuleEntity {

    public RuleEntity() {
    }

    public RuleEntity(String name, String description, String json, String template, String sqlStr, String sqlPart) {
        this.name = name;
        this.description = description;
        this.json = json;
        this.template = template;
        this.sqlStr = sqlStr;
        this.sqlPart = sqlPart;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "name is mandatory")
    @Length(max = 255, message = "name must be 255 char max")
    private String name;

    @NotBlank(message = "description is mandatory")
    @Length(max = 255, message = "description must be 255 char max")
    private String description;

    @NotBlank(message = "json is mandatory")
    @Length(max = 255, message = "json must be 255 char max")
    private String json;

    @NotBlank(message = "template is mandatory")
    @Length(max = 255, message = "template must be 255 char max")
    private String template;

    @NotBlank(message = "sqlStr is mandatory")
    @Length(max = 255, message = "sqlStr must be 255 char max")
    private String sqlStr;

    @NotBlank(message = "sqlPart is mandatory")
    @Length(max = 255, message = "sqlPart must be 255 char max")
    private String sqlPart;

}

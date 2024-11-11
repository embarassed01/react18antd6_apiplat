package com.example.demo.model.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class User implements Serializable {
    private String username;

    private static final long serialVersionUID = 13232L;
}

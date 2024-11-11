package com.example.demo.common;

import java.io.Serializable;

import lombok.Data;

@Data
public class IdRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 10001L;
}

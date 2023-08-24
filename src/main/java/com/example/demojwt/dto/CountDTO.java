package com.example.demojwt.dto;


/**
 * Data Transfer Object
 */

//dto; No son entidades, no se persisten en base de datos, solo son para devolver mensajes al front
public class CountDTO {

    private Long count;
    private String message;

    public CountDTO() { }

    public CountDTO(Long count) {
        this.count = count;

    }

    public Long getCount() { return count; }

    public void setCount(Long count) { this.count = count; }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }
}
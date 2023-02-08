package com.wolox.training.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthorDto {

    @JsonProperty("name")
    private String name;

    public AuthorDto() {
    }

    public AuthorDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AuthorDto{" +
                "name='" + name + '\'' +
                '}';
    }
}

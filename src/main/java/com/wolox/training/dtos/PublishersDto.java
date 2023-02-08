package com.wolox.training.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PublishersDto {

    @JsonProperty("name")
    private String name;

    public PublishersDto() {
    }

    public PublishersDto(String name) {
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
        return "PublishersDto{" +
                "name='" + name + '\'' +
                '}';
    }
}

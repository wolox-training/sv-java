package com.wolox.training.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Data
@NoArgsConstructor
@Setter
@Getter
public class AuthorDto {

    @JsonProperty("name")
    private String name;

    @JsonProperty("key")
    private String url;

    public AuthorDto(String name) {
        this.name = name;
    }

}

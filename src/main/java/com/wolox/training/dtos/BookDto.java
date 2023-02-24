package com.wolox.training.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class BookDto {

    @JsonProperty("isbn")
    private String isbn;

    @JsonProperty("title")
    private String title;

    @JsonProperty("subtitle")
    private String subtitle;

    @JsonProperty("publishers")
    private List<PublishersDto> publishers;

    @JsonProperty("publish_date")
    private String publishDate;

    @JsonProperty("number_of_pages")
    private float numberOfPages;

    @JsonProperty("authors")
    List<AuthorDto> authors;

}

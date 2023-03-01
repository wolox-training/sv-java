package com.wolox.training.models;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class Teacher extends User{
    private String subject;

    public Teacher(String username, String name, LocalDate birthdate, List<Book> books, String roleName, String password, String subject) {
        super(username, name, birthdate, books, roleName, password);
        this.subject = subject;
    }
}

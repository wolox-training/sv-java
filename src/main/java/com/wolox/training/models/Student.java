package com.wolox.training.models;


import jakarta.persistence.Entity;
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
public class Student extends User{
    private int years;

    public Student(String username, String name, LocalDate birthdate, List<Book> books, String roleName, String password, int years) {
        super(username, name, birthdate, books, roleName, password);
        this.years = years;
    }
}

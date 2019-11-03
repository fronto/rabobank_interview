package nl.rabobank.interview.domain;

import lombok.AllArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
public class Person {

    private final String firstName;
    private final String lastName;
    private final LocalDate dateOfBirth;
    private final String address;

}

package nl.rabobank.interview.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
public class Person {

    @Getter
    private final String firstName;
    @Getter
    private final String lastName;
    private final LocalDate dateOfBirth;
    private final String address;

}

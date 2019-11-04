package nl.rabobank.interview.domain;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class Person {

    @Id
    @GeneratedValue
    @SuppressWarnings("unused")
    private Long id;

    @Getter
    private String firstName;
    @Getter
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;

    @SuppressWarnings("unused")
    private Person() {
        //used by hibernate
    }

    public Person(String firstName, String lastName, LocalDate dateOfBirth, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }
}

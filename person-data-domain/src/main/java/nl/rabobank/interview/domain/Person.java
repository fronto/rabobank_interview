package nl.rabobank.interview.domain;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
public class Person {

    @Id
    @GeneratedValue
    @SuppressWarnings("unused")
    private Long id;

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;

    @SuppressWarnings("unused")
    private Person() {
        //used by hibernate
    }

    public Person(Long id, String firstName, String lastName, LocalDate dateOfBirth, String address) {
        this(firstName, lastName, dateOfBirth, address);
        this.id = id;
    }


    public Person(String firstName, String lastName, LocalDate dateOfBirth, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(firstName, person.firstName) &&
                Objects.equals(lastName, person.lastName) &&
                Objects.equals(dateOfBirth, person.dateOfBirth) &&
                Objects.equals(address, person.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, dateOfBirth, address);
    }
}

package nl.rabobank.interview.domain;

import lombok.Getter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
public final class Person {

    @Id
    @GeneratedValue
    @SuppressWarnings("unused")
    private Long id;

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;
    @OneToMany
    @Cascade(CascadeType.ALL)
    private List<Pet> pets;

    @SuppressWarnings("unused")
    Person() {
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
                Objects.equals(address, person.address) &&
                Objects.equals(pets, person.pets);
    }

    void addPet(Pet pet) {
        pets.add(pet);
        pet.addOwner(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, dateOfBirth, address, pets);
    }

    public boolean hasPet(Pet pet) {
        return pets.contains(pet);
    }
}

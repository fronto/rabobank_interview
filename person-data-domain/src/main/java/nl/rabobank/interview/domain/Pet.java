package nl.rabobank.interview.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Entity
public class Pet {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private Integer age;
    @ManyToOne
    @JoinColumn
    private Person owner;

    @SuppressWarnings("unused")
    Pet() {
        //for hibernate
    }

    public Pet(Long id, String name, Integer age) {
        this(name, age);
        this.id = id;
    }


    public Pet(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    void addOwner(Person owner) {
        if(!owner.hasPet(this)) {
            throw new IllegalStateException("cannot add person as owner unless person already has this pet");
        }
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pet pet = (Pet) o;
        return name.equals(pet.name) &&
                age.equals(pet.age) &&
                Objects.equals(owner, pet.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, owner);
    }
}

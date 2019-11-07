package nl.rabobank.interview.domain;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Pet {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private Integer age;

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

}

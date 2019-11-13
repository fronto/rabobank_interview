package nl.rabobank;

import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class PetDto {

    private Optional<String> id;
    private String name;
    private Integer age;

}

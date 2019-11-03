package nl.rabobank;

import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class PersonDto {

    private Optional<String> id;
    private String firstName;
    private String lastName;
    private String dateOfBirth;//TODO change to Date type
    private String address;

}

package nl.rabobank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonDto {

    private String firstName;
    private String lastName;
    private String dateOfBirth;//TODO change to Date type
    private String address;

}

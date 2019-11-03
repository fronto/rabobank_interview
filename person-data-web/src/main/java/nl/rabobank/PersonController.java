package nl.rabobank;

import nl.rabobank.interview.domain.Person;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public class PersonController {

    @PostMapping(path = "/person", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<PersonDto> createPerson(@RequestBody PersonDto personDto) {

        Person person = parseDomainObjectFromDto(personDto);

        throw new UnsupportedOperationException("not implemented yet");
    }

    private static Person parseDomainObjectFromDto(PersonDto personDto) {
        return new Person(personDto.getFirstName(), personDto.getLastName(), personDto.getDateOfBirth(), personDto.getAddress());
    }

    @GetMapping("/person/{id}/")
    @ResponseBody
    public ResponseEntity<PersonDto> getPerson(@PathVariable String id) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @PutMapping("/person/{id}/")
    public ResponseEntity<PersonDto> modifyPerson(@RequestBody PersonDto personDto, @PathVariable String id) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @DeleteMapping("/person/{id}/")
    public ResponseEntity<Void> deletePerson(@PathVariable String id) {
        throw new UnsupportedOperationException("not implemented yet");
    }


}

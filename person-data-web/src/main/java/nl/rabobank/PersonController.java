package nl.rabobank;

import nl.rabobank.interview.domain.Person;
import nl.rabobank.interview.domain.PersonRepository;
import nl.rabobank.interview.domain.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Access;
import java.util.Optional;

@RestController
public class PersonController {

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonRepository personRepository;

    @PostMapping(path = "/person", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<PersonDto> createPerson(@RequestBody PersonDto personDto) {

        Person person = parseDomainObjectFromDto(personDto);

        Person withId = personService.createNewPerson(person);

        PersonDto toEcho = serializeToDto(withId);

        return new ResponseEntity<>(toEcho, HttpStatus.CREATED);

    }

    private PersonDto serializeToDto(Person withId) {
        PersonDto personDto = new PersonDto();
        personDto.setId(Optional.of(withId.getId().toString()));
        personDto.setFirstName(withId.getFirstName());
        personDto.setLastName(withId.getLastName());
        personDto.setDateOfBirth(withId.getDateOfBirth());
        personDto.setAddress(withId.getAddress());
        return personDto;
    }

    private static Person parseDomainObjectFromDto(PersonDto personDto) {
        return new Person(personDto.getFirstName(), personDto.getLastName(), personDto.getDateOfBirth(), personDto.getAddress());
    }

    @GetMapping("/person/{id}/")
    @ResponseBody
    public ResponseEntity<PersonDto> getPerson(@PathVariable String id) {

        Person person = personRepository.getOne(Long.valueOf(id));
        PersonDto dto = serializeToDto(person);

        return new ResponseEntity<>(dto, HttpStatus.OK);
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

package nl.rabobank;

import nl.rabobank.interview.domain.Person;
import nl.rabobank.interview.domain.PersonRepository;
import nl.rabobank.interview.domain.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

@RestController
public class PersonController {

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonRepository personRepository;

    @PostMapping(path = "/person/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonDto> createPerson(@RequestBody PersonDto personDto) {

        Person person = parseDomainObjectFromDto(personDto);

        Person withId = personService.createNewPerson(person);

        PersonDto toEcho = serializeToDto(withId);

        return new ResponseEntity<>(toEcho, HttpStatus.CREATED);

    }

    private static PersonDto serializeToDto(Person withId) {
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

    private static Person parseDomainObjectFromDto(Long id, PersonDto personDto) {
        return new Person(id, personDto.getFirstName(), personDto.getLastName(), personDto.getDateOfBirth(), personDto.getAddress());
    }

    @GetMapping("/person/")
    public ResponseEntity<List<PersonDto>> index(@RequestParam(required = false) String firstName, @RequestParam(required = false) String lastName) {

        List<Person> result = personRepository.lookUpPeople(ofNullable(firstName), ofNullable(lastName));
        List<PersonDto> people = result.stream().map(PersonController::serializeToDto).collect(toList());

        return new ResponseEntity<>(people, HttpStatus.OK);

    }

    @GetMapping("/person/{id}/")
    public ResponseEntity<PersonDto> getPerson(@PathVariable String id) {

        Person person = personRepository.getOne(Long.valueOf(id));
        PersonDto dto = serializeToDto(person);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping("/person/{id}/")
    public ResponseEntity<PersonDto> modifyPerson(@RequestBody PersonDto personDto, @PathVariable String id) {

        Long idNumber = Long.valueOf(id);
        Person original = personRepository.getOne(idNumber);
        Person modified = parseDomainObjectFromDto(idNumber, personDto);

        //TODO consider sending feedback to user when validation fails
        if(!original.getFirstName().equals(modified.getFirstName())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if(!original.getLastName().equals(modified.getLastName())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if(!original.getDateOfBirth().equals(modified.getDateOfBirth())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Person result = personRepository.save(modified);
        PersonDto toReturn =  serializeToDto(result);

        return new ResponseEntity<>(toReturn, HttpStatus.OK);

    }

    @DeleteMapping("/person/{id}/")
    public ResponseEntity<Void> deletePerson(@PathVariable String id) {
        Long idNumber = Long.valueOf(id);
        personRepository.deleteById(idNumber);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}

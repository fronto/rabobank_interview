package nl.rabobank;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/*
 *
 *  This class is a stub that used to build up a suite of integration tests, to define and
 *  enforce contracts before the underlying functionality was written
 * 
 */
@RestController
@RequestMapping("/stub")
public class StubbedPersonController {

    static Random random = new Random();

    //TODO consider using Long instead of Int to strore id
    static Map<Integer, PersonDto> singleton = new ConcurrentHashMap<>();

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!\n";
    }

    private static int generateId() {
        //generate +ve id that is not already in use
        int id;
        do {
            id = random.nextInt() & Integer.MAX_VALUE;
        } while (singleton.containsKey(id));
        return id;
    }

    @PostMapping(path = "/person", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<PersonDto> createPerson(@RequestBody PersonDto person) {
        int id = generateId();
        person.setId(Optional.of(Integer.valueOf(id).toString()));
        singleton.put(id, person);
        return new ResponseEntity<>(singleton.get(id), HttpStatus.CREATED);

    }

    @GetMapping("/person/{id}/")
    @ResponseBody
    public ResponseEntity<PersonDto> getPerson(@PathVariable String id) {
        Integer idNumber = Integer.valueOf(id);
        if (singleton.containsKey(idNumber)) {
            PersonDto personDto = singleton.get(idNumber);
            return new ResponseEntity<>(personDto, HttpStatus.OK);

        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/person/{id}/")
    public ResponseEntity<PersonDto> modifyPerson(@RequestBody PersonDto personDto, @PathVariable String id) {
        //TODO what about null fields on DTO

        Integer idNumber = Integer.valueOf(id);
        if (singleton.containsKey(idNumber)) {


            PersonDto originial = singleton.get(idNumber);
            //TODO consider providing user with feeback
            if (!originial.getFirstName().equals(personDto.getFirstName())) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            if (!originial.getLastName().equals(personDto.getLastName())) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            if (!originial.getDateOfBirth().equals(personDto.getDateOfBirth())) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            singleton.put(idNumber, personDto);
            return new ResponseEntity<>(singleton.get(idNumber), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/person/{id}/")
    public ResponseEntity<Void> deletePerson(@PathVariable String id) {

        //TODO what about invalid id
        Integer idNumber = Integer.valueOf(id);
        singleton.remove(idNumber);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}

package nl.rabobank;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
public class HelloController {

    static PersonDto singleton;

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!\n";
    }

    @PostMapping(path = "/person", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<PersonDto> createPerson(@RequestBody PersonDto person, HttpServletResponse response) {

        singleton = person;

        return new ResponseEntity<>(person, HttpStatus.CREATED);

    }

    @GetMapping("/person")
    @ResponseBody
    public PersonDto getPerson() {
        return singleton;
    }

    @PutMapping("/person")
    public ResponseEntity<PersonDto> modifyPerson(@RequestBody PersonDto personDto) {
        //TODO what about null fields on DTO
        singleton = personDto;
        return new ResponseEntity<>(singleton, HttpStatus.OK);
    }

    @DeleteMapping("/person")
    public ResponseEntity<Void> deletePerson() {
        singleton = null;

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }




}

package nl.rabobank;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class HelloController {

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
        singleton.put(id,person);
        //TODO return id
        return new ResponseEntity<>(singleton.get(id), HttpStatus.CREATED);

    }

    @GetMapping("/person/{id}/")
    @ResponseBody
    public ResponseEntity<PersonDto> getPerson(@PathVariable String id) {
        //TODO what about non-existent entities
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
        //TODO what non-existent entities

        Integer idNumber = Integer.valueOf(id);
        singleton.put(idNumber, personDto);
        return new ResponseEntity<>(singleton.get(idNumber), HttpStatus.OK);
    }

    @DeleteMapping("/person/{id}/")
    public ResponseEntity<Void> deletePerson(@PathVariable String id) {

        //TODO what about invalid id
        Integer idNumber = Integer.valueOf(id);
        singleton.remove(idNumber);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }




}

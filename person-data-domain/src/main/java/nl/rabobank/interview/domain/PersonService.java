package nl.rabobank.interview.domain;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    //TODO think more deeply about transaction demarcation
    @Transactional
    public Person createNewPerson(Person person) {

        if(personRepository.hasPerson(person.getFirstName(), person.getLastName())) {
            throw new IllegalStateException("Attempting to overwrite existing person");
        }

        return personRepository.save(person);

    }

}

package nl.rabobank.interview.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    public void createNewPerson(Person person) {

        if(personRepository.hasPerson(person.getFirstName(), person.getLastName())) {
            throw new IllegalStateException("Attempting to overwrite existing person");
        }

        personRepository.savePerson(person);

    }

}

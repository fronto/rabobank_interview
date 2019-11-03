package nl.rabobank.interview.domain.test;

import nl.rabobank.interview.domain.Person;
import nl.rabobank.interview.domain.PersonRepository;
import nl.rabobank.interview.domain.PersonService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class PersonCreationTest {

    static final Person PAUL_SCHWARTZ = new Person("Paul", "Schwartz", localDate("20/04/1983"), "12 Fisher Avenue, Borrowdale, Harare, 1036EG");

    @Test
    void cannotOverwriteExistingPersonByRecreation() {

        assertThrows(IllegalStateException.class, () -> {

            PersonRepository personRepository = personRepository(hasPerson(PAUL_SCHWARTZ));
            PersonService personService = new PersonService(personRepository);

            personService.createNewPerson(PAUL_SCHWARTZ);

        });

    }

    @Test
    void canCreateNewPerson() {

        PersonRepository personRepository = personRepository(without(PAUL_SCHWARTZ));
        PersonService personService = new PersonService(personRepository);

        personService.createNewPerson(PAUL_SCHWARTZ);

        verify(personRepository).savePerson(PAUL_SCHWARTZ);

    }

    Behaviour<PersonRepository> without(final Person person) {
        return repository -> {
            when(repository.hasPerson(person.getFirstName(), person.getLastName())).thenReturn(false);
        };
    }

    static LocalDate localDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(date, formatter);
    }

    Behaviour<PersonRepository> hasPerson(Person person) {
        return repository -> {
            when(repository.hasPerson(person.getFirstName(), person.getLastName())).thenReturn(true);
        };
    }

    static PersonRepository personRepository(Behaviour<PersonRepository> ...alBehaviour) {
        PersonRepository personRepository = mock(PersonRepository.class);
        Stream.of(alBehaviour).forEach(behaviour -> behaviour.describeBehaviour(personRepository));
        return personRepository;
    }

    @FunctionalInterface
    interface Behaviour<Mock> {

        void describeBehaviour(Mock mock);

    }

}

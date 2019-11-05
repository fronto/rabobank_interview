package nl.rabobank.interview.domain.test;

import nl.rabobank.interview.domain.Person;
import nl.rabobank.interview.domain.PersonRepository;
import nl.rabobank.interview.domain.PersonService;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
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

        verify(personRepository).save(PAUL_SCHWARTZ);

    }

    @Test
    void creatingPersonEchosEntityWithId() {

        PersonRepository personRepository = personRepository(populatesIdOnSaveOf(PAUL_SCHWARTZ));
        PersonService personService = new PersonService(personRepository);

        Person withId = personService.createNewPerson(PAUL_SCHWARTZ);

        assertThat(withId).isEqualTo(PAUL_SCHWARTZ).has(anId());

    }

    Condition<Person> anId() {
        return new Condition<>(person -> person.getId() != null,"id must not be null");
    }

     Behaviour<PersonRepository> populatesIdOnSaveOf(Person withoutId) {
        Assertions.assertNull(withoutId.getId(), "Precondition violated: id field must be null");
        return repository -> {
            Person person = new Person(1234L, withoutId.getFirstName(), withoutId.getLastName(), withoutId.getDateOfBirth(), withoutId.getAddress());
            when(repository.save(withoutId)).thenReturn(person);
        };
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

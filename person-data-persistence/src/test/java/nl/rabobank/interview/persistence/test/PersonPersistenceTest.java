package nl.rabobank.interview.persistence.test;

import nl.rabobank.interview.domain.Person;
import nl.rabobank.interview.domain.PersonRepository;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


//TODO what about @ExtendWith(SpringExtension.class)
//TODO what about @SpringBootTest(classes = JpaH2TestConfiguration.class)
@DataJpaTest
@ContextConfiguration(classes = JpaH2TestConfiguration.class)
public class PersonPersistenceTest {

    @Autowired
    PersonRepository personRepository;

    //TODO duplication ???
    static final Person PAUL_SCHWARTZ = new Person("Paul", "Schwartz", localDate("20/04/1983"), "12 Fisher Avenue, Borrowdale, Harare, 1036EG");


    //TODO duplication ???
    static LocalDate localDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(date, formatter);
    }


    @Test
    void canPersistPerson() {

        personRepository.savePerson(PAUL_SCHWARTZ);
        assertEquals(PAUL_SCHWARTZ, personRepository.findAll().get(0));


    }

    @Test
    void persistingNewPersonEchosEntityWithIdPopulated() {

        //pre-condition
        assertNull(PAUL_SCHWARTZ.getId());

        Person result = personRepository.savePerson(PAUL_SCHWARTZ);

        assertThat(result).isEqualTo(PAUL_SCHWARTZ).has(anId());

    }

    //TODO duplication
    Condition<Person> anId() {
        return new Condition<>(person -> person.getId() != null,"id must not be null");
    }

    @Test
    void canCheckForPresenceOfPerson() {

        personRepository.savePerson(PAUL_SCHWARTZ);
        assertTrue(personRepository.hasPerson(PAUL_SCHWARTZ.getFirstName(), PAUL_SCHWARTZ.getLastName()));
    }

}

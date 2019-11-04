package nl.rabobank.interview.persistence.test;

import nl.rabobank.interview.domain.Person;
import nl.rabobank.interview.domain.PersonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


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
    void canCheckForPresenceOfPerson() {

        personRepository.savePerson(PAUL_SCHWARTZ);
        assertTrue(personRepository.hasPerson(PAUL_SCHWARTZ.getFirstName(), PAUL_SCHWARTZ.getLastName()));
    }

}

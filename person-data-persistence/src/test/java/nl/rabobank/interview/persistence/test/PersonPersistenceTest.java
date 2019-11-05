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
import java.util.List;
import java.util.Optional;

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
    static final Person MARK_SCHWARTZ = new Person("Natalie", "Schwartz", localDate("10/06/1982"), "12 Fisher Avenue, Borrowdale, Harare, 1036EG");
    static final Person PAUL_MASON = new Person("Paul", "Mason", localDate("21/10/1988"), "12 Crowhill Road, Borrowdale, Harare, 1036EG");


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
        return new Condition<>(person -> person.getId() != null, "id must not be null");
    }

    @Test
    void canCheckForPresenceOfPerson() {

        personRepository.savePerson(PAUL_SCHWARTZ);
        assertTrue(personRepository.hasPerson(PAUL_SCHWARTZ.getFirstName(), PAUL_SCHWARTZ.getLastName()));

    }

    @Test
    void canLookUpPeopleByFirstNameOnly() {

        personRepository.save(PAUL_SCHWARTZ);
        personRepository.save(MARK_SCHWARTZ);
        personRepository.save(PAUL_MASON);

        List<Person> results = personRepository.lookUpPeople(Optional.of("Paul"), Optional.empty());

        assertThat(results).contains(PAUL_SCHWARTZ, PAUL_MASON).doesNotContain(MARK_SCHWARTZ);

    }

    @Test
    void canLookUpPeopleByLastNameOnly() {

        personRepository.save(PAUL_SCHWARTZ);
        personRepository.save(MARK_SCHWARTZ);
        personRepository.save(PAUL_MASON);

        List<Person> results = personRepository.lookUpPeople(Optional.empty(), Optional.of("Schwartz"));

        assertThat(results).contains(PAUL_SCHWARTZ, MARK_SCHWARTZ).doesNotContain(PAUL_MASON);

    }

    @Test
    void canLookUpPeopleByFirstAndLastName() {

        personRepository.save(PAUL_SCHWARTZ);
        personRepository.save(MARK_SCHWARTZ);
        personRepository.save(PAUL_MASON);

        List<Person> results = personRepository.lookUpPeople(Optional.of("Paul"), Optional.of("Schwartz"));

        assertThat(results).contains(PAUL_SCHWARTZ).doesNotContain(PAUL_MASON, MARK_SCHWARTZ);

    }

    @Test
    void listsAllNamesWithoutCriteriaSpecified() {

        personRepository.save(PAUL_SCHWARTZ);
        personRepository.save(MARK_SCHWARTZ);
        personRepository.save(PAUL_MASON);

        List<Person> results = personRepository.lookUpPeople(Optional.empty(), Optional.empty());

        assertThat(results).contains(PAUL_SCHWARTZ, MARK_SCHWARTZ, PAUL_MASON);


    }


}

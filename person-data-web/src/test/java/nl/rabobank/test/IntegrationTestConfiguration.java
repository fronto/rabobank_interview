package nl.rabobank.test;

import nl.rabobank.interview.domain.Person;
import nl.rabobank.interview.domain.PersonRepository;
import nl.rabobank.interview.persistence.HibernatePersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.EntityManager;

@TestConfiguration
@EnableJpaRepositories
public class IntegrationTestConfiguration {

    @Bean
    @Autowired
    PersonRepository personRepository(EntityManager entityManager) {
        return new HibernatePersonRepository(Person.class, entityManager);
    }

}

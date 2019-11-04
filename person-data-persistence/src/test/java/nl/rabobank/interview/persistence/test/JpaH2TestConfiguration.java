package nl.rabobank.interview.persistence.test;

import nl.rabobank.interview.domain.Person;
import nl.rabobank.interview.domain.PersonRepository;
import nl.rabobank.interview.persistence.HibernatePersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.EntityManager;

@SpringBootApplication
@EnableJpaRepositories
@EntityScan(basePackages = "nl.rabobank.interview.domain")
public class JpaH2TestConfiguration {

    //TODO duplicated factory method
    @Bean
    @Autowired
    PersonRepository personRepository(EntityManager entityManager) {
        return new HibernatePersonRepository(Person.class, entityManager);
    }


}

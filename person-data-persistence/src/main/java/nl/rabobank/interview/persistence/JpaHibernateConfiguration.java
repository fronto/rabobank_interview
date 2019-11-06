package nl.rabobank.interview.persistence;

import nl.rabobank.interview.domain.Person;
import nl.rabobank.interview.domain.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.EntityManager;

@Configuration
@EnableJpaRepositories
@EntityScan(basePackages = "nl.rabobank.interview.domain")
public class JpaHibernateConfiguration {

    @Bean
    @Autowired
    PersonRepository personRepository(EntityManager entityManager) {
        return new HibernatePersonRepository(Person.class, entityManager);
    }


}

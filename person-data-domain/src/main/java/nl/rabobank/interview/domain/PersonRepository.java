package nl.rabobank.interview.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface PersonRepository extends JpaRepository<Person, Long> {

    void savePerson(Person person);

    boolean hasPerson(String firstName, String lastName);
}

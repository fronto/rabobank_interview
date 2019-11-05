package nl.rabobank.interview.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface PersonRepository extends JpaRepository<Person, Long> {

    boolean hasPerson(String firstName, String lastName);

    List<Person> lookUpPeople(Optional<String> firstName, Optional<String> lastName);
}

package nl.rabobank.interview.domain;

public interface PersonRepository {

    void savePerson(Person person);

    boolean hasPerson(String firstName, String lastName);
}

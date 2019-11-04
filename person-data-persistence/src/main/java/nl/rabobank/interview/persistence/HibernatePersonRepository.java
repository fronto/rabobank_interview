package nl.rabobank.interview.persistence;

import nl.rabobank.interview.domain.Person;
import nl.rabobank.interview.domain.PersonRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class HibernatePersonRepository extends SimpleJpaRepository<Person, Long> implements PersonRepository {

    private final EntityManager entityManager;

    public HibernatePersonRepository(JpaEntityInformation<Person, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    public HibernatePersonRepository(Class<Person> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.entityManager = entityManager;
    }

    public Person savePerson(Person person) {
        return super.save(person);//TODO consider inlining
    }

    public boolean hasPerson(String firstName, String lastName) {

        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(p) FROM Person AS p WHERE p.firstName = :firstName AND p.lastName = :lastName", Long.class);
        query.setParameter("firstName", firstName);
        query.setParameter("lastName", lastName);

        long count = query.getSingleResult();

        return count > 0;
    }
}

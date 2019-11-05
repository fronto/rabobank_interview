package nl.rabobank.interview.persistence;

import nl.rabobank.interview.domain.Person;
import nl.rabobank.interview.domain.PersonRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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

    public boolean hasPerson(String firstName, String lastName) {

        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(p) FROM Person AS p WHERE p.firstName = :firstName AND p.lastName = :lastName", Long.class);
        query.setParameter("firstName", firstName);
        query.setParameter("lastName", lastName);

        long count = query.getSingleResult();

        return count > 0;
    }

    @Override
    public List<Person> lookUpPeople(Optional<String> firstName, Optional<String> lastName) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Person> query = criteriaBuilder.createQuery(Person.class);
        Root<Person> root = query.from(Person.class);

        Optional<Predicate> firstNameFilter= firstName.map(value -> criteriaBuilder.equal(root.get("firstName"), value));
        Optional<Predicate> lastNameFilter = lastName.map(value -> criteriaBuilder.equal(root.get("lastName"), value));

        Predicate[] criteria = Stream.of(firstNameFilter, lastNameFilter)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toArray(size -> new Predicate[size]);

        TypedQuery<Person> finalQuery = entityManager.createQuery(query.select(root).where(criteria));

        return finalQuery.getResultList();

    }


}

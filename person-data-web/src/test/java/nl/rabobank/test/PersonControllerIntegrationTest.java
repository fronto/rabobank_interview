package nl.rabobank.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = IntegrationTestConfiguration.class, properties = {"spring.jpa.hibernate.ddl-auto=create-drop"})
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PersonControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    private PersonServiceRestClient client;

    @BeforeEach
    void setUpClient() {
        client = new PersonServiceRestClient(mvc);
    }

    @Test
    public void addPerson() throws Exception {

        PersonJsonBuilder tracy = PersonJsonBuilder.aPerson()
                .withFirstName("Tracy")
                .withLastName("Lane")
                .withDateOfBirth("20/03/1984")
                .withAddress("17 Kew Drive, Borrowdale, Harare, 2345WP");

        mvc.perform(post("/person/").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                .content(tracy.toJson()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id", notNullValue()))
                .andExpect(jsonPath("firstName", is("Tracy")))
                .andExpect(jsonPath("lastName", is("Lane")))
                .andExpect(jsonPath("dateOfBirth", is("20/03/1984")))
                .andExpect(jsonPath("address", is("17 Kew Drive, Borrowdale, Harare, 2345WP")));

    }

    @Test
    void retrieveAPerson() throws Exception {

        PersonJsonBuilder tracy = PersonJsonBuilder.aPerson()
                .withFirstName("Tracy")
                .withLastName("Lane")
                .withDateOfBirth("20/03/1984")
                .withAddress("17 Kew Drive, Borrowdale, Harare, 2345WP");

        String id = client.createPerson(tracy);

        mvc.perform(get(personById(id)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("firstName", is("Tracy")))
                .andExpect(jsonPath("lastName", is("Lane")))
                .andExpect(jsonPath("dateOfBirth", is("20/03/1984")))
                .andExpect(jsonPath("address", is("17 Kew Drive, Borrowdale, Harare, 2345WP")));

    }

    @Test
    void retrieveAllPeople() throws Exception {

        client.createPerson(tracy());
        client.createPerson(paul());

        mvc.perform(get("/person/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("[?(@.firstName == 'Tracy')]", notNullValue()))
                .andExpect(jsonPath("[?(@.firstName == 'Paul')]", notNullValue()));

    }

    @Test
    void retrieveByFirstName() throws Exception {

        client.createPerson(tracy());
        client.createPerson(paul());

        mvc.perform(get("/person/").param("firstName", "Paul"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("[?(@.firstName == 'Paul')]", notNullValue()))
                .andExpect(jsonPath("[*]", hasSize(equalTo(1))));

    }

    @Test
    void retrieveByLastName() throws Exception {

        client.createPerson(tracy());
        client.createPerson(paul().withLastName("Schwartz"));

        mvc.perform(get("/person/").param("lastName", "Schwartz"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("[?(@.lastName == 'Schwartz')]", notNullValue()))
                .andExpect(jsonPath("[*]", hasSize(equalTo(1))));

    }

    @Test
    void retrieveByFirstAndLastName() throws Exception {

        client.createPerson(tracy());
        client.createPerson(paul().withLastName("Schwartz"));

        mvc.perform(get("/person/")
                .param("firstName", "Paul")
                .param("lastName", "Schwartz"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("[?(@.firstName == 'Paul')]", notNullValue()))
                .andExpect(jsonPath("[?(@.lastName == 'Schwartz')]", notNullValue()))
                .andExpect(jsonPath("[*]", hasSize(equalTo(1))));

    }

    @Test
    void administratorCanModifyAddress() throws Exception {

        PersonJsonBuilder tracy = tracy()
                .withAddress("17 Kew Drive, Borrowdale, Harare, 2345WP");

        String id = client.createPerson(tracy);

        //change address
        tracy.withAddress("18 Fisher Avenue, Borrowdale, Harare, 2345WP");

        mvc.perform(put(personById(id))
                .with(administratorCredentials())
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                .content(tracy.toJson()))
                .andExpect(status().isOk());

        mvc.perform(get(personById(id)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("address", is("18 Fisher Avenue, Borrowdale, Harare, 2345WP")));

    }

    @Test
    void nonAdministratorCannotModifyAddress() throws Exception {

        PersonJsonBuilder tracy = tracy()
                .withAddress("17 Kew Drive, Borrowdale, Harare, 2345WP");

        String id = client.createPerson(tracy);

        //change address
        tracy.withAddress("18 Fisher Avenue, Borrowdale, Harare, 2345WP");

        mvc.perform(put(personById(id))
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                .content(tracy.toJson()))
                .andExpect(status().isUnauthorized());

    }

    @Test
    void evenAdminCannotModifyFirstName() throws Exception {

        PersonJsonBuilder tracy = tracy()
                .withFirstName("Tracy");

        String id = client.createPerson(tracy);

        //change firstName
        tracy.withFirstName("Hilda");

        mvc.perform(put(personById(id))
                .with(administratorCredentials())
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                .content(tracy.toJson()))
                .andExpect(status().isForbidden());

    }

    @Test
    void evenAdminCannotModifyLastName() throws Exception {

        PersonJsonBuilder tracy = tracy()
                .withLastName("Lane");

        String id = client.createPerson(tracy);

        //change lastName
        tracy.withLastName("Schwartz");

        mvc.perform(put(personById(id))
                .with(administratorCredentials())
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                .content(tracy.toJson()))
                .andExpect(status().isForbidden());

    }

    @Test
    void evenAdminCannotModifyDateOfBirth() throws Exception {

        PersonJsonBuilder tracy = tracy()
                .withDateOfBirth("20/03/1984");

        String id = client.createPerson(tracy);

        //change dateOfBirth
        tracy.withDateOfBirth("21/11/1991");


        mvc.perform(put(personById(id))
                .with(administratorCredentials())
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                .content(tracy.toJson()))
                .andExpect(status().isForbidden());

    }

    private static RequestPostProcessor administratorCredentials() {
        return httpBasic("admin", "T3st_P@55W0rd");
    }

    @Test
    void deletePerson() throws Exception {

        PersonJsonBuilder tracy = tracy();

        String id = client.createPerson(tracy);

        //confirm exists
        mvc.perform(get(personById(id)))
                .andExpect(status().isOk());

        mvc.perform(delete(personById(id))).andExpect(status().isNoContent());

        //confirm does not exist
        mvc.perform(get(personById(id)))
                .andExpect(status().isNotFound());

    }

    private PersonJsonBuilder tracy() {
        return PersonJsonBuilder.aPerson()
                .withFirstName("Tracy")
                .withLastName("Lane")
                .withDateOfBirth("20/03/1984")
                .withAddress("17 Kew Drive, Borrowdale, Harare, 2345WP");
    }

    private PersonJsonBuilder paul() {
        return PersonJsonBuilder.aPerson()
                .withFirstName("Paul")
                .withLastName("Schwartz")
                .withDateOfBirth("20/04/1983")
                .withAddress("12 Fisher Avenue, Borrowdale, Harare, 1036EG");
    }

    private String personById(String id) {
        return String.format("/person/%s/", id);
    }


}

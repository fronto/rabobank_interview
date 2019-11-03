package nl.rabobank.test;

import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class HelloControllerIT {

    @Autowired
    private MockMvc mvc;

    private PersonServiceRestClient client;

    @BeforeEach
    void setUpClient() {
        client = new PersonServiceRestClient(mvc);
    }

    @Test
    public void getHello() throws Exception {
        mvc.perform(get("/").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("Greetings from Spring Boot!\n")));
    }

    @Test
    public void addPerson() throws Exception {

        PersonJsonBuilder tracy = aPerson()
                .withFirstName("Tracy")
                .withLastName("Lane")
                .withDateOfBirth("20/03/1984")
                .withAddress("17 Kew Drive, Borrowdale, Harare, 2345WP");

        mvc.perform(post("/person").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
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

        PersonJsonBuilder tracy = aPerson()
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
    void canModifyAddress() throws Exception {

        PersonJsonBuilder tracy = tracy()
                .withAddress("17 Kew Drive, Borrowdale, Harare, 2345WP");

        String id = client.createPerson(tracy);

        //TODO restrict change to address change
        //change address
        tracy.withAddress("18 Fisher Avenue, Borrowdale, Harare, 2345WP");

        mvc.perform(put(personById(id)).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                .content(tracy.toJson()))
                .andExpect(status().isOk());

        mvc.perform(get(personById(id)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("address", is("18 Fisher Avenue, Borrowdale, Harare, 2345WP")));

    }

    @Test
    void cannotModifyFirstName() throws Exception {

        PersonJsonBuilder tracy = tracy()
                .withFirstName("Tracy");

        String id = client.createPerson(tracy);

        //change firstName
        tracy.withFirstName("Hilda");

        mvc.perform(put(personById(id)).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                .content(tracy.toJson()))
                .andExpect(status().isForbidden());

    }

    @Test
    void cannotModifyLastName() throws Exception {

        PersonJsonBuilder tracy = tracy()
                .withLastName("Lane");

        String id = client.createPerson(tracy);

        //change firstName
        tracy.withLastName("Schwartz");

        mvc.perform(put(personById(id)).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                .content(tracy.toJson()))
                .andExpect(status().isForbidden());

    }

    @Test
    void cannotModifyDateOfBirth() throws Exception {

        PersonJsonBuilder tracy = tracy()
                .withDateOfBirth("20/03/1984");

        String id = client.createPerson(tracy);

        //change firstName
        tracy.withDateOfBirth("21/11/1991");

        mvc.perform(put(personById(id)).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                .content(tracy.toJson()))
                .andExpect(status().isForbidden());


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
        return aPerson()
                .withFirstName("Tracy")
                .withLastName("Lane")
                .withDateOfBirth("20/03/1984")
                .withAddress("17 Kew Drive, Borrowdale, Harare, 2345WP");
    }

    private String personById(String id) {
        return String.format("/person/%s/", id);
    }

    @AllArgsConstructor
    static class PersonServiceRestClient {

        private final MockMvc mockMvc;

        String createPerson(PersonJsonBuilder person) {
            try {
                MvcResult result = mockMvc.perform(post("/person").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                        .content(person.toJson()))
                        .andExpect(status().isCreated()).andReturn();
                return obtainId(result);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }


        private String obtainId(MvcResult result) {
            try {
                return JsonPath.parse(result.getResponse().getContentAsString()).read("id");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private static PersonJsonBuilder aPerson() {
        return new PersonJsonBuilder();
    }

    static class PersonJsonBuilder {

        private final Map<String, String> fields = new HashMap<>();

        PersonJsonBuilder withFirstName(String firstName) {
            fields.put("firstName", firstName);
            return this;
        }

        PersonJsonBuilder withLastName(String lastName) {
            fields.put("lastName", lastName);
            return this;
        }

        PersonJsonBuilder withDateOfBirth(String dateOfBirth) {
            fields.put("dateOfBirth", dateOfBirth);
            return this;
        }

        PersonJsonBuilder withAddress(String address) {
            fields.put("address", address);
            return this;
        }

        String toJson() {
            Gson gson = new Gson();
            return gson.toJson(fields);
        }

    }



}

package nl.rabobank.test;

import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class HelloControllerIT {

    @Autowired
    private MockMvc mvc;

    @Test
    public void getHello() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("Greetings from Spring Boot!\n")));
    }

    @Test
    public void addPerson() throws Exception {

        PersonJsonBuilder tracy = person()
                .withFirstName("Tracy")
                .withLastName("Lane")
                .withDateOfBirth("20/03/1984")
                .withAddress("17 Kew Drive, Borrowdale, Harare, 2345WP");

        mvc.perform(MockMvcRequestBuilders.post("/person").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
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

        PersonJsonBuilder tracy = person()
                .withFirstName("Tracy")
                .withLastName("Lane")
                .withDateOfBirth("20/03/1984")
                .withAddress("17 Kew Drive, Borrowdale, Harare, 2345WP");

        PersonServiceRestClient client = new PersonServiceRestClient(mvc);
        String id = client.createPerson(tracy);

        mvc.perform(MockMvcRequestBuilders.get(String.format("/person/%s/", id)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("firstName", is("Tracy")))
                .andExpect(jsonPath("lastName", is("Lane")))
                .andExpect(jsonPath("dateOfBirth", is("20/03/1984")))
                .andExpect(jsonPath("address", is("17 Kew Drive, Borrowdale, Harare, 2345WP")));

    }

    @Test
    void modifyPerson() throws Exception {

        PersonJsonBuilder tracy = person()
                .withFirstName("Tracy")
                .withLastName("Lane")
                .withDateOfBirth("20/03/1984")
                .withAddress("17 Kew Drive, Borrowdale, Harare, 2345WP");

        PersonServiceRestClient client = new PersonServiceRestClient(mvc);
        String id = client.createPerson(tracy);

        //TODO restrict change to address change
        //change address
        tracy.withAddress("18 Fisher Avenue, Borrowdale, Harare, 2345WP");

        mvc.perform(MockMvcRequestBuilders.put(String.format("/person/%s/", id)).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                .content(tracy.toJson()))
                .andExpect(status().isOk());

        mvc.perform(MockMvcRequestBuilders.get(String.format("/person/%s/", id)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("firstName", is("Tracy")))
                .andExpect(jsonPath("lastName", is("Lane")))
                .andExpect(jsonPath("dateOfBirth", is("20/03/1984")))
                .andExpect(jsonPath("address", is("18 Fisher Avenue, Borrowdale, Harare, 2345WP")));

    }

    @Test
    void deletePerson() throws Exception {

        PersonJsonBuilder tracy = person()
                .withFirstName("Tracy")
                .withLastName("Lane")
                .withDateOfBirth("20/03/1984")
                .withAddress("17 Kew Drive, Borrowdale, Harare, 2345WP");


        PersonServiceRestClient client = new PersonServiceRestClient(mvc);
        String id = client.createPerson(tracy);

        //confirm exists
        mvc.perform(MockMvcRequestBuilders.get(String.format("/person/%s/", id)))
                .andExpect(status().isOk());

        mvc.perform(MockMvcRequestBuilders.delete(String.format("/person/%s/", id))).andExpect(status().isNoContent());

        //confirm does not exist
        mvc.perform(MockMvcRequestBuilders.get(String.format("/person/%s/", id)))
                .andExpect(status().isNotFound());


    }

    @AllArgsConstructor
    static class PersonServiceRestClient {

        final MockMvc mockMvc;

        String createPerson(PersonJsonBuilder person) {
            try {
                MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/person").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
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

    private PersonJsonBuilder person() {
        return new PersonJsonBuilder();
    }

    static class PersonJsonBuilder {

        final private Map<String, String> fields = new HashMap<>();

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

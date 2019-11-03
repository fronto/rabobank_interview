package nl.rabobank.test;

import com.jayway.jsonpath.JsonPath;
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

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/person").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        " \"firstName\" : \"Tracy\" ," +
                        " \"lastName\" : \"Lane\" ," +
                        " \"dateOfBirth\" : \"20/03/1984\" ," +
                        " \"address\" : \"17 Kew Drive, Borrowdale, Harare, 2345WP\" " +
                        "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id", notNullValue())).andReturn();

    }

    private String obtainId(MvcResult result) {
        try {
            return JsonPath.parse(result.getResponse().getContentAsString()).read("id");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void retrieveAPerson() throws Exception {

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/person").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                .content("{ " +
                        " \"firstName\" : \"Tracy\" ," +
                        " \"lastName\" : \"Lane\" ," +
                        " \"dateOfBirth\" : \"20/03/1984\" ," +
                        " \"address\" : \"17 Kew Drive, Borrowdale, Harare, 2345WP\" " +
                        "}"))
                .andExpect(status().isCreated()).andReturn();

        String id = obtainId(result);

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

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/person").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                .content("{ " +
                        " \"firstName\" : \"Tracy\" ," +
                        " \"lastName\" : \"Lane\" ," +
                        " \"dateOfBirth\" : \"20/03/1984\" ," +
                        " \"address\" : \"17 Kew Drive, Borrowdale, Harare, 2345WP\" " +
                        "}"))
                .andExpect(status().isCreated()).andReturn();

        String id = obtainId(result);

        //change address
        mvc.perform(MockMvcRequestBuilders.put(String.format("/person/%s/", id)).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                .content("{ " +
                        " \"firstName\" : \"Tracy\" ," +
                        " \"lastName\" : \"Lane\" ," +
                        " \"dateOfBirth\" : \"20/03/1984\" ," +
                        " \"address\" : \"18 Fisher Avenue, Borrowdale, Harare, 2345WP\" " +
                        "}"))
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
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/person").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                .content("{ " +
                        " \"firstName\" : \"Tracy\" ," +
                        " \"lastName\" : \"Lane\" ," +
                        " \"dateOfBirth\" : \"20/03/1984\" ," +
                        " \"address\" : \"17 Kew Drive, Borrowdale, Harare, 2345WP\" " +
                        "}"))
                .andExpect(status().isCreated()).andReturn();

        String id = obtainId(result);

        //confirm exists
        mvc.perform(MockMvcRequestBuilders.get(String.format("/person/%s/", id)))
                .andExpect(status().isOk());

        mvc.perform(MockMvcRequestBuilders.delete(String.format("/person/%s/", id))).andExpect(status().isNoContent());

    }

}

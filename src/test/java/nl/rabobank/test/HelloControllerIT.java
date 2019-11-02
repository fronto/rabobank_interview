package nl.rabobank.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
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

        mvc.perform(MockMvcRequestBuilders.post("/person").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        " \"firstName\" : \"Tracy\" ," +
                        " \"lastName\" : \"Lane\" ," +
                        " \"dateOfBirth\" : \"20/03/1984\" ," +
                        " \"address\" : \"17 Kew Drive, Borrowdale, Harare, 2345WP\" " +
                        "}"))
                .andExpect(status().isCreated());

    }

    @Test
    void retrieveAPerson() throws Exception {

        mvc.perform(MockMvcRequestBuilders.post("/person").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                .content("{ " +
                        " \"firstName\" : \"Tracy\" ," +
                        " \"lastName\" : \"Lane\" ," +
                        " \"dateOfBirth\" : \"20/03/1984\" ," +
                        " \"address\" : \"17 Kew Drive, Borrowdale, Harare, 2345WP\" " +
                        "}"))
                .andExpect(status().isCreated());

        mvc.perform(MockMvcRequestBuilders.get("/person"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("firstName", is("Tracy")))
                .andExpect(jsonPath("lastName", is("Lane")))
                .andExpect(jsonPath("dateOfBirth", is("20/03/1984")))
                .andExpect(jsonPath("address", is("17 Kew Drive, Borrowdale, Harare, 2345WP")));

    }

}

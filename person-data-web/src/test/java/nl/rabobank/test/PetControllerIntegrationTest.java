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

import static nl.rabobank.test.PetJsonBuilder.pet;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = IntegrationTestConfiguration.class, properties = {"spring.jpa.hibernate.ddl-auto=create-drop"})
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PetControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    PersonServiceRestClient client;

    @BeforeEach
    void setUpClient() {
        this.client = new PersonServiceRestClient(mockMvc);
    }

    @Test
    void canCreateAPet() throws Exception {

        PetJsonBuilder pet = pet().withName("Snoopy").withAge(4);

        mockMvc.perform(post("/pet/").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                .content(pet.toJson()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id", notNullValue()))
                .andExpect(jsonPath("name", is("Snoopy")))
                .andExpect(jsonPath("age", is(4)));

    }

    @Test
    void canRetrieveAPet() throws Exception {

        PetJsonBuilder pet = pet().withName("Snoopy").withAge(4);
        String id = client.createPet(pet);

        mockMvc.perform(get(petById(id)).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(id)))
                .andExpect(jsonPath("name", is("Snoopy")))
                .andExpect(jsonPath("age", is(4)));

    }

    @Test
    void canModifyAPetAge() throws Exception {

        PetJsonBuilder pet = pet().withName("Snoopy").withAge(4);
        String id = client.createPet(pet);

        mockMvc.perform(put(petById(id)).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                .content(pet.withAge(3).toJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("age", is(3)));

    }

    @Test
    void canModifyPetName() throws Exception {

        PetJsonBuilder pet = pet().withName("Snoopy").withAge(4);
        String id = client.createPet(pet);

        mockMvc.perform(put(petById(id)).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                .content(pet.withName("Odie").toJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is("Odie")));


    }

    @Test
    void canDeleteAPet() throws Exception {

        PetJsonBuilder pet = pet().withName("Snoopy").withAge(4);
        String id = client.createPet(pet);

        mockMvc.perform(delete(petById(id))).andExpect(status().isNoContent());
        mockMvc.perform(get(petById(id))).andExpect(status().isNotFound());


    }

    private String petById(String id) {
        return String.format("/pet/%s/", id);
    }

}

package nl.rabobank.test;

import com.jayway.jsonpath.JsonPath;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AllArgsConstructor
class PersonServiceRestClient {

    private final MockMvc mockMvc;

    String createPerson(PersonJsonBuilder person) {
        try {
            MvcResult result = mockMvc.perform(post("/stub/person").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
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

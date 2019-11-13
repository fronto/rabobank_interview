package nl.rabobank.test;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

class PetJsonBuilder {

    private Map<String, String> fields;

    static PetJsonBuilder pet() {
        Map<String, String> fields = new HashMap<>();
        return new PetJsonBuilder(fields);
    }

    private PetJsonBuilder(Map<String, String> fields) {
        this.fields = fields;
    }

    PetJsonBuilder withName(String name) {
        fields.put("name", name);
        return this;
    }

    PetJsonBuilder withAge(Integer age) {
        fields.put("age", age.toString());
        return this;
    }

    String toJson() {
        Gson gson = new Gson();
        return gson.toJson(fields);
    }


}

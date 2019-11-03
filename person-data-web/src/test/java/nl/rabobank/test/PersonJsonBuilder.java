package nl.rabobank.test;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

class PersonJsonBuilder {

    private final Map<String, String> fields = new HashMap<>();

    static PersonJsonBuilder aPerson() {
        return new PersonJsonBuilder();
    }

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

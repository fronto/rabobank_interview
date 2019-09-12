package nl.rabobank;


import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Pojo {

    private final String value;

    void method1() {
        System.out.println("foo");
    }
}

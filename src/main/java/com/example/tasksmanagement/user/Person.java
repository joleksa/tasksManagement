package com.example.tasksmanagement.user;

import java.util.Collections;
import java.util.List;

public final class Person {
    private final String name;
    private final String surname;
    private final List<Adres> adresList;

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public List<Adres> getAdresList() {
        return List.copyOf(adresList);
    }

    public Person(String name, String surname, List<Adres> adresList) {
        this.name = name;
        this.surname = surname;
        this.adresList = Collections.unmodifiableList(adresList);
    }
}

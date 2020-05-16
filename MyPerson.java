package com.oocourse.spec3.main;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class MyPerson implements Person {
    private int id;
    private String name;
    private BigInteger character;
    private int age;
    private ArrayList<Person> acquaintance = new ArrayList<>();
    private HashMap<Person,Integer> value = new HashMap<>();

    public MyPerson(int id,String name,BigInteger character,int age) {
        this.id = id;
        this.name = name;
        this.character = character;
        this.age = age;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public BigInteger getCharacter() {
        return character;
    }

    @Override
    public int getAge() {
        return age;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Person)) {
            return false;
        }
        else {
            return  (((Person) obj).getId() == id);
        }
    }

    @Override
    public boolean isLinked(Person person) {
        return acquaintance.contains(person) || person.getId() == id;
    }

    @Override
    public int queryValue(Person person) {
        if (acquaintance.contains(person)) {
            return value.get(person);
        }
        else {
            return 0;
        }
    }

    @Override
    public int getAcquaintanceSum() {
        return acquaintance.size();
    }

    @Override
    public int compareTo(Person p2) {
        return name.compareTo(p2.getName());
    }

    public void addAcquaintance(Person person,int value) {
        acquaintance.add(person);
        this.value.put(person,value);

    }
}

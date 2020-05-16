package com.oocourse.spec3.main;

import java.math.BigInteger;
import java.util.ArrayList;

public class MyGroup implements Group {
    private int id;
    private ArrayList<Person> people = new ArrayList<>();

    public MyGroup(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Group) {
            return (((Group) obj).getId() == id);
        }
        else {
            return false;
        }
    }

    @Override
    public void addPerson(Person person) {
        people.add(person);
    }

    @Override
    public boolean hasPerson(Person person) {
        return  (people.contains(person));
    }

    @Override
    public int getRelationSum() {
        int sum = 0;
        for (Person person1 : people) {
            for (Person person2 : people) {
                if (person1.isLinked(person2)) {
                    sum++;
                }
            }
        }
        return sum;
    }

    @Override
    public int getValueSum() {
        int sum = 0;
        for (Person person1 : people) {
            for (Person person2 : people) {
                if (person1.isLinked(person2)) {
                    sum = sum + person1.queryValue(person2);
                }
            }
        }
        return sum;
    }

    @Override
    public BigInteger getConflictSum() {

        if (people.size() == 0) {
            return BigInteger.ZERO;
        }
        else {
            BigInteger out = new BigInteger("0");
            ArrayList<BigInteger> temp = new ArrayList<>();
            temp.add(0,people.get(0).getCharacter());
            for (int i = 1;i < temp.size();i++) {
                temp.add(i,temp.get(i - 1).xor(people.get(i).getCharacter()));
            }
            return temp.get(temp.size() - 1);
        }
    }

    @Override
    public int getAgeMean() {
        if (people.size() == 0) {
            return 0;
        }
        else {
            int sum = 0;
            int size = people.size();
            for (Person person : people) {
                sum = sum + person.getAge() / size;
            }
            return sum;
        }
    }

    @Override
    public int getAgeVar() {
        if (people.size() == 0) {
            return 0;
        }
        else {
            int sum = 0;
            for (Person person : people) {
                sum = sum + (person.getAge() - getAgeMean()) * (person.getAge() - getAgeMean());
            }
            return  sum;
        }
    }

    @Override
    public void delPerson(Person person) {
        people.remove(person);
    }

    public int getSize() {
        return people.size();
    }
}

package com.oocourse.spec3.main;

import com.oocourse.spec3.exceptions.EqualGroupIdException;
import com.oocourse.spec3.exceptions.EqualPersonIdException;
import com.oocourse.spec3.exceptions.EqualRelationException;
import com.oocourse.spec3.exceptions.GroupIdNotFoundException;
import com.oocourse.spec3.exceptions.PersonIdNotFoundException;
import com.oocourse.spec3.exceptions.RelationNotFoundException;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class MyNetwork implements Network {
    private ArrayList<Person> people = new ArrayList<>();
    private HashMap<Integer,Person> peopleMap = new HashMap<>(800);
    private ArrayList<Group> groups = new ArrayList<>();
    private HashMap<Integer,Integer> money = new HashMap<>();
    private ArrayList<Integer> path = new ArrayList<>();
    private ArrayList<ArrayList<Person>> map = new ArrayList<>();
    private int valueSum = 0;

    public MyNetwork(){
    }

    @Override
    public boolean contains(int id) {
        return peopleMap.containsKey(id);
    }

    @Override
    public Person getPerson(int id) {
        return peopleMap.getOrDefault(id, null);
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        if (contains(person.getId())) {
            throw new EqualPersonIdException();
        }
        else {
            people.add(person);
            peopleMap.put(person.getId(),person);
            money.put(person.getId(),0);
        }
    }

    @Override
    public void addRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualRelationException {
        if (!contains(id1) || !contains(id2)) {
            throw new PersonIdNotFoundException();
        }
        else if (getPerson(id1).isLinked(getPerson(id2)) && id1 != id2) {
            throw new EqualRelationException();
        }
        else if (id1 != id2 && !getPerson(id1).isLinked(getPerson(id2))) {
            Person person1 = getPerson(id1);
            Person person2 = getPerson(id2);
            ((MyPerson)person1).addAcquaintance(person2,value);
            ((MyPerson)person2).addAcquaintance(person1,value);
        }
    }

    @Override
    public int queryValue(int id1, int id2) throws
            PersonIdNotFoundException, RelationNotFoundException {
        if (!contains(id1) || !contains(id2)) {
            throw new PersonIdNotFoundException();
        }
        else if (!getPerson(id1).isLinked(getPerson(id2))) {
            throw new RelationNotFoundException();
        }
        else {
            return getPerson(id1).queryValue(getPerson(id2));
        }
    }

    @Override
    public BigInteger queryConflict(int id1, int id2) throws PersonIdNotFoundException {
        if (!contains(id1) || !contains(id2)) {
            throw new PersonIdNotFoundException();
        }
        else {
            return getPerson(id1).getCharacter().xor(getPerson(id2).getCharacter());
        }
    }

    @Override
    public int queryAcquaintanceSum(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new PersonIdNotFoundException();
        }
        else {
            return getPerson(id).getAcquaintanceSum();
        }
    }

    @Override
    public int compareAge(int id1, int id2) throws PersonIdNotFoundException {
        if (!contains(id1) || !contains(id2)) {
            throw new PersonIdNotFoundException();
        }
        else {
            return getPerson(id1).getAge() - getPerson(id2).getAge();
        }
    }

    @Override
    public int compareName(int id1, int id2) throws PersonIdNotFoundException {
        if (!contains(id1) || !contains(id2)) {
            throw new PersonIdNotFoundException();
        }
        else {
            return getPerson(id1).getName().compareTo(getPerson(id2).getName());
        }
    }

    @Override
    public int queryPeopleSum() {
        return people.size();
    }

    @Override
    public int queryNameRank(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new PersonIdNotFoundException();
        }
        else {
            int sum = 1;
            for (Person person : people) {
                if (compareName(id,person.getId()) > 0) {
                    sum++;
                }
            }
            return sum;
        }
    }

    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (!contains(id1) || !contains(id2)) {
            throw new PersonIdNotFoundException();
        }
        else {
            Person person1 = getPerson(id1);
            Person person2 = getPerson(id2);
            ArrayList<Person> temp = new ArrayList<>();
            temp.add(person1);
            return Circle(person1,person2,people,temp);

        }
    }

    private boolean Circle(Person person1,Person person2,
                          ArrayList<Person> people,ArrayList<Person> link) {
        if (person1.isLinked(person2)) {
            return true;
        }
        if (people.size() == 0) {
            return false;
        }
        for (Person person : people) {
            if (person1.isLinked(person) && !link.contains(person)) {
                link.add(person);
                if (Circle(person,person2,people,link)) {
                    return true;
                }
                else {
                    link.remove(person);
                }
            }
        }
        return false;
    }

    @Override
    public void addGroup(Group group) throws EqualGroupIdException {
        if (GroupContains(group.getId())) {
            throw new EqualGroupIdException();
        }
        else {
            groups.add(group);
        }
    }

    @Override
    public Group getGroup(int id) {
        if (groups.size() == 0) {
            return null;
        }
        for (Group group : groups) {
            if (group.getId() == id) {
                return group;
            }
        }
        return null;
    }

    @Override
    public void addtoGroup(int id1, int id2) throws
            GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {

        boolean one = contains(id1);
        boolean two = GroupContains(id2);

        if (!one || !two) {
            if (!two) {
                throw new GroupIdNotFoundException();
            }
            else  {
                throw new PersonIdNotFoundException();
            }
        }
        else {
            Group group = getGroup(id2);
            Person person = getPerson(id1);
            boolean three = group.hasPerson(person);
            boolean four = ((MyGroup)group).getSize() < 1111;
            if (three) {
                throw new EqualPersonIdException();
            }
            else if (four) {
                group.addPerson(person);
            }
        }

    }

    private boolean GroupContains(int id) {
        for (Group group : groups) {
            if (group.getId() == id) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int queryGroupSum() {
        return groups.size();
    }

    @Override
    public int queryGroupPeopleSum(int id) throws GroupIdNotFoundException {
        if (!GroupContains(id)) {
            throw new GroupIdNotFoundException();
        }
        else {
            return ((MyGroup)getGroup(id)).getSize();
        }
    }

    @Override
    public int queryGroupRelationSum(int id) throws GroupIdNotFoundException {
        if (!GroupContains(id)) {
            throw new GroupIdNotFoundException();
        }
        else {
            return getGroup(id).getRelationSum();
        }
    }

    @Override
    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        if (!GroupContains(id)) {
            throw new GroupIdNotFoundException();
        }
        else {
            return getGroup(id).getValueSum();
        }
    }

    @Override
    public BigInteger queryGroupConflictSum(int id) throws GroupIdNotFoundException {
        if (!GroupContains(id)) {
            throw new GroupIdNotFoundException();
        }
        else {
            return getGroup(id).getConflictSum();
        }
    }

    @Override
    public int queryGroupAgeMean(int id) throws GroupIdNotFoundException {
        if (!GroupContains(id)) {
            throw new GroupIdNotFoundException();
        }
        else {
            return getGroup(id).getAgeMean();
        }
    }

    @Override
    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        if (!GroupContains(id)) {
            throw new GroupIdNotFoundException();
        }
        else {
            return getGroup(id).getAgeVar();
        }
    }

    @Override
    public int queryAgeSum(int l, int r) {
        int sum = 0;
        for (Person person : people) {
            int age = person.getAge();
            if (age >= l && age <= r) {
                sum++;
            }
        }
        return sum;
    }

    @Override
    public void delFromGroup(int id1, int id2) throws GroupIdNotFoundException,
            PersonIdNotFoundException, EqualPersonIdException {
        if (!groups.contains(getGroup(id2))) {
            throw new GroupIdNotFoundException();
        }
        else if (!contains(id1)) {
            throw new PersonIdNotFoundException();
        }
        else if (!getGroup(id2).hasPerson(getPerson(id1))) {
            throw  new EqualPersonIdException();
        }
        else {
            getGroup(id2).delPerson(getPerson(id1));
        }
    }

    @Override
    public int queryMinPath(int id1, int id2) throws PersonIdNotFoundException {
        if (!contains(id1) || !contains(id2)) {
            throw new PersonIdNotFoundException();
        }
        else {
            if (id1 == id2) {
                return 0;
            }
            else if (!isCircle(id1, id2)) {
                return -1;
            }
            else {
                ArrayList<Person> temp = new ArrayList<>();
                Person person1 = getPerson(id1);
                temp.add(person1);
                valueSum = 0;
                path.clear();
                Person person2 = getPerson(id2);
                getPath(person1,person2,people,temp);
                int min = 0;
                for (int x : path) {
                    if (x > min) {
                        min = x;
                    }
                }
                return min;
            }
        }
    }

    private void getPath(Person person1,Person person2,
                           ArrayList<Person> people,ArrayList<Person> link) {
        if (person1.isLinked(person2)) {
            valueSum = valueSum + person1.queryValue(person2);
            path.add(valueSum);
            valueSum = 0;
            return;
        }
        else if (people.size() == 0) {
            return;
        }
        for (Person person : people) {
            if (person1.isLinked(person) && !link.contains(person)) {
                link.add(person);
                valueSum = valueSum + person1.queryValue(person);
                getPath(person,person2,people,link);
                link.remove(person);
            }
        }
    }

    @Override
    public boolean queryStrongLinked(int id1, int id2) throws PersonIdNotFoundException {
        if (!contains(id1) || !contains(id2)) {
            throw new PersonIdNotFoundException();
        }
        else {
            if (id1 == id2) {
                return true;
            }
            else {
                Person person1 = getPerson(id1);
                Person person2 = getPerson(id2);
                ArrayList<Person> temp = new ArrayList<>();
                temp.add(person1);
                valueSum = 0;
                getStrong(person1,person2,people,temp);
                int min = 0;
                for (int x : path) {
                    if (x > min) {
                        min = x;
                    }
                }
                int one = path.indexOf(min);
                int two = path.lastIndexOf(min);
                if (two != one) {
                    ArrayList<Person> temp1 = map.get(one);
                    ArrayList<Person> temp2 = map.get(two);
                    if (temp1.size() != temp2.size()) {
                        return false;
                    }
                    else {
                        for (int i = 0;i < temp1.size();i++) {
                            if (!temp1.get(i).equals(temp2.get(i))) {
                                return false;
                            }
                        }
                        return true;
                    }
                }
                return false;
            }
        }
    }

    private void getStrong(Person person1,Person person2,
                         ArrayList<Person> people,ArrayList<Person> link) {
        if (person1.isLinked(person2)) {
            valueSum = valueSum + person1.queryValue(person2);
            ArrayList<Person> temp = new ArrayList<>(link);
            temp.add(person2);
            path.add(valueSum);
            map.add(temp);
            valueSum = 0;
            return;
        }
        else if (people.size() == 0) {
            return;
        }
        for (Person person : people) {
            if (person1.isLinked(person) && !link.contains(person)) {
                link.add(person);
                valueSum = valueSum + person1.queryValue(person);
                getStrong(person,person2,people,link);
                link.remove(person);
            }
        }
    }

    @Override
    public int queryBlockSum() {
        int sum = 1;
        for (int i = 0; i < people.size(); i++) {
            for (int j = 0; j < i; j++) {
                try {
                    if (!isCircle(people.get(i).getId(),people.get(j).getId())) {
                        sum++;
                    }
                } catch (PersonIdNotFoundException e) {
                    //e.printStackTrace();
                }
            }
        }
        return sum;
    }

    @Override
    public void borrowFrom(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualPersonIdException {
        if (!contains(id1) || !contains(id2)) {
            throw new PersonIdNotFoundException();
        }
        else {
            if (id1 == id2) {
                throw new EqualPersonIdException();
            }
            else {
                money.put(id1,money.get(id1) - value);
                money.put(id2,money.get(id2) + value);
            }
        }
    }

    @Override
    public int queryMoney(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new PersonIdNotFoundException();
        }
        else {
            return money.get(id);
        }
    }
}

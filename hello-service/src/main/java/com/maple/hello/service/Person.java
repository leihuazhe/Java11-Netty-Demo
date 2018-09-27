package com.maple.hello.service;

/**
 * @author maple 2018.09.27 上午9:53
 */
public class Person {
    private String name;
    private int age;
    private String hobbor;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getHobbor() {
        return hobbor;
    }

    public void setHobbor(String hobbor) {
        this.hobbor = hobbor;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", hobbor='" + hobbor + '\'' +
                '}';
    }
}

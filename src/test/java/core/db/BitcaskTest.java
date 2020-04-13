package core.db;

import org.junit.Test;

public class BitcaskTest {

    Bitcask bitcask = Bitcask.newInstance();

    @Test
    public void put() {
        Dog dog = new Dog(1, "mary");
        Cat cat = new Cat(4, "tom");
        bitcask.put("dog", dog.toString());
        bitcask.put("cat", cat.toString());
    }

    @Test
    public void remove() {
    }

    @Test
    public void get() {
        for (int i = 0; i < 5; i++) {
            Dog dog1 = new Dog(1, "mary");
            Cat cat1 = new Cat(4, "tom");
            bitcask.put("dog", dog1.toString());
            bitcask.put("cat", cat1.toString());
            String dog = bitcask.get("dog").get();
            String cat = bitcask.get("cat").get();
            System.out.println(dog);
            System.out.println(cat);
            bitcask.remove("dog");
        }



    }

    @Test
    public void testGet() {
    }
}

class Dog{

    private int age;

    private String name;

    public Dog() {
    }

    public Dog(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Dog{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}

class Cat{

    private int age;

    private String name;

    public Cat() {
    }

    public Cat(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Cat{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}
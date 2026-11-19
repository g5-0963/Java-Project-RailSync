package model;

public class Passenger {

    private int passengerId;
    private String name;
    private int age;
    private String gender;
    private String phone;

    public Passenger(int passengerId, String name, int age,
                     String gender, String phone) {

        this.passengerId = passengerId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return passengerId + " - " + name
                + " | Age: " + age
                + " | Gender: " + gender
                + " | Phone: " + phone;
    }
}

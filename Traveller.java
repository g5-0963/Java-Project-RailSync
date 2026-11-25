
package model;

public class Traveller {

    private String name;
    private int age;
    private String gender;
    private String phone;
    private String email;
    private String seatNumber;

    public Traveller(String name, int age,
                     String gender, String phone,
                     String email, String seatNumber) {

        this.name = name;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.seatNumber = seatNumber;
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

    public String getEmail() {
        return email;
    }

    public String getSeatNumber() {
        return seatNumber;
    }
}

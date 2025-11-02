public class Patient {
    private int id;
    private String name;
    private String phone;
    private int age;

    public Patient(int id, String name, String phone, int age) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.age = age;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    
    // toString method for easy display
    @Override
    public String toString() {
        return "Patient ID: " + id + ", Name: " + name + ", Age: " + age + ", Phone: " + phone;
    }
}
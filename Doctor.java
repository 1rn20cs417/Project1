public class Doctor {
    private int id;
    private String name;
    private String specialization;

    public Doctor(int id, String name, String specialization) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getSpecialization() { return specialization; }

    // toString method for easy display
    @Override
    public String toString() {
        return "Doctor ID: " + id + ", Name: " + name + ", Specialization: " + specialization;
    }
}
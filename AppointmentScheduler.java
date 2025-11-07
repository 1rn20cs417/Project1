import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AppointmentScheduler {
    // Data structures to hold our records (in memory)
    private List<Patient> patients = new ArrayList<>();
    private List<Doctor> doctors = new ArrayList<>();
    private List<Appointment> appointments = new ArrayList<>();

    // Counters for generating unique IDs
    private int nextPatientId = 101;
    private int nextDoctorId = 201;
    private int nextAppointmentId = 301;
    
    private Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    public static void main(String[] args) {
        AppointmentScheduler app = new AppointmentScheduler();
        app.run();
    }
    
    /** Initializes sample data for testing. */
    private void initializeData() {
        patients.add(new Patient(nextPatientId++, "Alice Smith", "555-1234", 30));
        patients.add(new Patient(nextPatientId++, "Bob Johnson", "555-5678", 55));
        doctors.add(new Doctor(nextDoctorId++, "Dr. Chen", "Cardiology"));
        doctors.add(new Doctor(nextDoctorId++, "Dr. Garcia", "Pediatrics"));
        
        System.out.println("--- Initial Sample Data Loaded ---");
    }

    /** Main application loop. */
    public void run() {
        initializeData();
        int choice = -1;
        
        while (choice != 0) {
            System.out.println("\n--- Healthcare Appointment System ---");
            System.out.println("1. Register Patient");
            System.out.println("2. Add Doctor");
            System.out.println("3. Schedule Appointment");
            System.out.println("4. View All Appointments");
            System.out.println("5. View All Patients");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");

            try {
                choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1: registerPatient(); break;
                    case 2: addDoctor(); break;
                    case 3: scheduleAppointment(); break;
                    case 4: viewAppointments(); break;
                    case 5: viewPatients(); break;
                    case 0: System.out.println("Exiting system. Goodbye!"); break;
                    default: System.out.println("**Invalid choice. Please try again.**");
                }
            } catch (NumberFormatException e) {
                System.out.println("**Invalid input. Please enter a number.**");
            }
        }
    }

    // --- Core Methods ---
    
    private void registerPatient() {
        System.out.println("\n--- Register New Patient ---");
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Phone: ");
        String phone = scanner.nextLine();
        System.out.print("Enter Age: ");
        int age;
        try {
            age = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("**Invalid age entered. Registration failed.**");
            return;
        }

        Patient newPatient = new Patient(nextPatientId++, name, phone, age);
        patients.add(newPatient);
        System.out.println("SUCCESS: Patient registered. " + newPatient);
    }

    private void addDoctor() {
        System.out.println("\n--- Add New Doctor ---");
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Specialization: ");
        String specialization = scanner.nextLine();

        Doctor newDoctor = new Doctor(nextDoctorId++, name, specialization);
        doctors.add(newDoctor);
        System.out.println("SUCCESS: Doctor added. " + newDoctor);
    }

    private void scheduleAppointment() {
        System.out.println("\n--- Schedule Appointment ---");
        if (patients.isEmpty() || doctors.isEmpty()) {
            System.out.println("**Cannot schedule: No patients or doctors registered.**");
            return;
        }

        viewPatients();
        System.out.print("Enter Patient ID for appointment: ");
        int pId = Integer.parseInt(scanner.nextLine());
        
        if (patients.stream().noneMatch(p -> p.getId() == pId)) {
            System.out.println("**Error: Patient ID not found.**");
            return;
        }

        viewDoctors();
        System.out.print("Enter Doctor ID for appointment: ");
        int dId = Integer.parseInt(scanner.nextLine());

        if (doctors.stream().noneMatch(d -> d.getId() == dId)) {
            System.out.println("**Error: Doctor ID not found.**");
            return;
        }

        System.out.print("Enter Appointment Date/Time (format YYYY-MM-DD HH:MM): ");
        String dtString = scanner.nextLine();
        LocalDateTime dt;
        try {
            dt = LocalDateTime.parse(dtString, FORMATTER);
        } catch (DateTimeParseException e) {
            System.out.println("**Invalid date/time format. Scheduling failed. Use YYYY-MM-DD HH:MM.**");
            return;
        }

        // Simple check to prevent double booking on the same slot for the doctor
        if (appointments.stream().anyMatch(a -> a.getDoctorId() == dId && a.getDateTime().equals(dt))) {
            System.out.println("**ERROR: Doctor is already booked at this time.**");
            return;
        }
        
        Appointment newAppointment = new Appointment(nextAppointmentId++, pId, dId, dt);
        appointments.add(newAppointment);
        System.out.println("SUCCESS: Appointment scheduled. " + newAppointment);
    }

    private void viewAppointments() {
        System.out.println("\n--- All Scheduled Appointments ---");
        if (appointments.isEmpty()) {
            System.out.println("No appointments scheduled yet.");
        } else {
            for (Appointment a : appointments) {
                // To make the output richer, we can fetch names using the IDs
                String pName = patients.stream().filter(p -> p.getId() == a.getPatientId()).findFirst().map(Patient::getName).orElse("N/A");
                String dName = doctors.stream().filter(d -> d.getId() == a.getDoctorId()).findFirst().map(Doctor::getName).orElse("N/A");
                
                System.out.println(
                    a.getId() + 
                    " | Date: " + a.getDateTime().format(FORMATTER) + 
                    " | Patient: " + pName + " (ID:" + a.getPatientId() + ")" +
                    " | Doctor: " + dName + " (ID:" + a.getDoctorId() + ")"
                );
            }
        }
    }

    private void viewPatients() {
        System.out.println("\n--- All Registered Patients ---");
        if (patients.isEmpty()) {
            System.out.println("No patients registered.");
        } else {
            patients.forEach(System.out::println);
        }
    }

    private void viewDoctors() {
        System.out.println("\n--- All Registered Doctors ---");
        if (doctors.isEmpty()) {
            System.out.println("No doctors registered.");
        } else {
            doctors.forEach(System.out::println);
        }
    }
}
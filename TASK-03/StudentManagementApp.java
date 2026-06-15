import java.io.*;
import java.util.*;

class Student implements Serializable {
    private int rollNumber;
    private String name;
    private String grade;
    private String email;
    private double marks;

    public Student(int rollNumber, String name, String grade, String email, double marks) {
        this.rollNumber = rollNumber;
        this.name = name;
        this.grade = grade;
        this.email = email;
        this.marks = marks;
    }

    public int getRollNumber() { return rollNumber; }
    public String getName() { return name; }
    public String getGrade() { return grade; }
    public String getEmail() { return email; }
    public double getMarks() { return marks; }

    public void setName(String name) { this.name = name; }
    public void setGrade(String grade) { this.grade = grade; }
    public void setEmail(String email) { this.email = email; }
    public void setMarks(double marks) { this.marks = marks; }

    @Override
    public String toString() {
        return String.format("Roll No: %-5d | Name: %-15s | Grade: %-3s | Email: %-20s | Marks: %.2f",
                rollNumber, name, grade, email, marks);
    }

    public String toFileString() {
        return rollNumber + "," + name + "," + grade + "," + email + "," + marks;
    }

    public static Student fromFileString(String line) {
        String[] parts = line.split(",");
        return new Student(Integer.parseInt(parts[0]), parts[1], parts[2], parts[3], Double.parseDouble(parts[4]));
    }
}

class StudentManagementSystem {
    private List<Student> students;
    private final String FILE_NAME = "students.txt";

    public StudentManagementSystem() {
        students = new ArrayList<>();
        loadFromFile();
    }

    public boolean addStudent(Student s) {
        if (searchByRoll(s.getRollNumber()) != null) {
            return false; // duplicate roll number
        }
        students.add(s);
        saveToFile();
        return true;
    }

    public boolean removeStudent(int rollNumber) {
        Student s = searchByRoll(rollNumber);
        if (s != null) {
            students.remove(s);
            saveToFile();
            return true;
        }
        return false;
    }

    public Student searchByRoll(int rollNumber) {
        for (Student s : students) {
            if (s.getRollNumber() == rollNumber) return s;
        }
        return null;
    }

    public List<Student> searchByName(String name) {
        List<Student> result = new ArrayList<>();
        for (Student s : students) {
            if (s.getName().toLowerCase().contains(name.toLowerCase())) {
                result.add(s);
            }
        }
        return result;
    }

    public boolean updateStudent(int rollNumber, String name, String grade, String email, double marks) {
        Student s = searchByRoll(rollNumber);
        if (s == null) return false;
        if (name != null && !name.isEmpty()) s.setName(name);
        if (grade != null && !grade.isEmpty()) s.setGrade(grade);
        if (email != null && !email.isEmpty()) s.setEmail(email);
        if (marks >= 0) s.setMarks(marks);
        saveToFile();
        return true;
    }

    public List<Student> getAllStudents() {
        return students;
    }

    private void saveToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Student s : students) {
                pw.println(s.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    students.add(Student.fromFileString(line));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }
}

public class StudentManagementApp {
    private static Scanner scanner = new Scanner(System.in);
    private static StudentManagementSystem sms = new StudentManagementSystem();

    public static void main(String[] args) {
        int choice;
        do {
            printMenu();
            choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1: addStudent(); break;
                case 2: editStudent(); break;
                case 3: removeStudent(); break;
                case 4: searchStudent(); break;
                case 5: displayAllStudents(); break;
                case 6: System.out.println("Exiting... Goodbye!"); break;
                default: System.out.println("Invalid choice. Try again.");
            }
            System.out.println();
        } while (choice != 6);

        scanner.close();
    }

    private static void printMenu() {
        System.out.println("===== Student Management System =====");
        System.out.println("1. Add New Student");
        System.out.println("2. Edit Existing Student");
        System.out.println("3. Remove Student");
        System.out.println("4. Search Student");
        System.out.println("5. Display All Students");
        System.out.println("6. Exit");
        System.out.println("=======================================");
    }

    private static void addStudent() {
        System.out.println("--- Add New Student ---");

        int roll = readInt("Enter Roll Number: ");
        if (sms.searchByRoll(roll) != null) {
            System.out.println("Error: A student with this roll number already exists.");
            return;
        }

        String name = readNonEmptyString("Enter Name: ");
        String grade = readGrade("Enter Grade (A/B/C/D/F): ");
        String email = readEmail("Enter Email: ");
        double marks = readDouble("Enter Marks (0-100): ", 0, 100);

        Student s = new Student(roll, name, grade, email, marks);
        if (sms.addStudent(s)) {
            System.out.println("Student added successfully!");
        } else {
            System.out.println("Failed to add student.");
        }
    }

    private static void editStudent() {
        System.out.println("--- Edit Student ---");
        int roll = readInt("Enter Roll Number to edit: ");
        Student existing = sms.searchByRoll(roll);

        if (existing == null) {
            System.out.println("No student found with roll number " + roll);
            return;
        }

        System.out.println("Current details: " + existing);
        System.out.println("Leave field blank to keep current value.");

        System.out.print("New Name [" + existing.getName() + "]: ");
        String name = scanner.nextLine().trim();

        String grade = "";
        while (true) {
            System.out.print("New Grade [" + existing.getGrade() + "]: ");
            grade = scanner.nextLine().trim();
            if (grade.isEmpty() || isValidGrade(grade)) break;
            System.out.println("Invalid grade. Use A, B, C, D, or F.");
        }

        String email = "";
        while (true) {
            System.out.print("New Email [" + existing.getEmail() + "]: ");
            email = scanner.nextLine().trim();
            if (email.isEmpty() || isValidEmail(email)) break;
            System.out.println("Invalid email format.");
        }

        double marks = -1;
        while (true) {
            System.out.print("New Marks [" + existing.getMarks() + "]: ");
            String marksInput = scanner.nextLine().trim();
            if (marksInput.isEmpty()) break;
            try {
                marks = Double.parseDouble(marksInput);
                if (marks < 0 || marks > 100) {
                    System.out.println("Marks must be between 0 and 100.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format.");
            }
        }

        if (sms.updateStudent(roll, name, grade, email, marks)) {
            System.out.println("Student updated successfully!");
        } else {
            System.out.println("Update failed.");
        }
    }

    private static void removeStudent() {
        System.out.println("--- Remove Student ---");
        int roll = readInt("Enter Roll Number to remove: ");

        if (sms.removeStudent(roll)) {
            System.out.println("Student removed successfully!");
        } else {
            System.out.println("No student found with roll number " + roll);
        }
    }

    private static void searchStudent() {
        System.out.println("--- Search Student ---");
        System.out.println("1. Search by Roll Number");
        System.out.println("2. Search by Name");
        int option = readInt("Choose search option: ");

        if (option == 1) {
            int roll = readInt("Enter Roll Number: ");
            Student s = sms.searchByRoll(roll);
            if (s != null) {
                System.out.println("Student found:");
                System.out.println(s);
            } else {
                System.out.println("No student found with roll number " + roll);
            }
        } else if (option == 2) {
            String name = readNonEmptyString("Enter Name (or part of name): ");
            List<Student> results = sms.searchByName(name);
            if (results.isEmpty()) {
                System.out.println("No students found matching '" + name + "'");
            } else {
                System.out.println("Found " + results.size() + " student(s):");
                for (Student s : results) {
                    System.out.println(s);
                }
            }
        } else {
            System.out.println("Invalid option.");
        }
    }

}

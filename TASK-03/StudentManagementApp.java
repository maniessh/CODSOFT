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

}

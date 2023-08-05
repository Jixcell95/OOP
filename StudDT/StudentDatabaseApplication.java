import java.io.*;
import java.util.*;

class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int studentIDCounter = 10000;
    private String name;
    private int year;
    private int studentID;
    private ArrayList<String> enrolledCourses;
    private double balance;

    public Student(String name, int year) {
        this.name = name;
        this.year = year;
        this.studentID = generateStudentID(year);
        this.enrolledCourses = new ArrayList<>();
        this.balance = 0;
    }

    private int generateStudentID(int year) {
        return year * 10000 + studentIDCounter++;
    }

    public void enroll(String course) {
        enrolledCourses.add(course);
        balance += 600.0;
    }

    public void viewStatus() {
        System.out.println("Name: " + name);
        System.out.println("ID: " + studentID);
        System.out.println("Courses Enrolled: " + enrolledCourses);
        System.out.println("Balance: $" + balance);
    }

    public int getStudentID() {
        return studentID;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}

public class StudentDatabaseApplication {
    private static final String FILE_NAME = "students.txt";

    public static void main(String[] args) {
        ArrayList<Student> students = readStudentsFromFile();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nOptions:");
            System.out.println("1. Add students to the database");
            System.out.println("2. Enroll in a course");
            System.out.println("3. View student status");
            System.out.println("4. Pay tuition");
            System.out.println("5. Print database with unique IDs");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    addStudentsToDatabase(students, scanner);
                    break;
                case 2:
                    enrollInCourse(students, scanner);
                    break;
                case 3:
                    viewStudentStatus(students, scanner);
                    break;
                case 4:
                    payTuition(students, scanner);
                    break;
                case 5:
                    printDatabaseWithUniqueIDs(students);
                    break;
                case 6:
                    saveStudentsToFile(students);
                    scanner.close();
                    System.out.println("Exiting the application.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static ArrayList<Student> readStudentsFromFile() {
        ArrayList<Student> students = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(FILE_NAME);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            students = (ArrayList<Student>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No existing data found. Creating a new database.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return students;
    }

    private static void saveStudentsToFile(ArrayList<Student> students) {
        try (FileOutputStream fos = new FileOutputStream(FILE_NAME);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(students);
            System.out.println("Student data saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addStudentsToDatabase(ArrayList<Student> students, Scanner scanner) {
        System.out.print("Enter the number of students to add to the database: ");
        int numStudents = scanner.nextInt();

        for (int i = 0; i < numStudents; i++) {
            System.out.print("Enter student name: ");
            String name = scanner.next();

            System.out.print("Enter student year of study: ");
            int year = scanner.nextInt();

            Student student = new Student(name, year);
            students.add(student);
            System.out.println("Student added to the database.");
        }
    }

    private static void enrollInCourse(ArrayList<Student> students, Scanner scanner) {
        System.out.println("Available Courses:");
        System.out.println("1. History 101");
        System.out.println("2. Mathematics 101");
        System.out.println("3. English 101");
        System.out.println("4. Chemistry 101");
        System.out.println("5. Computer Programming 101");
        System.out.print("Enter the course number to enroll in: ");
        int courseChoice = scanner.nextInt();

        String course = null;
        switch (courseChoice) {
            case 1:
                course = "History 101";
                break;
            case 2:
                course = "Mathematics 101";
                break;
            case 3:
                course = "English 101";
                break;
            case 4:
                course = "Chemistry 101";
                break;
            case 5:
                course = "Computer Programming 101";
                break;
            default:
                System.out.println("Invalid course number.");
                return;
        }

        System.out.print("Enter the student's 5-digit ID: ");
        int studentID = scanner.nextInt();

        boolean studentFound = false;
        for (Student student : students) {
            if (student.getStudentID() == studentID) {
                studentFound = true;
                student.enroll(course);
                System.out.println("Enrollment successful.");
                return;
            }
        }

        if (!studentFound) {
            System.out.println("Student with ID not found. Please check the ID and try again.");
        }
    }

    private static void viewStudentStatus(ArrayList<Student> students, Scanner scanner) {
        System.out.print("Enter the student's 5-digit ID: ");
        int studentID = scanner.nextInt();

        boolean studentFound = false;
        for (Student student : students) {
            if (student.getStudentID() == studentID) {
                studentFound = true;
                student.viewStatus();
                return;
            }
        }

        if (!studentFound) {
            System.out.println("Student with ID not found. Please check the ID and try again.");
        }
    }

    private static void payTuition(ArrayList<Student> students, Scanner scanner) {
        System.out.print("Enter the student's 5-digit ID: ");
        int studentID = scanner.nextInt();

        boolean studentFound = false;
        for (Student student : students) {
            if (student.getStudentID() == studentID) {
                studentFound = true;
                System.out.print("Enter the amount to pay: $");
                double amount = scanner.nextDouble();
                if (amount <= student.getBalance()) {
                    student.setBalance(student.getBalance() - amount);
                    System.out.println("Payment successful.");
                    return;
                } else {
                    System.out.println("Insufficient balance. Payment unsuccessful.");
                    return;
                }
            }
        }

        if (!studentFound) {
            System.out.println("Student with ID not found. Please check the ID and try again.");
        }
    }

    private static void printDatabaseWithUniqueIDs(ArrayList<Student> students) {
        System.out.println("Student Database:");
        for (Student student : students) {
            System.out.println("ID: " + student.getStudentID());
            student.viewStatus();
            System.out.println("--------------------------------------------------");
        }
    }
}

class Student {
    private String name;
    private int marks;

    // Method to assign values
    public void setDetails(String name, int marks) {
        this.name = name;
        this.marks = marks;
    }

    // Method to retrieve name
    public String getName() {
        return name;
    }

    // Method to retrieve marks
    public int getMarks() {
        return marks;
    }

    // Method to calculate grade
    public String calculateGrade() {
        if (marks > 75) {
            return "A";
        } else if (marks > 65) {
            return "B";
        } else if (marks > 45) {
            return "C";
        } else {
            return "Fail";
        }
    }

    // Main method to test the class
    public static void main(String[] args) {
        Student student1 = new Student();
        student1.setDetails("Alice", 82);

        Student student2 = new Student();
        student2.setDetails("Bob", 58);

        System.out.println("Student: " + student1.getName());
        System.out.println("Marks: " + student1.getMarks());
        System.out.println("Grade: " + student1.calculateGrade());

        System.out.println();

        System.out.println("Student: " + student2.getName());
        System.out.println("Marks: " + student2.getMarks());
        System.out.println("Grade: " + student2.calculateGrade());
    }
}

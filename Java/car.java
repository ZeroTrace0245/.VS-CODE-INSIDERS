public class car {
    private String model;
    private int year;

    // Default constructor
    public car() {
        this.model = "unknown";
        this.year = 0;
    }

    // Parameterized constructor
    public car(String model, int year) {
        this.model = model;
        this.year = year;
    }

    // Display method
    public void display() {
        System.out.println("Model: " + model + ", Year: " + year);
    }

    public static void main(String[] args) {
        // Creating car object using default constructor
        car car1 = new car();
        car1.display();

        // Creating car object using parameterized constructor
        car car2 = new car("Toyota", 2020);
        car2.display();
    }
}

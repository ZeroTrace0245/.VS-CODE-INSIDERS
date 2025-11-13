public class MethodExamples {
    private String greeting = "Welcome";

    public int addNumbers(int a, int b) {
        return a + b;
    }

    public void displayUser(String name, int age, String city) {
        System.out.println("User: " + name + ", Age: " + age + ", City: " + city);
    }

    public String getGreeting(String name) {
        return greeting + ", " + name + "!";
    }

    public static void main(String[] args) {
        MethodExamples example = new MethodExamples();
        int sum = example.addNumbers(12, 8);
        System.out.println("Sum: " + sum);
        example.displayUser("Alex", 30, "Seattle");
        System.out.println(example.getGreeting("Alex"));
    }
}

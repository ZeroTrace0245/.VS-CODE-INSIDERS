package Java.Assignment;

public class Practicalsheet_{
	public static void main(String[] args) {
		//using setters/getters
		System.out.println("Exercise 3-1 (setters/getters):");
		Employee emp1 = new Employee();
		emp1.setName("Sachitha");
		emp1.setAge(19);
		emp1.setSalary(5000);
		System.out.println("Name: " + emp1.getName());
		System.out.println("Age: " + emp1.getAge());
		System.out.println("Salary: " + emp1.getSalary());

	
		System.out.println("\nExercise 3-1 (constructor):");
		Employee emp2 = new Employee("Sachitha", 19, 5000);
		System.out.println("Name: " + emp2.getName());
		System.out.println("Age: " + emp2.getAge());
		System.out.println("Salary: " + emp2.getSalary());

		//(bonus via constructor)
		System.out.println("\nExercise 3-2:");
		EmployeeWithBonus ewb = new EmployeeWithBonus(10000); 
		ewb.setName("Sachitha"); 
		ewb.setBasicSalary(50000); 
		System.out.println("Employee Name: " + ewb.getName());
		System.out.println("Basic Salary: " + (int)ewb.getBasicSalary());
		System.out.println("Bonus: " + (int)ewb.getBonus());
		System.out.println("Bonus Amount: " + (int)ewb.calculateBonusAmount());
	}
}

class Employee {
	private String name;
	private int age;
	private double salary;

	public Employee() {
	}

	// Constructor alternative to setters
	public Employee(String name, int age, double salary) {
		this.name = name;
		this.age = age;
		this.salary = salary;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}
}

class EmployeeWithBonus {
	private String name;
	private double basicSalary;
	private double bonus;

	public EmployeeWithBonus() {
	}

	public EmployeeWithBonus(double bonus) {
		this.bonus = bonus;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getBasicSalary() {
		return basicSalary;
	}

	public void setBasicSalary(double basicSalary) {
		this.basicSalary = basicSalary;
	}

	public double getBonus() {
		return bonus;
	}

	public void setBonus(double bonus) {
		this.bonus = bonus;
	}

	// Bonus amount = basic salary + bonus
	public double calculateBonusAmount() {
		return basicSalary + bonus;
	}
}

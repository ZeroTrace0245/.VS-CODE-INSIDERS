class Circle {
	private final double radius;

	Circle(double radius) {
		this.radius = radius;
	}

	void printArea() {
		double area = Math.PI * radius * radius;
		System.out.println("Area: " + area);
	}

	public static void main(String[] args) {
		new Circle(5).printArea();
	}
}

//create a class car with model and year as variables a defult constructor that set unknown and year as zero a paprameter constructor to set both value a display methode to show modele and 
//year in main methode create one car object using the defult constructror and another objected using parameterized constructor  

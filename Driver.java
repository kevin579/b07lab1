import java.io.*;
public class Driver {
	public static void main(String [] args) {
		// Polynomial p = new Polynomial();
		// System.out.println(p.evaluate(3));
		// double [] c1 = {6,0,0,5};
		// Polynomial p1 = new Polynomial(c1);
		// double [] c2 = {0,-2,0,0,-9};
		// Polynomial p2 = new Polynomial(c2);
		// Polynomial s = p1.add(p2);
		// System.out.println("s(0.1) = " + s.evaluate(0.1));
		// Polynomial p = new Polynomial(new double[]{-1.0,2.0},new int[]{0,1});
		Polynomial p = new Polynomial(new File("test"));
		for (int i=0;i<p.coefficients.length;i++) {
			System.out.println("coef: "+p.coefficients[i]);
		}
		for (int i=0;i<p.coefficients.length;i++) {
			System.out.println("degree: "+p.degree[i]);
		}
		if(p.hasRoot(0.5))
			System.out.println("0.5 is a root of s");
		else
			System.out.println("0.5 is not a root of s");
		p.saveToFile("test2");
		}
		
	
}
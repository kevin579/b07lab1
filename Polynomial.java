import java.io.*;
import java.util.Scanner;
public class Polynomial{
	double[] coefficients;
	int[] degree;
	double EPS = 1e-9;
	
	public Polynomial(){
		coefficients = new double[]{0.0};
		degree = new int[]{0};
	}

	public Polynomial(double[] arr){
		int len = 0;
		for (double coef:arr) {
			if (Math.abs(coef) > EPS) {
				len++;
			}
		}
		
		if (len ==0) {
			coefficients = new double[]{0.0};
			degree = new int[]{0};
			return;
		}
		
		coefficients = new double[len];
		degree = new int[len];
		
		int index = 0;
		for (int i=0;i<arr.length;i++) {
			if (Math.abs(arr[i])>EPS) {
				coefficients[index] = arr[i];
				degree[index] = i;
				index++;
			}
		}
	}
	
	public Polynomial(double[] coefficients, int[] degree) {
		this.coefficients = coefficients.clone();
		this.degree = degree.clone();
	}
	
	public Polynomial(File file) throws FileNotFoundException {
		Scanner input = new Scanner(file); 
		String poly = input.nextLine();
		input.close();
		poly = poly.replaceAll("\\s+", "");
		if (poly.isEmpty()) {
			coefficients = new double[]{0.0};
			degree = new int[]{0};
			return;
		}
		this.parse(poly);
	}
	
	private void parse(String poly) {
		boolean negStart = false;
		if (poly.charAt(0)=='-') {
			poly = poly.substring(1);
			negStart = true;
		}
		String[] components = poly.split("[+-]");
		double[] signs = new double[components.length];
		int index = 0;
		if (negStart) {
			signs[index]=-1;
			index=1;
		}else {
			signs[index]=1;
			index=1;
		}
		for (int i = 0; i<poly.length();i++) {
			if (poly.charAt(i)=='-') {
				signs[index]=-1;
				index++;
			}else if (poly.charAt(i)=='+') {
				signs[index]=1;
				index++;
			}
		}
		
		int maxDegree = 0;
		for (String c:components) {
			if (c.startsWith("x")) {
				c = "1"+c;
			}
			if (c.endsWith("x")) {
				c = c+"1";
			}
			
			String[] cd = c.split("x");
			if (cd.length==1) {
				continue;
			}
			int degree = Integer.parseInt(cd[1]);
			maxDegree = Math.max(maxDegree, degree);
		}
		
		double[] densePoly = new double[maxDegree+1];
		for (int i=0;i<components.length;i++) {
			String body = components[i];
			if (body.startsWith("x")) {
				body = "1"+body;
			}
			if (body.endsWith("x")) {
				body = body+"1";
			}
			String[] elements = body.split("x");
			int degree=0;
			if(elements.length==2) {
				degree = Integer.parseInt(elements[1]);
			}
			double coef = Double.parseDouble(elements[0])*signs[i];
			
			
			densePoly[degree] += coef;
		}
		
		
		int len = 0;
	    for (int d = 0; d < densePoly.length; d++) {
	        if (Math.abs(densePoly[d]) > EPS) len++;
	    }

		coefficients = new double[len];
		degree = new int[len];
		index = 0;
		for (int i=0;i<densePoly.length;i++) {
			if (Math.abs(densePoly[i])>EPS) {
				coefficients[index] = densePoly[i];
				degree[index] = i;
				index++;
			}	
		}
	}

	public Polynomial add(Polynomial p){
		int l1 = this.coefficients.length;
		int l2 = p.coefficients.length;
	    double[] c = new double[l1 + l2];
	    int[] e = new int[l1 + l2];

	    int index1 = 0, index2 = 0, indexNew = 0;
	    while (index1 < l1 && index2 < l2) {
	        int e1 = this.degree[index1];
	        int e2 = p.degree[index2];
	        if (e1 == e2) {
	            double s = this.coefficients[index1] + p.coefficients[index2];
	            if (Math.abs(s) > EPS) { 
	                e[indexNew] = e1;
	                c[indexNew] = s;
	                indexNew++;
	            }
	            index1++; 
	            index2++;
	        } else if (e1 < e2) {
	            e[indexNew] = e1;
	            c[indexNew] = this.coefficients[index1];
	            indexNew++; 
	            index1++;
	        } else {
	            e[indexNew] = e2;
	            c[indexNew] = p.coefficients[index2];
	            indexNew++; 
	            index2++;
	        }
	    }
	    while (index1 < l1) {
	    	e[indexNew] = this.degree[index1]; 
	    	c[indexNew] = this.coefficients[index1]; 
	    	indexNew++; 
	    	index1++; 
	    }
	    while (index2 < l2) { 
	    	e[indexNew] = p.degree[index2];   
	    	c[indexNew] = p.coefficients[index2];   
	    	indexNew++; 
	    	index2++; 
	    }

	    if (indexNew == 0) {
	    	return new Polynomial();
	    }

	    double[] c2 = new double[indexNew];
	    int[] e2 = new int[indexNew];
	    for (int t = 0; t < indexNew; t++) { 
	    	c2[t] = c[t]; 
	    	e2[t] = e[t]; 
	    }

	    return new Polynomial(c2, e2);
	}	
	
	public double evaluate(double x) {
		double value = 0;
		for (int i = 0;i<this.coefficients.length;i++) {
			value += this.coefficients[i] * Math.pow(x, this.degree[i]);
		}
		return value;
	}
	
	public boolean hasRoot(double x) {
		return Math.abs(evaluate(x)) <= EPS;
	}
	
	public Polynomial multiply(Polynomial p) {
		int maxDeg = this.degree[this.degree.length - 1] + p.degree[p.degree.length - 1];
		double[] newPoly = new double[maxDeg+1];
		for (int x =0;x<this.coefficients.length;x++) {
			for (int y =0;y<p.coefficients.length;y++) {
				double newCoef = this.coefficients[x]*p.coefficients[y];
				int newDeg = this.degree[x]+p.degree[y];
				newPoly[newDeg] +=newCoef;
			}
		}
		return new Polynomial(newPoly);
	}
	
	public void saveToFile(String path) throws IOException{
		FileWriter output = new FileWriter(path,false);
		String poly = "";

		for (int i=0;i<this.degree.length;i++) {
			poly+=this.toStr(this.coefficients[i],this.degree[i]);
		}
		if (!poly.isEmpty()) {
			if (poly.charAt(0)=='+') {
				poly = poly.substring(1);
			}
		}

		output.append(poly);
		output.close();
	}
	
	public String toStr(double c, int d) {
		String result = "";
		if (c >= 0) {
	        result += "+";
	    } else {
	        result += "-";
	    }

	    double abs = Math.abs(c);

	    if (!(Math.abs(abs - 1.0) < EPS && d != 0)) {
	        result += abs;
	    }

	    if (d > 0) {
	        result += "x";
	        if (d > 1) {
	            result += d;
	        }
	    }
		return result;
	}
}
public class Polynomial{
	double[] coefficients;
	double EPS = 1e-9;
	
	public Polynomial(){
		coefficients = new double[1];
		coefficients[0] = 0;
	}

	public Polynomial(double[] arr){
		coefficients = arr.clone();
	}

	public Polynomial add(Polynomial p){
		int l1 = this.coefficients.length;
		int l2 = p.coefficients.length;
		double[] newP = new double[Math.max(l1,l2)];
		if (l1<=l2){
			for (int i = 0; i< l1;i++){
				newP[i] = this.coefficients[i]+p.coefficients[i];
			}
			for (int i =l1; i<l2;i++){
				newP[i]=p.coefficients[i];
			}
		}

		else {
			for (int i = 0; i< l2;i++){
				newP[i] = this.coefficients[i] + p.coefficients[i];
			}
			for (int i =l2; i<l1;i++){
				newP[i]=this.coefficients[i];
			}
		}
		return new Polynomial(newP);
	}	
	
	public double evaluate(double x) {
		double value = 0;
		for (int i = 0;i<this.coefficients.length;i++) {
			value += this.coefficients[i] * Math.pow(x, i);
		}
		return value;
	}
	
	public boolean hasRoot(double x) {
		return Math.abs(evaluate(x)) <= EPS;
	}
}
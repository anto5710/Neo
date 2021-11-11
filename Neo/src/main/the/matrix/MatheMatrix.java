package main.the.matrix;

public abstract class MatheMatrix implements IMatrix{

	public int m() {
		return rowLength();
	}

	public int n() {
		return columnLength();
	}

	public boolean invertible() {
		return isInvertible();
	}

	public boolean square() {
		return isSquare();
	}

	public Matrix T() {
		return transpose();
	}

	public Matrix I() {
		return inverse();
	}

	public Matrix x(IMatrix B) {
		return multiply(B);
	}

	public Matrix x(double r) {
		return multiply(r);
	}

	public Matrix plus(IMatrix B) {
		return add(B);
	}
	
	public Matrix minus(IMatrix B) {
		return subtract(B);
	}

	public double at(int y, int x) {
		return get(y, x);
	}

	public double[][] array() {
		return toArray();
	}
}

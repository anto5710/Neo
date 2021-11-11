package main.the.matrix;

public interface IMatrix {
	public int rowLength();
	public int columnLength();
	
	public boolean isInvertible();
	public boolean isSquare();
	public boolean equalSized(IMatrix B);
	
	public Matrix multiply(IMatrix B);
	public Matrix multiply(double r);
	public Matrix add(IMatrix B);
	public Matrix subtract(IMatrix B);
	
	public Matrix inverse();
	public Matrix transpose();
	
	public double get(int y, int x);
	public void set(int y, int x, double v);
	public boolean within(int y, int x);
	
	public Matrix copy();
	
	public double[][] toArray();
}

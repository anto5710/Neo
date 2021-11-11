package main.the.matrix;

public interface IMatrix{
	public int m();
	public int n();
	public boolean invertible();
	public IMatrix x(IMatrix B);
	public IMatrix x(double r);
	public IMatrix I();
	public IMatrix plus(IMatrix B);
	public IMatrix minus(IMatrix B);
	public double get(int y, int x);
	public void set(int y, int x, double v);
	public boolean within(int y, int x);
	public IMatrix copy();
	public boolean square();
	public double[][] array();
}

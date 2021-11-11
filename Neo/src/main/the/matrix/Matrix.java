package main.the.matrix;

import java.util.Arrays;

public class Matrix implements IMatrix{
	private double[][] A;
	
	public static double det(IMatrix A) {
		if (!A.square()) {
			return Double.NaN;
			
		} else if (A.m() == 1) {
			return A.get(0, 0);
			
		} else if (A.m() == 2) {
			double a = A.get(0, 0);
			double b = A.get(0, 1);
			double c = A.get(1, 0);
			double d = A.get(1, 1);
			
			return a*d - b*c;
		}
		
		int x = 0;	
		double det = 0;
		
		for (int y = 0; y < A.m(); y++) {
			
			det += (y % 2 == 0 ? +1 : -1) * det(skip(A, y, x));
		}
		
		return det;
	}
	
	private static IMatrix skip(IMatrix A, int y, int x) {
		 if (A == null || !A.within(y, x)) {
			 return null;
		 }
		 
		 double[][] Amn = new double[A.m() - 1][A.n() - 1];
		 int y_shifter = 0, x_shifter = 0;
		 for (int ys = 0; ys < A.m() - 1; ys++) {
			if (ys != y) {
				for (int xs = 0; xs < A.n() - 1; xs++) {
					if (xs != x) {
						Amn[ys + y_shifter][xs + x_shifter] = A.get(y, x);
					} else {
						x_shifter--;
					}
				}
			} else {
				y_shifter--;
			}
		}
		 
		 return new Matrix(Amn);
	}
	
	public Matrix(int m, int n) {
		if (m < 0 || n < 0) {
			try {
				throw new Exception(String.format("[StandardMatrix]: Negative matrix size: [%d x %d]\n",m, n));
			} catch(Exception e) {
				e.printStackTrace();
			};
		}
		
		this.A = new double[m][n];
		for (int y = 0; y < m(); y++) {
			for (int x = 0; x < n(); x++) {
				set(y, x, 0);
			}
		}
	}
	
	public Matrix(double[][] A) {
		if (A == null) {
			try {
				throw new Exception(String.format("[StandardMatrix]: Null Matrix in Constructor\n"));
			} catch(Exception e) {
				e.printStackTrace();
			};
		}
		this.A = A;
	}
	
	@Override
	public int m() {
		return A.length;
	}

	@Override
	public int n() {
		return A[0].length;
	}

	@Override
	public boolean invertible() {
		return square() && det(this) != 0;
	}

	@Override
	public IMatrix x(IMatrix B) {
		if (n() != B.m()) {
            return null;
        }

        double[][] product = new double[m()][B.n()]; 

        for (int y = 0; y < product.length; y++) {
            for (int x = 0; x < product[y].length; x++) {
                product[y][x] = 0;

                for (int ax = 0; ax < n(); ax++) {
                    product[y][x] += get(y, ax) * B.get(ax, x);
                }
            }
        }

        return new Matrix(product);
	}

	@Override
	public IMatrix I() {
		if (!invertible() || m() > 2) {
			return null;
		}
		
		if (m() == 1) {
			return copy();
		}
		
		double a = get(0, 0);
		double b = get(0, 1);
		double c = get(1, 0);
		double d = get(1, 1);
		
		double det = det(this);
		
		double[][] A_i = {{d/det, -b/det}, {-c/det, a/det}};
		return new Matrix(A_i);
	}

	@Override
	public IMatrix plus(IMatrix B) {
		if (B.m() != m() || B.n() != n()) {
			return null;
		}
		
		IMatrix C = new Matrix(m(), n());
		for (int y = 0; y < m(); y++) {
			for (int x = 0; x < n(); x++) {
				C.set(y, x, get(y,x) + B.get(y, x));
			}
		}
		
		return C;
	}

	@Override
	public double get(int y, int x) {
		return within(y, x) ? A[y][x] : Double.NaN;
	}

	@Override
	public boolean within(int y, int x) {
		return 0 <= y && y < m() && 0 <= x && x < n();
	}

	@Override
	public IMatrix copy() {
		return new Matrix(array());
	}

	@Override
	public void set(int y, int x, double v) {
		A[y][x] = v;
	}

	@Override
	public boolean square() {
		return m() == n();
	}

	@Override
	public double[][] array() {
		return Arrays.copyOf(A, A.length);
	}

	@Override
	public IMatrix minus(IMatrix B) {
		return plus(B.x(-1));
	}

	@Override
	public IMatrix x(double r) {
		double [][] B = new double[m()][n()];
		for (int y = 0; y < m(); y++) {
			for (int x = 0; x < n(); x++) {
				B[y][x] = r * get(y, x);
			}
		}
		return new Matrix(B);
	}	
	
	
}

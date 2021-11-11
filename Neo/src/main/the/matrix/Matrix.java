package main.the.matrix;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class Matrix extends MatheMatrix {
	private double[][] matrix;

	public static Matrix matrix(double[][] matrix) {
		return new Matrix(matrix);
	}
	
	public static Matrix I(int n) {
		Matrix identity = new Matrix(n, n);
		
		for (int i = 0; i < n; i++) {
			identity.set(i, i, 1);
		}
		
		return identity;
	}
	
	public static int signum(int y, int x) {
		return (y % 2 == 0 ? +1 : -1) * (x % 2 == 0 ? +1 : -1);
	}

	public static double cofactor(Matrix A, int y, int x) {
		if (A == null || !A.within(y, x)) {
			try {
				throw new Exception(String.format("[StandardMatrix::cofactor]: Invalid Matrix input\n"));
			} catch (Exception e) {
				e.printStackTrace();
			}

			return 0;
		}
		
		return signum(y, x) * det(submatrix(A, y, x)); /* sgn_yx * minor_yx */
	}

	public static double det(Matrix A) {
		if (!A.isSquare()) {
			return Double.NaN;

		} else if (A.rowLength() == 1) {
			return A.get(0, 0);

		} else if (A.rowLength() == 2) {
			double a = A.get(0, 0), b = A.get(0, 1), 
				   c = A.get(1, 0), d = A.get(1, 1);

			return a * d - b * c;
		}

		int y = 0;
		double det = 0;

		for (int x = 0; x < A.columnLength(); x++) {
			det += A.get(y,x) * cofactor(A, y, x);
		}

		return det;
	}

	protected static Matrix submatrix(Matrix A, int y_axis, int x_axis) {
		if (A == null || !A.within(y_axis, x_axis)) {
			return null;
		}

		double[][] Amn = new double[A.rowLength() - 1][A.columnLength() - 1];
		
		int y_shifter = 0;
		for (int y = 0; y < A.rowLength(); y++) {
				
			if (y == y_axis) {
				y_shifter--;

			} else {				
				int x_shifter = 0;
				for (int x = 0; x < A.columnLength(); x++) {
					if (x == x_axis) {
						x_shifter--;

					} else {
						Amn[y + y_shifter][x + x_shifter] = A.get(y, x);
					}
				}
			}
		}

		return new Matrix(Amn);
	}

	public Matrix(int m, int n) {
		if (m < 0 || n < 0) {
			try {
				throw new Exception(String.format("[StandardMatrix]: Negative matrix size: [%d x %d]\n", m, n));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		this.matrix = new double[m][n];

		foreach((y, x) -> set(y, x, 0));
	}

	public Matrix(double[][] A) {
		if (A == null) {
			try {
				throw new Exception(String.format("[StandardMatrix]: Null Matrix in Constructor\n"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.matrix = A;
	}

	public int rowLength() {
		return matrix.length;
	}

	public int columnLength() {
		return matrix[0].length;
	}

	public boolean isInvertible() {
		return isSquare() && det(this) != 0;
	}

	public Matrix multiply(IMatrix B) {

		if (columnLength() != B.rowLength()) {
			return null;
		}

		double[][] product = new double[rowLength()][B.columnLength()];

		for (int y = 0; y < product.length; y++) {
			for (int x = 0; x < product[y].length; x++) {

				product[y][x] = 0;

				for (int ax = 0; ax < columnLength(); ax++) {
					product[y][x] += get(y, ax) * B.get(ax, x);
				}
			}
		}

		return new Matrix(product);
	}

	public Matrix inverse() {

		if (!isInvertible()) {
			return null;

		} else if (rowLength() == 1) {
			return copy();
		}

		double detA = det(this);
		Matrix cofactored = map((y, x) -> cofactor(this, y, x));
		return cofactored.transpose().multiply(1 / detA);
	}

	public Matrix add(IMatrix B) {
		if (!equalSized(B)) {
			return null;
		}

		return map((y, x) -> (get(y, x) + B.get(y, x)));
	}

	public double get(int y, int x) {
		return within(y, x) ? matrix[y][x] : Double.NaN;
	}

	public boolean within(int y, int x) {
		return 0 <= y && y < rowLength() && 0 <= x && x < columnLength();
	}

	public Matrix copy() {
		return new Matrix(toArray());
	}

	public void set(int y, int x, double v) {
		matrix[y][x] = v;
	}

	public boolean isSquare() {
		return rowLength() == columnLength();
	}

	public double[][] toArray() {
		return Arrays.copyOf(matrix, matrix.length);
	}

	public Matrix subtract(IMatrix B) {
		return add(B.multiply(-1));
	}

	public Matrix multiply(double scalar) {
		return map((y, x) -> scalar * get(y, x));
	}

	public String toString() {
		String result = "";
		String delimiter = "  ";

		int cellLength = 1;

		for (int y = 0; y < rowLength(); y++) {
			for (int x = 0; x < columnLength(); x++) {

				String s = String.format("%.3f", get(y, x));
				cellLength = Math.max(s.length(), cellLength);
			}
		}

		for (int y = 0; y < rowLength(); y++) {
			for (int x = 0; x < columnLength(); x++) {

				String s = String.format("%.3f", get(y, x));

				for (int w = 0; w < cellLength - s.length(); w++) {
					s = " " + s;
				}

				result += s;

				if (x < columnLength() - 1) {
					result += delimiter;
				}
			}
			result += "\n";
		}

		return result;
	}

	public Matrix transpose() {
		double[][] t = new double[columnLength()][rowLength()];

		foreach((y, x) -> t[x][y] = get(y, x));

		return new Matrix(t);
	}

	protected void foreach(BiConsumer<Integer, Integer> iterator) {
		if (iterator == null) {
			return;
		}

		for (int y = 0; y < rowLength(); y++) {
			for (int x = 0; x < columnLength(); x++) {
				iterator.accept(y, x);
			}
		}
	}

	protected Matrix map(BiFunction<Integer, Integer, Double> mapper) {
		double[][] new_matrix = new double[rowLength()][columnLength()];

		if (mapper == null) {
			return null;
		}

		for (int y = 0; y < rowLength(); y++) {
			for (int x = 0; x < columnLength(); x++) {
				new_matrix[y][x] = mapper.apply(y, x);
			}
		}

		return new Matrix(new_matrix);
	}

	public boolean equalSized(IMatrix B) {
		return B != null && B.columnLength() == columnLength() && B.rowLength() == rowLength();
	}

	public Matrix rescale(int r0, double scalar) {
		if (!within(r0, 0)) {
			try {
				throw new Exception(
						String.format("[StandardMatrix::replace]: Array Index out of Bound: R%d\n", r0));
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		
			return null;
		}
		
		Matrix rescaled = copy();
		
		for (int x = 0; x < rescaled.columnLength(); x++) {
			double r0x = rescaled.get(r0, x);
			rescaled.set(r0, x, scalar * r0x);
		}
		
		return rescaled;
	}
	
	public Matrix interchange(int r0, int r1) {
		if (!within(r0, 0) || !within(r1, 0)) {
			try {
				throw new Exception(
						String.format("[StandardMatrix::replace]: Array Index out of Bound: R%d, R%d\n", r0, r1));
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		
			return null;
		}
		
		Matrix interchanged = copy();
		
		for (int x = 0; x < interchanged.columnLength(); x++) {
			double r0x = interchanged.get(r0, x);
			double r1x = interchanged.get(r1, x);
			interchanged.set(r0, x, r1x);
			interchanged.set(r1, x, r0x);
		}
		
		return interchanged;
	}
	
	public Matrix replace(int r0, double c1, int r1) {
		if (!within(r0, 0) || !within(r1, 0)) {
			try {
				throw new Exception(
						String.format("[StandardMatrix::replace]: Array Index out of Bound: R%d, R%d\n", r0, r1));
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		
			return null;
		}
		
		Matrix replaced = copy();
		for (int x = 0; x < replaced.columnLength(); x++) {
			double replaced_yx = get(r0, x) + c1 * get(r1, x);
			replaced.set(r0, x, replaced_yx);
		}
		
		return replaced;
	}
}

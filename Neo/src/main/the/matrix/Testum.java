package main.the.matrix;

import static main.the.matrix.Matrix.*;

public class Testum {
	public static void main(String[] args) {
		double [][] pb = {{ 1,-1,-1},
						  {-1, 1, 0},
						  { 1, 0, 1}};
	    double [][] b  = {{1,0,0},{0,-2,0},{0,0,-2}};
	    
	    Matrix Pb = matrix(pb);
	    Matrix B  = matrix(b);
	    
	    System.out.println(Pb);
	    
	    System.out.println(Pb.x(B).x(Pb.I()));
	}
}

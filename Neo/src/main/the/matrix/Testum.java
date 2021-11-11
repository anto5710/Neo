package main.the.matrix;
import static main.the.matrix.Matrix.*;

import main.logger.GenericLogger;
import main.logger.Logger;

public class Testum {
	public static void main(String[] args) {
//		
		Logger log = new GenericLogger();
		
		double [][] a = {{2, 3}, 
						 {4, 5}};
		double [][] pb = {{2131,-23213},{-20,-230}};
		
		IMatrix A = new Matrix(a);
		IMatrix Pb = new Matrix(pb);
		IMatrix PbI = Pb.I();
		
		log.println(A.minus(Pb.x(PbI.x(A).x(Pb)).x(PbI)).array());
	}
}

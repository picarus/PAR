package mai.par;

import mai.par.parse.TrainParser;

public class Main {

	/**
	 * @param args
	 */
	
	static String s="Wagons=A,B,C,D\n"
			+"Initial_state=ON-STATION(A);ON-STATION(B);ON-STATION(D)\nIN-FRONT-OF(C,D);FREE(A);\nFREE(B);FREE(C);FREE-LOCOMOTIVE;EMPTY(B);EMPTY(D);LOADED(A);LOADED(C)\n"
			+"Goal_state=ON-STATION(B);ON-STATION(A);ON-STATION(C);IN-FRONT-OF(D,C);FREE(B);\nFREE(A);FREE(D);FREE-LOCOMOTIVE;LOADED(B);LOADED(C);LOADED(D);EMPTY(A)\n";
	
	public static void main(String[] args) {
		TrainParser.tokenize(s);

	}

}

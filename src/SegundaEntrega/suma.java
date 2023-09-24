package SegundaEntrega;

public class suma extends OperacionesArit{

	public String Operar(float op1,float op2) {
		
        String toreturn; 

        float resultado = 0;


		float operando1 = op1;
		float operando2 = op2;
		int codigo_resultado= 0;
		String codigo_resultado_string= "";
		char operacion='+';

		System.out.print("\nOperando1: "+operando1);
		System.out.print("\nOperando2: "+operando2);
		System.out.print("\nOperacion: "+operacion);

				//Realizando operacion deseada

				resultado = operando1+operando2;
				codigo_resultado = 11;

				//Devuelve resultado
				toreturn = String.valueOf(resultado);
				System.out.print("\nResultado operacion: "+toreturn);
				codigo_resultado_string = String.valueOf(codigo_resultado);
				return codigo_resultado_string+","+toreturn;
}
}



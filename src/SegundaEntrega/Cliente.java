package SegundaEntrega;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.apache.commons.codec.digest.DigestUtils;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 *
 * @author mkc
 */

public class Cliente extends Application {
	int [] puertosNodos = {9800,9801,9802,9803,9804};
	String repuesta_servidor ="";
	boolean bandera = false;
	GridPane grid = new GridPane();
	Label llTitulo = new Label("Calculadora");
	TextField tfEntrada = new TextField();
	Button bUno = new Button("1");
	Button bDos = new Button("2");
	Button bTres = new Button("3");
	Button bCuatro = new Button("4");
	Button bCinco = new Button("5");
	Button bSeis = new Button("6");
	Button bSiete = new Button("7");
	Button bOcho = new Button("8");
	Button bNueve = new Button("9");
	Button bCero = new Button("0");
	Button bSuma = new Button("+");
	Button bResta = new Button("-");
	Button bAC = new Button("AC");
	Button bMultiplicacion = new Button("*");
	Button bDivision = new Button("/");
	Button bPunto = new Button(".");
	Button bIgual = new Button("=");
	Button bConectar = new Button("Conectar");
	Button bAcuses = new Button("Acuses");
	Label resultado = new Label("");
	Label resultado_resta = new Label("Resultado resta: ");
	Label resultado_suma = new Label("Resultado suma: ");
	Label resultado_multi = new Label("Resultado multiplicacion: ");
	Label resultado_div = new Label("Resultado division: ");
	Label huellaLabel = new Label("Huella: ");
	Date fecha= new Date();	 
	Timestamp ts= null;
	String huella="";
	Socket socket = null;
	DataOutputStream flujoSalida=null;
	DataInputStream flujoEntrada=null;
	boolean conectado = false;
	int acusesSuma = 0;
	int acusesResta= 0;
	int acusesMulti = 0;
	int acusesDivision = 0;
	Operacion [] listaSumas = new Operacion[20];
	Operacion [] listaRestas = new Operacion[20];
	Operacion [] listaMultis = new Operacion[20];
	Operacion [] listaDivs = new Operacion[20];
	int numSuma=0;
	int numResta=0;
	int numMulti=0;
	int numDiv=0;
	boolean acusesSuficientes =false;
	
	public static void main(String[] args) {		
		launch(); //Lanzamos la app
	}

	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Calculadora");
		
	
		resultado.setText("Resultado: ");
		
		bIgual.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.out.println("\nHas pulsado el boton");
				String input_usuario = tfEntrada.getText();
				
				System.out.println("\nRealizando acuses");
				acusar();
				
				if(!acusesSuficientes) {
					System.out.println("NO SE PUEDE REALIZAR LA OPERCION");
					System.out.println("NO HAY SUFICIENTES SERVIDORES");
				}else {
					
				}
				
				if(acusesSuficientes) {
					conectar();
					repuesta_servidor=peticion(input_usuario);
					resultado.setText(resultado.getText()+"\n"+tfEntrada.getText()+" = "+repuesta_servidor);
					tfEntrada.setText("");	
					
				}else {
					 DuplicarServidores();
				}
				
					

			}
			
		});
		bAcuses.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				for(int i=0;i<3;i++) {
					System.out.println("Intento "+(i+1)+" de obtener los acuses");
					System.out.println("Total de acuses: "+acusesSuma);
					
					if(acusesSuma >= 3) {
						break;
						
					}else {
						conectar();
						acusesSuma =0;
						acusesRecibidos();
					}
					
				}
				if(acusesSuma <3) {
					System.out.println("NO SE PUEDE REALIZAR LA OPERCION");
					System.out.println("NO HAY SUFICIENTES SERVIDORES");
				}
				
				

			}
			
		});
		bConectar.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {				
				conectar();
				
			}	
		});
		bUno.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				tfEntrada.appendText("1");
			}	
		});
		bDos.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				tfEntrada.appendText("2");
			}	
		});
		bTres.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				tfEntrada.appendText("3");
			}	
		});
		bCuatro.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				tfEntrada.appendText("4");
			}	
		});
		bCinco.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				tfEntrada.appendText("5");
			}	
		});
		bSeis.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				tfEntrada.appendText("6");
			}	
		});
		bSiete.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				tfEntrada.appendText("7");
			}	
		});
		bOcho.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				tfEntrada.appendText("8");
			}	
		});
		bNueve.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				tfEntrada.appendText("9");
			}	
		});
		bCero.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				tfEntrada.appendText("0");
			}	
		});
		bSuma.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				tfEntrada.appendText("+");
			}	
		});
		bResta.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				tfEntrada.appendText("-");
			}	
		});
		bMultiplicacion.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				tfEntrada.appendText("*");
			}	
		});
		bDivision.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				tfEntrada.appendText("/");
			}	
		});
		bPunto.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				tfEntrada.appendText(".");
			}	
		});
		bAC.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				tfEntrada.setText("");
			}	
		});
                //grid.setGridLinesVisible(true);
		grid.setPadding(new Insets(25,25,25,25));
		grid.setHgap(25);
		grid.setVgap(25);
                
                GridPane.setConstraints(tfEntrada, 0, 0);
                GridPane.setColumnSpan(tfEntrada, 4);
		
                bAC.setMinWidth(90);
                GridPane.setConstraints(bAC, 0, 1);
                GridPane.setColumnSpan(bAC, 2); 
                bDivision.setMinWidth(31);
                GridPane.setConstraints(bDivision, 3, 1);
                
                bSiete.setMinWidth(31);
                GridPane.setConstraints(bSiete, 0, 2);
                bOcho.setMinWidth(31);
                GridPane.setConstraints(bOcho, 1, 2);
                bNueve.setMinWidth(31);
                GridPane.setConstraints(bNueve, 2, 2);
                bMultiplicacion.setMinWidth(31);
                GridPane.setConstraints(bMultiplicacion, 3, 2);
                
                bCuatro.setMinWidth(31);
                GridPane.setConstraints(bCuatro, 0, 3);
                bCinco.setMinWidth(31);
                GridPane.setConstraints(bCinco, 1, 3);
                bSeis.setMinWidth(31);
                GridPane.setConstraints(bSeis, 2, 3);
                bResta.setMinWidth(31);
                GridPane.setConstraints(bResta, 3, 3);
                
                bUno.setMinWidth(31);
                GridPane.setConstraints(bUno, 0, 4);
                bDos.setMinWidth(31);
                GridPane.setConstraints(bDos, 1, 4);
                bTres.setMinWidth(31);
                GridPane.setConstraints(bTres, 2, 4);
                bSuma.setMinWidth(31);
                GridPane.setConstraints(bSuma, 3, 4);
                
                bCero.setMinWidth(90);
                GridPane.setConstraints(bCero, 0, 5);
                GridPane.setColumnSpan(bCero, 2); 
                bPunto.setMinWidth(31);
                GridPane.setConstraints(bPunto, 2, 5);
                bIgual.setMinWidth(31);
                GridPane.setConstraints(bIgual, 3, 5);
                
                GridPane.setConstraints(resultado, 0, 6);
                GridPane.setColumnSpan(resultado, 3);
                GridPane.setRowSpan(resultado, 3);
                
                GridPane.setConstraints(resultado_div, 5, 0);
                GridPane.setColumnSpan(resultado_div, 3);
                GridPane.setRowSpan(resultado_div, 3);
                
                GridPane.setConstraints(resultado_multi, 5, 2);
                GridPane.setColumnSpan(resultado_multi, 3);
                GridPane.setRowSpan(resultado_multi, 3);
                
                GridPane.setConstraints(resultado_resta, 5, 4);
                GridPane.setColumnSpan(resultado_resta, 3);
                GridPane.setRowSpan(resultado_resta, 3);
                
                GridPane.setConstraints(resultado_suma, 5, 6);
                GridPane.setColumnSpan(resultado_suma, 3);
                GridPane.setRowSpan(resultado_suma, 3);
                
                grid.getChildren().addAll(tfEntrada, bAC, bDivision, bSiete, bOcho,bNueve, bMultiplicacion, bCuatro, bCinco, bSeis, bResta, bUno, bDos, bTres, bSuma, bCero, bPunto, bIgual,resultado_div, resultado_multi, resultado_resta, resultado_suma, resultado);
		               
                Scene calculadora = new Scene(grid, 500, 450);
		primaryStage.setScene(calculadora);
		primaryStage.show();
		 //Obtienes la TimeStamp
		 long time = fecha.getTime();
		 ts = new Timestamp(time);
		 //Usamos SHA1 para conseguir la huella del cliente
		 huella= DigestUtils.sha1Hex(ts.toString());
		 huellaLabel.setText(huellaLabel.getText()+huella);
		 System.out.println("\nHuella del nodo: "+huella);
	}
	
	
	public void acusesRecibidos() {
		String received ="";
		int contador =0;
		int contentCode = 0;
		try {
			
		
				while(contador<3) {
					flujoSalida = new DataOutputStream(socket.getOutputStream());
					flujoEntrada = new DataInputStream(socket.getInputStream());
					
					//Enviamos la peticion para el acuse de recibido
					flujoSalida.writeUTF("100,0,0,0");
					received = flujoEntrada.readUTF(); 
					System.out.println("Mensaje servidor: "+received);
					contentCode = Integer.parseInt(received.split(",")[0]);
					System.out.println("Content Code: "+contentCode);
					if(contentCode==101) {
						acusesSuma++;
						System.out.println("Acuse recibido");

					}
					contador++;
				}
				contador=0;	
				
			
			
			
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Total de acuses: " + acusesSuma);
		
	}
	public String peticion(String userInput) {
		//int [] puertosNodos = {9800,9801,9802,9803,9804};
		//boolean conectado = false;
		String[] operaciones = userInput.split("\\+|\\-|\\*|\\/");
		float operando1 = Float.parseFloat(operaciones[0]);
		float operando2 = Float.parseFloat(operaciones[1]);
		String received ="";
		String resultado_recibido ="";
		//Socket socket = null;
		int codigo_Operacion = 0;

		String evento="";
		
		//Checamos que tipo de operacion es 
		if(userInput.contains("+")) {
			codigo_Operacion = 1;
			Operacion op= new Operacion(numSuma,operando1,operando2,huella);
			listaSumas[numSuma] = op;

			evento = op.evento;
			numSuma++;

		}else if(userInput.contains("-")) {
			codigo_Operacion = 2;
			
			Operacion op= new Operacion(numResta,operando1,operando2,huella);
			listaRestas[numResta] = op;
			evento = op.evento;
			numResta++;
		}else if(userInput.contains("*")) {
			codigo_Operacion = 3;
			Operacion op= new Operacion(numMulti,operando1,operando2,huella);
			listaMultis[numMulti] = op;
			evento = op.evento;
			numMulti++;

		}else if(userInput.contains("/")) {
			codigo_Operacion = 4;
			Operacion op= new Operacion(numDiv,operando1,operando2,huella);
			listaDivs[numDiv] = op;
			evento = op.evento;
			numDiv++;

		}
		
		if(acusesSuma <3) {
			System.out.println("No hay suficientes servidores para la operacion");
			return "";
		}
		acusesSuma=0;
		System.out.println("Cliente Iniciado");
		try {
			String datos = String.format("%d,%.2f,%.2f,%s",codigo_Operacion,operando1, operando2,evento);
			System.out.println("Se quiere enviar " +datos+ " al servidor");
			if(conectado) {

				//Creacion del Flujo de datos
				while(true) {
					flujoSalida = new DataOutputStream(socket.getOutputStream());
					flujoEntrada = new DataInputStream(socket.getInputStream());
					
							
							//Enviamos la operacion
			                flujoSalida.writeUTF(datos); 
			                System.out.println("Se envio "+datos+" al servidor exitosamente");
			                  
			                received = flujoEntrada.readUTF(); 
			                System.out.println("Se recibio "+received+" del servidor exitosamente");

			                String [] resultado_string = received.split(",");
			                
			                //Clasificamos la respuesta segun su codigo de contenido
			              
			                int contentCode= Integer.parseInt(resultado_string[0]);
			                resultado_recibido = resultado_string[1];
			                switch(contentCode) {
			                case 11:{
				                System.out.println("Respuesta del servidor: \nResultado suma= "+resultado_recibido); 
								resultado_suma.setText(resultado_suma.getText()+"\n"+tfEntrada.getText()+" = "+resultado_recibido);
			                	break;
			                }
			                case 12:{
				                System.out.println("Respuesta del servidor: \nResultado resta= "+resultado_recibido); 
								resultado_resta.setText(resultado_resta.getText()+"\n"+tfEntrada.getText()+" = "+resultado_recibido);

			                	break;
			                }
			                case 13:{
			                	 System.out.println("Respuesta del servidor: \nResultado multiplicacion= "+resultado_recibido); 
			                	 resultado_multi.setText(resultado_multi.getText()+"\n"+tfEntrada.getText()+" = "+resultado_recibido);
			                	break;
			                }
			                case 14:{
			                	 System.out.println("Respuesta del servidor: \nResultado division= "+resultado_recibido); 
			                	 resultado_div.setText(resultado_div.getText()+"\n"+tfEntrada.getText()+" = "+resultado_recibido);
			                	break;
			                }
			                default:{
			                	 System.out.println("No se reconocio el resultado"); 
			                }
			                }

				}

			}
			

		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultado_recibido;
	}
	public static boolean available(int port) { //Funcion que revisa si un puerto esta en uso
	    System.out.println("-Intentado conectarse al puerto: " + port);
	    Socket s = null;
	    try {
	        s = new Socket("localhost", port);

	        //Si llega hasta aqui es que hay un puerto aqui
	        System.out.println("Servidor detectado en el puerto " + port);
	        return false;
	    } catch (IOException e) {
	    	System.out.println("El puerto:  " + port + " esta libre");
	        return true;
	    } 
	}
        
	public void DuplicarServidores() {
		int required = 3-acusesSuma;
		
		for(int i =0;i<required;i++) {
			try {
				TimeUnit.SECONDS.sleep(5);
				Runtime rt = Runtime.getRuntime();
				 rt.exec("cmd /c start cmd.exe /K \"cd C:\\Users\\mkc\\Desktop\\Escuela\\Computo Distribuido\\Calculadora\\src\\ && java -jar Servidor.jar\"");

			}catch(IOException e) {
				
				
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}	
			
		}
		
	}
	
	public void conectar() {
		//Creacion de Socket
		 for(int i=0;i<5;i++) {
			 //Se conecta si esta abierto
			if(!Cliente.available(puertosNodos[i])) {
				try {
					socket = new Socket("localhost", puertosNodos[i]);
				} catch (IOException e) {
					
					e.printStackTrace();
				}
				System.out.println("Conectado a puerto: "+ puertosNodos[i]);
				conectado = true;
				break;
			};
		}
		
	}
	public void acusar() {
		
		for(int i=0;i<3;i++) {
			System.out.println("Intento "+(i+1)+"de obtener los acuses");
			System.out.println("Total acuses: "+acusesSuma);
			
			if(acusesSuma >= 3) {
				acusesSuficientes = true;
				break;
			}else {
				conectar();
				acusesSuma =0;
				acusesRecibidos();
			}
			
		}
		

		
	}
}

class Operacion{
	int indice;
	float operando1;
	float operando2;
	String evento;
	
	public Operacion(int ind, float op1,float op2,String huella) {
		indice = ind;
		operando1 = op1;
		operando2 = op2;
		String temporal = ""+indice+","+huella+","+operando1+","+operando2;
		evento = DigestUtils.sha1Hex(temporal);
	}
	
}


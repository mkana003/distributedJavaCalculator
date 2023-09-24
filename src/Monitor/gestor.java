package Monitor;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;

public class gestor {
	int puertoMonitor =10320; //Nodo 
	boolean conectado = false;
	ServerSocket miServidor = null;
	int puerto_utilizado=0;
	String action ="";
	ArrayList<String> huellas = new ArrayList<String>();
	boolean valido=false;
	Socket socket = null;
	DataInputStream flujoEntrada = null;
	DataOutputStream flujoSalida = null;
	
	public static void main(String[] args) {
		gestor gestor = new gestor();
		gestor.Iniciar();
	}
	public void Iniciar() {
		//Conmexion inicial al monitor
		if(available(puertoMonitor)) {
			 try {
				miServidor = new ServerSocket(puertoMonitor);
			} catch (IOException e) {
				e.printStackTrace();
			}
			 System.out.println("\nConexion inicial a monitor");
			 puerto_utilizado = puertoMonitor;
			 conectado = true;
			 
		}else {
			System.out.println("\nNo se pudo conectar el monitor");
			return;
		}
		System.out.println("\n=================MONITOR ADSOA=====================");
		System.out.println("\n==================================================");
		System.out.println("\n==================BIENVENIDO======================");
		System.out.println("\n==================================================");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		



							while(true) {
								System.out.println("Accion a realizar: (Acuse/Suma/Resta/Multiplicacion/Division)");
								try {
									 action = reader.readLine();
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								System.out.println("\nAccion escogida: "+action);
								
									if(conectado) { //Si se logro inicializar el monitor
										Socket miSocket = null;
										switch(action) {
											case "Acuse":{
												valido=true;
												System.out.println("Intentando conexion con el campo de datos");
												conectar();
												acusesRecibidos();
												try {
													flujoSalida = new DataOutputStream(socket.getOutputStream());
												} catch (IOException e1) {
													
													e1.printStackTrace();
												}
												try {
													flujoEntrada = new DataInputStream(socket.getInputStream());
												} catch (IOException e) {
													
													e.printStackTrace();
												}
												System.out.println("Conexion Exitosa");
												for(String ele : huellas) 
										    	{ 
										    		System.out.println("Huella: "+ele); 
										    	} 
												break;
											}
											case "Suma":{
												System.out.println("Intentando conexion con el campo de datos para sumar");
												conectar();
												
												try {
													 byte[] bytesSuma = Files.readAllBytes(Paths.get("C:/Users/mkc/Desktop/Escuela/Computo Distribuido/Operaciones/Suma.jar"));
												     String op1 = Base64.getEncoder().encodeToString(bytesSuma);
												     String huellaDestino = huellas.get(0);
												     System.out.println("Vamos a enviarlo a "+huellaDestino);
													flujoSalida = new DataOutputStream(socket.getOutputStream());
													flujoSalida.writeUTF("61,"+op1+","+huellaDestino+",0");
													flujoEntrada = new DataInputStream(socket.getInputStream());
												} catch (IOException e) {
													
													e.printStackTrace();
												}
												
												valido=true;
												break;				
											}
											case "Resta":{
												System.out.println("Intentando conexion con el campo de datos para restar");
												conectar();
												
												try {
													byte[] bytesResta = Files.readAllBytes(Paths.get("C:/Users/mkc/Desktop/Escuela/Computo Distribuido/Operaciones/Resta.jar"));
												     String op2 = Base64.getEncoder().encodeToString(bytesResta);
												     String huellaDestino = huellas.get(0);
												     System.out.println("Vamos a enviarlo a "+huellaDestino);
													flujoSalida = new DataOutputStream(socket.getOutputStream());
													flujoSalida.writeUTF("62,"+op2+","+huellaDestino+",0");
													flujoEntrada = new DataInputStream(socket.getInputStream());
												} catch (IOException e) {
													
													e.printStackTrace();
												}
												valido=true;
												break;
											}
											case "Multiplicacion":{
												System.out.println("Intentando conexion con el campo de datos para multiplicar");
												conectar();
												
												try {
													byte[] bytesMulti = Files.readAllBytes(Paths.get("C:/Users/mkc/Desktop/Escuela/Computo Distribuido/Operaciones/Multiplicacion.jar"));
												     String op3 = Base64.getEncoder().encodeToString(bytesMulti);
													flujoSalida = new DataOutputStream(socket.getOutputStream());
													String huellaDestino = huellas.get(0);
												     System.out.println("Vamos a enviarlo a "+huellaDestino);
													flujoSalida.writeUTF("63,"+ op3+","+huellaDestino+",0");
													flujoEntrada = new DataInputStream(socket.getInputStream());
												} catch (IOException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
												valido=true;
												break;				
											}
											case "Division":{
												System.out.println("Intentando conexion con el campo de datos para restar");
												conectar();
												
												try {
													byte[] bytesDiv = Files.readAllBytes(Paths.get("C:/Users/mkc/Desktop/Escuela/Computo Distribuido/Operaciones/Division.jar"));
												     String op4 = Base64.getEncoder().encodeToString(bytesDiv);
													flujoSalida = new DataOutputStream(socket.getOutputStream());
													String huellaDestino = huellas.get(0);
												     System.out.println("Vamos a enviarlo a "+huellaDestino);
													flujoSalida.writeUTF("64,"+op4+","+huellaDestino+",0");
													flujoEntrada = new DataInputStream(socket.getInputStream());
												} catch (IOException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
												valido=true;
												break;
											}
											default:{
												System.out.println("Accion invalida");
												break;
											}
										
										}
								
								}	

					
					
					}
				
				
			}



	

		//Inicializamos el nodo a un puerto
		
							
	
	public static boolean available(int port) {//Funcion que revisa si un puerto esta en uso
	    System.out.println("--------------Probando puerto " + port);
	    Socket s = null;
	    try {
	        s = new Socket("localhost", port);

	        //Si llega hasta aqui es que hay un puerto aqui

	        System.out.println("--------------Puerto " + port + " tiene un servidor en linea");
	        return false;
	    } catch (IOException e) {
	    	System.out.println("--------------Puerto " + port + " no tiene un servidor en linea");
	        return true;
	    } 
	}
	public void conectar() {
		int [] puertosNodos = {9800,9801,9802,9803,9804};
		
		//Creacion de Socket
		 for(int i=0;i<5;i++) {
			 //Se conecta si esta abierto
			if(!gestor.available(puertosNodos[i])) {
				try {
					socket = new Socket("localhost",puertosNodos[i]);
				} catch (IOException e) {
					
					e.printStackTrace();
				}
				System.out.println("Conectado a puerto: "+puertosNodos[i]);
				conectado = true;
				break;
			};
		}
		
	}
	public void acusesRecibidos() {
		String received ="";
		int contador =0;
		int contentCode = 0;
		try {
			
				System.out.println("Intentando acuses");
				while(contador<3) {
					flujoSalida = new DataOutputStream(socket.getOutputStream());
					flujoEntrada = new DataInputStream(socket.getInputStream());
					
					//Enviamos la peticion para el acuse de recibido
					flujoSalida.writeUTF("50,0,0,0");
					received = flujoEntrada.readUTF(); 
					System.out.println("Mensaje servidor: "+received);
					contentCode = Integer.parseInt(received.split(",")[0]);
					System.out.println("Content Code: "+contentCode);
					if(contentCode==51) {
						huellas.add(received.split(",")[1]);
						System.out.println("Acuse recibido");

					}
					contador++;
				}
				contador=0;	
				
			
			
			
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Acuses completados" );
		
	}
	static Random rand = new Random();
	static <T> T getRandomItem(ArrayList<T> list) {
	    return list.get(rand.nextInt(list.size()));
	}

}




package SegundaEntrega;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Date;
import org.apache.commons.codec.digest.DigestUtils;


/**
 *
 * @author mkc
 */

public class Servidor  {

	public static void main(String[] args){
			
		System.out.print("Bienvenido");		

		int [] puertosServidores = {9995,9996,9997,9998,9999};

		boolean conectado = false;
		ServerSocket miServidor = null;
		Date fecha= new Date();	 
		Timestamp ts= null;
		String huella="";
		try{
			String currentDirectory = System.getProperty("user.dir");
		     System.out.println("\nDirectorio actual " + currentDirectory);
			//Inicializamos el servidor
			for(int i=0;i<5;i++) {
				if(available(puertosServidores[i])) {
					 miServidor = new ServerSocket(puertosServidores[i]);
					 System.out.println("Conexion");
					 conectado = true;
					 //Obtienes la TimeStamp
					 long time = fecha.getTime();
					 ts = new Timestamp(time);
					 //Usamos SHA1 para conseguir la huella del servidor
					 System.out.println("\nIntentando obtener huella");
					 huella= DigestUtils.sha1Hex(ts.toString());
					 System.out.println("\nHuella del servidor: "+huella);
					 break;
				}
				
			}
			//Si fue exitosa la inicializacion nos ponemos a la escucha
			if(conectado) {
				Socket miSocket = null;
				while(true) {
					miSocket = miServidor.accept(); //Acepta todas las conexiones
					System.out.println("Se ha aceptado un nuevo cliente");
				
					//Creacion del Flujo de datos
					DataInputStream flujoEntrada = new DataInputStream(miSocket.getInputStream());
					DataOutputStream flujoSalida = new DataOutputStream(miSocket.getOutputStream());

					System.out.println("Asignando un nuevo Thread al cliente");	
					Thread t = new ClientHandler2(miSocket, flujoEntrada, flujoSalida,huella);
					t.start();		

				}
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
			
		}

	private static boolean available(int port) { //Funcion que revisa si un puerto esta en uso
	    System.out.println("--------------Probando puerto " + port);
	    Socket s = null;
	    try {
	        s = new Socket("localhost", port);

	        //Si llega hasta aqui es que hay un puerto aqui

	        System.out.println("--------------Puerto " + port + " no esta disponible");
	        return false;
	    } catch (IOException e) {
	    	System.out.println("--------------Puerto " + port + " esta disponible");
	        return true;
	    } 
	}
	
}

class ClientHandler2 extends Thread {

	DataInputStream dataInputStream; 
    DataOutputStream dataOutputStream; 
    Socket sock; 
    int [] puertosServidores = {9999,9998,9997,9996,9995};
	int [] puertosNodos = {9800,9801,9802,9803,9804};
	String fingerprint="";
	String currentDirectory = System.getProperty("user.dir");
	public ClientHandler2(Socket s, DataInputStream dis, DataOutputStream dos,String huella)  
    { 
        this.sock = s; 
        this.dataInputStream = dis; 
        this.dataOutputStream = dos; 
        this.fingerprint =huella;
    } 
	 
	 @Override
	    public void run()  
	    { 

	        while(true) {
	            try { 
					//Recibiendo mensaje y separandolo en variables
					String mensaje = dataInputStream.readUTF();
					System.out.println("Mensaje del cliente: "+mensaje);
					
					String [] operandos = mensaje.split(",");
					int op = Integer.parseInt(operandos[0]);
					if(op==100) {
						//100 es el codigo de contenido para un acuse
						dataOutputStream.writeUTF("101,0,0,0,0"); //Regresa 101, que es el contentCode para decir que esta en linea
					}else if(op==50){
						//50 es el contentCode de un acuse del Monitor
						dataOutputStream.writeUTF("51,"+this.fingerprint+",0,0,0"); //Regresa 51 y su huella, que es el contentCode para decir que esta en linea
					}
					else if(op==61||op==62||op==63||op==64) {
						switch(op) {
						
							case 61:{
								System.out.println("Huella: "+this.fingerprint);
								System.out.println("Comparacion: "+operandos[2].equals(this.fingerprint));
								if(operandos[2].equals(this.fingerprint)) {
									System.out.println("Huella: "+mensaje);
									byte[] decode = Base64.getDecoder().decode(operandos[1]);
									writeBytesToFile(currentDirectory+"/Suma.jar", decode);
								}
								break;
							}
							case 62:{
								if(operandos[2].equals(this.fingerprint)) {
									System.out.println("Huella: "+this.fingerprint);
									System.out.println("Comparacion: "+operandos[2].equals(this.fingerprint));
									byte[] decode = Base64.getDecoder().decode(operandos[1]);
									writeBytesToFile(currentDirectory+"/Resta.jar", decode);
								}
								break;
							}
							case 63:{
								if(operandos[2].equals(this.fingerprint)) {
									System.out.println("Huella: "+this.fingerprint);
									System.out.println("Comparacion: "+operandos[2].equals(this.fingerprint));
									byte[] decode = Base64.getDecoder().decode(operandos[1]);
									writeBytesToFile(currentDirectory+"/Multiplicacion.jar", decode);
								}
								break;
							}	
							case 64:{
								if(operandos[2].equals(this.fingerprint)) {
									System.out.println("Huella: "+this.fingerprint);
									System.out.println("Comparacion: "+operandos[2].equals(this.fingerprint));
									byte[] decode = Base64.getDecoder().decode(operandos[1]);
									writeBytesToFile(currentDirectory+"/Division.jar", decode);
								}
								break;
							}
						}
						System.out.println("Se completo");
						
					}
					else{
						String path = "SegundaEntrega";
						String classname = "";
						String archivo = "";
						float operando1 = Float.parseFloat(operandos[1]);
						float operando2 = Float.parseFloat(operandos[2]);
						Class cls = null;
						String respuesta = "";
						Object obj = null;
						boolean exists=false;
						ClassLoader clsloader = ClassLoader.getSystemClassLoader();
						System.out.print("\nTratando de usar el ClassLoader");
						char operacion=' ';
						
						//Determinas la operacion segun el content code
						switch(op) {
						case 1:
							operacion = '+';
							classname = "suma";
							archivo="Suma";
							File f = new File(currentDirectory+"/"+archivo + ".jar");
				        	exists = f.exists();
							break;
						case 2:
							operacion = '-';
							classname = "resta";
							archivo="Resta";
							File f1 = new File(currentDirectory+"/"+archivo + ".jar");
				        	exists = f1.exists();
							break;
						case 3:
							operacion = '*';
							classname = "multiplicacion";
							archivo="Multiplicacion";
							File f2 = new File(currentDirectory+"/"+archivo + ".jar");
				        	exists = f2.exists();
							break;
						case 4:
							operacion = '/';
							classname = "division";
							archivo="Division";
							File f3 = new File(currentDirectory+"/"+archivo + ".jar");
				        	exists = f3.exists();
							break;
						default:
							operacion = ' ';
							break;
						}
						
			        	 if(exists) {
			        		 System.out.println("\n-----Puedo realizar esa operacion-----");
			        		//Usamos el class loader para cargar dinamicamente las clases
								try {
                                                                        System.out.println("\nRuta de la operacion " + path+"."+classname);
									cls=clsloader.loadClass(path+"."+classname);
								} catch (ClassNotFoundException e) {
									
									e.printStackTrace();
								}
								
								try {
									 obj = cls.newInstance();
								} catch (InstantiationException e) {
									
									e.printStackTrace();
								} catch (IllegalAccessException e) {
									
									e.printStackTrace();
								}
								//Realizamos la operacion
								respuesta=((OperacionesArit)obj).Operar(operando1,operando2);
								System.out.print("\nOperando1: "+operando1);
								System.out.print("\nOperando2: "+operando2);
								System.out.print("\nOperacion: "+operacion);
								System.out.print("\nRespuesta de la clase: "+respuesta);
								dataOutputStream.writeUTF(respuesta); 
			        	 }else {
			        		 System.out.println("\n-----No se realizar esa operacion----");
			        		 respuesta="0,0";
			        		 dataOutputStream.writeUTF(respuesta); 
			        	 }
						
						
		                     
					}
					
	                        

	            } catch (IOException e) { 
	                e.printStackTrace(); 
	            } 
    
//	        try
//	        { 
//	            // closing resources 
//	            this.dataInputStream.close(); 
//	            this.dataOutputStream.close(); 
//	              
//	        }catch(IOException e){ 
//	            e.printStackTrace(); 
//	        } 
	    }
	    }
	 private void writeBytesToFile(String dDivisionjar, byte[] bytes) throws IOException{
	        try (FileOutputStream fos = new FileOutputStream(dDivisionjar)) {
	            fos.write(bytes);
	        }
	    }

}


package PrimerIntento;

import java.awt.BorderLayout;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author mkc
 */
public class Servidor extends UnicastRemoteObject implements Middleware{
    private static final long serialVersionUID = 1L;
    private static JTextArea jta = new JTextArea();
    private JFrame jframe = new JFrame();
    
    public Servidor() throws RemoteException {
	  super();
	  InterfazServidor();
  }
    
    public void InterfazServidor() {
	  jframe.setLayout(new BorderLayout());
	  jframe.add(new JScrollPane(jta), BorderLayout.CENTER);
	  jframe.setTitle("Servidor");
	  jframe.setSize(500, 300);
	  jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  jframe.setVisible(true);

  }
    
    public String GetClientIP() {
	  try {
		  jta.append("\nCliente conectado en: " + getClientHost());
		  
	  } catch (ServerNotActiveException ex) {
		  System.out.println(ex);
	  }
	  return "Cliente Conectado";
  }
    
  public int Suma(int x, int y) {
    int suma = x + y;
    try {
                  jta.append("\nOperacion solicitada del Cliente: " + getClientHost() 
                  + "\nPrimer valor: " + x 
                  + "\nSegundo valor: " + y 
                  + "\nOperacion: +" + "\nResultado: " + suma);
          } catch (ServerNotActiveException e) {
                  e.printStackTrace();
          }
    return suma;
  }
  
    public int Mult(int x, int y) {
            int mult = x * y;
            try {
                    jta.append("\nOperacion solicitada del Cliente:" + getClientHost() 
                    + "\nPrimer valor: " + x 
                    + "\nSegundo valor:  " + y 
                    + "\nOperacion: x" + "\nResultado: " + mult );
            } catch (ServerNotActiveException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
            }
            return mult;	
    }
    
    public int Div(int x, int y) {
        double div = (double)x/(double)y;
        try {
                jta.append("\nOperacion solicitada del Cliente: " + getClientHost() 
                + "\nPrimer valor: " + x 
                + "\nSegundo valor:  " + y 
                + "\nOperacion /" + "\nResultado: " + div );
        } catch (ServerNotActiveException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }

        return (int)div;	
    }
    
    public int Resta(int x, int y) {
        int resta = x - y;	
        try {
                jta.append("\nOperacion solicitada del Cliente:" + getClientHost() 
                + "\nPrimer valor: " + x 
                + "\nSegundo valor:  " + y 
                + "\nOperacion -" + "\nResultado: " + resta );
        } catch (ServerNotActiveException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        } 	
        return resta;	
    }
    
    public static void main(String[] args) {
	  try {
		  Servidor obj = new Servidor();
		  jta.append("Servidor iniciado: " + new Date() + '\n');
		  Registry registry = LocateRegistry.createRegistry( 1200 );
		  registry.rebind("ServerRMI", obj);
		  jta.append("Server bound in registry");
	  }
	  catch (Exception e) {
		  System.out.println("Server error: " + e.getMessage());
		  e.printStackTrace();
	  }
  }

}

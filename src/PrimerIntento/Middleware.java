package PrimerIntento;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author mkc
 */
public interface Middleware extends Remote {
	
	int Suma(int x, int y) throws RemoteException;
	int Mult(int x, int y) throws RemoteException;
	int Div(int x, int y) throws RemoteException;
	int Resta(int x, int y) throws RemoteException;
	String GetClientIP() throws RemoteException;

}

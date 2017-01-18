package client_graphique;

import java.net.Socket;

import pair.Client;

public class ServiceClient extends Thread {
	
	private Socket socket;
	private Client clientServeur;

	public ServiceClient(Socket socket, Client clientServeur) {
		this.socket = socket;
		this.clientServeur = clientServeur;
	}
	
	public void run() {
		System.out.println("Run du ServiceClient");
		this.clientServeur.Service_Client(this.socket);
	}
	
}

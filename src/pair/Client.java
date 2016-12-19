package pair;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Client implements Runnable {
	
	long key;	
	InetAddress adr;
	int port;
	Socket socket;
	ServerSocket serverSocket;
		
	/**
	 * Un client est d�fini par sa key, son adress et son port	
	 * @param key
	 * @param adressClient
	 * @param port
	 */
	public Client (int key, InetAddress adressClient, int port){
		this.key = genererKey(adressClient);
		this.adr = adressClient;
		this.port = port;
	}
	
	public static void main(String[] args) {
		
	}

	private long genererKey(InetAddress adressClient){
		return adressClient.hashCode();
	}
	
	/*public client getPairResponsable(){
		
		
	}
	
	*/
	@Override
	public String toString() {
		return "client [id=" + this.key + ", adr=" + this.adr + ", port=" + this.port + "]";
	}

	public Client getClient(){
		return this;
	}

	public long getKey() {
		return key;
	}


	public InetAddress getAdr() {
		return adr;
	}


	public int getPort() {
		return port;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	
}

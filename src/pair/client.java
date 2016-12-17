package pair;

import java.net.InetAddress;

public class client {
	int key;	
	InetAddress adr;
	int port;
		
	/**
	 * Un client est défini par sa key, son adress et son port	
	 * @param key
	 * @param adressClient
	 * @param port
	 */
	public client (int key, InetAddress adressClient, int port){
		this.key = key;
		this.adr = adressClient;
		this.port = port;
	}

	
	/*public client getPairResponsable(){
		
		
	}
	
	*/
	@Override
	public String toString() {
		return "client [id=" + this.key + ", adr=" + this.adr + ", port=" + this.port + "]";
	}

	public client getClient(){
		return this;
	}

	public int getKey() {
		return key;
	}


	public InetAddress getAdr() {
		return adr;
	}


	public int getPort() {
		return port;
	}
	
	
}

package pair;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

import p2p.Annuaire;
import node.ChordPeer;

public class Client /*implements Runnable */{
	
	private long key;	
	private InetAddress adr;
	private int port;
	private Socket socket;
	private ServerSocket serverSocket;
	private ChordPeer chordPeer;
	private static Annuaire annuaire;
		
	/**
	 * Un client est defini par sa key, son adress et son port	
	 * @param key
	 * @param adressClient
	 * @param port
	 */
	public Client (InetAddress adressClient, int port){
		this.key = genererKey(adressClient, port);
		this.adr = adressClient;
		this.port = port;
		this.chordPeer = new ChordPeer(this.key);
		this.chordPeer.setClient(this);
		this.socket = new Socket();
		try {
			//this.serverSocket = new ServerSocket(this.chordPeer.getClient().getPort());
			this.serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (annuaire == null) {
			annuaire = new Annuaire();
		}
		annuaire.ajouterClient(this);
		
		ChordPeer handle = annuaire.getChordPeerHandle();
		if (handle == null) {
			handle = this.chordPeer;
		}
		this.joinMainChord(handle);
		
		Thread t = new Thread() {
		    public void run() {
		    	try {
					execute();
					System.out.println("socket lanc�");
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }
		};
		t.start();		
		
		//this.chordPeer.getSalons().
		
	}
	
	private long genererKey(InetAddress adressClient, int port){
		 return adressClient.hashCode() + port;
	}
	
	/**
	 * Retourne l'emplacement de key dans l’anneau
	 * @param key du pair souhaitant rejoindre l’anneau
	 * @return InetAddress adresse IP du successeur de key dans l’anneau
	 */
	public InetAddress findMainChord(long key)  {
		
		ChordPeer responsable = this.chordPeer.findkey(key);
		return responsable.getSuccesseur().getClient().getAdr();
		
	}
	
	/**
	 * Permet de s'inserer par le biais d'un pair connu qui se trouve dans le reseau (handleChordPeer)
	 * Initialisation des references distantes (predecesseur et successeur)
	 * Si qu'un chord dans l'annuaire = renvoie toi-meme (?)
	 */
	public void joinMainChord(ChordPeer handleChordPeer) {
		
		this.chordPeer.setSuccesseur(handleChordPeer.findkey(this.getKey()));
		this.chordPeer.setPredecesseur(this.chordPeer);
		
	}
	
	/**
	 * Met à jour les liens de l’anneau virtuel 
	 * i,e notre predecesseur devra des lors pointer sur notre successeur
	 */
	public void leaveMainChord(ChordPeer ancienChordPeer) {
		
		this.chordPeer.getPredecesseur().setSuccesseur(this.chordPeer.getSuccesseur());
	}
	
	/**
	 * Faire circuler un message vers son/ses sucesseurs
	 * On veillera a ne pas transmettre le meme message deux fois
	 * @param data message a faire circuler
	 */
	public void forwardMessage(String data) {
		
		// Pas circuler deux fois le meme message
		for (byte[] ancienMessageByte : this.chordPeer.getChainesStockees()) {
			String ancienMessage = new String(ancienMessageByte, StandardCharsets.UTF_8);
			if (ancienMessage == data) {
				return;
			}
		}
		
		// TODO Au pluriel ?
		/*for (ChordPeer successeur : this.chordPeer.getSuccesseur()) {
			
		}*/
		
		InetAddress destinataireAdresse = this.chordPeer.getSuccesseur().getClient().getAdr();
		System.out.println("destinataireAdresse : " + destinataireAdresse);
		int destinatairePort = this.chordPeer.getSuccesseur().getClient().getPort();
		System.out.println("destinatairePort : " + destinatairePort);
		InetSocketAddress destinaireAdresseFormat = new InetSocketAddress(destinataireAdresse, destinatairePort);
		System.out.println("destinaireAdresseFormat : " + destinaireAdresseFormat);
		
		System.out.println("port via chorPeer "  + this.chordPeer.getClient().getPort());
		
		try {
			System.out.println("(SocketAddress) destinaireAdresseFormat : " + (SocketAddress) destinaireAdresseFormat);
			
			this.socket.connect((SocketAddress) destinaireAdresseFormat, 500);

			//Send the message to the server
	        OutputStream os = socket.getOutputStream();
	        OutputStreamWriter osw = new OutputStreamWriter(os);
	        BufferedWriter bw = new BufferedWriter(osw);

	        bw.write(data);
	        bw.flush();
	        
		} catch (SocketTimeoutException ste) {
			System.out.println("Timeout depasse");
			forwardMessage(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
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
	
	

	public ChordPeer getChordPeer() {
		return chordPeer;
	}

	
	public void execute() throws IOException {

		while (true) {
			System.out.println("[Serveur]:  waiting for connexion");
			this.socket = this.serverSocket.accept();
			String c_ip = this.socket.getInetAddress().toString();
			int c_port = this.socket.getPort();
			System.out.format("[Serveur] : Arr. Client IP %s sur %d\n", c_ip, c_port);
			System.out.format("[Serveur ]: Creation du thread T_%d\n", c_port);
			
			//Reading the message from the client
            InputStream is = this.socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String data = br.readLine();
            System.out.println("Message received from client is " + data);
		}
	}
	
}

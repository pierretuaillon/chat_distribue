package pair;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

import p2p.Annuaire;
import node.ChordPeer;

public class Client implements Runnable {
	
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
		this.key = genererKey(adressClient);
		this.adr = adressClient;
		this.port = port;
		this.chordPeer = new ChordPeer(this.key);
		
		try {
			this.serverSocket = new ServerSocket(this.chordPeer.getClient().getPort());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ChordPeer handle = annuaire.getChordPeerHandle();
		if (handle == null) {
			handle = this.chordPeer;
		}
		this.joinMainChord(handle);
		annuaire.ajouterClient(this);
		
		//this.chordPeer.getSalons().
		
	}
	
	private long genererKey(InetAddress adressClient){
		return adressClient.hashCode();
	}
	
	/**
	 * Retourne l'emplacement de key dans l’anneau
	 * @param key du pair souhaitant rejoindre l’anneau
	 * @return int
	 */
	public int findMainChord(long key)  {
		return 0;
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
		
		// Faire circuler
		// sendToChatRoom ? 
		// TODO
		
		// Pas circuler deux fois le meme message
		for (byte[] ancienMessageByte : this.chordPeer.getChainesStockees()) {
			String ancienMessage = new String(ancienMessageByte, StandardCharsets.UTF_8);
			if (ancienMessage == data) {
				return;
			}
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

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {

		Client client;
		try {
			client = new Client(InetAddress.getLocalHost(), 12000);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		// definir le timeout
		// new SocketException("Timeout depasse");
	}
	
	public void execute() throws IOException {

		while (true) {
			System.out.println("[Serveur]:  waiting for connexion");
			this.socket = this.serverSocket.accept();
			String c_ip = this.socket.getInetAddress().toString();
			int c_port = this.socket.getPort();
			System.out.format("[Serveur] : Arr. Client IP %s sur %d\n", c_ip, c_port);
			System.out.format("[Serveur ]: Creation du thread T_%d\n", c_port);

			//TODO new Thread(new Client().start());
		}
	}
	
}

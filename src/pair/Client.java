package pair;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.swing.JPanel;
import javax.swing.JTextField;

import client_graphique.Graphique_client;
import client_graphique.ServiceClient;
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
	Graphique_client graphique_client;

	public void setGraphique_client(Graphique_client graphique_client) {
		this.graphique_client = graphique_client;
	}

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
		//this.socket = new Socket();

		try {
			//this.serverSocket = new ServerSocket(this.chordPeer.getClient().getPort());
			this.serverSocket = new ServerSocket(port);

			// Je suis le premier a arriver donc le client par defaut
			if (getAnnuaire() == null) { // Singleton plutot ?
				setAnnuaire(new Annuaire(InetAddress.getLocalHost()));
				this.chordPeer.setPredecesseur(this.chordPeer);
				this.chordPeer.setSuccesseur(this.chordPeer);
				getAnnuaire().setChordPeer(this.chordPeer);
				getAnnuaire().setMaxKey(this.key);
			}
			// Si je ne suis pas le client par defaut, je me connecte a celui-ci (il ne va pas se connecter a lui-meme)
			else {

				if (getAnnuaire().testMaxKey(this.key)){
					getAnnuaire().setMaxKey(this.key);
				}
				System.out.println("Je suis port " + getPort() + " avec key " + this.key + " et je veux rejoindre");
				this.joinMainChord(getAnnuaire().getChordPeer());

				//InetSocketAddress destinaireAdresseFormat = new InetSocketAddress(this.chordPeer.getSuccesseur().getClient().getAdr(), this.chordPeer.getSuccesseur().getClient().getPort());
				//this.socket.connect((SocketAddress) destinaireAdresseFormat, 500);

				//this.socket = new Socket(this.chordPeer.getSuccesseur().getClient().getAdr(), this.chordPeer.getSuccesseur().getClient().getPort());

			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}

		Thread t = new Thread() {
			public void run() {
				try {
					System.out.println("Thread lance");



					execute();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		//t.start();
		t.start();

		//this.chordPeer.getSalons().

	}

	private long genererKey(InetAddress adressClient, int port){
		//System.out.println((adressClient.hashCode() + port) *-1);
		return ((adressClient.hashCode() + port) *-1);
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
	 */
	public void joinMainChord(ChordPeer handleChordPeer) {

		System.out.println();
		System.out.println("----");

		System.out.println("joinMainChord");
		System.out.println("successeur : " + handleChordPeer.findkey(this.getKey()));


		ChordPeer successeur = handleChordPeer.findkey(this.getKey());

		ChordPeer predecesseur = successeur.getPredecesseur();
		System.out.println("predecesseur :" + successeur.getPredecesseur());

		System.out.println();
		System.out.println("----");

		this.chordPeer.setSuccesseur(successeur);
		this.chordPeer.setPredecesseur(predecesseur);

		successeur.setPredecesseur(this.chordPeer);
		predecesseur.setSuccesseur(this.chordPeer);

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
		
		if (data == "" || data == " " || data == "\n") {
			System.out.println("pas de char");
			return;
		}

		System.out.println("Je suis " + toString() + " et j'envoie a " + this.chordPeer.getSuccesseur().getClient());
		System.out.println("forwardMessage de " + data);

		// Pas circuler deux fois le meme message
		for (byte[] ancienMessageByte : this.chordPeer.getChainesStockees()) {
			String ancienMessage = new String(ancienMessageByte, StandardCharsets.UTF_8);
			System.out.println("Ancien message : " + ancienMessage + " different de " + data + "?");
			if (ancienMessage.equals(data)) {
				System.out.println("Identique !");
				return;
			}
			else {
				System.out.println("Pas identique !");
			}
		}

		// TODO Au pluriel ?
		/*for (ChordPeer successeur : this.chordPeer.getSuccesseur()) {

		}*/


		try {
			this.socket = new Socket(this.chordPeer.getSuccesseur().getClient().getAdr(), this.chordPeer.getSuccesseur().getClient().getPort());

			//Send the message to the server
			OutputStream os = socket.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			BufferedWriter bw = new BufferedWriter(osw);
			System.out.println("Avant Write data " + data);
			bw.write(data);
			bw.flush();
			
			// On stocke dans les chaines envoyees
			byte[] tradBytes = data.getBytes(Charset.forName("UTF-8"));
			this.chordPeer.getChainesStockees().add(tradBytes);
			System.out.println("On ajoute le byte[] de " + data);
			
			System.out.println("après Write data " + data);

			this.socket.close();

		} catch (SocketTimeoutException ste) {
			System.out.println("Timeout depasse");
			this.forwardMessage(data);
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

	public void gererReceptionMessage(JsonObject message) {

		// POST = message normal aux gens
		// Si je gere ca, c'est que je suis le serveur
		if (message.getString("GET") == "CONNEXION") {
			InetAddress successeurDuNew = this.findMainChord(Long.parseLong(message.getString("KEY")));
		}

		// Le client defaut va recevoir des instructions que les autres ne recevront pas
		// Genre cas ou un nouveau client veut rejoindre etc.
		// Les autres recevront les messages uniquement (par le client defaut) et peut-etre les notif d'event (nouvel arrivant...)
	}

	public void execute() throws IOException {



		while(true) {
			try {
				System.out.println("Waiting for client on port " + 
						serverSocket.getLocalPort() + "...");

				Socket socketAccept = serverSocket.accept();
				ServiceClient serviceClient = new ServiceClient(socketAccept, this);
				serviceClient.run();


			}catch(SocketTimeoutException s) {
				System.out.println("Socket timed out!");
				break;
			}catch(IOException e) {
				e.printStackTrace();
				break;
			}
		}
	
	}
	
	

	public JPanel getGraphique_client() {
		return graphique_client;
	}

	public void Service_Client(Socket socket2) {

		System.out.println("Thread gestion client lance");

		//System.out.println("Just connected to " + socket2.getRemoteSocketAddress());
		DataInputStream in;
		try {
			in = new DataInputStream(socket2.getInputStream());


			// available stream to be read
			int length = in.available();

			// create buffer
			byte[] buf = new byte[length];

			// read the full data into the buffer
			in.readFully(buf);


			System.out.println("J'ai reçu : ");
			StringBuilder sb = new StringBuilder(); // ou StringBuffer je sais plus

			// for each byte in the buffer
			for (byte b:buf)
			{
				// convert byte to char
				char c = (char)b; 
				sb.append(c);

				// prints character
				//System.out.println("On append : " + c);
			}
			
			System.out.println("Taille sb : " + sb.length());

			String messageRecu = sb.toString();
			System.out.println("Message recu : " + messageRecu);
			
			// Evite la lecture alors que le message n'a pas fini d'etre envoye
			if (sb.length() == 0) {
				System.out.println("Je n'ai pas encore recu, j'attends et je reessaie");
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Service_Client(socket2);
			}
			else {
				System.out.println("j'ai bien quelque chose !" + messageRecu + "<-");
			}

			// Faudrait passer par JSON dans l'envoi et reception
			// puis gererReceptionMessage()
			
			if (this.graphique_client != null) {
				this.graphique_client.ajouterMessage(messageRecu);	
			}
			
			// Puis on transmet au sucesseur
			//this.forwardMessage(messageRecu);
			
			System.out.println("close socket");
			socket2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Annuaire getAnnuaire() {
		return annuaire;
	}

	public static void setAnnuaire(Annuaire annuaire) {
		Client.annuaire = annuaire;
	}

}

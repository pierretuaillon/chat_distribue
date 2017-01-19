package pair;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.swing.JPanel;

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

		try {
			this.serverSocket = new ServerSocket(port);

			// Je suis le premier a arriver donc le client par defaut
			if (getAnnuaire() == null) {
				setAnnuaire(new Annuaire());

				getAnnuaire().setPortClientConnecte(this.port);
				getAnnuaire().setAdresseClientConnecte(this.adr);

				//InetAddress.getLocalHost())
				this.chordPeer.setPredecesseur(this.chordPeer);
				this.chordPeer.setSuccesseur(this.chordPeer);
				getAnnuaire().setChordPeer(this.chordPeer);
				getAnnuaire().setMaxKey(this.key);

				System.out.println("Je suis l'annuaire : " + this.toString());
			}
			// Si je ne suis pas le client par defaut, je me connecte a celui-ci (il ne va pas se connecter a lui-meme)
			else {

				if (getAnnuaire().testMaxKey(this.key)){
					getAnnuaire().setMaxKey(this.key);
				}
				System.out.println("Je suis port " + getPort() + " avec key " + this.key + " et je veux rejoindre");
				this.joinMainChord(getAnnuaire().getChordPeer());
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}

		Thread t = new Thread() {
			public void run() {
				try {
					execute();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		t.start();
	}

	private long genererKey(InetAddress adressClient, int port) {
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

		System.out.println("joinMainChord");
		System.out.println("Mon successeur : " + handleChordPeer.findkey(this.getKey()));

		ChordPeer successeur = handleChordPeer.findkey(this.getKey());

		ChordPeer predecesseur = successeur.getPredecesseur();
		System.out.println("Mon predecesseur :" + successeur.getPredecesseur());

		System.out.println();

		this.chordPeer.setSuccesseur(successeur);
		this.chordPeer.setPredecesseur(predecesseur);

		successeur.setPredecesseur(this.chordPeer);
		predecesseur.setSuccesseur(this.chordPeer);

	}

	/**
	 * Met à jour les liens de l’anneau virtuel 
	 * i,e notre predecesseur devra des lors pointer sur notre successeur
	 */
	public void leaveMainChord() {

		System.out.println("maxkey : " + getAnnuaire().getMaxKey());

		// Si je suis la max key de l'annuaire, on cherche l'autre max en dessous
		if (getAnnuaire() != null) {
			if (getAnnuaire().getMaxKey() == this.key) {
				System.out.println("Je pars alors que suis la max key");
				long maxKey = this.chordPeer.getSuccesseur().getClient().getKey();
				//System.out.println("Key de mon successeur : " + maxKey);

				ChordPeer next = this.chordPeer.getSuccesseur().getSuccesseur();
				//System.out.println("Prochain sur la liste : " + next.getClient().toString());
				while (next != this.chordPeer) {
					if (maxKey < next.getKey()) {
						//System.out.println("Nouveau max : " + next.getKey());
						maxKey = next.getKey();
					}
					next = next.getSuccesseur();
					//System.out.println("On passe au prochain : " + next);
				}
				System.out.println("Nouveau max de l'annuaire : " + maxKey);
				getAnnuaire().setMaxKey(maxKey);
			}
		}

		this.chordPeer.getPredecesseur().setSuccesseur(this.chordPeer.getSuccesseur());
		this.chordPeer.getSuccesseur().setPredecesseur(this.chordPeer.getPredecesseur());
		System.out.println("maj successeur");

		// Si je suis l'annuaire et que je pars
		if (getAnnuaire().getChordPeer().getKey() == this.key) {

			// Si je suis mon propre successeur
			if (this.chordPeer.getSuccesseur() == this.chordPeer) {
				System.out.println("Annuaire null");
				setAnnuaire(null);
			}
			else {
				System.out.println("Nouvel annuaire : client " + this.chordPeer.getSuccesseur().getClient().getPort());
				// le nouvel annuaire devient mon successeur
				getAnnuaire().setChordPeer(this.chordPeer.getSuccesseur());
				getAnnuaire().setAdresseClientConnecte(this.chordPeer.getSuccesseur().getClient().getAdr());
				getAnnuaire().setPortClientConnecte(this.chordPeer.getSuccesseur().getClient().getPort());

			}
		}

		// On demande a notre predecesseur (qui a maintenant notre successeur en successeur) de transmettre le message de notre depart
		this.chordPeer.getPredecesseur().getClient().forwardMessage("--> Client " + this.port + " s'est deconnecte");

	}


	/**
	 * Faire circuler un message vers son/ses sucesseurs
	 * On veillera a ne pas transmettre le meme message deux fois
	 * @param data message a faire circuler
	 */
	public void forwardMessage(String data) {

		System.out.println("Je suis " + toString() + " et j'envoie a " + this.chordPeer.getSuccesseur().getClient());
		System.out.println("forwardMessage de " + data);

		// Pas circuler deux fois le meme message parmi ceux deja envoyes
		/*for (byte[] ancienMessageByte : this.chordPeer.getChainesStockees()) {
			String ancienMessage = new String(ancienMessageByte, StandardCharsets.UTF_8);
			if (ancienMessage.equals(data)) {
				return;
			}
		}*/
		// Pas envoyer deux fois le meme message d'affiles
		if (! this.chordPeer.getChainesStockees().isEmpty()) {
			String dernierMessage = new String(this.chordPeer.getChainesStockees().get(this.chordPeer.getChainesStockees().size()-1), StandardCharsets.UTF_8);
			if (dernierMessage.equals(data)) {
				return;
			}
		}

		try {
			this.socket = new Socket(this.chordPeer.getSuccesseur().getClient().getAdr(), this.chordPeer.getSuccesseur().getClient().getPort());

			OutputStream os = socket.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			BufferedWriter bw = new BufferedWriter(osw);
			bw.write(data);
			bw.flush();

			// On stocke dans les chaines envoyees
			byte[] tradBytes = data.getBytes(Charset.forName("UTF-8"));
			this.chordPeer.getChainesStockees().add(tradBytes);

			this.socket.close();

		} catch (SocketTimeoutException ste) {
			System.out.println("Timeout depasse");
			this.forwardMessage(data);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (getAnnuaire() != null) {
			System.out.println("L'annuaire est : client " + getAnnuaire().getPortClientConnecte());
			System.out.println("Maxkey : " + getAnnuaire().getMaxKey());
		}

	}

	@Override
	public String toString() {
		return "Client [id=" + this.key + ", adr=" + this.adr + ", port=" + this.port + "]";
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

	/*public void gererReceptionMessage(JsonObject message) {

		if (message.getString("GET") == "CONNEXION") {
			InetAddress successeurDuNew = this.findMainChord(Long.parseLong(message.getString("KEY")));
		}

	}*/

	public void execute() throws IOException {



		while(true) {
			try {
				//System.out.println("Waiting for client on port " + 
				//		serverSocket.getLocalPort() + "...");

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

		//System.out.println("Just connected to " + socket2.getRemoteSocketAddress());
		DataInputStream in;
		try {
			in = new DataInputStream(socket2.getInputStream());
			int length = in.available();
			byte[] buf = new byte[length];
			in.readFully(buf);

			StringBuilder sb = new StringBuilder();

			for (byte b:buf) {
				char c = (char)b; 
				sb.append(c);
			}

			String messageRecu = sb.toString();

			// Evite la lecture alors que le message n'a pas fini d'etre envoye : on attend un peu et on reessaie
			if (sb.length() == 0) {
				try {
					Thread.sleep(300);
					Service_Client(socket2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			else {

				if (this.graphique_client != null) {
					this.graphique_client.ajouterMessage(messageRecu);	
				}

				// Puis on transmet au sucesseur
				this.forwardMessage(messageRecu);

				socket2.close();
			}


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

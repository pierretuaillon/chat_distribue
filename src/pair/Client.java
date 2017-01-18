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
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

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
		
		// Je suis le premier a arriver donc le client par defaut
		if (annuaire == null) { // Singleton plutot ?
			try {
				annuaire = new Annuaire(InetAddress.getLocalHost());
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// Si je ne suis pas le client par defaut, je me connecte a celui-ci (il ne va pas se connecter a lui-meme)
		else {
			InetSocketAddress destinaireAdresseFormat = new InetSocketAddress(annuaire.getAdresseClientDefaut(), annuaire.getPortClientDefaut());
			try {
				this.socket.connect((SocketAddress) destinaireAdresseFormat, 500);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		/*annuaire.ajouterClient(this);
		
		ChordPeer handle = annuaire.getChordPeerHandle();
		if (handle == null) {
			handle = this.chordPeer;
		}*/
		
		//this.joinMainChord();
		
		Thread t = new Thread() {
		    public void run() {
		    	try {
		    		System.out.println("Thread lance");
		    		
					// Avant le execute, on envoie au serveur notre demande pour s'integrer a l'anneau
			        OutputStream os = socket.getOutputStream();
			        OutputStreamWriter osw = new OutputStreamWriter(os);
			        BufferedWriter bw = new BufferedWriter(osw);
			       
			        JsonObjectBuilder comBuilder = Json.createObjectBuilder();
					comBuilder.add("GET", "CONNEXION").add("KEY", key);
					JsonObject jsonMessage = comBuilder.build();
					
			        bw.write(jsonMessage.toString());
			        bw.flush();
			        
					execute();
					
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
		
		System.out.println("forwardMessage de " + data);
		
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
		
		// forwardMessage du successeur
		
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
	            this.socket = serverSocket.accept();
	            
	            System.out.println("Just connected to " + this.socket.getRemoteSocketAddress());
	            DataInputStream in = new DataInputStream(this.socket.getInputStream());
	            
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
	               System.out.print(c);
	            }
	            
	            String messageRecu = sb.toString();
	            System.out.print("Message recu : " + messageRecu);
	            
	            // Faudrait passer par JSON dans l'envoi et reception
	            // puis gererReceptionMessage()
	            
	            System.out.println("close socket");
	            this.socket.close();
	       
	         }catch(SocketTimeoutException s) {
	            System.out.println("Socket timed out!");
	            break;
	         }catch(IOException e) {
	            e.printStackTrace();
	            break;
	         }
	      }
		
		
		
		/*
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
		*/
	}
	
}

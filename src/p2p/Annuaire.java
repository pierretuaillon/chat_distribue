package p2p;

import java.net.InetAddress;
import java.util.ArrayList;

import node.ChordPeer;
import pair.Client;

/**
 * Refere a l’adresse IP/port d’un pair toujours connecte
 *
 */
public class Annuaire {
	
	InetAddress adresseClientDefaut;
	int portClientDefaut = 12000;
	
	ChordPeer chordPeer;
	
	
	public ChordPeer getChordPeer(){
		return this.chordPeer;
	}
	
	//ArrayList<Client> listeClient = new ArrayList<Client>();
	
	public void setChordPeer(ChordPeer chordPeer) {
		this.chordPeer = chordPeer;
	}

	/*
	 * Fournir InetAddress.getLocalHost()
	 */
	public Annuaire(InetAddress localHost) {
		this.adresseClientDefaut = localHost;
	}

	public InetAddress getAdresseClientDefaut() {
		return adresseClientDefaut;
	}

	public int getPortClientDefaut() {
		return portClientDefaut;
	}

	/*public ChordPeer getChordPeerHandle() {
		if (this.listeClient.isEmpty()) {
			return null;
		}
		return this.listeClient.get(0).getChordPeer();
		
	}*/
	
	/*public void ajouterClient(Client c) {
		this.listeClient.add(c);
	}
	
	public void supprimerClient(Client c) {
		this.listeClient.remove(c);
	}*/

}

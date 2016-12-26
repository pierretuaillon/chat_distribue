package p2p;

import java.util.ArrayList;

import node.ChordPeer;
import pair.Client;

/**
 * Refere a l’adresse IP/port d’un pair toujours connecte
 *
 */
public class Annuaire {

	ArrayList<Client> listeClient = new ArrayList<Client>();
	
	public Annuaire() {
	}

	public ChordPeer getChordPeerHandle() {
		if (this.listeClient.isEmpty()) {
			return null;
		}
		return this.listeClient.get(0).getChordPeer();
	}
	
	public void ajouterClient(Client c) {
		this.listeClient.add(c);
	}
	
	public void supprimerClient(Client c) {
		this.listeClient.remove(c);
	}

}

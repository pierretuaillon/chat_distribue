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
	
	InetAddress adresseClientConnecte;
	int portClientConnecte;
	
	ChordPeer chordPeer;
	
	long MaxKey;
	
	public boolean testMaxKey(long key){
		if (MaxKey < key ){
			return true;
		}else{
			return false;
		}
	}
	
	public long getMaxKey() {
		return MaxKey;
	}

	public void setMaxKey(long key) {
		MaxKey = key;
	}

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
	public Annuaire() {
	}

	public InetAddress getAdresseClientDefaut() {
		return adresseClientConnecte;
	}

	public int getPortClientDefaut() {
		return portClientConnecte;
	}

	public InetAddress getAdresseClientConnecte() {
		return adresseClientConnecte;
	}

	public void setAdresseClientConnecte(InetAddress adresseClientConnecte) {
		this.adresseClientConnecte = adresseClientConnecte;
	}

	public int getPortClientConnecte() {
		return portClientConnecte;
	}

	public void setPortClientConnecte(int portClientConnecte) {
		this.portClientConnecte = portClientConnecte;
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

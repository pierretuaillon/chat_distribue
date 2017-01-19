package p2p;

import java.net.InetAddress;

import node.ChordPeer;

/**
 * Refere a l’adresse IP/port d’un pair toujours connecte
 *
 */
public class Annuaire {
	
	InetAddress adresseClientConnecte;
	int portClientConnecte;
	
	ChordPeer chordPeer;
	
	long MaxKey;
	
	public Annuaire() {
	}
	
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
	
	public void setChordPeer(ChordPeer chordPeer) {
		this.chordPeer = chordPeer;
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

}

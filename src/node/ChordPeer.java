package node;

import java.util.ArrayList;

import pair.Client;

public class ChordPeer {
	
	/**
	 * Chaque pair appartient a un ensemble eventuellement vide de salons de conversation
	 */
	private ArrayList<SalonConversation> salons = new ArrayList<SalonConversation>();
	private ArrayList<byte[]> chainesStockees = new ArrayList<byte[]>();
	private Client client;
	
	/**
	 * Determine la place sur l'anneau
	 * Peut etre : fonction de hachage sur l’IP du pair ou l’identifiant de l’utilisateur
	 * ou encore aleatoire
	 */
	private long key;
	/**
	 * Paire successeur dans l'anneau principal 
	 * A maintenir
	 */
	private ChordPeer successeur;
	/**
	 * Paire predecesseur dans l'anneau principal 
	 * A maintenir
	 */
	private ChordPeer predecesseur;
	
	public ChordPeer(long key) {
		this.key = key;
		this.predecesseur = null;
		this.successeur = this;
	}
	
	/**
	 * Renvoie la reference du paire responsable de key
	 * @param key
	 * @return
	 */
	public ChordPeer findkey(long key) {
		// Cas pair unique
		if (this == this.successeur && this == this.predecesseur) {
			return this;
		}
		
		// Cas intervalles circulaires
		if (this.predecesseur.getKey() > this.key) {
			
			// Si on considere qu'on va jusqu'a 100 cles, a voir
			if (this.predecesseur.getKey() <= key && key <= 100 || key >= 0 && key <= this.key) {
				return this;
			}
		}
		
		// Cas non circulaire ou je suis responsable
		if (key > this.predecesseur.getKey() && key <= this.key) {
			return this;
		}
		
		// Sinon je me refere a mon successeur
		return this.successeur.findkey(key);
	}
	
	/** 
	 * Renvoie la chaine dont la clef est key
	 * @param key
	 */
	public void getItem(long key) {
		// Utiliser la fonction findkey afin de determiner le pair responsable de key
	}
	
	/** 
	 * Publie la chaine dont la clef est key et la stocke dans le dictionnaire du pair responsable de key
	 * @param key
	 */
	public void setItem(long key) {
		
	}
	
	public long getKey() {
		return key;
	}
	
	public ArrayList<SalonConversation> getSalons() {
		return salons;
	}

	public void setSuccesseur(ChordPeer successeur) {
		this.successeur = successeur;
	}

	public void setPredecesseur(ChordPeer predecesseur) {
		this.predecesseur = predecesseur;
	}
	
	public ChordPeer getPredecesseur() {
		return this.predecesseur;
	}
	
	public ChordPeer getSuccesseur() {
		return this.successeur;
	}

	public ArrayList<byte[]> getChainesStockees() {
		return chainesStockees;
	}

	public Client getClient() {
		return client;
	}

	@Override
	public String toString() {
		return "ChordPeer [key=" + key + ", successeur=" + successeur
				+ ", predecesseur=" + predecesseur + "]";
	}

}

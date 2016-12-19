package node;

import java.util.ArrayList;

public class ChordPeer {
	
	/**
	 * Chaque pair appartient a un ensemble eventuellement vide de salons de conversation
	 */
	private ArrayList<SalonConversation> salons = new ArrayList<SalonConversation>();
	private ArrayList<byte[]> chainesStockees = new ArrayList<byte[]>();
	
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

	/**
	 * Retourne l'emplacement de key dans l’anneau
	 * @param key du pair souhaitant rejoindre l’anneau
	 * @return int
	 */
	public int dindMainChord(long key)  {
		return 0;
	}
	
	/**
	 * Permet de s'inserer par le biais d'un pair connu qui se trouve dans le reseau (handleChordPeer)
	 * Initialisation des references distantes (predecesseur et successeur)
	 */
	public void joinMainChord(ChordPeer handleChordPeer) {
		
		this.successeur = handleChordPeer.findkey(this.getKey());
		this.successeur.setPredecesseur(this);
		//TODO autres ?
		
	}
	
	/**
	 * Met à jour les liens de l’anneau virtuel 
	 * i,e notre predecesseur devra des lors pointer sur notre successeur
	 */
	public void leaveMainChord(ChordPeer ancienChordPeer) {
		
	}
	
	/**
	 * Faire circuler un message vers son/ses sucesseurs
	 * On veillera a ne pas transmettre le meme message deux fois
	 * @param data message a faire circuler
	 */
	public void forwardMessage(String data) {
		
	}
	
	public long getKey() {
		return key;
	}
	
	
	public void setSuccesseur(ChordPeer successeur) {
		this.successeur = successeur;
	}

	public void setPredecesseur(ChordPeer predecesseur) {
		this.predecesseur = predecesseur;
	}

	@Override
	public String toString() {
		return "ChordPeer [key=" + key + ", successeur=" + successeur
				+ ", predecesseur=" + predecesseur + "]";
	}

}

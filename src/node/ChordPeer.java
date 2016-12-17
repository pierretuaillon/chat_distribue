package node;

import java.util.ArrayList;

public class ChordPeer {
	
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
	}
	
	/**
	 * Renvoie la reference du paire responsable de key
	 * @param key
	 * @return
	 */
	public ChordPeer findkey(long key) {
		if (key > this.predecesseur.getKey() && key <= this.key) {
			return this;
		}
		return this.successeur.findkey(key);
		
		//TODO 
		// On fera attention au fait que les intervales sont circulaires et au cas particulier 
		// d’un pair unique (cas où:math:succ=pred=this).
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
	 * Permet a un autre pair de s'inserer a sa place
	 * Initialisation des references distantes (predecesseur et successeur)
	 */
	public void joinMainChord(ChordPeer nouveauChordPeer) {
		
		/*Ainsi, si son identifiant est myID il devra s’inserer juste avant le pair S=handle.findkey(myID)
		, on devra alors mettre à jour les objets S,S.pred
		 ainsi que les informations du nouveau pair N
		.*/
		//TODO
		this.successeur = nouveauChordPeer.findkey(this.getKey());
		
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

	@Override
	public String toString() {
		return "ChordPeer [key=" + key + ", successeur=" + successeur
				+ ", predecesseur=" + predecesseur + "]";
	}

	public long getKey() {
		return key;
	}

	
}

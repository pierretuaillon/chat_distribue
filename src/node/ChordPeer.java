package node;

public class ChordPeer {

	/**
	 * Determine la place sur l'anneau
	 * Peut etre : fonction de hachage sur l’IP du pair ou l’identifiant de l’utilisateur
	 * ou encore aléatoire
	 */
	private int key;
	/**
	 * Paire successeur dans l'anneau principal 
	 * A maintenir
	 */
	//TODO ou une ArrayList ?
	private ChordPeer successeur;
	/**
	 * Paire predecesseur dans l'anneau principal 
	 * A maintenir
	 */
	//TODO ou une ArrayList ?
	private ChordPeer predecesseur;
	
	/**
	 * Retourne l'emplacement de key dans l’anneau
	 * @param key du pair souhaitant rejoindre l’anneau
	 * @return int
	 */
	public int dindMainChord(long key)  {
		return 0;
	}
	
	/**
	 * Permet au paire courant de s'inserer
	 * Initialisation des references distantes(predecesseur et successeur)
	 */
	public void joinMainChord() {
		
	}
	
	/**
	 * Met à jour les liens de l’anneau virtuel 
	 * i,e notre predecesseur devra des lors pointer sur notre successeur
	 */
	public void leaveMainChord() {
		
	}
	
	/**
	 * Faire circuler un message vers son/ses sucesseurs
	 * On veillera a ne pas transmettre le meme message deux fois
	 * @param data message a faire circuler
	 */
	public void forwardMessage(String data) {
		
	}
	
}

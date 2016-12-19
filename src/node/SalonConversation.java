package node;

import java.util.ArrayList;

public class SalonConversation { // Ou GestionnaireSalons pour ces methodes, a voir
	
	/**
	 * Chaque pair appartient a un ensemble eventuellement vide de salons de conversation
	 * Autre sens de la fleche
	 */
	private ArrayList<ChordPeer> pairsPossedes = new ArrayList<ChordPeer>();
	
	/** 
	 * Renvoie la liste des identifiants des salles de chat
	 * @return ArrayList<Long>
	 */
	public ArrayList<Long> getChatRoomsList()  {
		return null;
	}

	/**
	 * Rejoint la salle si elle existe, la cree sinon
	 * @param chatkey
	 */
	public void joinChatRoom(long chatkey) {
		
	}
	
	/**
	 * Envoie un message dans le salon chatkey
	 * @param s message
	 * @param chatkey
	 */
	public void sendToChatRoom(String s, long chatkey) {
	
	}
	
	/**
	 * Reference a un tampon stockant les messages venant du salon
	 */
	public void readChatRoom(long chatkey) {
		
	}
	
}

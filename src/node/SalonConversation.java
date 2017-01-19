package node;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

import pair.Client;

public class SalonConversation { // Ou GestionnaireSalons pour ces methodes, a voir

	long KeySalon;
	String nameSalon;
	ArrayList<byte[]> tampon;
	
	
	public String getNom(){
		return this.nameSalon;
	}
	
	
	public SalonConversation(){
		this.nameSalon = "Default";
		this.KeySalon = genererKey(nameSalon);
	}
	
	public SalonConversation(String nameSalon){
		this.nameSalon = nameSalon;
		this.KeySalon = genererKey(nameSalon);
	}
	
	public SalonConversation(long chatkey) {
		// TODO Auto-generated constructor stub
	}

	private long genererKey(String nameSalon) {
		return ((nameSalon.hashCode()) *-1);
	}
	
	/**
	 * Chaque pair appartient a un ensemble eventuellement vide de salons de conversation
	 * Autre sens de la fleche
	 */
	private ArrayList<ChordPeer> pairsPossedes = new ArrayList<ChordPeer>();

	/** 
	 * Renvoie la liste des identifiants des salles de chat
	 * @return ArrayList<Long>
	 */
	public static HashMap<Long, SalonConversation> getChatRoomsList()  {
		HashMap<Long, SalonConversation> salons = new HashMap<Long, SalonConversation>();
		ChordPeer next = Client.getAnnuaire().getChordPeer();

		while  (next != Client.getAnnuaire().getChordPeer()){
			salons.putIfAbsent(next.getClient().getSalon().getKey(), next.getClient().getSalon());
			next = next.getSuccesseur();
		}
		return salons;
	}

	private Long getKey() {
		return this.KeySalon;
	}

	/**
	 * Rejoint la salle si elle existe, la cree sinon
	 * @param chatkey
	 */
	public SalonConversation joinChatRoom(long chatkey) {
		HashMap<Long, SalonConversation> salons = SalonConversation.getChatRoomsList();
		
			if (salons.get(chatkey) != null){
				return salons.get(chatkey);
			}else{
				return null;
			}
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

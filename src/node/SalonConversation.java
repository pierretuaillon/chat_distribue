package node;

import java.util.ArrayList;
import java.util.HashMap;

import pair.Client;

public class SalonConversation { // Ou GestionnaireSalons pour ces methodes, a voir

	long KeySalon;
	String nameSalon;
	ArrayList<String> tampon = new ArrayList<String>();


	public String getNom(){
		return this.nameSalon;
	}

	public ArrayList<String> getTampon(){
		return this.tampon;
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

	public static long genererKey(String nameSalon) {
		return ((nameSalon.hashCode()) *-1);
	}

	/** 
	 * Renvoie la liste des identifiants des salles de chat
	 * @return ArrayList<Long>
	 */
	public static HashMap<Long, SalonConversation> getChatRoomsList()  {
		HashMap<Long, SalonConversation> salons = new HashMap<Long, SalonConversation>();
		ChordPeer next = Client.getAnnuaire().getChordPeer();
		
		if (next.getClient().getSalon() != null) {
			salons.putIfAbsent(next.getClient().getSalon().getKey(), next.getClient().getSalon());
			next = next.getSuccesseur();

			while  (next != Client.getAnnuaire().getChordPeer()){
				if (next.getClient().getSalon() != null) {
					salons.putIfAbsent(next.getClient().getSalon().getKey(), next.getClient().getSalon());
				}
				next = next.getSuccesseur();
			}
		}
		
		return salons;
	}

	public Long getKey() {
		return this.KeySalon;
	}

	/**
	 * Rejoint la salle si elle existe, la cree sinon
	 * @param chatkey
	 */
	public static SalonConversation joinChatRoom(long chatkey) {
		System.out.println("joinChatRoom : " + chatkey);
		HashMap<Long, SalonConversation> salons = SalonConversation.getChatRoomsList();
		System.out.println("salons taille : " + salons.size());

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
		HashMap<Long, SalonConversation> salons = SalonConversation.getChatRoomsList();

		if (salons.get(chatkey) != null) {
			salons.get(chatkey).getTampon().add(s);
		}

	}

	/**
	 * Reference a un tampon stockant les messages venant du salon
	 */
	public static ArrayList<String> readChatRoom(long chatkey) {
		HashMap<Long, SalonConversation> salons = SalonConversation.getChatRoomsList();

		if (salons.get(chatkey) != null){
			return salons.get(chatkey).getTampon();
		}else{
			return null;
		}
	}
}

package client_graphique;

import java.util.HashMap;

import node.SalonConversation;

public class VerificationListeSalons extends Thread {

	ComboBoxSalon cbs;
	
	public VerificationListeSalons(ComboBoxSalon cbs){
		this.cbs = cbs;
	}
	
	public void run(){
		while (true) {
			
			HashMap<Long, SalonConversation> salons = SalonConversation.getChatRoomsList();
			
			cbs.MiseAJourSalons(salons);
			
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}

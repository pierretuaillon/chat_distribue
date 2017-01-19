package client_graphique;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import node.SalonConversation;
import pair.Client;

public class ComboBoxSalon extends JPanel implements ActionListener{
	
	Client client;
	JComboBox<?> listeSalon;
	
	HashMap<Long, SalonConversation> salons;
	
	
	public ComboBoxSalon(Client client) {
		//Client.getAnnuaire().getChordPeer().getClient().getSalon();
		HashMap<Long, SalonConversation> salons = SalonConversation.getChatRoomsList();
		this.salons = salons;
		this.client = client;
		String [] tabNomsSalons = new String[salons.size()];
		
		int i =0;
		
		for (Long key : salons.keySet()) {
			tabNomsSalons[i] = salons.get(key).getNom();
			i++;
		}
		
		if (salons.isEmpty()){
			System.out.println("Client comboxSalon "+ this.client);
			this.client.setSalon(new SalonConversation());
		}else{
			this.client.setSalon(SalonConversation.joinChatRoom(SalonConversation.genererKey("Default")));
		}
		
		ArrayList<String> tampon = SalonConversation.readChatRoom(SalonConversation.genererKey("Default"));
		if (tampon != null){
			for (String string : tampon) {
	    		this.client.getGraphique_client().ajouterMessage(string);
			}
		}
		
		this.listeSalon = new JComboBox<String>(tabNomsSalons);
		this.listeSalon.setEditable(true);
		this.listeSalon.addActionListener(this);
		this.client = client;
		
	}

	public void actionPerformed(ActionEvent e) {
		int index = listeSalon.getSelectedIndex();
        if(index >= 0) {
        	SalonConversation sc = SalonConversation.joinChatRoom( SalonConversation.genererKey(listeSalon.getItemAt(index).toString()));
        	client.setSalon(sc);
        	ArrayList<String> tampon = SalonConversation.readChatRoom(SalonConversation.genererKey(listeSalon.getItemAt(index).toString()));
        	
        	for (String string : tampon) {
        		this.client.getGraphique_client().ajouterMessage(string);
			}
        	
        }else if("comboBoxEdited".equals(e.getActionCommand())) {
        	//System.out.println("....comboBoxEdited....");
        	String newSalon = listeSalon.getSelectedItem().toString();
        	//System.out.println("new Salon : " + newSalon);
        	SalonConversation sc = SalonConversation.joinChatRoom( SalonConversation.genererKey(newSalon));
        	
        	if (sc == null){
        		sc = new SalonConversation(newSalon);
        	}else{
        		ArrayList<String> tampon = SalonConversation.readChatRoom(SalonConversation.genererKey(listeSalon.getItemAt(index).toString()));
            	for (String string : tampon) {
            		this.client.getGraphique_client().ajouterMessage(string);
    			}
        	}
        	client.setSalon(sc);
        }
    }

	
	public void MiseAJourSalons(HashMap<Long, SalonConversation> salons){
		
		this.salons = salons;
		
		String [] tabNomsSalons = new String[salons.size()];
		
		int i =0;
		
		for (Long key : salons.keySet()) {
			tabNomsSalons[i] = salons.get(key).getNom();
			i++;
		}
		
		this.listeSalon = new JComboBox<String>(tabNomsSalons);
		this.listeSalon.setEditable(true);
		this.listeSalon.addActionListener(this);
	}
	
	
	public JComboBox<?> getListeSalon() {
		return listeSalon;
	}
		
}

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
		Client.getAnnuaire().getChordPeer().getClient().getSalon();
		HashMap<Long, SalonConversation> salons = SalonConversation.getChatRoomsList();
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
		this.client = client;
	}

	public void actionPerformed(ActionEvent e) {
		int index = listeSalon.getSelectedIndex();
        if(index >= 0) {
        	for (Long Key : this.salons.keySet()) {
				if (this.salons.get(Key).getNom() == listeSalon.getItemAt(index).toString()){
					client.setSalon(this.salons.get(Key));
				}
			}
        }else if("comboBoxEdited".equals(e.getActionCommand())) {
        	
        }
		
		/*
        JComboBox<?> cb = (JComboBox<?>)e.getSource();
        String SalonName = (String)cb.getSelectedItem();
    	*/
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

package client_graphique;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;

public class ComboBoxSalon extends JPanel implements ActionListener{
	
	JComboBox<?> listeSalon;
	
	public ComboBoxSalon(String[] listeSalons){
		this.listeSalon = new JComboBox(listeSalons);
		this.listeSalon.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
        JComboBox<?> cb = (JComboBox<?>)e.getSource();
        String SalonName = (String)cb.getSelectedItem();
    }

	public JComboBox<?> getListeSalon() {
		return listeSalon;
	}
		
}

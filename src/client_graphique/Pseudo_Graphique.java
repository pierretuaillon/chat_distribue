package client_graphique;

import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Pseudo_Graphique extends JPanel{

	JTextArea textArea_Pseudo;
	
	public Pseudo_Graphique(String pseudo) {
		textArea_Pseudo = new JTextArea(5, 20);
        textArea_Pseudo.setEditable(false);
        textArea_Pseudo.append("Votre pseudo : "+ pseudo );
        add(textArea_Pseudo);
	}

	public JTextArea getTextArea_Pseudo() {
		return textArea_Pseudo;
	}

}

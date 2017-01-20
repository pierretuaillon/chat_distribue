package client_graphique;

import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Description_Projet extends JPanel{

	private static final long serialVersionUID = -7152912309378886674L;
	JTextArea Descritpion;
	
	public Description_Projet(){
		Descritpion = new JTextArea(5, 20);
		Descritpion.setEditable(false);
		Descritpion.append("Projet r�alis� par \n     Tuaillon Pierre, \n     Pierre Laeticia, \n     Milanesio Alexandre, \n     Magharbi Nadia");
        add(Descritpion);
	}

	public JTextArea getDescritpion() {
		return Descritpion;
	}
	
	
	
}

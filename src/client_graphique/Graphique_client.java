package client_graphique;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import pair.Client;

public class Graphique_client extends JPanel implements ActionListener {

	JPanel panel;
    JTextField textField;
    JTextArea textArea;
    JTextArea textArea_Pseudo;
    Client client;
    
    final static String newline = "\n";

    public JTextField getTextField(){
    	return this.textField;
    }
    
    public Graphique_client(Client client) {
        super(new GridBagLayout());
        
        this.client = client;

        textField = new JTextField(70);
        textField.addActionListener(this);

        textArea = new JTextArea(20, 70);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

                
        //Ajout des composants au panel.
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(scrollPane, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        add(textField, c);
    }
    
    

    public void actionPerformed(ActionEvent evt) {
        String text = textField.getText();
        textArea.append(text + newline);
        client.forwardMessage(text);
        //textField.selectAll();
        textField.setText("");
        //Make sure the new text is visible, even if there
        //was a selection in the text area.
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI(Client client) {
        //Creation de la Frame
        JFrame frame = new JFrame("Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        
        //Ajout des composant � la fenetre
        frame.add(new Graphique_client(client), BorderLayout.LINE_START);
        Pseudo_Graphique pseudo = new Pseudo_Graphique("Test");
        JPanel pan = new JPanel();
        pan.setLayout(new BorderLayout());
        pan.add(pseudo.getTextArea_Pseudo(), BorderLayout.NORTH);  
        
        String [] listeSalons = {"salon 1", "salon 2", "salon 3", "salon 4", "salon 5"};
        ComboBoxSalon cbs = new ComboBoxSalon(listeSalons);
        pan.add(cbs.getListeSalon(), BorderLayout.CENTER);
        
        Description_Projet dp = new Description_Projet();
        pan.add(dp.getDescritpion(), BorderLayout.SOUTH);
        
        frame.add(pan , BorderLayout.CENTER);
        
      
        //affiche la fenetre
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
    	
		final Client client;
		final Client client2;
		final Client client3;
		final Client client4;
		try {
			// Client par defaut de l'annuaire
			client = new Client(InetAddress.getLocalHost(), 12000);
			client2 = new Client(InetAddress.getLocalHost(), 13000);
			client3 = new Client(InetAddress.getLocalHost(), 14000);
			client4 = new Client(InetAddress.getLocalHost(), 17000);

			System.out.println("client : " + client);
			System.out.println("client2 : " + client2);
			//Planifier une tache pour le thread
	        //creer et affiche le GUI
	        javax.swing.SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	                //createAndShowGUI(client);
	                //createAndShowGUI(client2);
	                //createAndShowGUI(client3);
	            }
	        });
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
      
    }
}
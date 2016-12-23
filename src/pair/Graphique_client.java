package pair;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Graphique_client extends JPanel implements ActionListener {

	JPanel panel;
    JTextField textField;
    JTextArea textArea;
    JTextArea textArea_Pseudo;
    
    final static String newline = "\n";

    
    public Graphique_client() {
        super(new GridBagLayout());

        textField = new JTextField(70);
        textField.addActionListener(this);

        textArea = new JTextArea(20, 70);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        textArea_Pseudo = new JTextArea(5, 70);
        textArea_Pseudo.setEditable(false);
        textArea_Pseudo.append("Votre pseudo : Truc" );
        
        //Ajout des composant au panel.
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
    private static void createAndShowGUI() {
        //Création de la Frame
        JFrame frame = new JFrame("Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Ajout des composant à la fenetre
        frame.add(new Graphique_client());
               
        //affiche la fenetre
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Planifier une tache pour le thread
        //creer et affiche le GUI
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
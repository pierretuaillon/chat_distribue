package client_graphique;

import java.io.IOException;
import java.net.InetAddress;

import node.ChordPeer;
import p2p.Annuaire;
import pair.Client;

public class VerificationBadDeco extends Thread {

	public VerificationBadDeco() {
	}

	public void run() {

		while (true) {

			Annuaire annuaire = Client.getAnnuaire();

			if (annuaire != null) {

				ChordPeer reference = annuaire.getChordPeer();
				ChordPeer successeur = annuaire.getChordPeer().getSuccesseur();

				while (successeur != reference) {

					InetAddress inet = successeur.getClient().getAdr();
					int port = successeur.getClient().getPort();

					// Ping aupres de notre successeur
					try {
						// Si deconnecte
						if (! inet.isReachable(100)) {
							
							// On cherche le prochain successeur viable
							// Et on remplace notre successeur par celui-ci
							ChordPeer successeurCo = prochainSuccesseurConnecte(reference);
							reference.setSuccesseur(successeurCo);
							// Je deviens sont predecesseur
							successeurCo.setPredecesseur(reference);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}

					reference = reference.getSuccesseur();
				}

			}
			try {
				sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	public ChordPeer prochainSuccesseurConnecte(ChordPeer reference) {

		ChordPeer successeur = Client.getAnnuaire().getChordPeer().getSuccesseur();

		while (successeur != reference) {

			InetAddress inet = successeur.getClient().getAdr();
			int port = successeur.getClient().getPort();

			// Ping aupres de notre successeur
			try {
				if (inet.isReachable(port)) {
					return successeur;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			successeur = successeur.getSuccesseur();
		}

		// Si pas trouve, lui-meme
		return reference;
	}

}

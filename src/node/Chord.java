package node;

import java.util.ArrayList;

public class Chord {
	
	ArrayList<ChordPeer> listChordPeer = new ArrayList<ChordPeer>();
	
	public void creerChordPeer(long key) {
		ChordPeer nouveauChordPeer = new ChordPeer(key);
		this.listChordPeer.add(nouveauChordPeer);
	}
	
	public ChordPeer getChordPeer(int i) {
		return listChordPeer.get(i);
	}

	/**
	 * Demande a un de ses pairs de trouver la clef
	 * @param key
	 * @return
	 */
	public ChordPeer peerFindKey(long key) {
		if (this.listChordPeer.size() > 1) {
			return this.listChordPeer.get(0).findkey(key);
		}
		return null;
	}

	@Override
	public String toString() {
		return "Chord [listChordPeer=" + listChordPeer + "]";
	}
	
}

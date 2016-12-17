package node;

import java.util.ArrayList;

public class Chord {
	
	ArrayList<ChordPeer> listChordPeer = new ArrayList<ChordPeer>();
	
	public void creerChordPeer(String key) {
		ChordPeer nouveauChordPeer = new ChordPeer(key);
		this.listChordPeer.add(nouveauChordPeer);
	}
	
	public ChordPeer getChordPeer(int i) {
		return listChordPeer.get(i);
	}

}

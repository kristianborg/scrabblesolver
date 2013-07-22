package nl.krisborg.gwt.scrabblesolver.client.ui.interfaces;

public interface AddWordListener {
	
	public void addTempWord(int x, int y, String word);
	
	public void addWord(int x, int y, String word);
	
	public void clearTempWord();
}

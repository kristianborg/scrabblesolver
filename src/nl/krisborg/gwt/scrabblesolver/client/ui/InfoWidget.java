package nl.krisborg.gwt.scrabblesolver.client.ui;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class InfoWidget extends VerticalPanel {
	
	private Label wordListSize = new Label();
	private Label status = new Label();
	
	public InfoWidget(){
		add(wordListSize);
		add(status);
	}
	
	public void setWordListSize(int size){
		wordListSize.setText("Wordlist size: " + size);
	}
	
	public void setStatus(String text){
		status.setText(text);
	}

}

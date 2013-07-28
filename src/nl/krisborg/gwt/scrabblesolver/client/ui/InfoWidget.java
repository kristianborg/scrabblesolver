package nl.krisborg.gwt.scrabblesolver.client.ui;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class InfoWidget extends VerticalPanel {

	private Label status = new Label();
	
	public InfoWidget(){
		add(status);
	}
	
	public void setStatus(String text){
		status.setText(text);
	}

}

package nl.krisborg.gwt.scrabblesolver.client.ui;

import nl.krisborg.gwt.scrabblesolver.client.TyleType;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;

public class TileWidget extends SimplePanel {

	public TileWidget(Character character, TyleType tyleType){
		super();
		//setStyleName(tyleType.getType());
		addStyleName("tile");
		addStyleName(tyleType.getType());
		Label label = new Label(character.toString().toUpperCase());
		add(label);
	}
}
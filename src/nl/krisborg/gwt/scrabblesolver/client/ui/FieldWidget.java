package nl.krisborg.gwt.scrabblesolver.client.ui;

import nl.krisborg.gwt.scrabblesolver.client.grammar.ScoreMultiplier;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;

public class FieldWidget extends SimplePanel {
	
	public FieldWidget(ScoreMultiplier scoreMultiplier){
		super();
		addStyleName("field");
		if (scoreMultiplier != ScoreMultiplier.START){
			addStyleName(scoreMultiplier.toString() + "Field");
			Label label = new Label(scoreMultiplier.toString());
			add(label);
		}
	}
}

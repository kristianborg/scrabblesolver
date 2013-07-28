package nl.krisborg.gwt.scrabblesolver.client.ui;

import nl.krisborg.gwt.scrabblesolver.shared.Solution;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;

public class SolutionWidget extends SimplePanel {

	public SolutionWidget(final SolutionsWidget solutionsWidget, final Solution solution){
		addStyleName("solution");
		add(new Label(solution.toString()));
		
		sinkEvents(Event.ONCLICK);
		addHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				solutionsWidget.activateSolution(solution);
			}

		}, ClickEvent.getType());
	}
}

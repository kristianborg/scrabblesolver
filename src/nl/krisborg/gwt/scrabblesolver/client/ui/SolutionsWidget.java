package nl.krisborg.gwt.scrabblesolver.client.ui;

import nl.krisborg.gwt.scrabblesolver.client.ui.interfaces.SolutionListener;
import nl.krisborg.gwt.scrabblesolver.shared.Solution;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SolutionsWidget extends VerticalPanel {
	
	private Solution[] solutions;
	
	private SolutionListener solutionListener;

	public SolutionsWidget(SolutionListener solutionListener) {
		this.solutionListener = solutionListener;
		redraw();
	}
	
	public void setSolutions(Solution[] solutions){
		this.solutions = solutions;
		redraw();
	}
	
	public void reset(){
		solutions = null;
		redraw();
	}
	
	private void redraw(){
		clear();
		add(new Label("Solutions"));
		if (solutions != null){
			for(int i = 0; i < solutions.length; i++){
				add(new SolutionWidget(this, solutions[i]));
			}
		}
	}

	public void activateSolution(Solution solution) {
		solutionListener.undoTempSolution();
		solutionListener.addTempSolution(solution);
		
	}
}

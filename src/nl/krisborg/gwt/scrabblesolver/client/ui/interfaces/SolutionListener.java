package nl.krisborg.gwt.scrabblesolver.client.ui.interfaces;

import nl.krisborg.gwt.scrabblesolver.shared.Solution;

public interface SolutionListener {

	public void addTempSolution(Solution solution);
	
	public void acceptSolution();
	
	public void undoTempSolution();
}

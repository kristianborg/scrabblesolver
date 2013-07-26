package nl.krisborg.gwt.scrabblesolver.server;

import java.util.List;

import nl.krisborg.gwt.scrabblesolver.client.ScrabbleSolver;
import nl.krisborg.gwt.scrabblesolver.client.grammar.Board;
import nl.krisborg.gwt.scrabblesolver.client.grammar.Solution;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ScrabbleSolverImpl extends RemoteServiceServlet implements ScrabbleSolver {

	@Override
	public List<Solution> findSolutions(Board board) {
		// TODO Auto-generated method stub
		return null;
	}

}

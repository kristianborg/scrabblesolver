package nl.krisborg.gwt.scrabblesolver.client;

import java.util.List;

import nl.krisborg.gwt.scrabblesolver.client.grammar.Board;
import nl.krisborg.gwt.scrabblesolver.client.grammar.Solution;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ScrabbleSolverAsync {

	void findSolutions(Board board, AsyncCallback<List<Solution>> callback);

	
}

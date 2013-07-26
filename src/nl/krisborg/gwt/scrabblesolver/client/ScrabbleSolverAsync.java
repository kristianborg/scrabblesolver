package nl.krisborg.gwt.scrabblesolver.client;

import nl.krisborg.gwt.scrabblesolver.client.grammar.Solution;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ScrabbleSolverAsync {

	void getSolutions(Character[] boardTiles, Character[] handTiles,
			AsyncCallback<Solution[]> callback);

}

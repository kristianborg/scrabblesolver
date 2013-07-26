package nl.krisborg.gwt.scrabblesolver.client;

import java.util.List;

import nl.krisborg.gwt.scrabblesolver.client.grammar.Board;
import nl.krisborg.gwt.scrabblesolver.client.grammar.Solution;

import com.google.gwt.user.client.rpc.RemoteService;

public interface ScrabbleSolver extends RemoteService {
	
	List<Solution> findSolutions(Board board);

}

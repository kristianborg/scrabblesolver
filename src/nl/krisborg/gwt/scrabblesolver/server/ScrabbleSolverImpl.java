package nl.krisborg.gwt.scrabblesolver.server;

import java.util.Arrays;
import java.util.List;

import nl.krisborg.gwt.scrabblesolver.client.ScrabbleSolver;
import nl.krisborg.gwt.scrabblesolver.client.grammar.Solution;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ScrabbleSolverImpl extends RemoteServiceServlet implements ScrabbleSolver {
	
	WordList wordList = new WordList(Arrays.asList("wordlist.txt"));

	@Override
	public Solution[] getSolutions(Character[] boardTiles, Character[] handTiles){
		Board board = new Board(wordList, boardTiles);
		List<Solution> list = board.findSolutions(Arrays.asList(handTiles));
		Object[] objectArray = list.toArray();
		Solution[] result = new Solution[objectArray.length];
		for (int i = 0; i < objectArray.length; i++){
			result[i] = (Solution)objectArray[i];
		}
		return result;
	}

}

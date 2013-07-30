package nl.krisborg.gwt.scrabblesolver.server;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import nl.krisborg.gwt.scrabblesolver.client.ScrabbleSolver;
import nl.krisborg.gwt.scrabblesolver.shared.Solution;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ScrabbleSolverImpl extends RemoteServiceServlet implements
		ScrabbleSolver {

	private static final long serialVersionUID = 6259470137519300210L;
	private WordList wordList = new WordList();

	@Override
	public Solution[] getSolutions(Character[] boardTiles, Character[] handTiles) {
		Board board = new Board(wordList, boardTiles);
		List<Solution> list = board.findSolutions(Arrays.asList(handTiles));
		Collections.sort(list);
		Collections.reverse(list);
		Object[] objectArray = list.toArray();
		Solution[] result = new Solution[objectArray.length];
		for (int i = 0; i < objectArray.length; i++) {
			result[i] = (Solution) objectArray[i];
		}
		return result;
	}

}

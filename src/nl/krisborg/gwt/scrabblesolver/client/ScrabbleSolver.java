package nl.krisborg.gwt.scrabblesolver.client;

import nl.krisborg.gwt.scrabblesolver.shared.Solution;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("solver")
public interface ScrabbleSolver extends RemoteService {
	
	public Solution[] getSolutions(Character[] boardTiles, Character[] handTiles);

}

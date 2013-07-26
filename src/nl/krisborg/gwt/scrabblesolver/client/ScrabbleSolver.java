package nl.krisborg.gwt.scrabblesolver.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("solver")
public interface ScrabbleSolver extends RemoteService {
	
	public int doStuff(int bla);

}

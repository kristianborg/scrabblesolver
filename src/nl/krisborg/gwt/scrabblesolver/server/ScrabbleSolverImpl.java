package nl.krisborg.gwt.scrabblesolver.server;

import nl.krisborg.gwt.scrabblesolver.client.ScrabbleSolver;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ScrabbleSolverImpl extends RemoteServiceServlet implements ScrabbleSolver {

	@Override
	public int doStuff(int bla){
		return bla - 2;
	}

}

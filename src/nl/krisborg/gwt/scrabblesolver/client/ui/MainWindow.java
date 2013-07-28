package nl.krisborg.gwt.scrabblesolver.client.ui;

import nl.krisborg.gwt.scrabblesolver.client.ScrabbleSolver;
import nl.krisborg.gwt.scrabblesolver.client.ScrabbleSolverAsync;
import nl.krisborg.gwt.scrabblesolver.client.ui.interfaces.KeyBoardInterceptor;
import nl.krisborg.gwt.scrabblesolver.client.ui.interfaces.KeyBoardListener;
import nl.krisborg.gwt.scrabblesolver.shared.Solution;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MainWindow implements EntryPoint, KeyBoardInterceptor {

	private InfoWidget infoWidget = new InfoWidget();
	private TilesWindget tilesWidget;
	private BoardWidget boardWidget;
	private KeyBoardListener activeKeyboardListener;
	

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		RootPanel.get("solveButtonContainer").add(getSolveButton());

		RootPanel.get("infoTextContainer").add(infoWidget);

		boardWidget = new BoardWidget(this);
		RootPanel.get("boardContainer").add(boardWidget);

		tilesWidget = new TilesWindget(this);
		RootPanel.get("boardContainer").add(tilesWidget);
		
		RootPanel.get().addDomHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(activeKeyboardListener != null){
					activeKeyboardListener.onKeyPress(event);
				}
			}
		}, KeyPressEvent.getType());
	}

	private Button getSolveButton() {
		Button button = new Button("Solve");
		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ScrabbleSolverAsync scrabblesolver = GWT
						.create(ScrabbleSolver.class);
				AsyncCallback<Solution[]> callback = new AsyncCallback<Solution[]>() {
					public void onFailure(Throwable caught) {
						// TODO: Do something with errors.
					}

					public void onSuccess(Solution[] result) {
						findSolution(result);
					}
				};
				scrabblesolver.getSolutions(boardWidget.getBoardTiles(), tilesWidget.getTiles(), callback);
			}
		});
		return button;
	}

	private void findSolution(Solution[] solutions) {
		Solution best = null;
		for (Solution solution : solutions) {
			if (best == null || solution.getPoints() > best.getPoints()) {
				best = solution;
			}
		}
		if (best == null) {
			infoWidget.setStatus("No solution found for current tiles");
		} else {
			infoWidget.setStatus("Best solution: " + best.toString());
			updateBoard(best);
		}
	}

	private void updateBoard(Solution solution) {
		boardWidget.addSolution(solution);
	}

	@Override
	public void setAvtiveListener(KeyBoardListener listener) {
		activeKeyboardListener = listener;
	}
}

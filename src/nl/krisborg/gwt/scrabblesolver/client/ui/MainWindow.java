package nl.krisborg.gwt.scrabblesolver.client.ui;

import java.util.List;

import nl.krisborg.gwt.scrabblesolver.client.WordList;
import nl.krisborg.gwt.scrabblesolver.client.grammar.Board;
import nl.krisborg.gwt.scrabblesolver.client.grammar.Solution;
import nl.krisborg.gwt.scrabblesolver.client.grammar.TileStack;
import nl.krisborg.gwt.scrabblesolver.client.ui.interfaces.BoardListener;
import nl.krisborg.gwt.scrabblesolver.client.ui.interfaces.WordListListener;
import nl.krisborg.gwt.scrabblesolver.client.utils.BoardFactory;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MainWindow implements EntryPoint {
	
	private WordList wordList;
	private InfoWidget infoWidget = new InfoWidget();
	private Board board;
	private TilesWindget tilesWidget;
	private BoardListener boardListener;
	private TileStack ts = new TileStack();
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		
		wordList = new WordList();
		
		board = new Board(wordList);
		board.setBoard(BoardFactory.createBoard());
		
		RootPanel.get("solveButtonContainer").add(getSolveButton());
		
		RootPanel.get("infoTextContainer").add(infoWidget);
		
		BoardWidget boardWidget = new BoardWidget(15);
		boardListener = boardWidget;
		boardListener.registerNewBoard(board);
		RootPanel.get("boardContainer").add(boardWidget);
		
		tilesWidget = new TilesWindget();
		RootPanel.get("boardContainer").add(tilesWidget);
	}
	
	private Button getSolveButton(){
		Button button = new Button("Solve");
		button.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				if (!wordList.isInitialized()){
					wordList.init(infoWidget);
					infoWidget.setStatus("Downloading word-list. Please wait...");
					wordList.registerListener(new WordListListener(){

						@Override
						public void wordListLoaded() {;
							infoWidget.setWordListSize(wordList.getNumWords());
							infoWidget.setStatus("Looking for solution. Please wait...");
							findSolution();
						}
						
					});
				} else {
					findSolution();
				}
			}
		});
		return button;
	}
	
	private void findSolution(){
		List<Solution> solutions = board.findSolutions(tilesWidget.getTiles());
		Solution best = null;
		for (Solution solution : solutions){
			if (best == null || solution.getPoints() > best.getPoints()){
				best = solution;
			}
		}
		if (best == null){
			infoWidget.setStatus("No solution found for current tiles");
		} else {
			infoWidget.setStatus("Best solution: " + best.toString());
			updateBoard(best);
		}
	}
	
	private void updateBoard(Solution solution){
		board.addSolution(solution);
		boardListener.registerNewBoard(board);
	}
}

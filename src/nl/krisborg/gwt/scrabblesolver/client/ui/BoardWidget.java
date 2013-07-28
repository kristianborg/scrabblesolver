package nl.krisborg.gwt.scrabblesolver.client.ui;

import nl.krisborg.gwt.scrabblesolver.client.TyleType;
import nl.krisborg.gwt.scrabblesolver.client.grammar.Board;
import nl.krisborg.gwt.scrabblesolver.client.ui.interfaces.AddWordListener;
import nl.krisborg.gwt.scrabblesolver.client.ui.interfaces.KeyBoardInterceptor;
import nl.krisborg.gwt.scrabblesolver.client.ui.interfaces.KeyBoardListener;
import nl.krisborg.gwt.scrabblesolver.shared.Field;
import nl.krisborg.gwt.scrabblesolver.shared.ScoreMultiplier;
import nl.krisborg.gwt.scrabblesolver.shared.Solution;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class BoardWidget extends AbsolutePanel implements 
		AddWordListener, KeyBoardListener {
	
	private static final int TILE_OFFSET = 1;
	private static final int TILE_FIELD_SIZE = 40;
	private static final int BOARD_OFFSET = 1;
	private static final int BOARD_SIZE = 15;

	private Board board;
	private Board tempBoard;

	private boolean editMode = false;
	private int xEditPosition = 0;
	private int yEditPosition = 0;
	
	private SimplePanel focusField = new SimplePanel(){
		{
			addStyleName("focusField");
		}
	};

	public BoardWidget(final KeyBoardInterceptor interceptor) {
		board = new Board();
		tempBoard = board.clone();
		setStyleName("scrabbleBoard");
		setSize("602px", "602px");

		sinkEvents(Event.ONCLICK);
		addHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				xEditPosition = (event.getRelativeX(getElement()) - BOARD_OFFSET)
						/ TILE_FIELD_SIZE;
				yEditPosition = BOARD_SIZE - 1
						- (event.getRelativeY(getElement()) - BOARD_OFFSET)
						/ TILE_FIELD_SIZE;
				editMode = true;
				interceptor.setAvtiveListener(BoardWidget.this);
				updateFocusField();
			}

		}, ClickEvent.getType());
		drawBoard();
	}
	
	private void updateFocusField(){
		focusField.setVisible(true);
		remove(focusField);
		addWidget(xEditPosition, yEditPosition, focusField);
	}
	
	private void addCharacter(char c){
		addTile(xEditPosition, yEditPosition, c, TyleType.ACTIVE);
		tempBoard.addCharacter(c, xEditPosition, yEditPosition);
		if (xEditPosition == BOARD_SIZE - 1){
			xEditPosition = 0;
			yEditPosition--;
		} else {
			xEditPosition++;
		}
		
		// if we run out of board, move to the start
		if (yEditPosition < 0){
			yEditPosition = BOARD_SIZE - 1;
		}
		updateFocusField();
	}
	
	private void moveEditPositionLeft(){
		if (xEditPosition == 0){
			xEditPosition = BOARD_SIZE - 1;
		} else {
			xEditPosition--;
		}
		updateFocusField();
	}
	
	private void moveEditPositionRight(){
		if (xEditPosition == BOARD_SIZE - 1){
			xEditPosition = 0;
		} else {
			xEditPosition++;
		}
		updateFocusField();
	}
	
	private void moveEditPositionUp(){
		if (yEditPosition == BOARD_SIZE - 1){
			yEditPosition = 0;
		} else {
			yEditPosition++;
		}
		updateFocusField();
	}
	
	private void moveEditPositionDown(){
		if (yEditPosition == 0){
			yEditPosition = BOARD_SIZE - 1;
		} else {
			yEditPosition--;
		}
		updateFocusField();
	}

	@Override
	public void addWord(int x, int y, String word) {
		Field[][] fields = board.getBoardCharArray();
		for (int i = 0; i < word.length() && i < BOARD_SIZE; i++) {
			fields[x + i][y].setTile(word.charAt(i));
		}
		clear();
		drawBoard();
	}

	public void addTempWord(int x, int y, String word) {
		for (int i = 0; i < word.length() && i < BOARD_SIZE; i++) {
			addTile(x + i, y, word.charAt(i), TyleType.ACTIVE);
		}
	}
	
	private void doCancel(){
		drawBoard();
		focusField.setVisible(false);
		tempBoard = board.clone();
		editMode = false;
		
	}

	public void clearTempWord() {
		tempBoard = board.clone();
	}

	private void drawBoard() {
		clear();
		Field[][] fields = board.getBoardCharArray();
		for (int y = 0; y < BOARD_SIZE; y++) {
			for (int x = 0; x < BOARD_SIZE; x++) {
				Field field = fields[x][y];
				addField(x, y, field);
			}
		}
	}

	private void addField(int x, int y, Field field) {
		Character c = field.getTileValue();
		if (c != null && !c.equals(' ') && !c.equals('.')) {
			addTile(x, y, c, TyleType.NORMAL);
		} else if (field.getScoreMultiplier() != null) {
			addScoreMultiplier(x, y, field.getScoreMultiplier());
		}
	}

	private void addScoreMultiplier(int x, int y,
			ScoreMultiplier scoreMultiplier) {
		FieldWidget fieldWidget = new FieldWidget(scoreMultiplier);
		addWidget(x, y, fieldWidget);
	}

	private void addTile(int x, int y, Character tile, TyleType type) {
		TileWidget tileWidget = new TileWidget(tile, type);
		addWidget(x, y, tileWidget);
	}

	private void addWidget(int x, int y, Widget widget) {
		add(widget, (x * TILE_FIELD_SIZE) + BOARD_OFFSET + TILE_OFFSET, ((BOARD_SIZE
				- y - 1) * TILE_FIELD_SIZE)
				+ BOARD_OFFSET + TILE_OFFSET);
	}


	@Override
	public void onKeyPress(KeyPressEvent event) {
		if (!editMode) {
			return;
		}
		int keyCode = event.getNativeEvent().getKeyCode();
		if (keyCode == KeyCodes.KEY_ESCAPE) {
			doCancel();
		} else if (keyCode == KeyCodes.KEY_ENTER) {
			doOk();
		} else if (keyCode == KeyCodes.KEY_DOWN){
			moveEditPositionDown();
		} else if (keyCode == KeyCodes.KEY_UP){
			moveEditPositionUp();
		} else if (keyCode == KeyCodes.KEY_LEFT){
			moveEditPositionLeft();
		} else if (keyCode == KeyCodes.KEY_RIGHT){
			moveEditPositionRight();
		} else {
			char c = event.getCharCode();
			if (Character.isLetter(c) || c == ' ') {
				addCharacter(c);
			}
		}
	}

	private void doOk() {
		focusField.setVisible(false);
		board = tempBoard;
		tempBoard = board.clone();
		editMode = false;
		drawBoard();
	}

	public void addSolution(Solution solution) {
		board.addSolution(solution);
		drawBoard();
	}

	public Character[] getBoardTiles() {
		return board.getBoardTiles();
	}
}

package nl.krisborg.gwt.scrabblesolver.client.ui;

import nl.krisborg.gwt.scrabblesolver.client.TyleType;
import nl.krisborg.gwt.scrabblesolver.client.grammar.Board;
import nl.krisborg.gwt.scrabblesolver.client.ui.interfaces.AddWordListener;
import nl.krisborg.gwt.scrabblesolver.client.ui.interfaces.BoardListener;
import nl.krisborg.gwt.scrabblesolver.shared.Field;
import nl.krisborg.gwt.scrabblesolver.shared.ScoreMultiplier;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

//public class BoardWidget extends Grid implements BoardListener {
public class BoardWidget extends AbsolutePanel implements BoardListener,
		AddWordListener {

	private static final int TILE_OFFSET = 1;
	private static final int TILE_FIELD_SIZE = 40;
	private static final int BOARD_OFFSET = 1;
	private static final int BOARD_SIZE = 15;

	private Board board;
	private int size;

	private boolean editMode = false;
	private int xEditPosition = 0;
	private int yEditPosition = 0;
	private String tempWord = "";

	public BoardWidget(final int size) {
		
		super();
		
		RootPanel.get().addDomHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				int keyCode = event.getNativeEvent().getKeyCode();
				if (keyCode == KeyCodes.KEY_ESCAPE) {
					doCancel();
				} else if (keyCode == KeyCodes.KEY_ENTER) {
					// doOk();
				}
			}
		}, KeyDownEvent.getType());
		
		RootPanel.get().addDomHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				char c = event.getCharCode();
				if (editMode) {
					if (Character.isLetter(c) || c == ' ') {
						tempWord += c;
						addCharacter(c);
					}
				}
			}

		}, KeyPressEvent.getType());
		
		setStyleName("scrabbleBoard");
		this.size = size;
		setSize("602px", "602px");

		sinkEvents(Event.ONCLICK);
		addHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				xEditPosition = (event.getRelativeX(getElement()) - BOARD_OFFSET)
						/ TILE_FIELD_SIZE;
				yEditPosition = size - 1
						- (event.getRelativeY(getElement()) - BOARD_OFFSET)
						/ TILE_FIELD_SIZE;
				editMode = true;
			}

		}, ClickEvent.getType());

		addHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				int keyCode = event.getNativeEvent().getKeyCode();
				if (keyCode == KeyCodes.KEY_ESCAPE) {
					doCancel();
				} else if (keyCode == KeyCodes.KEY_ENTER) {
					// doOk();
				}
			}
		}, KeyDownEvent.getType());

		addHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				char c = event.getCharCode();
				if (editMode) {
					if (Character.isLetter(c) || c == ' ') {
						tempWord += c;
						addCharacter(c);
					}
				}
			}

		}, KeyPressEvent.getType());
	}
	
	private void addCharacter(char c){
		addTile(xEditPosition, yEditPosition, c, TyleType.ACTIVE);
		if (xEditPosition == BOARD_SIZE - 1){
			xEditPosition = 0;
			yEditPosition++;
		} else {
			xEditPosition++;
		}
		
		// if we run out of board, move to the start
		if (yEditPosition == BOARD_SIZE - 1){
			yEditPosition = 0;
		}
	}

	public void registerNewBoard(Board board) {
		this.board = board;
		drawBoard();
	}

	@Override
	public void addWord(int x, int y, String word) {
		Field[][] fields = board.getBoardCharArray();
		for (int i = 0; i < word.length() && i < size; i++) {
			fields[x + i][y].setTile(word.charAt(i));
		}
		clear();
		drawBoard();
	}

	public void addTempWord(int x, int y, String word) {
		for (int i = 0; i < word.length() && i < size; i++) {
			addTile(x + i, y, word.charAt(i), TyleType.ACTIVE);
		}
	}
	
	private void doCancel(){
		drawBoard();
		tempWord = "";
		editMode = false;
		
	}

	public void clearTempWord() {
		drawBoard();
	}

	private void drawBoard() {
		clear();
		Field[][] fields = board.getBoardCharArray();
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
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
		add(widget, (x * TILE_FIELD_SIZE) + BOARD_OFFSET + TILE_OFFSET, ((size
				- y - 1) * TILE_FIELD_SIZE)
				+ BOARD_OFFSET + TILE_OFFSET);
	}
}

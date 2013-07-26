package nl.krisborg.gwt.scrabblesolver.client.ui;

import nl.krisborg.gwt.scrabblesolver.client.TyleType;
import nl.krisborg.gwt.scrabblesolver.client.grammar.Board;
import nl.krisborg.gwt.scrabblesolver.client.grammar.Field;
import nl.krisborg.gwt.scrabblesolver.client.grammar.ScoreMultiplier;
import nl.krisborg.gwt.scrabblesolver.client.ui.interfaces.AddWordListener;
import nl.krisborg.gwt.scrabblesolver.client.ui.interfaces.BoardListener;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;

//public class BoardWidget extends Grid implements BoardListener {
public class BoardWidget extends AbsolutePanel implements BoardListener, AddWordListener {
	
	private static final int TILE_OFFSET = 1;
	private static final int TILE_FIELD_SIZE = 40;
	private static final int BOARD_OFFSET = 1;
	
	private Board board;
	private int size;
	private InputPanel inputPanel;
	
	public BoardWidget(final int size){
		super();
		setStyleName("scrabbleBoard");
		this.size = size;
		setSize("602px", "602px");
		
		inputPanel = new InputPanel(this);
		
		sinkEvents(Event.ONCLICK);
	    addHandler(new ClickHandler(){

	        @Override
	        public void onClick(ClickEvent event) {
	        	inputPanel.setGlassEnabled(true);
	            inputPanel.show();
	            int x = (event.getRelativeX(getElement()) - BOARD_OFFSET) / TILE_FIELD_SIZE;
	            int y = size - 1 - (event.getRelativeY(getElement()) - BOARD_OFFSET) / TILE_FIELD_SIZE;
	            inputPanel.setLocation(x, y);
	            inputPanel.setMaxInputLength(size - x);
	        }

	    }, ClickEvent.getType());
	}
	
	public void registerNewBoard(Board board){
		this.board = board;
		drawBoard();
	}

	@Override
	public void addWord(int x, int y, String word) {
		Field[][] fields = board.getBoardCharArray();
		for (int i = 0; i < word.length() && i < size; i++){
			fields[x+i][y].setTile(word.charAt(i));
		}
		clear();
		drawBoard();
	}
	
	public void addTempWord(int x, int y, String word){
		for (int i = 0; i < word.length() && i < size; i++){
			addTile(x + i, y, word.charAt(i), TyleType.ACTIVE);
		}
	}
	
	public void clearTempWord(){
		drawBoard();
	}
	
	private void drawBoard(){
		clear();
		Field[][] fields = board.getBoardCharArray();
		for (int y = 0; y < size; y++){
            for (int x = 0; x < size; x++){
            	Field field = fields[x][y];
            	addField(x, y, field);
            }
        }
	}
	
	private void addField(int x, int y, Field field){
		Character c = field.getTileValue();
		if (c != null && !c.equals(' ') && !c.equals('.')){
			addTile(x, y, c, TyleType.NORMAL);
		} else if (field.getScoreMultiplier() != null){
			addScoreMultiplier(x, y, field.getScoreMultiplier());
		}
	}
	
	private void addScoreMultiplier(int x, int y, ScoreMultiplier scoreMultiplier) {
		FieldWidget fieldWidget = new FieldWidget(scoreMultiplier);
		addWidget(x, y, fieldWidget);
	}

	private void addTile(int x, int y, Character tile, TyleType type){
		TileWidget tileWidget = new TileWidget(tile, type);
		addWidget(x, y, tileWidget);
	}
	
	private void addWidget(int x, int y, Widget widget){
		add(widget, (x * TILE_FIELD_SIZE) + BOARD_OFFSET + TILE_OFFSET, ((size - y - 1) * TILE_FIELD_SIZE) + BOARD_OFFSET + TILE_OFFSET);
	}
}

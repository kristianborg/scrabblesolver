package nl.krisborg.gwt.scrabblesolver.client.ui;

import nl.krisborg.gwt.scrabblesolver.client.TyleType;
import nl.krisborg.gwt.scrabblesolver.client.ui.interfaces.AddWordListener;
import nl.krisborg.gwt.scrabblesolver.client.ui.interfaces.KeyBoardInterceptor;
import nl.krisborg.gwt.scrabblesolver.client.ui.interfaces.KeyBoardListener;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;

public class TilesWindget extends AbsolutePanel implements AddWordListener, KeyBoardListener {
	
	private String tiles = ""; 
	private static final int TILE_FIELD_SIZE = 40;
	private static final int TILE_OFFSET = 5;
	
	public TilesWindget(final KeyBoardInterceptor interceptor){
		addStyleName("tilesBoard");
		
		sinkEvents(Event.ONCLICK);
	    addHandler(new ClickHandler(){

	        @Override
	        public void onClick(ClickEvent event) {
	        	interceptor.setAvtiveListener(TilesWindget.this);
	        }

	    }, ClickEvent.getType());
		
		
		drawTiles();
	}

	@Override
	public void addTempWord(int x, int y, String word) {
		clear();
		addWord(word, TyleType.ACTIVE);
	}

	@Override
	public void addWord(int x, int y, String word) {
		this.tiles = word;
		drawTiles();
		
	}

	@Override
	public void clearTempWord() {
		drawTiles();
	}
	
	public Character[] getTiles() {
		char[] charArray = tiles.toCharArray();
		Character[] result = new Character[charArray.length];
		for(int i = 0; i < charArray.length; i++ ){
			result[i] = charArray[i];
		}
		return result;
	}
	
	private void drawTiles(){
		clear();
		addWord(tiles, TyleType.NORMAL);
	}
	
	private void addWord(String word, TyleType type){
		for (int i = 0; i < word.length(); i++){
			TileWidget tileWidget = new TileWidget(word.charAt(i), type);
			add(tileWidget, TILE_OFFSET + i * (TILE_FIELD_SIZE + TILE_OFFSET), TILE_OFFSET);
		}
	}

	@Override
	public void onKeyPress(KeyPressEvent event) {
		char c = event.getCharCode();
		if (Character.isLetter(c) && tiles.length() <= 7) {
			tiles += c;
			addWord(tiles, TyleType.NORMAL);
		}
	}
}

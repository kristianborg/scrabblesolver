package nl.krisborg.gwt.scrabblesolver.client.grammar;

import java.util.*;

/**
 * User: Kris
 * Since: 9-10-11 14:47
 */
public class TileStack {

    // source: http://nl.wikipedia.org/wiki/Scrabble#Letters
    private static final String INITIAL_TILES = "aaaaaabbccdddddeeeeeeeeeeeeeeeeeeffggghhiiiijjkkklllmmmnnnnnnnnnnooooooppqrrrrrssssstttttuuuvvwwxyzz";
    private List<Character> tiles = new ArrayList<Character>();
    private Random random;

    public TileStack(){
        reset();
    }

    public void reset(){
        random = new Random();
        tiles.clear();
        for (int i = 0; i < INITIAL_TILES.length(); i++){
            tiles.add(INITIAL_TILES.charAt(i));
        }
    }

    public int tilesLeft(){
        return tiles.size();
    }

    public List<Character> drawTiles(int numTiles){
        if (numTiles > tilesLeft()){
            numTiles = tilesLeft();
        }

        List<Character> result = new ArrayList<Character>();
        for (int i = 0; i < numTiles; i++){
            result.add(drawTile());
        }

        return result;
    }

    private char drawTile(){
        return tiles.remove(random.nextInt(tilesLeft()));
    }

    public boolean isEmpty() {
        return tiles.isEmpty();
    }

    public int getSize() {
        return tiles.size();
    }
}

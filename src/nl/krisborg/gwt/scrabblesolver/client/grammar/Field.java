package nl.krisborg.gwt.scrabblesolver.client.grammar;

import java.io.Serializable;



/**
 * User: Kris
 * Since: 15-10-11 15:17
 */
public class Field implements Serializable {

    private Tile tile;
    private ScoreMultiplier scoreMultiplier;

    public Field(){
        this(null);
    }

    public Field(ScoreMultiplier scoreMultiplier){
        this.scoreMultiplier = scoreMultiplier;
    }

    public void setTile(Character c) {
        if (c == null || c == 0 || c == ' '){
            tile = null;
        } else {
            tile = Tile.valueOf(c.toString().toUpperCase());
        }
    }
    
    public void setTileObject(Tile tile){
    	this.tile = tile;
    }

    public void setScoreMultiplier(Character c) {
        switch (c){
            case 'd':
                this.scoreMultiplier = ScoreMultiplier.DL;
                break;
            case 't':
                this.scoreMultiplier = ScoreMultiplier.TL;
                break;
            case 'w':
                this.scoreMultiplier = ScoreMultiplier.DW;
                break;
            case 'x':
                this.scoreMultiplier = ScoreMultiplier.TW;
                break;
            default:
                throw new IllegalArgumentException("Invalid point value: " + c);
        }
    }

    public boolean containsTile(){
        return tile != null;
    }

    public Character getTileValue() {
        return tile == null ? '.' : tile.toString().toLowerCase().charAt(0);
    }

    public int getPoints(){
        if (tile == null){
            return 0;
        }
        return tile.getPoints();
    }

    public ScoreMultiplier getScoreMultiplier(){
        return scoreMultiplier;
    }

    public int getScoreMultiplierValue(){
        if (scoreMultiplier == null){
            return 1;
        }
        return scoreMultiplier.getMultiplier();
    }

    public boolean hasWordScopedMultiplier(){
        if (scoreMultiplier == null){
            return false;
        }

        return scoreMultiplier.isWordScoped();
    }

    public boolean hasTileScopedMultiplier(){
        if (scoreMultiplier == null){
            return false;
        }

        return !scoreMultiplier.isWordScoped();
    }

    public Field clone(){
        Field clone = new Field(scoreMultiplier);
        clone.setTileObject(tile);
        return clone;
    }
}

package nl.krisborg.gwt.scrabblesolver.client.grammar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Kris
 * Since: 14-10-11 23:00
 */
public class Solution implements Comparable<Solution>, Serializable {
    private int x;
    private int y;
    private String word;
    private List<Solution> sideEffecSolutions;
    private List<Character> requiredTiles;
    private int points = 0;
    private Boolean horizontal = null;
    
    public Solution(){
    	
    }

    public Solution(int x, int y, String word){
        this (x,  y, word, new ArrayList<Solution>());
    }

    public Solution(int x, int y, String word, List<Solution> sideEffecSolutions){
        this.x = x;
        this.y = y;
        this.word = word;
        this.sideEffecSolutions = sideEffecSolutions;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public String getWord() {
        return word;
    }

    public List<Solution> getSideEffecSolutions(){
        return this.sideEffecSolutions;
    }

    public int getPoints(){
        return this.points;
    }

    public void setPoints(int points){
        this.points = points;
    }

    public void setHorizontal(boolean horizontal){
        this.horizontal = horizontal;
    }

    public Boolean isHorizontal(){
        return horizontal;
    }

    // TODO: add test
    public void setRequiredTiles(List<Character> requiredTiles){
        this.requiredTiles = requiredTiles;
    }

    public List<Character> getRequiredTiles(){
        return this.requiredTiles;
    }

    @Override
    public int compareTo(Solution o) {
        if (!(points == o.points)){
            return points - o.points;
        }
        
        if (!o.word.equals(word)){
            return word.compareTo(o.word);
        }

        if (!(x == o.x)){
            return x - o.x;
        }

        return y - o.y;
    }

    @Override
    public String toString(){
        String result = "";
        if (points != 0){
            result += points + ": ";
        }

        String orientation = horizontal == null ? "" : horizontal ?", h":", v";
        result += word + " (" + x + ", " + y + orientation + ")";
        if (sideEffecSolutions != null && !sideEffecSolutions.isEmpty()){
            result += " [";
            for (Solution sideEffecSolution : sideEffecSolutions) {
                result+= sideEffecSolution + ", ";
            }
            result = result.substring(0, result.length() - 2);
            result += "]";
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Solution that = (Solution) o;

        if (x != that.x) return false;
        if (y != that.y) return false;
        if (points != that.points) return false;
        if (sideEffecSolutions != null ? !sideEffecSolutions.equals(that.sideEffecSolutions) : that.sideEffecSolutions != null)
            return false;
        if (word != null ? !word.equals(that.word) : that.word != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + points;
        result = 31 * result + (word != null ? word.hashCode() : 0);
        result = 31 * result + (sideEffecSolutions != null ? sideEffecSolutions.hashCode() : 0);
        return result;
    }
}


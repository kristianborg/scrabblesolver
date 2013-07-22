package nl.krisborg.gwt.scrabblesolver.client.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Kris
 * Since: 14-10-11 15:00
 */
public abstract class TileHelper {

    public static boolean containsAllMissingTiles(List<Character> needles, List<Character> hayStack){
        for (Character target : needles) {
            int numOwned = countOccurence(target, hayStack);
            int numReruired = countOccurence(target, needles);
            if (numReruired > numOwned){
                return false;
            }
        }
        return true;
    }

    private static int countOccurence(Character needle, List<Character> hayStack){
        int result = 0;
        for (Character character : hayStack) {
            if (character.equals(needle)){
                result++;
            }
        }
        return result;
    }

    public static List<Character> removeTiles(List<Character> allTiles, List<Character> toBeRemovedTiles) {
        List<Character> result = new ArrayList<Character>();
        for (Character tile : allTiles) {
            result.add(tile);
        }

        for (Character toBeRemovedTile : toBeRemovedTiles) {
            int removeIndex = -1;
            for (int i = 0; i < result.size(); i++){
                if (result.get(i) == toBeRemovedTile){
                    removeIndex = i;
                    break;
                }
            }
            if (removeIndex != -1){
                result.remove(removeIndex);
            }
        }
        return result;
    }
}

package nl.krisborg.gwt.scrabblesolver.client.grammar;

import java.util.*;

import nl.krisborg.gwt.scrabblesolver.client.WordList;
import nl.krisborg.gwt.scrabblesolver.client.utils.BoardFactory;
import nl.krisborg.gwt.scrabblesolver.client.utils.RotationHelper;
import nl.krisborg.gwt.scrabblesolver.client.utils.TileHelper;

/**
 * User: Kris
 * Since: 9-10-11 13:47
 */
// TODO: if I add a wrapper around both 'board' and 'coordinates' they can have a rotate() function and its users
// don't need to know its orientation
public class Board {

    // 0, 0 is bottom left; 14, 14 is top right
    public static final int DEFAULT_BOARD_SIZE = 15;

    private int boardSize;
    // TODO: rename to fields?
    private Field[][] board;

    private WordList wordList;

    public Board(WordList wordList){
        this.wordList = wordList;
        initialize();
    }

    public void initialize(){
        boardSize = DEFAULT_BOARD_SIZE;
        this.board = BoardFactory.createBoard();
    }

    public List<Solution> findSolutions(List<Character> tiles){
        if (boardEmpty()){
            return getSolutionsForEmptyBoard(tiles);
        }

        List<Solution> words = new ArrayList<Solution>();
        for (int y = Board.DEFAULT_BOARD_SIZE - 1; y >= 0; y--){
            for (int x = 0; x < Board.DEFAULT_BOARD_SIZE - 1; x++){
                words.addAll(findWordsOnPosition(x, y, tiles));
            }
        }
        return words;
    }

    // TODO: add test
    public void addSolution(Solution solution){
        String word = solution.getWord();

        if (solution.isHorizontal()){

            int y = solution.getY();
            for (int i = 0; i < word.length(); i++){

                int x = solution.getX() + i;
                Character tile = word.charAt(i);
                if (containsTile(x, y, board)){

                    if (board[x][y].getTileValue() != tile){
                        throw new RuntimeException("Illegal solution: " + solution + " for board " + board);
                    }
                } else {

                    board[x][y].setTile(tile);
                }
            }
        } else {
            int x = solution.getX();
            for (int i = 0; i < word.length(); i++){

                int y = solution.getY() - i;
                Character tile = word.charAt(i);
                if (containsTile(x, y, board)){

                    if (board[x][y].getTileValue() != tile){
                        System.out.println(toFormattedString(board));
                    }
                } else {

                    board[x][y].setTile(tile);
                }
            }
        }
    }

    public void setBoard(Field[][] board){
        this.board = board;
        this.boardSize = board.length;
    }

    public Field[][] getBoardCharArray(){
        return board;
    }

    // TODO: add test
    private List<Solution> getSolutionsForEmptyBoard(List<Character> tiles) {
        List<Solution> result = new ArrayList<Solution>();
        Collection<String> words = wordList.getWordsWithMaxLength(tiles.size());

        for (String word : words) {

            List<Character> requiredTiles = new ArrayList<Character>();
            for (int i = 0; i < word.length(); i++){
                requiredTiles.add(word.charAt(i));
            }

            if (!TileHelper.containsAllMissingTiles(requiredTiles, tiles)){
                continue;
            }

            Solution solution = new Solution(boardSize / 2, boardSize / 2, word);
            solution.setPoints(getPoints(solution, board, false));
            solution.setHorizontal(true);
            solution.setRequiredTiles(requiredTiles);

            result.add(solution);
        }

        return result;
    }

    private List<Solution> findWordsOnPosition(int x, int y, List<Character> tiles){
        List<Solution> solutions = new ArrayList<Solution>();
        int[] flippedCoordinates = RotationHelper.rotateLeft(new int[]{x, y}, boardSize);
        Field[][] flippedBoard = RotationHelper.rotateLeft(board);

        solutions.addAll(findWordsOnPosition(x, y, board, tiles, false));
        solutions.addAll(findWordsOnPosition(flippedCoordinates[0], flippedCoordinates[1], flippedBoard, tiles, true));

        return solutions;
    }

    protected List<Solution> findWordsOnPosition(int x, int y, Field[][] myBoard, List<Character> tiles, boolean isFlipped){
         List<Solution> result = new ArrayList<Solution>();

        if (containsTile(x - 1, y, myBoard)){

            // if the previous position contains a tile, this position is not
            // the start of a word, so we are done here
            return result;
        }

        int distanceTillNextTile = distanceTillNextTile(x, y, myBoard);

        Solution subWord = getWordOnPosition(x + distanceTillNextTile, y, myBoard);
        String wordSubstring = subWord == null ? "" : subWord.getWord();
        Collection<String> wordCandidates = wordList.getWordWithSubstringOnPosition(wordSubstring, distanceTillNextTile, boardSize - x);

        for (String wordCandidate : wordCandidates) {

            // for every candidate find out if it will fit
            if (!wordFits(x, y, wordCandidate, myBoard)){
                continue;
            }

            // check if the candidate does not equal the word we already found on this position
            if (wordCandidate.equals(wordSubstring)){
                continue;
            }

            // for every candidate that fits; check if we have the necessary tiles
            // to make the word
            List<Character> requiredTiles = getMissingCharactersForWord(x, y, wordCandidate, myBoard);
            if (!TileHelper.containsAllMissingTiles(requiredTiles, tiles)){
                continue;
            }

            // check if any words that are introduces as side-effects exist
            List<Solution> introducedWords = getAllIntroducedWords(x, y, myBoard, isFlipped, wordCandidate);
            if (!wordList.containsAllWords(extractWords(introducedWords))){
                continue;
            }

            // if applicable, find out the original x,y coordinates before flipping their positions
            Solution solution;
            if (isFlipped){
                int[] originalCoordinates = RotationHelper.rotateRight(new int[]{x, y}, boardSize);
                solution = new Solution(originalCoordinates[0], originalCoordinates[1], wordCandidate, introducedWords);
            } else {
                solution = new Solution(x, y, wordCandidate, introducedWords);
            }

            int points = getPoints(solution, myBoard, isFlipped);
            solution.setPoints(points);
            solution.setHorizontal(!isFlipped);
            solution.setRequiredTiles(requiredTiles);

            result.add(solution);
        }

        return result;
    }

    private List<String> extractWords(List<Solution> introducedWords) {
        List<String> result = new ArrayList<String>();
        for (Solution introducedWord : introducedWords) {
            result.add(introducedWord.getWord());
        }
        return result;
    }

    protected int getPoints(Solution solution, Field[][] myBoard, boolean isFlipped) {

        // First, get the points for the base word
        int result;
        if (isFlipped){
            // since the board is flipped, we need to also rotate the Solutions coordinates
            // This because Solution always contains normalized coordinates
            int[] flippedCoordinates = RotationHelper.rotateLeft(new int[]{solution.getX(), solution.getY()}, boardSize);
            result = getPoints(flippedCoordinates[0], flippedCoordinates[1], solution.getWord(), myBoard);
        } else {
            result = getPoints(solution.getX(), solution.getY(), solution.getWord(), myBoard);
        }
        
        for (Solution sideEffectSolution : solution.getSideEffecSolutions()){
            Field[][] flipped;
            int[] flippedCoordinates;
            if (isFlipped){
                // Since the board is flipped, we need to normalize it for the side-effect solutions
                flipped = RotationHelper.rotateRight(myBoard);

                // Coordinate always contains normalize coordinates, so we don't need to flip anything here
                flippedCoordinates = new int[]{sideEffectSolution.getX(), sideEffectSolution.getY()};
            } else {

                // The board is not flipped yet, so we need to flip the board, and the side-effect's coordinates
                flipped = RotationHelper.rotateLeft(myBoard);
                flippedCoordinates = RotationHelper.rotateLeft(new int[]{sideEffectSolution.getX(), sideEffectSolution.getY()}, boardSize);
            }
            result+= getPoints(flippedCoordinates[0], flippedCoordinates[1], sideEffectSolution.getWord(), flipped);
        }
        return result;
    }

    private int getPoints(int x, int y, String word, Field[][] myBoard){
        int points = 0;
        int wordMultiplier = 1;
        for (int i = 0; i < word.length(); i++){

            Tile tile = Tile.valueOfChar(word.charAt(i));
            Field field = myBoard[x + i][y];
            if (containsTile(x + i, y, myBoard)){

                points += field.getPoints();
            } else if (field.hasWordScopedMultiplier()){

                wordMultiplier *= field.getScoreMultiplierValue();
                points += tile.getPoints();
            } else if (field.hasTileScopedMultiplier()) {

                points += tile.getPoints() * field.getScoreMultiplierValue();
            } else { // no multiplier

                points += tile.getPoints();
            }
        }
        return points * wordMultiplier;
    }

    protected boolean containsTile(int x, int y, Field[][] my) {
        if (x < 0 || x > boardSize - 1 || y < 0 || y > boardSize -1) {
            return false;
        }

        return my[x][y].containsTile();
    }

    protected int distanceTillNextTile(int x, int y, Field[][] my) {
        for (int i = 0; i + x < boardSize; i++){
            if (containsTile(x + i, y, my)){
                return i;
            }

            if (containsTile(x + i, y - 1, my) || containsTile(x + i, y + 1, my)){
                // if the row above or below this one contains a tile, the current position
                // is empty, so we add one to the result
                return i + 1;
            }
        }

        return Integer.MAX_VALUE;
    }

    protected Solution getWordOnPosition(int x, int y, Field[][] my) {
        if (!containsTile(x, y, my)){
            return null;
        }

        String word = "";
        int startIndex = -1;
        for (int i = 0; i < boardSize; i++){

            if (containsTile(i, y, my)){

                // keep track of start-position of the word
                if (startIndex == -1){
                    startIndex = i;
                }

                // add this character to the result
                word+= my[i][y].getTileValue();
            } else if (i < x){

                // no tile on this position. If we are not on (x, y) yet: start over from here
                word = "";
                startIndex = -1;
            } else if (i > x){

                // we found the first empty place after (x, y) so we are done
                break;
            }
        }

        return new Solution(startIndex, y, word);
    }

    protected boolean wordFits(int x, int y, String wordCandidate, Field[][] my) {

        if (x + wordCandidate.length() > boardSize){
            return false;
        }
        for (int i = 0; i < wordCandidate.length(); i++){
            if (!containsTile(x + i, y, my)){
                // if there is no tile in this place we are fine
                continue;
            }

            if (my[x + i][y].getTileValue() == wordCandidate.charAt(i)){
                // if the tile in this place matches the one in the word,
                // we are fine
                continue;
            }

            // if we end up here there is an unmatching tile so the
            // word does not fit
            return false;
        }

        // now that the word fits; lets see if the position before the start
        // and after the end of the word is not taken
        int positionBeforeCurrentWord = x - 1;
        if (isInsideBoard(positionBeforeCurrentWord) && containsTile(positionBeforeCurrentWord, y, my)){
            return false;
        }

        int positionAfterCurrentWord = x + wordCandidate.length();
        if (isInsideBoard(positionAfterCurrentWord) && containsTile(positionAfterCurrentWord, y, my)){
            return false;
        }

        return true;
    }

    private boolean isInsideBoard(int point){
        return point >= 0 && point < boardSize;
    }

    protected List<Character> getMissingCharactersForWord(int x, int y, String wordCandidate, Field[][] my) {
        List<Character> result = new ArrayList<Character>();
        for (int i = 0; i < wordCandidate.length(); i ++){
            if (!containsTile(x + i, y, my)){
                result.add(wordCandidate.charAt(i));
            }
        }
        return result;
    }

    /**
     * Find als words that were introduced vertically by adding 'wordCandidate' horizontally on (x, y).
     * We need to know wether the board is flipped or not because depending on that, we need to rotate
     * the board clockwise or counterclockwise to find the vertical words
     */
    protected List<Solution> getAllIntroducedWords(int x, int y, Field[][] myBoard, boolean isFlipped, String wordCandidate) {
        List<Solution> result = new ArrayList<Solution>();
        for (int i = 0; i < wordCandidate.length(); i++){

            // if there already is a tile on this position, we did not introduce a word here
            if (containsTile(x + i, y, myBoard)){
                continue;
            }

            // if the position above or below contains a tile, this is where we introduced  a new word
            if (containsTile(x + i, y - 1, myBoard) || containsTile(x + i, y + 1, myBoard)){
                myBoard[x + i][y].setTile(wordCandidate.charAt(i));

                // we need to flip the board and the (x, y) position to find out
                Field[][] flipped;
                int[] flippedCoordinates;
                if (isFlipped){
                    // TODO: create unittest for this part. No idea if its correct
                    flippedCoordinates = RotationHelper.rotateRight(new int[]{x + i, y}, boardSize);
                    flipped = RotationHelper.rotateRight(myBoard);

                    result.add(getWordOnPosition(flippedCoordinates[0], flippedCoordinates[1], flipped));
                } else {
                    flippedCoordinates = RotationHelper.rotateLeft(new int[]{x + i, y}, boardSize);
                    flipped = RotationHelper.rotateLeft(myBoard);

                    Solution introducedWord = getWordOnPosition(flippedCoordinates[0], flippedCoordinates[1], flipped);
                    int[] introducedWordFlippedCoordinates = RotationHelper.rotateRight(new int[]{introducedWord.getX(), introducedWord.getY()}, boardSize);

                    result.add(new Solution(introducedWordFlippedCoordinates[0], introducedWordFlippedCoordinates[1], introducedWord.getWord()));
                }
                
                myBoard[x + i][y].setTile(null);
            }
        }

        return result;
    }

    // TODO: add test
    private boolean boardEmpty() {
        for (Field[] fields : board) {
            for (Field field : fields) {
                if (field.containsTile()){
                    return false;
                }
            }
        }
        return true;
    }

    public String toFormattedString(){
        return toFormattedString(board);
    }

    public String toFormattedString(Field[][] myBoard){
        String result = "  _______________\r\n";
        for (int y = boardSize - 1; y >= 0; y--){
            result+= y%10 + "|";
            for (int x = 0; x < boardSize; x++){
                result+= myBoard[x][y].getTileValue();
            }
            result+= "|\r\n";
        }
        result += "  _______________\r\n";
        result += "  012345678901234\r\n";
        return result;
    }

    @Override
    public String toString(){
        String result = "";
        for (int y = boardSize - 1; y >= 0; y--){
            for (int x = 0; x < boardSize; x++){
                result+= board[x][y].getTileValue();
            }
            result+= "\r\n";
        }
        return result;
    }
}

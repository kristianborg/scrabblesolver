package nl.krisborg.gwt.scrabblesolver.client.grammar;

import nl.krisborg.gwt.scrabblesolver.shared.BoardFactory;
import nl.krisborg.gwt.scrabblesolver.shared.Field;
import nl.krisborg.gwt.scrabblesolver.shared.Solution;

public class Board  {

    private int BOARD_SIZE = 15;
    private Field[][] board;
    
    public Board(){
    	this.board = BoardFactory.createBoard();
    }

    public void addSolution(Solution solution){
        String word = solution.getWord();

        if (solution.isHorizontal()){

            int y = solution.getY();
            for (int i = 0; i < word.length(); i++){
                int x = solution.getX() + i;
                Character tile = word.charAt(i);
                board[x][y].setTile(tile);
            }
        } else {
            int x = solution.getX();
            for (int i = 0; i < word.length(); i++){
                int y = solution.getY() - i;
                Character tile = word.charAt(i);
                board[x][y].setTile(tile);
            }
        }
    }

    public Field[][] getBoardCharArray(){
        return board;
    }
    
    public Character[] getBoardTiles(){
    	Character[] result = new Character[BOARD_SIZE * BOARD_SIZE];
    	int i = 0;
    	for(int y = 0; y < BOARD_SIZE; y++){
    		for(int x = 0; x < BOARD_SIZE; x++){
    			result[i] = board[x][y].getTileValue();
    			i++;
    		}
    	}
    	return result;
    }

    public String toFormattedString(){
        return toFormattedString(board);
    }

    public String toFormattedString(Field[][] myBoard){
        String result = "  _______________\r\n";
        for (int y = BOARD_SIZE - 1; y >= 0; y--){
            result+= y%10 + "|";
            for (int x = 0; x < BOARD_SIZE; x++){
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
        for (int y = BOARD_SIZE - 1; y >= 0; y--){
            for (int x = 0; x < BOARD_SIZE; x++){
                result+= board[x][y].getTileValue();
            }
            result+= "\r\n";
        }
        return result;
    }
}

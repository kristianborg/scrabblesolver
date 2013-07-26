package nl.krisborg.gwt.scrabblesolver.shared;


public abstract class BoardFactory {

    private static final int BOARD_SIZE = 15;

    private static ScoreMultiplier nl = null;
    private static ScoreMultiplier dl = ScoreMultiplier.DL;
    private static ScoreMultiplier tl = ScoreMultiplier.TL;
    private static ScoreMultiplier dw = ScoreMultiplier.DW;
    private static ScoreMultiplier tw = ScoreMultiplier.TW;
    private static ScoreMultiplier st = ScoreMultiplier.START;

    public static Field[][] createBoard(){
        return createBoard(createEmptyString(BOARD_SIZE * BOARD_SIZE));
    }

    public static Field[][] createBoard(String boardTiles){
        Field[][] result = new Field[][]{
                {f(tl), f(nl), f(nl), f(nl), f(tw), f(nl), f(nl), f(dl), f(nl), f(nl), f(tw), f(nl), f(nl), f(nl), f(tl)},
                {f(nl), f(dl), f(nl), f(nl), f(nl), f(tl), f(nl), f(nl), f(nl), f(tl), f(nl), f(nl), f(nl), f(dl), f(nl)},
                {f(nl), f(nl), f(dw), f(nl), f(nl), f(nl), f(dl), f(nl), f(dl), f(nl), f(nl), f(nl), f(dw), f(nl), f(nl)},
                {f(nl), f(nl), f(nl), f(tl), f(nl), f(nl), f(nl), f(dw), f(nl), f(nl), f(nl), f(tl), f(nl), f(nl), f(nl)},
                {f(tw), f(nl), f(nl), f(nl), f(dw), f(nl), f(dl), f(nl), f(dl), f(nl), f(dw), f(nl), f(nl), f(nl), f(tw)},
                {f(nl), f(tl), f(nl), f(nl), f(nl), f(tl), f(nl), f(nl), f(nl), f(tl), f(nl), f(nl), f(nl), f(tl), f(nl)},
                {f(nl), f(nl), f(dl), f(nl), f(dl), f(nl), f(nl), f(nl), f(nl), f(nl), f(dl), f(nl), f(dl), f(nl), f(nl)},
                {f(dl), f(nl), f(nl), f(dw), f(nl), f(nl), f(nl), f(st), f(nl), f(nl), f(nl), f(dw), f(nl), f(nl), f(dl)},
                {f(nl), f(nl), f(dl), f(nl), f(dl), f(nl), f(nl), f(nl), f(nl), f(nl), f(dl), f(nl), f(dl), f(nl), f(nl)},
                {f(nl), f(tl), f(nl), f(nl), f(nl), f(tl), f(nl), f(nl), f(nl), f(tl), f(nl), f(nl), f(nl), f(tl), f(nl)},
                {f(tw), f(nl), f(nl), f(nl), f(dw), f(nl), f(dl), f(nl), f(dl), f(nl), f(dw), f(nl), f(nl), f(nl), f(tw)},
                {f(nl), f(nl), f(nl), f(tl), f(nl), f(nl), f(nl), f(dw), f(nl), f(nl), f(nl), f(tl), f(nl), f(nl), f(nl)},
                {f(nl), f(nl), f(dw), f(nl), f(nl), f(nl), f(dl), f(nl), f(dl), f(nl), f(nl), f(nl), f(dw), f(nl), f(nl)},
                {f(nl), f(dl), f(nl), f(nl), f(nl), f(tl), f(nl), f(nl), f(nl), f(tl), f(nl), f(nl), f(nl), f(dl), f(nl)},
                {f(tl), f(nl), f(nl), f(nl), f(tw), f(nl), f(nl), f(dl), f(nl), f(nl), f(tw), f(nl), f(nl), f(nl), f(tl)},
        };

        for (int i = 0; i < BOARD_SIZE; i++){
            String tiles = boardTiles.substring(i * BOARD_SIZE, (i + 1) * BOARD_SIZE);
            if (tiles.length() != BOARD_SIZE){
                throw new IllegalArgumentException("invalid size " + BOARD_SIZE + " for input length " + tiles.length());
            }

            int y = BOARD_SIZE - i - 1;
            for (int x = 0; x < BOARD_SIZE; x++){
                char tile = tiles.charAt(x);
                if (tile != ' '){
                    result[x][y].setTile(tile);
                }
            }
        }

        return result;
    }

    private static Field f(ScoreMultiplier sm){
        return new Field(sm);
    }

    private static String createEmptyString(int length){
        String result = "";
        for (int i = 0; i < length; i++){
            result += " ";
        }
        return result;
    }
}

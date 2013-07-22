package nl.krisborg.gwt.scrabblesolver.client.grammar;

public enum ScoreMultiplier {
    START(false, 1),
    DL(false, 2),
    TL(false, 3),
    DW(true, 2),
    TW(true, 3);

    private boolean wordScoped;
    private int multiplier;


    private ScoreMultiplier(boolean wordScoped, int multiplier){
        this.wordScoped = wordScoped;
        this.multiplier = multiplier;
    }

    boolean isWordScoped(){
        return wordScoped;
    }

    public int getMultiplier(){
        return multiplier;
    }
}

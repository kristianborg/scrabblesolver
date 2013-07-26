package nl.krisborg.gwt.scrabblesolver.server;

import java.util.Arrays;
import java.util.List;

public abstract class IllegalWordList {

    private static List<String> illegalWords = Arrays.asList(
            "bv",
            "cd",
            "cv",
            "dc",
            "vpn",
            "lp",
            "tv",
            "dtp",
            "pc",
            "vn",
            "nv",
            "bnp",
            "pcb",
            "ftp",


            "atv",
            "fte",
            "edi",
            "ic",
            "eu",
            "ub",
            "iq",
            "ov",
            "ij",
            "ek",
            "ip",
            "ix",
            "azc",
            "xiv",
            "xvi",
            "ii",
            "xi",
            "aov",
            "naw",
            "aow"
    );

    public static boolean contains(String word){
        return illegalWords.contains(word);
    }

}

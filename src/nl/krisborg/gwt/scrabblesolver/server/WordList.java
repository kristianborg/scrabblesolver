package nl.krisborg.gwt.scrabblesolver.server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: Kris
 * Since: 9-10-11 13:20
 */
public class WordList {

    public static long getWordWithSubstringOnPositionTime = 0;
    public static long getWordWithSubstringOnPositionOccurence = 0;

    private static final int MAX_WORD_LENGTH = 15;

    private List<String> files;
    private Set<String> words;
    private List<Character> allowedCharacters = Arrays.asList('a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z');
    private Map<Integer, List<String>> wordsPerLength = new HashMap<Integer, List<String>>();

    public static final List<String> UNKNOWN_BUT_SUPPORTED_WORDS = Arrays.asList("hi");

    int i = 0;

    public WordList(List<String> files){
        this.files = files;
        words = new HashSet<String>();
        init();
    }

    private void init(){

        long before = System.currentTimeMillis();
        words.addAll(UNKNOWN_BUT_SUPPORTED_WORDS);
        BufferedReader br = null;
        try {

            for(String fileName : files){
                br = new BufferedReader(new FileReader(fileName));
                String line;
                while ((line = br.readLine()) != null){
                    line = line.trim().toLowerCase();
                    if (line.length() > MAX_WORD_LENGTH || line.length() == 1){
                        continue;
                    }

                    boolean illegalCharacter = false;
                    boolean containsVowels = false;
                    for (int i = 0; i < line.length(); i++){
                        Character c = line.charAt(i);
                        if (!allowedCharacters.contains(c)){
                            illegalCharacter = true;
                        }

                        if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u' || c == 'y'){
                            containsVowels = true;
                        }
                    }
                    if (illegalCharacter){
                        continue;
                    }

                    // assuming this is an acronym
                    if (!containsVowels){
                        continue;
                    }

                    if (IllegalWordList.contains(line)){
                        continue;
                    }

                    words.add(line);
                    int wordLength = line.length();
                    List<String> wordsByLength = wordsPerLength.get(wordLength);
                    if (wordsByLength == null){
                        wordsByLength = new ArrayList<String>();
                        wordsPerLength.put(wordLength, wordsByLength);
                    }
                    wordsByLength.add(line);
                }
            }
        } catch (IOException iox){
            throw new RuntimeException(iox);
        } finally {
        	if (br != null){
        		try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }

        System.out.println("Wordlist init took " + (System.currentTimeMillis() - before));
    }

    protected Collection <String> getWords(){
        return words;
    }

    public Collection<String> getWordsWithMaxLength(int max){
        return getWordWithSubstringOnPosition("", 0, max);
    }

    public Collection<String> getWordWithSubstringOnPosition(String wordSubstring, int distanceTillNextTile, int maxWordLength) {
        getWordWithSubstringOnPositionOccurence++;
        List<String> result = new ArrayList<String>();
        boolean isEmptyWord = wordSubstring.equals("");
        if (distanceTillNextTile > MAX_WORD_LENGTH){
            return result;
        }

        long before = System.currentTimeMillis();
        int minWordLenght = distanceTillNextTile + wordSubstring.length();
        for (int i = minWordLenght; i <= maxWordLength; i++){

            List<String> wordsByLength = wordsPerLength.get(i);
            if (wordsByLength == null){
                continue;
            }
            
            for (String word : wordsPerLength.get(i)) {
                if (isEmptyWord || word.indexOf(wordSubstring) == distanceTillNextTile){
                    result.add(word);
                }
            }
        }

        getWordWithSubstringOnPositionTime += (System.currentTimeMillis() - before);
        return result;
    }

    public boolean containsAllWords(List<String> introducedWords) {
        return words.containsAll(introducedWords);
    }
}

package nl.krisborg.gwt.scrabblesolver.client;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

import nl.krisborg.gwt.scrabblesolver.client.ui.InfoWidget;
import nl.krisborg.gwt.scrabblesolver.client.ui.MainWindow;
import nl.krisborg.gwt.scrabblesolver.client.ui.interfaces.WordListListener;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.RequestBuilder.Method;
import com.google.gwt.user.client.Window;

/**
 * User: Kris
 * Since: 9-10-11 13:20
 */
public class WordList implements Serializable {
    private static final int MAX_WORD_LENGTH = 15;

    private Set<String> words;
    private List<Character> allowedCharacters = Arrays.asList('a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','w','r','s','t','u','v','w','x','y','z');
    Map<Integer, List<String>> wordsPerLength = new HashMap<Integer, List<String>>();
    private WordListListener listener;
    private boolean initialized = false;
    private boolean initializing = false;
    
    public void registerListener(WordListListener listener){
    	this.listener = listener;    	
    }

    public WordList(){
        words = new HashSet<String>();
    }
    
    public boolean isInitialized(){
    	return initialized;
    }

    public void init(final InfoWidget infoWidget){
    	if (initialized || initializing){
    		return;
    	}
    	
    	initializing = true;
    	try {
			new RequestBuilder(RequestBuilder.GET, "wordlist.txt").sendRequest("", new RequestCallback() {

				@Override
				public void onError(Request request, Throwable exception) {
				}

				@Override
				public void onResponseReceived(Request request, Response response) {
					Window.alert("msg1");
					String text = response.getText();
					String[] wordsArray = response.getText().split("\\r\\n");
			    	//String line = null;;
					infoWidget.setTitle("Processing wordList of " + wordsArray.length + " words");
					int j = 0;
			        for (String line : wordsArray){
			        	j++;
			        	if (j%1000 == 0){
			        		infoWidget.setTitle("Processing word " + j +  " of " + wordsArray.length);
			        	}
			            line = line.trim().toLowerCase();
			            if (line.length() > MAX_WORD_LENGTH){
			                continue;
			            }

			            boolean illegalCharacter = false;
			            for (int i = 0; i < line.length(); i++){
			                Character c = line.charAt(i);
			                if (!allowedCharacters.contains(c)){
			                    illegalCharacter = true;
			                    break;
			                }
			            }
			            if (illegalCharacter){
			            	new String();
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
			        initialized = true;
			        notifyListener();
					
				}
				});
		} catch (RequestException e) {
			e.printStackTrace();
		}
    }
    
    private void notifyListener(){
    	listener.wordListLoaded();
    }

    public int getNumWords(){
        return words.size();
    }

    public Collection<String> getWordsWithMaxLength(int max){
        return getWordWithSubstringOnPosition("", 0, max);
    }

    public Collection<String> getWordWithSubstringOnPosition(String wordSubstring, int distanceTillNextTile, int maxWordLength) {
        List<String> result = new ArrayList<String>();
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
                if (word.indexOf(wordSubstring) == distanceTillNextTile){
                    result.add(word);
                }
            }
        }
        return result;
    }

    public boolean containsAllWords(List<String> introducedWords) {
        return words.containsAll(introducedWords);
    }
}

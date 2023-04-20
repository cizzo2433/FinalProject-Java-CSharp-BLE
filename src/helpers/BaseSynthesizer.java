package helpers;

import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class BaseSynthesizer {
	
	/**
	 * Gets an InputStream to MP3 data for the returned information from a request
	 *
	 * @param synthText Text you want to be synthesized into MP3 data
	 * @return Returns an input stream of the MP3 data from Google
	 * @throws IOException Throws exception if it can not complete the request
	 */
	public abstract InputStream getMP3Data(String synthText) throws IOException;
	
	/**
	 * Gets an InputStream to MP3Data for the returned information from a request
	 * 
	 * @param synthText List of Strings you want to be synthesized into MP3 data
	 * @return Returns an input stream of all the MP3 data that is returned from Google
	 * @throws IOException Throws exception if it cannot complete the request
	 */
	public InputStream getMP3Data(List<String> synthText) throws IOException {

		//Uses an executor service pool for concurrency. Limit to 1000 threads max.
		ExecutorService pool = Executors.newFixedThreadPool(1000);

		//Stores data that will be returned in the future
		Set<Future<InputStream>> set = new LinkedHashSet<>(synthText.size());

		//vIterates through the list
		for (String part : synthText) {


			Callable<InputStream> callable = new MP3DataFetcher(part);
			Future<InputStream> future = pool.submit(callable);

			// Adds the response that will be returned to a set.
			set.add(future);
		}
		List<InputStream> inputStreams = new ArrayList<>(set.size());
		for (Future<InputStream> future : set) {
			try {
				inputStreams.add(future.get());
			} catch (ExecutionException e) {
				Throwable ex = e.getCause();
				if (ex instanceof IOException) {
					throw (IOException) ex;
				}

				//Will probably never be called, but just in case...
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		return new SequenceInputStream(Collections.enumeration(inputStreams));
	}
	
	/**
	 * Separates a string into smaller parts so that Google will not reject the request.
	 * 
	 * @param input The string you want to separate
	 * @return A List of the String fragments from your input
	 */
	protected List<String> parseString(String input) {
		return parseString(input, new ArrayList<>());
	}
	
	/**
	 * Separates a string into smaller parts so that Google will not reject the request.
	 * 
	 * @param input The string you want to break up into smaller parts
	 * @param fragments List<String> to add to
	 * @return A list of the fragments of the original String
	 */
	private List<String> parseString(String input , List<String> fragments) {
		if (input.length() <= 100) {
			fragments.add(input);
			return fragments;
		} else {

			// Checks if a space exists
			int lastWord = findLastWord(input);
			if (lastWord <= 0) {
				fragments.add(input.substring(0, 100));
				return parseString(input.substring(100), fragments);
			} else {
				fragments.add(input.substring(0, lastWord));

				// Otherwise, adds the last word to the list
				return parseString(input.substring(lastWord), fragments);
			}
		}
	}
	
	/**
	 * Finds the last word in your String (before the index of 99) by searching for spaces and ending punctuation.
	 * 
	 * @param input The String you want to search through.
	 * @return The index of where the last word of the string ends before the index of 99.
	 */
	private int findLastWord(String input) {
		if (input.length() < 100)
			return input.length();
		int space = -1;
		for (int i = 99; i > 0; i--) {
			char tmp = input.charAt(i);
			if (isEndingPunctuation(tmp)) {
				return i + 1;
			}
			if (space == -1 && tmp == ' ') {
				space = i;
			}
		}
		if (space > 0) {
			return space;
		}
		return -1;
	}
	
	/**
	 * Checks if char is an ending character Ending punctuation for all languages according to Wikipedia
	 * (Except for Sanskrit non-unicode)
	 * 
	 * @param input The char you want check
	 * @return True if it is, false if not.
	 */
	private boolean isEndingPunctuation(char input) {
		return input == '.' || input == '!' || input == '?' || input == ';' || input == ':' || input == '|';
	}
	
	/**
	 * Automatically determines the language of the original text
	 * 
	 * @param text represents the text you want to check the language of
	 * @return the languageCode in ISO-639
	 * @throws IOException if it cannot complete the request
	 */
	public String detectLanguage(String text) throws IOException {
		return GoogleTranslate.detectLanguage(text);
	}
	
	/**
	 * This class is a callable. A callable is like a runnable except that it can return data and throw exceptions.
	 */
	private class MP3DataFetcher implements Callable<InputStream> {
		private String synthText;
		
		public MP3DataFetcher(String synthText) {
			this.synthText = synthText;
		}
		
		public InputStream call() throws IOException {
			return getMP3Data(synthText);
		}
	}
}

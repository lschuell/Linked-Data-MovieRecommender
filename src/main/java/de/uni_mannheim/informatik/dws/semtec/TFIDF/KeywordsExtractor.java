package de.uni_mannheim.informatik.dws.semtec.TFIDF;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.standard.ClassicFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

/**
 * Keywords extractor functionality handler
 */
class KeywordsExtractor {
	
	static HashMap<String,Integer> allStems = new HashMap<String,Integer>();

    /**
     * Get list of keywords with stem form, frequency rank, and terms dictionary
     *
     * @param fullText
     * @return List<CardKeyword>, which contains keywords cards
     * @throws IOException
     */
    static List<CardKeyword> getKeywordsList(String fullText) throws IOException {

        TokenStream tokenStream = null;

        try {
            // treat the dashed words, don't let separate them during the processing
            fullText = fullText.replaceAll("-+", "-0");

            // replace any punctuation char but apostrophes and dashes with a space
            fullText = fullText.replaceAll("[\\p{Punct}&&[^'-]]+", " ");

            // replace most common English contractions
            fullText = fullText.replaceAll("(?:'(?:[tdsm]|[vr]e|ll))+\\b", "");

            StandardTokenizer stdToken = new StandardTokenizer();
            stdToken.setReader(new StringReader(fullText));

            tokenStream = new StopFilter(new ASCIIFoldingFilter(new ClassicFilter(new LowerCaseFilter(stdToken))), EnglishAnalyzer.getDefaultStopSet());
            tokenStream.reset();

            List<CardKeyword> cardKeywords = new LinkedList<CardKeyword>();

            CharTermAttribute token = tokenStream.getAttribute(CharTermAttribute.class);
            int totalNoStems = 0;

            while (tokenStream.incrementToken()) {

                String term = token.toString();
                String stem = getStemForm(term);

                if (stem != null) {
                    CardKeyword cardKeyword = findAndAddToAllStems(cardKeywords, new CardKeyword(stem.replaceAll("-0", "-")));
                    // treat the dashed words back, let look them pretty
                    cardKeyword.add(term.replaceAll("-0", "-"));
                    totalNoStems++;
                }
            }
            
            for (ListIterator<CardKeyword> iter = cardKeywords.listIterator(); iter.hasNext(); ) {
                CardKeyword keyword = iter.next();
            	keyword.calculateTF(totalNoStems);
            }

            // reverse sort by frequency
            //Collections.sort(cardKeywords);
            
            return cardKeywords;
        } finally {
            if (tokenStream != null) {
                try {
                    tokenStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Get stem form of the term
     *
     * @param term
     * @return String, which contains the stemmed form of the term
     * @throws IOException
     */
    private static String getStemForm(String term) throws IOException {

        TokenStream tokenStream = null;

        try {
            StandardTokenizer stdToken = new StandardTokenizer();
            stdToken.setReader(new StringReader(term));

            tokenStream = new PorterStemFilter(stdToken);
            tokenStream.reset();

            // eliminate duplicate tokens by adding them to a set
            Set<String> stems = new HashSet<String>();

            CharTermAttribute token = tokenStream.getAttribute(CharTermAttribute.class);

            while (tokenStream.incrementToken()) {
                stems.add(token.toString());
            }

            // if stem form was not found or more than 2 stems have been found, return null
            if (stems.size() != 1) {
                return null;
            }

            String stem = stems.iterator().next();

            // if the stem form has non-alphanumerical chars, return null
            if (!stem.matches("[a-zA-Z0-9-]+")) {
                return null;
            }

            return stem;
        } finally {
            if (tokenStream != null) {
                try {
                    tokenStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static List<List<CardKeyword>> addStems(List<List<CardKeyword>> movieKeywords) {
    	int totalNoDocs = movieKeywords.size();
    	for (Iterator<List<CardKeyword>> iter = movieKeywords.iterator(); iter.hasNext(); ) {
    		List<CardKeyword> keywordsList = iter.next();
    		for (String stem : allStems.keySet()) {
        		findAndCalculateTfIdf(keywordsList, new CardKeyword(stem.replaceAll("-0", "-")), totalNoDocs);
        	}
        	
        	Collections.sort(keywordsList);
        	
        	/*for (CardKeyword c : keywordsList) {
        		if (c.getStem().startsWith("z")) {
        			System.out.println(c.getStem() + ":" + c.getTermFrequency());
        		}
    		}*/
    	}

    	return movieKeywords;
    	
    }

    /**
     * Find sample in collection
     *
     * @param collection
     * @param sample
     * @param <T>
     * @return <T> T, which contains the found object within collection if exists, otherwise the initially searched object
     */
    private static <Object> CardKeyword find(Collection<CardKeyword> collection, CardKeyword sample) {

        for (CardKeyword element : collection) {
            if (element.equals(sample)) {
                return element;
            }
        }

        collection.add(sample);

        return null;
    }
    
    private static void findAndCalculateTfIdf(Collection<CardKeyword> collection, CardKeyword sample, int totalNoDocs) {

    	CardKeyword foundSample = find(collection, sample);

        if (foundSample == null) {
        	foundSample = sample;
        }
        foundSample.calculateTfIdf(totalNoDocs, allStems.get(sample.getStem()));
    }
    
    private static <Object> CardKeyword findAndAddToAllStems(Collection<CardKeyword> collection, CardKeyword sample) {

        CardKeyword foundSample = find(collection, sample);

        if (foundSample == null) {
	        String stem = sample.getStem();
	        if (allStems.containsKey(stem)) {
	        	allStems.put(stem, allStems.get(stem)+1);
	        } else {
	        	allStems.put(stem, 1);
	        }
        }

        return sample;
    }
}

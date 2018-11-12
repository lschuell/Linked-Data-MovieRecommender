//read CSV file, preprocess abstract data, calculate idf
package de.uni_mannheim.informatik.dws.semtec.TFIDF;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class TfIdf {
	
	public static void getCosineSimilarity(List<List<CardKeyword>> movieKeywords) {
		List<double[]> tfidfDocsVector = new ArrayList<double[]>();
		
		for (Iterator<List<CardKeyword>> movieIter = movieKeywords.iterator(); movieIter.hasNext(); ) {
    		List<CardKeyword> keywordsList = movieIter.next();
    		double[] tfidfScores = new double[keywordsList.size()];
    		int i = 0;
    		for (ListIterator<CardKeyword> iter = keywordsList.listIterator(); iter.hasNext(); ) {
                CardKeyword keyword = iter.next();
                tfidfScores[i] = keyword.getTfIdf();
                i++;
    		}
    		tfidfDocsVector.add(tfidfScores);
		}
		
        for (int i = 0; i < tfidfDocsVector.size(); i++) {
            for (int j = 0; j < tfidfDocsVector.size(); j++) {
                System.out.println("between " + i + " and " + j + "  =  "
                                   + new CosineSimilarity().cosineSimilarity
                                       (
                                         tfidfDocsVector.get(i), 
                                         tfidfDocsVector.get(j)
                                       )
                                  );
            }
        }
    }

	public static void main(String[] args) {
		String csvFile = "data/test_small.csv";
        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"; // use comma as separator but take care of quotes

        try {

            br = new BufferedReader(new FileReader(csvFile));
            
            //use header line to determine index of abstract
            line = br.readLine();
            String[] arrayHeader = line.split(csvSplitBy);
            List<String> header = Arrays.asList(arrayHeader);
            int idxAbstract = header.indexOf("abstract");

            //store all movies: one entry per movie, consisting of fields according to header
            List<String[]> movies = new ArrayList<String[]>();
            while ((line = br.readLine()) != null) {
                String[] movie = line.split(csvSplitBy, -1);
                movies.add(movie);
            }
            
            List<List<CardKeyword>> movieKeywords = new LinkedList<List<CardKeyword>>();
            //preprocess abstract: collect terms, stems, sum frequencies and calculate tf scores
            for (ListIterator<String[]> iter = movies.listIterator(); iter.hasNext(); ) {
                String[] movie = iter.next();
                if (idxAbstract < movie.length) {
                	//System.out.println("Abstract = " + movie[idxAbstract]);
                	List<CardKeyword> keywordsList = KeywordsExtractor.getKeywordsList(movie[idxAbstract]);
    				/*for (CardKeyword c : keywordsList) {
    					System.out.println(c.getStem() + ":" + c.getTermFrequency());
    				}*/
                	movieKeywords.add(keywordsList);
                }
	            
            }
            KeywordsExtractor.addStems(movieKeywords);
            //System.out.println(KeywordsExtractor.allStems.get("more"));
            getCosineSimilarity(movieKeywords);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
		
	}

}

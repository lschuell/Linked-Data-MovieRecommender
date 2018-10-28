package de.uni_mannheim.informatik.dws.semtec.Recommender.Similarity;

import de.uni_mannheim.informatik.dws.winter.similarity.SimilarityMeasure;

import java.util.List;

public class OverlapSimilarity extends SimilarityMeasure<List<String>> {


    private static final long serialVersionUID = 1L;

    @Override
    public double calculate(List<String> first, List<String> second) {

        int min = Math.min(first.size(), second.size());
        int matches = 0;

        for (String s1 : first) {
            for (String s2 : second) {
                if (s1.equals(s2)) {
                    matches++;
                    continue;
                }
            }
        }

        return (double) matches / (double) min;
    }

}
package de.uni_mannheim.informatik.dws.semtec.Recommender.Similarity;

import de.uni_mannheim.informatik.dws.winter.similarity.SimilarityMeasure;

import java.util.List;


public class MaxDifferenceDeviation extends SimilarityMeasure<Integer> {

    private static final long serialVersionUID = 1L;
    private int maxDifference;

    public MaxDifferenceDeviation(int maxDifference) {
        this.maxDifference = maxDifference;
    }

    // applicable for Attributes Year (e.g. maxDifference=20), Runtime (e.g. maxDifference=50)
    @Override
    public double calculate(Integer first, Integer second) {

        if (first == 0 || second == 0) {
            return 0.0;
        } else {
            int diff = Math.abs(first-second);
            double deviation = Math.min((double) diff / (double) maxDifference, 1.0);

            return 1 - deviation;
        }
    }
}


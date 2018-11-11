package de.uni_mannheim.informatik.dws.semtec.Recommender.Similarity;

import de.uni_mannheim.informatik.dws.winter.similarity.SimilarityMeasure;


public class MagnitudeSimilarity extends SimilarityMeasure<Long> {

    private static final long serialVersionUID = 1L;
    private int maxDifference;

    public MagnitudeSimilarity(int maxDifference) {
        this.maxDifference = maxDifference;
    }

    // applicable for Attributes Budget | Gross (e.g. maxDifference=2)
    @Override
    public double calculate(Long first, Long second) {

        if (first == 0L || second == 0L) {
            return 0L;
        } else {
            int diff = Math.abs(String.valueOf(first).length()-String.valueOf(second).length());
            double deviation = Math.min((double) diff / (double) maxDifference, 1.0);

            return 1 - deviation;
        }
    }
}


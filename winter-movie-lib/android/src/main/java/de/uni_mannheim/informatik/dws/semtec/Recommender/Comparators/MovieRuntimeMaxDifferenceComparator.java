package de.uni_mannheim.informatik.dws.semtec.Recommender.Comparators;

import de.uni_mannheim.informatik.dws.semtec.Extractor.Movie;
import de.uni_mannheim.informatik.dws.semtec.Recommender.Similarity.MaxDifferenceDeviation;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

public class MovieRuntimeMaxDifferenceComparator implements Comparator<Movie, Attribute>{


    private static final long serialVersionUID = 1L;
    private MaxDifferenceDeviation sim = new MaxDifferenceDeviation(50);

    private ComparatorLogger comparisonLog;

    @Override
    public double compare(
            Movie record1,
            Movie record2,
            Correspondence<Attribute, Matchable> schemaCorrespondences) {

        int rt1 = record1.getRuntime();
        int rt2 = record2.getRuntime();

        double similarity = sim.calculate(rt1, rt2);

        if(this.comparisonLog != null){
            this.comparisonLog.setComparatorName(getClass().getName());

            this.comparisonLog.setRecord1Value(Integer.toString(rt1));
            this.comparisonLog.setRecord2Value(Integer.toString(rt2));

            this.comparisonLog.setSimilarity(Double.toString(similarity));
        }
        return similarity;
    }

    @Override
    public ComparatorLogger getComparisonLog() {
        return this.comparisonLog;
    }

    @Override
    public void setComparisonLog(ComparatorLogger comparatorLog) {
        this.comparisonLog = comparatorLog;
    }

}


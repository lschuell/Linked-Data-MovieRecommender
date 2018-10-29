package de.uni_mannheim.informatik.dws.semtec.Recommender.Comparators;

import de.uni_mannheim.informatik.dws.semtec.Extractor.Movie;
import de.uni_mannheim.informatik.dws.semtec.Recommender.Similarity.MaxDifferenceDeviation;

import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

public class MovieYearMaxDifferenceComparator implements Comparator<Movie, Attribute>{


    private static final long serialVersionUID = 1L;
    private MaxDifferenceDeviation sim = new MaxDifferenceDeviation(20);

    private ComparatorLogger comparisonLog;

    @Override
    public double compare(
            Movie record1,
            Movie record2,
            Correspondence<Attribute, Matchable> schemaCorrespondences) {

        int year1 = record1.getYear();
        int year2 = record2.getYear();

        double similarity = sim.calculate(year1, year2);

        if(this.comparisonLog != null){
            this.comparisonLog.setComparatorName(getClass().getName());

            this.comparisonLog.setRecord1Value(Integer.toString(year1));
            this.comparisonLog.setRecord2Value(Integer.toString(year2));

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


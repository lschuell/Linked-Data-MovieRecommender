package de.uni_mannheim.informatik.dws.semtec.Recommender.Comparators;

import de.uni_mannheim.informatik.dws.semtec.Extractor.Movie;
import de.uni_mannheim.informatik.dws.semtec.Recommender.Similarity.MagnitudeSimilarity;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

public class MovieBudgetMagnitudeComparator implements Comparator<Movie, Attribute>{


    private static final long serialVersionUID = 1L;
    private MagnitudeSimilarity sim = new MagnitudeSimilarity(2);

    private ComparatorLogger comparisonLog;

    @Override
    public double compare(
            Movie record1,
            Movie record2,
            Correspondence<Attribute, Matchable> schemaCorrespondences) {

        long budget1 = record1.getBudget();
        long budget2 = record2.getBudget();

        double similarity = sim.calculate(budget1, budget2);

        if(this.comparisonLog != null){
            this.comparisonLog.setComparatorName(getClass().getName());

            this.comparisonLog.setRecord1Value(Long.toString(budget1));
            this.comparisonLog.setRecord2Value(Long.toString(budget2));

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


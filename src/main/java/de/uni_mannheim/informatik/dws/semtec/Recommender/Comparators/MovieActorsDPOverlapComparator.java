package de.uni_mannheim.informatik.dws.semtec.Recommender.Comparators;

import de.uni_mannheim.informatik.dws.semtec.Extractor.Movie;
import de.uni_mannheim.informatik.dws.semtec.Recommender.Similarity.OverlapSimilarity;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

import java.util.List;

public class MovieActorsDPOverlapComparator implements Comparator<Movie, Attribute>{


    private static final long serialVersionUID = 1L;
    private OverlapSimilarity sim = new OverlapSimilarity();

    private ComparatorLogger comparisonLog;

    @Override
    public double compare(
            Movie record1,
            Movie record2,
            Correspondence<Attribute, Matchable> schemaCorrespondences) {

        List<String> actorsDP1 = record1.getActorsDP();
        List<String> actorsDP2 = record2.getActorsDP();

        double similarity = sim.calculate(actorsDP1, actorsDP2);

        if(this.comparisonLog != null){
            this.comparisonLog.setComparatorName(getClass().getName());

            this.comparisonLog.setRecord1Value(String.join("|", actorsDP1));
            this.comparisonLog.setRecord2Value(String.join("|", actorsDP2));

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


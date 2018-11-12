package de.uni_mannheim.informatik.dws.semtec.Recommender.Models;

import de.uni_mannheim.informatik.dws.semtec.Extractor.CSVMovieReader;
import de.uni_mannheim.informatik.dws.semtec.Extractor.Movie;
import de.uni_mannheim.informatik.dws.semtec.Recommender.Comparators.*;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.rules.LinearCombinationMatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVCorrespondenceFormatter;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;
import org.apache.logging.log4j.Logger;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.NoBlocker;

import java.io.File;
import java.util.*;


public class LinearCombinationRule {

    private static final Logger logger = WinterLogManager.activateLogger("default");

    public static void main(String[] args) throws Exception {
        // loading data
        HashedDataSet<Movie, Attribute> movies = new HashedDataSet<>();
        new CSVMovieReader().loadFromCSV(new File("data/moviesI.csv"), movies);

        // create a matching rule

        LinearCombinationMatchingRule<Movie, Attribute> matchingRule = new LinearCombinationMatchingRule<>(
                0.3);

        // add comparators
        matchingRule.addComparator(new MovieActorsDPOverlapComparator(), 3);
        matchingRule.addComparator(new MovieActorsWikiOverlapComparator(), 3);
        matchingRule.addComparator(new MovieAwardsOverlapComparator(), 3);
        matchingRule.addComparator(new MovieNominationsOverlapComparator(), 2);
        matchingRule.addComparator(new MovieFSKEqualsComparator(), 1);
        matchingRule.addComparator(new MovieDirectorEqualsComparator(), 6);
        matchingRule.addComparator(new MovieComposersOverlapComparator(), 2);
        matchingRule.addComparator(new MovieCompaniesOverlapComparator(), 2);
        matchingRule.addComparator(new MovieDistributorsOverlapComparator(), 2);
        matchingRule.addComparator(new MovieGenresOverlapComparator(), 8);
        matchingRule.addComparator(new MovieLanguagesOverlapComparator(), 3);
        matchingRule.addComparator(new MovieSubjectsOverlapComparator(), 10);
        matchingRule.addComparator(new MovieWritersOverlapComparator(), 2);
        matchingRule.addComparator(new MovieYearMaxDifferenceComparator(), 1);
        matchingRule.addComparator(new MovieProducersOverlapComparator(), 3);
        matchingRule.addComparator(new MovieGrossMagnitudeComparator(), 2);
        matchingRule.addComparator(new MovieStudiosOverlapComparator(), 2);
        matchingRule.normalizeWeights();


        // Initialize Matching Engine
        MatchingEngine<Movie, Attribute> engine = new MatchingEngine<>();

        // create a blocker (blocking strategy)
        NoBlocker<Movie, Attribute> blocker = new NoBlocker<Movie, Attribute>();
        //StandardRecordBlocker<Movie, Attribute> blocker = new StandardRecordBlocker<Movie, Attribute>(new StaticBlockingKeyGenerator<Movie, Attribute>());
        blocker.collectBlockSizeData("data/output/debugBlocking.csv", 1000);

        // get Collection with just one Movie - the Movie we want recommendations for
        Movie testMovie = movies.getRecord("Inception");
        List<Movie> testMovieList = new LinkedList<Movie>();
        testMovieList.add(testMovie);
        HashedDataSet<Movie, Attribute> recommendFor = new HashedDataSet<Movie, Attribute>(testMovieList);

        // Execute the matching
        Processable<Correspondence<Movie, Attribute>> correspondences = engine.runIdentityResolution(
                recommendFor, movies, null, matchingRule, blocker);


        // write the correspondences to the output files
        //new CSVCorrespondenceFormatter().writeCSV(new File("data/output/test_recommendations.csv"), correspondences);

        List<Correspondence<Movie, Attribute>> correspondencesList = (List) correspondences.get();

        Collections.sort(correspondencesList, (Comparator<Correspondence>) (o1, o2) -> -Double.compare(o1.getSimilarityScore(), o2.getSimilarityScore()));

        String[] topResults = new String[10]; int i = -1;

        for (Object elem: correspondencesList) {
            if(i >= topResults.length)
                break;
            // dismiss first entry
            if(i != -1) {
                Correspondence<Movie, Attribute> x = (Correspondence) elem;
                topResults[i] = x.getSecondRecord().getName();
                System.out.println(topResults[i]);
            }
            //System.out.println(x.getFirstRecord() + ", " + x.getSecondRecord() + ", " + x.getSimilarityScore());
            ++i;
        }


    }
}

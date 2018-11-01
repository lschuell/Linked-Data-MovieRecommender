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

import java.io.*;
import java.util.*;


public class PopularMovieRecommendationScores {

    private static final Logger logger = WinterLogManager.activateLogger("default");

    private static HashedDataSet<Movie, Attribute> getPopularMovies () throws IOException {

        HashedDataSet<Movie, Attribute> moviesHD = new HashedDataSet<>();
        new CSVMovieReader().loadFromCSV(new File("data/movies.csv"), moviesHD);

        Collection<Movie> moviesCol = moviesHD.get();
        List<Movie> movieList = new LinkedList<>();
        List<Movie> restList = new LinkedList<>();

        int counter = 0;
        for (Movie m : moviesCol){
            if(m.getGross()>100000 || m.getAwards().size() > 0){
                movieList.add(m);
                counter++;
            }
            else{
                restList.add(m);
            }
        }

        for (Movie m : restList){
            if(m.getNominations().size() > 0){
                movieList.add(m);
                counter++;
                if (counter==10000){
                    break;
                }
            }
        }

        return  new HashedDataSet<>(movieList);
    }

    private static void runFractionalRecommendation(HashedDataSet<Movie, Attribute> allMovies, List<Movie> fraction, int i) throws Exception {

        // create a matching rule

        LinearCombinationMatchingRule<Movie, Attribute> matchingRule = new LinearCombinationMatchingRule<>(
                0.25);

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

        // Execute the matching
        Processable<Correspondence<Movie, Attribute>> correspondences = engine.runIdentityResolution(
                new HashedDataSet<Movie, Attribute>(fraction), allMovies, null, matchingRule, blocker);

        // write the correspondences to the output files
        new CSVCorrespondenceFormatter().writeCSV(new File("data/output/popular_movies/popular_movie_recommendations" + i + ".csv"), correspondences);

    }

    public static void main(String[] args) throws Exception {
        // loading data

        HashedDataSet<Movie, Attribute> popMovs = getPopularMovies();
        System.out.println(popMovs.size());

        /*
        List<List<Movie>> bundle = new ArrayList<>(100);

        for(int i=0; i<100; i++) {
            bundle.add(new ArrayList<>());
        }

        int counter=0;
        for (Movie m : popMovs.get()){
            int bundleIndex = counter/100;
            counter++;
            bundle.get(bundleIndex).add(m);
        }

        for(int i=0; i<bundle.size(); i++){
            runFractionalRecommendation(popMovs, bundle.get(i), i);
        }
        */


        int counter = 0;
        for (Movie m : popMovs.get()){
            List<Movie> movie= new ArrayList<>();
            movie.add(m);
            runFractionalRecommendation(popMovs, movie, counter);
            counter++;
        }

    }
}

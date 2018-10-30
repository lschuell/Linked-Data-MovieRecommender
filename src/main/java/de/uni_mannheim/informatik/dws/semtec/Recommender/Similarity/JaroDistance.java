package de.uni_mannheim.informatik.dws.semtec.Recommender.Similarity;

import de.uni_mannheim.informatik.dws.semtec.Extractor.CSVMovieReader;
import de.uni_mannheim.informatik.dws.semtec.Extractor.Movie;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import org.apache.lucene.search.spell.JaroWinklerDistance;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class JaroDistance {

    private static HashedDataSet<Movie, Attribute> movies = new HashedDataSet<>();

    public JaroDistance() throws IOException {
        new CSVMovieReader().loadFromCSV(new File("data/moviesI.csv"), movies);
    }

    public static List<String> getMovieNamesFromSearch(String name) {
        JaroWinklerDistance jwd = new JaroWinklerDistance();

        Collection<Movie> test = movies.get();

        double distance = 0.0;
        List<String> movieNames = new LinkedList<String>();
        HashMap<String,Double> movieDistances = new HashMap<String,Double>();

        for (Movie m : test) {
            distance = jwd.getDistance(name, m.getName());
            movieDistances.put(m.getName(), distance);
        }

        movieDistances.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .limit(10)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                )).forEach((s, val) -> movieNames.add(s));

        return movieNames;
    }
}
package de.uni_mannheim.informatik.dws.semtec.Extractor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

import static de.uni_mannheim.informatik.dws.semtec.Extractor.FlatMovie.MovieToFlatMovie;

public class JSONMovieWriter {

    public static void main(String[] args) throws IOException {

        Logger logger = LoggerFactory.getLogger(JSONMovieWriter.class.getName());

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

        System.out.println(counter);

        for (Movie m : restList){
            if(m.getNominations().size() > 0){
                movieList.add(m);
                counter++;
                if (counter==10000){
                    break;
                }
            }
        }


        Gson gson = new Gson();
        Type type = new TypeToken<FlatMovie>() {}.getType();

        try(
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File(
                "data/movies.json")))
        ) {
            String json;

            for (Movie m : movieList) {
                json = gson.toJson(MovieToFlatMovie(m), type);
                bw.write(json + "\n");
            }
        }
    }
}

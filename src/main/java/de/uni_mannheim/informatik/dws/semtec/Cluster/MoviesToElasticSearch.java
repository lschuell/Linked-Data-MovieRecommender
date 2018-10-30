package de.uni_mannheim.informatik.dws.semtec.Cluster;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.uni_mannheim.informatik.dws.semtec.Extractor.CSVMovieReader;
import de.uni_mannheim.informatik.dws.semtec.Extractor.Movie;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class CSVtoJSON {

    public static void main(String[] args) throws IOException {


        HashedDataSet<Movie, Attribute> moviesHD = new HashedDataSet<>();
        new CSVMovieReader().loadFromCSV(new File("data/moviesI.csv"), moviesHD);

        Collection<Movie> moviesCol = moviesHD.get();
        List<Movie> movieList = new LinkedList<>();

        int counter = 0;
        for (Movie m : moviesCol) {
            movieList.add(m);
            counter ++;
            if (counter > 20){
                break;
            }
        }

        Gson gson = new Gson();
        Type type = new TypeToken<List<Movie>>() {}.getType();

        String json = gson.toJson(movieList, type);
        System.out.println(json);


        List<Movie> fromJson = gson.fromJson(json, type);

        for (Movie m : fromJson) {
            System.out.println(m);
        }

    }

}


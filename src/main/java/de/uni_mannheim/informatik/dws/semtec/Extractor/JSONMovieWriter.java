package de.uni_mannheim.informatik.dws.semtec.Extractor;

import au.com.bytecode.opencsv.CSVReader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static de.uni_mannheim.informatik.dws.semtec.Extractor.FlatMovie.MovieToFlatMovie;

public class JSONMovieWriter {

    private static Logger logger = LoggerFactory.getLogger(JSONMovieWriter.class.getName());

    private static void writeMovieToJson(HashedDataSet<Movie, Attribute> popMovs, int i) throws IOException, IndexOutOfBoundsException, NullPointerException{

        String filename = "data/output/popular_movies/popular_movie_recommendations" + i + ".csv";
        String line;
        try (
                Reader reader = Files.newBufferedReader(Paths.get(filename));
                CSVReader csvReader = new CSVReader(reader);
                BufferedWriter bw = new BufferedWriter(new FileWriter(new File(
                        "data/movies.json"), true))
        ) {

            String[] input;

            String movieName = null;
            Map<String, Double> map = new HashMap<String, Double>();

            int counter = 0;
            while ((input = csvReader.readNext()) != null) {
                if(counter==0){
                    movieName = input[0];
                    counter++;
                }
                map.put(input[1], Double.parseDouble(input[2]));
            }

            List<String> top10 =
                    map.entrySet().stream()
                            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                            .limit(11)
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new))
                            .keySet()
                            .stream()
                            .collect(Collectors.toList());

            top10.remove(0);
            String topString = String.join("|", top10);

            logger.debug(movieName);
            logger.debug(topString);

            Gson gson = new Gson();
            Type type = new TypeToken<FlatMovie>() {}.getType();

            if(movieName != null) {
                Movie m = popMovs.getRecord(movieName);
                FlatMovie fm = MovieToFlatMovie(m);
                fm.setTop10Recommendations(topString);

                String json = gson.toJson(fm, type);
                bw.write(json + "\n");
            }

        }
    }

    public static void main(String[] args) throws IOException {

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

        HashedDataSet<Movie, Attribute> popMovs = new HashedDataSet<>(movieList);

        for(int i = 0; i<popMovs.size(); i++){
            try {
                writeMovieToJson(popMovs, i);
            }catch (IndexOutOfBoundsException|NullPointerException e){
                e.printStackTrace();
                logger.warn("Skipping incomplete record");
            }
        }

    }
}

package de.uni_mannheim.informatik.dws.semtec.Extractor;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import de.uni_mannheim.informatik.dws.semtec.Recommender.Similarity.OverlapSimilarity;
import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVMatchableReader;
import de.uni_mannheim.informatik.dws.winter.preprocessing.datatypes.ValueNormalizer;
import de.uni_mannheim.informatik.dws.winter.similarity.EqualsSimilarity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CSVMovieReader extends CSVMatchableReader<Movie, Attribute>{


    private ValueNormalizer valueNormalizer;
    private Logger logger;

    public CSVMovieReader() {
        this.valueNormalizer = new ValueNormalizer();
        logger = LoggerFactory.getLogger(CSVMovieReader.class.getName());

    }

    protected void initialiseDataset(DataSet<Movie, Attribute> dataset) {

        dataset.addAttribute(Movie.ABSTRACTPARAGRAPH);
        dataset.addAttribute(Movie.NAME);
        dataset.addAttribute(Movie.RUNTIME);
        dataset.addAttribute(Movie.ACTORSWIKI);
        dataset.addAttribute(Movie.ACTORSDP);
        dataset.addAttribute(Movie.COMPANIES);
        dataset.addAttribute(Movie.DIRECTOR);
        dataset.addAttribute(Movie.BUDGET);
        dataset.addAttribute(Movie.GROSS);
        dataset.addAttribute(Movie.YEAR);
        dataset.addAttribute(Movie.NOMINATIONS);
        dataset.addAttribute(Movie.AWARDS);
        dataset.addAttribute(Movie.GENRES);
        dataset.addAttribute(Movie.CINEMATOGRAPHY);
        dataset.addAttribute(Movie.COMPOSERS);
        dataset.addAttribute(Movie.WRITERS);
        dataset.addAttribute(Movie.DISTRIBUTORS);
        dataset.addAttribute(Movie.LANGUAGES);
        dataset.addAttribute(Movie.COUNTRIES);
        dataset.addAttribute(Movie.FSK);
        dataset.addAttribute(Movie.SUBJECTS);
        dataset.addAttribute(Movie.PRODUCERS);
        dataset.addAttribute(Movie.STUDIOS);

    }

    @Override
    protected void readLine(File file, int rowNumber, String[] values, DataSet<Movie, Attribute> dataset) {


        //logger.debug(Integer.toString(values.length) + " LINE: " + String.join("<>", values));

        if(rowNumber == 0){
            initialiseDataset(dataset);
        }

        else {
            if (values.length == 18 || values.length == 24) {
                //Movie m = new Movie("Movie " + rowNumber, file.getAbsolutePath());
                Movie m = new Movie(values[0], file.getAbsolutePath());

                m.setName(values[0]);
                m.setBudget(parseLong(values[2]));

                m.setGross(parseLong(values[3]));
                m.setRuntime(parseRuntime(values[4]));
                m.setDirector(parseString(values[5]));
                m.setProducers(parseList(values[6]));
                m.setCinematography(parseString(values[7]));
                m.setDistributors(parseList(values[8]));
                m.setSubjects(parseList(values[9]));
                m.setStudios(parseList(values[10]));
                m.setWriters(parseList(values[11]));
                m.setComposers(parseList(values[12]));
                m.setCompanies(parseList(values[13]));
                m.setLanguages(parseList(values[14]));
                m.setCountries(parseList(values[15]));
                m.setActorsDP(parseList(values[16]));
                m.setAbstractParagraph(parseString(values[17]));


                if (values.length > 18) {
                    m.setYear(parseYear(values[18]));
                    m.setGenres(parseList(values[19]));
                    m.setFsk(parseString(values[20]));
                    m.setAwards(parseList(values[21]));
                    m.setNominations(parseList(values[22]));
                    m.setActorsWiki(parseList(values[23]));
                }

                dataset.add(m);

            }else{
                logger.debug("Unacceptable Length: " + Integer.toString(values.length));
            }
        }

    }

    public LinkedList<String> parseList (String input){
        LinkedList<String> list = new LinkedList<>();
        if(input.equals("Null")){
            return list;
        }
        list.addAll(Arrays.asList(input.split("\\|")));
        return list;
    }

    public String parseString(String input){
        if(input.equals("Null")){
            return null;
        }
        return input;
    }

    public int parseYear(String year){
        int num = 0;
        if (!year.equals("Null")){
            String clean = year.replaceAll("^([0-9]+).*", "$1");
            try{
                num = Integer.parseInt(clean);
                return num;
            }catch(NumberFormatException e){
                e.printStackTrace();
            }
        }
        return num;
    }

    public int parseRuntime(String runtimes){
        int num = 0;
        if (!runtimes.equals("Null")){
            String runtime = runtimes.replaceAll("^-?([0-9]+).*", "$1");
            try {
                num = Integer.parseInt(runtime) / 60;
                return num;
            }catch(NumberFormatException e){
                e.printStackTrace();
            }
        }
        return num;
    }

    public long parseLong(String budget){
        long num = 0L;
        if (!budget.equals("Null")) {
            if (budget.contains("dbpedia.org/datatype/")) {
                String currency = budget.replaceAll(".*dbpedia\\.org/datatype/(.*)", "$1");

                if (currency.equals("usDollar") || currency.equals("euro")) {
                    try {
                        num = Long.parseLong(budget.replaceAll("([0-9]+).*", "$1"));
                        return num;
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                String[] nums = budget.split("\\s");
                try {
                    double fine = Double.parseDouble(nums[0]);
                    double base = Math.pow(10, Double.parseDouble(nums[1]));
                    num = new Double(base * fine).longValue();
                    return num;
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        }
        return num;
    }

    public static void main(String[] args) throws IOException {


        HashedDataSet<Movie, Attribute> movies = new HashedDataSet<>();
        new CSVMovieReader().loadFromCSV(new File("data/moviesI.csv"), movies);

        Collection<Movie> test = movies.get();

        int counter = 0;


        for (Movie m : test) {
            System.out.println("++++++++ Movie " + counter +  " +++++++++++");
            System.out.println(m.getIdentifier());
            System.out.println(m.getName());
            System.out.println(String.join("|", m.getActorsWiki()));
            System.out.println(String.join("|", m.getActorsDP()));
            System.out.println(String.join("|", m.getLanguages()));
            //System.out.println(String.join("|", m.getComposers()));
            //System.out.println(String.join("|", m.getNominations()));
            //System.out.println(String.join("|", m.getAwards()));
            //System.out.println(m.getAbstractParagraph());
            //System.out.println(m.getYear());

            System.out.println();


            counter ++;

            if (counter > 100){
                break;
            }

        }

        System.out.println(movies.size());

        Movie m = movies.getRecord("Inception");
        System.out.println(m.getAbstractParagraph());
        System.out.println(String.join("|", m.getActorsDP()));
        System.out.println(String.join("|",m.getActorsWiki()));
        System.out.println(String.join("|",m.getLanguages()));
        System.out.println(String.join("|",m.getComposers()));
        System.out.println(String.join("|",m.getNominations()));
        System.out.println(String.join("|",m.getAwards()));
        System.out.println(String.join("|",m.getAwards()));
        System.out.println(m.getDirector());

        OverlapSimilarity sim = new OverlapSimilarity();
        System.out.println(sim.calculate(m.getGenres(), m.getGenres()));
        System.out.println(sim.calculate(m.getActorsDP(), m.getActorsDP()));
        System.out.println(sim.calculate(m.getActorsWiki(), m.getActorsWiki()));
        System.out.println(sim.calculate(m.getActorsWiki(), m.getActorsDP()));

        EqualsSimilarity<String> sim2 = new EqualsSimilarity<String>();

        System.out.println(sim2.calculate(m.getDirector(), m.getDirector()));
        System.out.println(sim2.calculate(null, null));
        System.out.println(sim2.calculate(String.join("|",m.getActorsDP()), String.join("|",m.getActorsDP())));








    }
}

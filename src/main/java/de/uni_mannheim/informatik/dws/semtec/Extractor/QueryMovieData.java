package de.uni_mannheim.informatik.dws.semtec.Extractor;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import org.apache.jena.query.*;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CountDownLatch;


public class QueryMovieData {
    private static final String ENDDP = "http://dbpedia.org/sparql";
    private static final String ENDWIKI = "https://query.wikidata.org/sparql";

    private static final String PREFIXES = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                                            "PREFIX schema: <http://schema.org/>\n" +
                                            "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                                            "PREFIX dbo: <http://dbpedia.org/ontology/>\n" +
                                            "PREFIX dbp: <http://dbpedia.org/property/>\n" +
                                            "PREFIX dct: <http://purl.org/dc/terms/>\n" +
                                            "PREFIX foaf: <http://xmlns.com/foaf/0.1/> \n" +
                                            "PREFIX wd: <http://www.wikidata.org/entity/> \n" +
                                            "PREFIX wdt: <http://www.wikidata.org/prop/direct/> \n" +
                                            "PREFIX wikibase: <http://wikiba.se/ontology#> \n" +
                                            "PREFIX bd: <http://www.bigdata.com/rdf#> \n" +
                                            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n";


    // Attributes - DBPEDIA
    private static final String BUDGET = "budget";
    private static final String GROSS = "gross";
    private static final String LINK = "link";
    private static final String NAMES = "names";
    private static final String PRODUCERS = "producers";
    private static final String CINEMATOGRAPHY = "cinematography";
    private static final String ABSTRACT = "abstract";
    private static final String DISTRIBUTORS = "distributors";
    private static final String SUBJECTS = "subjects";
    private static final String STUDIOS = "studios";
    private static final String WRITERS = "writers";
    private static final String DIRECTOR = "director";
    private static final String COMPOSERS = "composers";
    private static final String COMPANIES = "companies";
    private static final String RUNTIMES = "runtimes";
    private static final String LANGUAGES = "languages";
    private static final String COUNTRIES = "countries";

    // Attributes - WIKIDATA
    private static final String NOMINATIONS = "nominations";
    private static final String AWARDS = "awards";
    private static final String FSK = "FSKLabel";
    private static final String YEAR = "year";
    private static final String GENRES = "genres";

    // Both
    private static final String ACTORS = "actors";

    private static final String[] DBATTRS = {NAMES, LINK, BUDGET, GROSS, RUNTIMES, DIRECTOR, PRODUCERS, CINEMATOGRAPHY, DISTRIBUTORS,
                                            SUBJECTS, STUDIOS, WRITERS, COMPOSERS, COMPANIES, LANGUAGES, COUNTRIES, ACTORS, ABSTRACT};

    private static final String[] WIKIATTRS = {YEAR, GENRES, FSK, AWARDS, NOMINATIONS, ACTORS};

    private static String DBpediaQuery(String URI) {
        String queryString = PREFIXES +
                            "PREFIX mov:<" + URI + ">\n" +
                            "SELECT ?budget ?director ?link ?cinematography ?gross \n" +
                            "(GROUP_CONCAT(DISTINCT ?name;separator='|') AS ?names) \n" +
                            "(GROUP_CONCAT(DISTINCT ?runtime;separator='|') AS ?runtimes) \n" +
                            "(GROUP_CONCAT(DISTINCT ?country;separator='|') AS ?countries) \n" +
                            "(GROUP_CONCAT(DISTINCT ?writer;separator='|') AS ?writers) \n" +
                            "(GROUP_CONCAT(DISTINCT ?starring;separator='|') AS ?actors) \n" +
                            "(GROUP_CONCAT(DISTINCT ?language;separator='|') AS ?languages) \n" +
                            "(GROUP_CONCAT(DISTINCT ?distributor;separator='|') AS ?distributors) \n" +
                            "(GROUP_CONCAT(DISTINCT ?composer;separator='|') AS ?composers) \n" +
                            "(GROUP_CONCAT(DISTINCT ?producer;separator='|') AS ?producers) \n" +
                            "(GROUP_CONCAT(DISTINCT ?subject;separator='|') AS ?subjects) \n" +
                            "(GROUP_CONCAT(DISTINCT ?prodcompany;separator='|') AS ?companies) \n" +
                            "(GROUP_CONCAT(DISTINCT ?studio;separator='|') AS ?studios) ?abstract \n" +

                            "WHERE {" +
                                "mov: rdf:type schema:Movie. \n" +
                                "OPTIONAL {mov: dbo:budget ?budget.} \n" +
                                "OPTIONAL {mov: dbo:cinematography ?cinematography.} \n" +
                                "OPTIONAL {mov: dbo:director ?director.} \n" +
                                "OPTIONAL {mov: dbo:distributor ?distributor.} \n" +
                                "OPTIONAL {mov: dbp:studio ?studio. FILTER(regex(?studio,'resource'))} \n" +
                                "OPTIONAL {mov: owl:sameAs ?link. FILTER(regex(?link,'wikidata'))} \n" +
                                "OPTIONAL {mov: dbo:gross ?gross.} \n" +
                                "OPTIONAL {mov: dbo:writer ?writer.} \n" +
                                "OPTIONAL {mov: dbp:country ?country.} \n" +
                                "OPTIONAL {mov: dbo:musicComposer ?composer.} \n" +
                                "OPTIONAL {mov: dbp:productionCompanies ?prodcompany.} \n" +
                                "OPTIONAL {mov: dbo:starring ?starring.} \n" +
                                "OPTIONAL {mov: dbp:language ?language.} \n" +
                                "OPTIONAL {mov: dct:subject ?subject.} \n" +
                                "OPTIONAL {mov: dbo:producer ?producer.} \n" +
                                "OPTIONAL {mov: dbo:runtime ?runtime.} \n" +
                                "OPTIONAL {mov: foaf:name ?name. FILTER (lang(?name) = 'en')} \n" +
                                "OPTIONAL {mov: dbo:abstract ?abstract. FILTER (lang(?abstract) = 'en')} \n" +
                            "} \n" +

                            "group by ?budget ?link ?director ?cinematography ?gross ?abstract \n" +
                            "LIMIT 1";

        return queryString;
    }

    private static String WikiDataQuery(String ID) {
        String queryString = PREFIXES +
                "SELECT ?m ?year ?FSKLabel (GROUP_CONCAT(DISTINCT(?genreLabel);separator='|') AS ?genres) \n" +
                "(GROUP_CONCAT(DISTINCT(?actorLabel);separator='|') AS ?actors) \n" +
                "(GROUP_CONCAT(DISTINCT(?nominationLabel);separator='|') AS ?nominations) \n" +
                "(GROUP_CONCAT(DISTINCT(?awardLabel);separator='|') AS ?awards) \n" +

                "WHERE \n" +
                "{ \n" +
                    "BIND(wd:" + ID + " AS ?m) \n" +
                    "OPTIONAL {?m wdt:P136 ?genre .} \n" +
                    "OPTIONAL {?m wdt:P577 ?date .} \n" +
                    "OPTIONAL {?m wdt:P161 ?actor .} \n" +
                    "OPTIONAL {?m wdt:P1411 ?nomination .} \n" +
                    "OPTIONAL {?m wdt:P166 ?award .} \n" +
                    "OPTIONAL {?m wdt:P1981 ?FSK .} \n" +
                    "BIND(str(YEAR(?date)) AS ?year) \n" +

                    "SERVICE wikibase:label { bd:serviceParam wikibase:language 'en'. \n" +
                        "?date rdfs:label ?dateLabel . \n" +
                        "?genre rdfs:label ?genreLabel . \n" +
                        "?actor rdfs:label ?actorLabel . \n" +
                        "?nomination rdfs:label ?nominationLabel . \n" +
                        "?FSK rdfs:label ?FSKLabel. \n" +
                        "?award rdfs:label ?awardLabel. } \n" +
                "} \n" +
                "GROUP BY ?m ?year ?FSKLabel \n" +
                "LIMIT 1 \n";


        return queryString;
    }


    public static String DBpediaParseExp(String toParse){
        toParse = toParse.replaceAll("(\\d+\\.\\d+)E(\\d+).*", "$1 $2");
        toParse =  toParse.replaceAll("\\r\\n|\\r|\\n|\\*|\\t|,", "");
        return toParse.trim();

    }

    public static String DBpediaParseResource(String toParse){
        toParse = toParse.replaceAll("(http://www\\.wikidata\\.org/entity/|http://(wikidata\\.)?dbpedia\\.org/resource/)(.*)","$3");
        return Cleaner(toParse);
    }

    public static String DBpediaParseResourceList(String toParse){
        String[] resources = toParse.split("\\|");
        List<String> parsed = new ArrayList<String>();
        for (int i=0; i<resources.length; i++){
            parsed.add(DBpediaParseResource(resources[i]));
        }
        return String.join("|", parsed);
    }

    public static String Cleaner(String toParse){
        toParse = toParse.replaceAll("\\r\\n|\\r|\\n|\\*|\\t|,+", " ");
        toParse = toParse.replaceAll("\\s+", " ");
        return toParse.trim();
    }

    private static String ParseAny(String val, String var){
        switch (var) {
            case BUDGET:
            case GROSS:
                val = DBpediaParseExp(val);
                break;
            case PRODUCERS:
            case ACTORS:
            case AWARDS:
            case NOMINATIONS:
            case GENRES:
            case DISTRIBUTORS:
            case WRITERS:
            case COMPOSERS:
            case COMPANIES:
            case STUDIOS:
            case SUBJECTS:
            case NAMES:
                val = DBpediaParseResourceList(val);
                break;
            case DIRECTOR:
            case LINK:
            case CINEMATOGRAPHY:
                val = DBpediaParseResource(val);
                break;
            case LANGUAGES:
            case COUNTRIES:
            case ABSTRACT:
            case FSK:
            case YEAR:
                val = Cleaner(val);
                break;
        }
        return val;
    }

    public static void main(String[] args) {
        QueryMovieData qmd = new QueryMovieData();

        try {
            qmd.run();
        }catch(InterruptedException e){
            e.printStackTrace();
        }

    }

    public void run() throws InterruptedException{

        Logger logger = LoggerFactory.getLogger(QueryMovieData.class.getName());


        int numTotal = 111938;
        int numLatches = 20;

        List<List<String>> URILatchLists = new ArrayList<List<String>>();

        for(int i =0; i<numLatches; i++){
            URILatchLists.add(new ArrayList<String>());
        }

        try (
                Reader reader = Files.newBufferedReader(Paths.get("data/movieURIs.csv"));
                CSVReader csvReader = new CSVReader(reader);
        ) {
            String[] URI;
            int counter = 0;
            while ((URI = csvReader.readNext()) != null && counter < numTotal) {
                String uri = URI[0];
                int latchIndex = counter % numLatches;
                URILatchLists.get(latchIndex).add(uri);
                //logger.debug(uri);
                counter++;
            }
        }catch(IOException e){
            e.printStackTrace();
        }


        for (int i=0; i<URILatchLists.size(); i++){
            List<String> currentLatch = URILatchLists.get(i);

            runLatch(currentLatch, 5);
            Thread.sleep(1000*60*2);
        }

    }

    private void runLatch(List<String> currentURIs, int numThreads){

        Logger logger = LoggerFactory.getLogger(QueryMovieData.class.getName());

        List<List<String>> URIThreadLists = new ArrayList<List<String>>();

        for(int i =0; i<numThreads; i++){
            URIThreadLists.add(new ArrayList<String>());
        }

        String uri;

        for(int counter = 0; counter < currentURIs.size(); counter++) {
            uri = currentURIs.get(counter);
            int threadIndex = counter % numThreads;
            URIThreadLists.get(threadIndex).add(uri);
        }


        CountDownLatch latch = new CountDownLatch(numThreads);

        RunnableMovieDataFraction[] runnables = new RunnableMovieDataFraction[numThreads];
        for(int i=0; i<runnables.length; i++) {
            String Tname = "Thread " + Integer.toString(i + 1);

            runnables[i] = new RunnableMovieDataFraction(Tname, URIThreadLists.get(i), latch);
            runnables[i].start();
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            logger.error("Application got interrupted", e);
        } finally {
            logger.info("Processing latch is done");
        }
    }

    class RunnableMovieDataFraction implements Runnable {
        private Thread t;
        private String Tname;
        private List<String> MovieURIs;
        private Logger logger;
        private CountDownLatch latch;

        RunnableMovieDataFraction(String Tname, List<String> MovieURIs, CountDownLatch latch) {
            this.Tname = Tname;
            this.MovieURIs = MovieURIs;
            this.latch = latch;
            this.logger = LoggerFactory.getLogger(RunnableMovieDataFraction.class.getName());
            logger.info("Creating Runnable " +  Tname);

        }

        public void run() {

            logger.info("Running " +  Tname );

            try{
                String URI = null;
                for(int i=0; i<MovieURIs.size(); i++){
                    URI = MovieURIs.get(i);
                    if(i % 100 == 0){
                        Thread.sleep(5*1000);
                    }
                    try{
                        writeAMovie(URI,i+1);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                latch.countDown();
            }
            logger.info("Thread " + Tname + " exiting.");
        }

        public void start () {
            logger.info("Starting " +  Tname );
            if (t == null) {
                t = new Thread (this, Tname);
                t.start ();
            }
        }

        public void writeAMovie(String URI, int n) throws QueryParseException, IOException {

            String queryString = DBpediaQuery(URI);

            Query query = QueryFactory.create(queryString);
            QueryExecution qexecution = QueryExecutionFactory.sparqlService(ENDDP, query);
            ((QueryEngineHTTP) qexecution).addParam("timeout", "10000");

            ResultSet rs = qexecution.execSelect();

            if (rs.hasNext()) {
                QuerySolution qs = rs.next();
                Iterator<String> attributes = qs.varNames();

                List<String> values = new ArrayList<>();
                List<String> fromWiki;
                String ID = null;

                Map<String, String> var2val = new LinkedHashMap<>();

                for(int i=0; i<DBATTRS.length; i++){
                    var2val.put(DBATTRS[i], "Null");
                }

                while (attributes.hasNext()) {
                    String var = attributes.next();
                    String val = qs.get(var).toString();

                    String toAdd = ParseAny(val, var);

                    if (toAdd.equals("")){
                        toAdd = "Null";
                    }

                    var2val.replace(var, toAdd);


                    if(var.equals(LINK)){
                        ID = toAdd;
                    }

                    logger.info(Tname + " - DBpedia " + n + " [" + var + "]: " + toAdd);
                }

                values.addAll(var2val.values());

                if(ID != null){
                    fromWiki = extractWikiMovie(ID,n);
                    values.addAll(fromWiki);
                }

                if(!values.isEmpty()){
                    try (
                            Writer writer = new FileWriter(new File("data/movies.csv"), true);
                            CSVWriter csvWriter = new CSVWriter(writer);
                    ) {
                            csvWriter.writeNext(values.toArray(new String[0]));
                    }
                }

                qexecution.close();
            }
        }

        private List<String> extractWikiMovie(String ID, int n) throws QueryParseException{
            String queryString = WikiDataQuery(ID);

            Query query = QueryFactory.create(queryString);
            QueryExecution qexecution = QueryExecutionFactory.sparqlService(ENDWIKI, query);
            ((QueryEngineHTTP) qexecution).addParam("timeout", "10000");

            ResultSet rs = qexecution.execSelect();

            Map<String, String> var2val = new LinkedHashMap<>();
            for(int i=0; i<WIKIATTRS.length; i++){
                var2val.put(WIKIATTRS[i], "Null");
            }

            List<String> fromWiki = new ArrayList<String>();

            if (rs.hasNext()) {
                QuerySolution qs = rs.next();
                Iterator<String> attributes = qs.varNames();

                while (attributes.hasNext()) {
                    String var = attributes.next();
                    String val = qs.get(var).toString();

                    String toAdd = ParseAny(val, var);

                    if(toAdd.equals("")){
                        toAdd = "Null";
                    }

                    var2val.replace(var, toAdd);


                    logger.info(Tname + " - Wikidata " + n + " [" + var + "]: " + val);
                }
                fromWiki.addAll(var2val.values());
            }
            qexecution.close();
            return fromWiki;
        }
    }
}


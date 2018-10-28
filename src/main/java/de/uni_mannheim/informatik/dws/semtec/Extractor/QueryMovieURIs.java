package de.uni_mannheim.informatik.dws.semtec.Extractor;

import org.apache.jena.query.*;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import au.com.bytecode.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class QueryMovieURIs {

    private static final String EndDP = "http://dbpedia.org/sparql";
    private static final String prefixes = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
            "PREFIX schema: <http://schema.org/>\n";


    private String QgetMovies(int offset, int limit) {
        String queryString = prefixes + "SELECT ?m where { \n" +
                "?m rdf:type schema:Movie.\n" +
                "}\n" +
                //"ORDER BY ?m\n" +
                "LIMIT " + Integer.toString(limit) + " OFFSET " + Integer.toString(offset);
        return queryString;
    }

    private String QgetMovieCount() {
        String queryString = prefixes + "SELECT COUNT(DISTINCT ?m) where { \n" +
                "?m rdf:type schema:Movie.\n" +
                "}";
        return queryString;

    }

    public static void main(String[] args) {
        QueryMovieURIs qmu = new QueryMovieURIs();
        qmu.run();
    }

    private void run(){

        Logger logger = LoggerFactory.getLogger(QueryMovieURIs.class.getName());

        // JENA-SPARQL ENDPOINT DOES NOT SUPPORT COUNT - ENTER NUMBER MANUALLY INSTEAD
        /*
        String queryString = QgetMovieCount();

        Query query = QueryFactory.create(queryString);
        QueryExecution qexecution = QueryExecutionFactory.sparqlService(EndDP, query);
        ((QueryEngineHTTP) qexecution).addParam("timeout", "10000");
        String val = null;

        try {
            ResultSet rs = qexecution.execSelect();

            while (rs.hasNext()) {
                QuerySolution qs = rs.next();
                Iterator<String> itVars = qs.varNames();

                while (itVars.hasNext()) {
                    String var = itVars.next();
                    val = qs.get(var).toString();
                }
            }
            numMovies = Integer.parseInt(val);
        }catch (QueryParseException|NumberFormatException e){
            e.printStackTrace();
        }
        */

        int numMovies = 111938;

        int numThreads = numMovies/10000 + 1;

        CountDownLatch latch = new CountDownLatch(numThreads);

        RunnableMovieURIFraction[] runnables = new RunnableMovieURIFraction[numThreads];
        for(int i=0; i<runnables.length; i++) {
            String Tname = "Thread " + Integer.toString(i + 1);
            int limit = 10000;
            runnables[i] = new RunnableMovieURIFraction(Tname, limit, limit*(i), latch);
            runnables[i].start();
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            logger.error("Application got interrupted", e);
        } finally {
            logger.info("Processing is done");
        }
    }

    class RunnableMovieURIFraction implements Runnable {
        private Thread t;
        private String Tname;
        private int limit;
        private int offset;
        private Logger logger;
        private CountDownLatch latch;

        RunnableMovieURIFraction(String Tname, int limit, int offset, CountDownLatch latch) {
            this.Tname = Tname;
            this.limit = limit;
            this.offset = offset;
            this.latch = latch;
            this.logger = LoggerFactory.getLogger(RunnableMovieURIFraction.class.getName());
            logger.info("Creating Runnable " +  Tname);
        }

        public void run() {

            logger.info("Running " +  Tname );

            String queryString = QgetMovies(offset, limit);

            try{
                Query query = QueryFactory.create(queryString);
                QueryExecution qexecution = QueryExecutionFactory.sparqlService(EndDP, query);
                ((QueryEngineHTTP) qexecution).addParam("timeout", "10000");
                int counter = 0;

                ResultSet rs = qexecution.execSelect();

                while (rs.hasNext()) {
                    QuerySolution qs = rs.next();
                    Iterator<String> itVars = qs.varNames();
                    counter++;

                    List<String> vals = new ArrayList<String>();
                    while (itVars.hasNext()) {
                        String var = itVars.next();
                        String val = qs.get(var).toString();
                        vals.add(val);
                        logger.info("Result " + (offset + counter) + "[" + var + "]: " + val);
                    }
                    try (
                            Writer writer = new FileWriter(new File("data/movieURIs.csv"), true);
                            CSVWriter csvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
                    ) {
                            csvWriter.writeNext(vals.toArray(new String[0]));
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
    }
}


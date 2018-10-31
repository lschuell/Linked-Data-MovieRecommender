package de.uni_mannheim.informatik.dws.semtec.TFIDF;

import de.uni_mannheim.informatik.dws.semtec.Recommender.Similarity.JaroDistance;

import java.io.IOException;

public class JaroDistanceExample {
    public static void main(String[] args) {

        try {
            JaroDistance jd = new JaroDistance();
            System.out.println("inception: " + jd.getMovieNamesFromSearch("Inception"));
            System.out.println("Harry Potter: " + jd.getMovieNamesFromSearch("Harry Potter"));
        } catch (IOException e) {
            System.out.println(e);
        }

    }
}

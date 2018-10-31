package de.uni_mannheim.informatik.dws.semtec.Extractor;

import java.io.Serializable;
import java.util.List;

public class FlatMovie implements Serializable {

    private long budget;
    private long gross;
    private String name;
    private String cinematography;
    private String abstractParagraph;
    private String director;
    private int runtime;
    private String fsk;
    private int year;

    private String genres;
    private String actorsWiki;
    private String actorsDP;
    private String producers;
    private String distributors;
    private String subjects;
    private String studios;
    private String writers;
    private String composers;
    private String companies;
    private String languages;
    private String countries;
    private String nominations;
    private String awards;


    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }

    public void setCinematography(String cinematography) { this.cinematography = cinematography; }

    public String getCinematography() {
        return cinematography;
    }

    public void setAbstractParagraph(String abstractParagraph) { this.abstractParagraph = abstractParagraph; }

    public String getAbstractParagraph() {
        return abstractParagraph;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) { this.director = director; }

    public String getFsk() {
        return fsk;
    }

    public void setFsk(String fsk) { this.fsk = fsk; }

    public String getProducers() {
        return producers;
    }

    public void setProducers(String producers) {
        this.producers = producers;
    }

    public String getDistributors() {
        return distributors;
    }

    public void setDistributors(String distributors) {
        this.distributors = distributors;
    }

    public String getSubjects() {
        return subjects;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }

    public String getStudios() {
        return studios;
    }

    public void setStudios(String studios) {
        this.studios = studios;
    }

    public String getWriters() {
        return writers;
    }

    public void setWriters(String writers) {
        this.writers = writers;
    }

    public String getComposers() {
        return composers;
    }

    public void setComposers(String composers) {
        this.composers = composers;
    }

    public String getCompanies() {
        return companies;
    }

    public void setCompanies(String companies) {
        this.companies = companies;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public String getCountries() {
        return countries;
    }

    public void setCountries(String countries) {
        this.countries = countries;
    }

    public String getNominations() {
        return nominations;
    }

    public void setNominations(String nominations) {
        this.nominations = nominations;
    }

    public String getAwards() {
        return awards;
    }

    public void setAwards(String awards) {
        this.awards = awards;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getActorsWiki() {
        return actorsWiki;
    }

    public void setActorsWiki(String actorsWiki) {
        this.actorsWiki = actorsWiki;
    }

    public String getActorsDP() {
        return actorsDP;
    }

    public void setActorsDP(String actorsDP) {
        this.actorsDP = actorsDP;
    }

    public long getGross() { return gross; }

    public void setGross(long gross) {
        this.gross = gross;
    }

    public long getBudget() { return budget; }

    public void setBudget(long budget) { this.budget = budget; }

    public int getRuntime() { return runtime; }

    public void setRuntime(int runtime) { this.runtime = runtime; }

    public int getYear() { return year; }

    public void setYear(int year) { this.year = year; }


    public static FlatMovie MovieToFlatMovie(Movie m){
        FlatMovie fm = new FlatMovie();
        fm.setBudget(m.getBudget());
        fm.setGross(m.getGross());
        fm.setName(m.getName());
        fm.setCinematography(m.getCinematography());
        fm.setAbstractParagraph(m.getAbstractParagraph());
        fm.setDirector(m.getDirector());
        fm.setRuntime(m.getRuntime());
        fm.setFsk(m.getFsk());
        fm.setYear(m.getYear());
        fm.setGenres(ListToString(m.getGenres()));
        fm.setActorsWiki(ListToString(m.getActorsWiki()));
        fm.setActorsDP(ListToString(m.getActorsDP()));
        fm.setProducers(ListToString(m.getProducers()));
        fm.setDistributors(ListToString(m.getDistributors()));
        fm.setSubjects(ListToString(m.getSubjects()));
        fm.setStudios(ListToString(m.getStudios()));
        fm.setWriters(ListToString(m.getWriters()));
        fm.setComposers(ListToString(m.getComposers()));
        fm.setCompanies(ListToString(m.getCompanies()));
        fm.setLanguages(ListToString(m.getLanguages()));
        fm.setCountries(ListToString(m.getCountries()));
        fm.setNominations(ListToString(m.getNominations()));
        fm.setAwards(ListToString(m.getAwards()));

        return fm;
    }

    private static String ListToString(List<String> list){
        return String.join(" | ", list);
    }

    @Override
    public String toString() {
        return String.format("Movie: %s / %s / %s ]", getName(), getYear(),
                getDirector());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof FlatMovie){
            return this.getName().equals(((FlatMovie) obj).getName());
        }else
            return false;
    }

}

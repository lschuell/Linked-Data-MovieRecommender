package de.uni_mannheim.informatik.dws.semtec.Extractor;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

public class Movie extends AbstractRecord<Attribute> implements Serializable {

    private String id;
    private String provenance;
    private long budget;
    private long gross;
    private String name;
    private List<String> producers;
    private String cinematography;
    private String abstractParagraph;
    private List<String> distributors;
    private List<String> subjects;
    private List<String> studios;
    private List<String> writers;
    private String director;
    private List<String> composers;
    private List<String> companies;
    private int runtime;
    private List<String> languages;
    private List<String> countries;
    private List<String> nominations;
    private List<String> awards;
    private String fsk;
    private int year;
    private List<String> genres;
    private List<String> actorsWiki;
    private List<String> actorsDP;


    public Movie(String identifier, String provenance) {
        id = identifier;
        this.provenance = provenance;
        producers = new LinkedList<>();
        distributors = new LinkedList<>();
        subjects = new LinkedList<>();
        studios = new LinkedList<>();
        writers = new LinkedList<>();
        composers = new LinkedList<>();
        companies = new LinkedList<>();
        languages = new LinkedList<>();
        countries = new LinkedList<>();
        nominations = new LinkedList<>();
        awards = new LinkedList<>();
        genres = new LinkedList<>();
        actorsWiki = new LinkedList<>();
        actorsDP = new LinkedList<>();
    }

    @Override
    public String getIdentifier() {
        return id;
    }

    public void setIdentifier(String id){ this.id = id; }

    @Override
    public String getProvenance() {
        return provenance;
    }

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

    public List<String> getProducers() {
        return producers;
    }

    public void setProducers(List<String> producers) {
        this.producers = producers;
    }

    public List<String> getDistributors() {
        return distributors;
    }

    public void setDistributors(List<String> distributors) {
        this.distributors = distributors;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }

    public List<String> getStudios() {
        return studios;
    }

    public void setStudios(List<String> studios) {
        this.studios = studios;
    }

    public List<String> getWriters() {
        return writers;
    }

    public void setWriters(List<String> writers) {
        this.writers = writers;
    }

    public List<String> getComposers() {
        return composers;
    }

    public void setComposers(List<String> composers) {
        this.composers = composers;
    }

    public List<String> getCompanies() {
        return companies;
    }

    public void setCompanies(List<String> companies) {
        this.companies = companies;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    public List<String> getNominations() {
        return nominations;
    }

    public void setNominations(List<String> nominations) {
        this.nominations = nominations;
    }

    public List<String> getAwards() {
        return awards;
    }

    public void setAwards(List<String> awards) {
        this.awards = awards;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public List<String> getActorsWiki() {
        return actorsWiki;
    }

    public void setActorsWiki(List<String> actorsWiki) {
        this.actorsWiki = actorsWiki;
    }

    public List<String> getActorsDP() {
        return actorsDP;
    }

    public void setActorsDP(List<String> actorsDP) {
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


    public static final Attribute BUDGET = new Attribute("budget");
    public static final Attribute GROSS = new Attribute("gross");
    public static final Attribute NAME = new Attribute("name");
    public static final Attribute PRODUCERS = new Attribute("producers");
    public static final Attribute CINEMATOGRAPHY = new Attribute("cinematography");
    public static final Attribute ABSTRACTPARAGRAPH = new Attribute("abstractParagraph");
    public static final Attribute DISTRIBUTORS = new Attribute("distributors");
    public static final Attribute SUBJECTS = new Attribute("subjects");
    public static final Attribute STUDIOS = new Attribute("studios");
    public static final Attribute WRITERS = new Attribute("writers");
    public static final Attribute DIRECTOR = new Attribute("director");
    public static final Attribute COMPOSERS = new Attribute("composers");
    public static final Attribute COMPANIES = new Attribute("companies");
    public static final Attribute RUNTIME = new Attribute("runtime");
    public static final Attribute LANGUAGES = new Attribute("languages");
    public static final Attribute COUNTRIES = new Attribute("countries");
    public static final Attribute NOMINATIONS = new Attribute("nominations");
    public static final Attribute AWARDS = new Attribute("awards");
    public static final Attribute FSK = new Attribute("fsk");
    public static final Attribute YEAR = new Attribute("year");
    public static final Attribute GENRES = new Attribute("genres");
    public static final Attribute ACTORSWIKI = new Attribute("actorsWiki");
    public static final Attribute ACTORSDP = new Attribute("actorsDP");

    @Override
    public boolean hasValue(Attribute attribute) {
        if(attribute==NAME)
            return getName() != null;
        else if(attribute==BUDGET)
            return getBudget() != 0L;
        else if(attribute==GROSS)
            return getGross() != 0L;
        else if(attribute==YEAR)
            return getYear() != 0;
        else if(attribute==RUNTIME)
            return getRuntime() != 0;
        else if(attribute==CINEMATOGRAPHY)
            return getCinematography() != null;
        else if(attribute==DIRECTOR)
            return getDirector() != null;
        else if(attribute==FSK)
            return getFsk() != null;
        else if(attribute==PRODUCERS)
            return getProducers() != null && !getProducers().isEmpty();
        else if(attribute==ABSTRACTPARAGRAPH)
            return getAbstractParagraph() != null;
        else if(attribute==DISTRIBUTORS)
            return getDistributors() != null && !getDistributors().isEmpty();
        else if(attribute==SUBJECTS)
            return getSubjects() != null && !getSubjects().isEmpty();
        else if(attribute==STUDIOS)
            return getStudios() != null && !getStudios().isEmpty();
        else if(attribute==WRITERS)
            return getWriters() != null && !getWriters().isEmpty();
        else if(attribute==COMPOSERS)
            return getComposers() != null && !getComposers().isEmpty();
        else if(attribute==COMPANIES)
            return getCompanies() != null && !getCompanies().isEmpty();
        else if(attribute==LANGUAGES)
            return getLanguages() != null && !getLanguages().isEmpty();
        else if(attribute==COUNTRIES)
            return getCountries() != null && !getCountries().isEmpty();
        else if(attribute==NOMINATIONS)
            return getNominations() != null && !getNominations().isEmpty();
        else if(attribute==AWARDS)
            return getAwards() != null && !getAwards().isEmpty();
        else if(attribute==GENRES)
            return getGenres() != null && !getGenres().isEmpty();
        else if(attribute==ACTORSWIKI)
            return getActorsWiki() != null && !getActorsWiki().isEmpty();
        else if(attribute==ACTORSDP)
            return getActorsDP() != null && !getActorsDP().isEmpty();
        else
            return false;
    }


    @Override
    public String toString() {
        return String.format("Movie: %s / %s / %s  / %s ]", getIdentifier(), getName(), getYear(),
                getDirector());
    }


    @Override
    public int hashCode() {
        return getIdentifier().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Movie){
            return this.getIdentifier().equals(((Movie) obj).getIdentifier());
        }else
            return false;
    }
}

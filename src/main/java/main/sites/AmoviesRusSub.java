package main.sites;


import data.datasets.EpisodeTranslation;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class AmoviesRusSub implements TVSeriesTranslationSite{
    /**
     * Link on pages with TV Series from site
     *
     * Note that link expects page number
     * like a parameter of String.format().
     */
    private static final String PAGES_LINK = "http://amovies.org/serials-rus-sub/page/%d/";

    @Override
    public List<EpisodeTranslation> getAllEpisodes(Session session) {
        List<EpisodeTranslation> episodeTranslationList = new ArrayList<>();
        return null;
    }

    @Override
    public List<EpisodeTranslation> getNewEpisodes(Session session, Date date) {
        return null;
    }

    @Override
    public List<EpisodeTranslation> getNewEpisodes(Session session) {
        return null;
    }
}

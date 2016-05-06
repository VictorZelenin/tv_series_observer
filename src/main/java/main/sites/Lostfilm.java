package main.sites;


import data.datasets.EpisodeTranslation;
import org.hibernate.Session;

import java.util.Date;
import java.util.List;

public class Lostfilm implements TVSeriesTranslationSite{
    @Override
    public List<EpisodeTranslation> getAllEpisodes(Session session) {
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

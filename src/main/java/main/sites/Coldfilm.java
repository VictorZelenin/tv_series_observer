package main.sites;


import data.datasets.EpisodeTranslation;
import org.hibernate.Session;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Coldfilm implements TVSeriesTranslationSite{
    @Override
    public List<EpisodeTranslation> getAllEpisodes(Session session) {
        return new LinkedList<>();
    }

    @Override
    public List<EpisodeTranslation> getNewEpisodes(Session session) {
        return new LinkedList<>();
    }
}

package main.sites;


import data.datasets.EpisodeTranslation;
import data.datasets.TVSeries;
import org.hibernate.Session;

import java.util.Date;
import java.util.List;

public interface TVSeriesTranslationSite {

    List<EpisodeTranslation> getAllEpisodes(Session session);

    List<EpisodeTranslation> getNewEpisodes(Session session, Date date);

    List<EpisodeTranslation> getNewEpisodes(Session session);
}

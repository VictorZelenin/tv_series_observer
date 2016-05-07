package main.sites;


import data.datasets.EpisodeTranslation;
import data.datasets.TVSeries;
import main.exception.CantFindObjectException;
import main.exception.ConnectionProblemsException;
import org.hibernate.Session;

import java.util.Date;
import java.util.List;

public interface TVSeriesTranslationSite {

    List<EpisodeTranslation> getAllEpisodes(Session session)
            throws ConnectionProblemsException, CantFindObjectException;

    List<EpisodeTranslation> getNewEpisodes(Session session)
            throws ConnectionProblemsException, CantFindObjectException;
}

package main;


import data.DBService;
import data.dao.EpisodeTranslationDAO;
import main.exception.CantFindObjectException;
import main.exception.ConnectionProblemsException;
import main.sites.*;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class EpisodesTranslationsData {

    private List<TVSeriesTranslationSite> sites = new ArrayList<>();
    {
        addBasicSites();
    }

    private DBService dbService = DBService.getInstance();

    private void addBasicSites() {
        sites.add(new AmoviesRusSub());
        sites.add(new Lostfilm());
//        sites.add(new NewStudio());
//        sites.add(new Coldfilm());
//        sites.add(new AlternativeProduction());
    }

    void fillWithBasicData() throws ConnectionProblemsException, CantFindObjectException{
        try (Session session = dbService.getSession()) {
            EpisodeTranslationDAO episodeTranslationDAO = new EpisodeTranslationDAO(session);
            for (TVSeriesTranslationSite site : sites) {
                site.getAllEpisodes(session).forEach( episodeTranslationDAO::save);
            }
        }
    }

    void update() throws ConnectionProblemsException, CantFindObjectException{
        try (Session session = dbService.getSession()) {
            EpisodeTranslationDAO episodeTranslationDAO = new EpisodeTranslationDAO(session);
            for (TVSeriesTranslationSite site : sites) {
                site.getNewEpisodes(session).forEach(episodeTranslationDAO::save);
            }
        }
    }


}

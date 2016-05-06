package main;


import data.DBService;
import data.dao.EpisodeTranslationDAO;
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
        sites.add(new AlternativeProduction());
        sites.add(new AmoviesRusSub());
        sites.add(new Coldfilm());
        sites.add(new NewStudio());
        sites.add(new Lostfilm());
    }

    void fillWithBasicData() {
        try (Session session = dbService.getSession()) {
            EpisodeTranslationDAO episodeTranslationDAO = new EpisodeTranslationDAO(session);
            for (TVSeriesTranslationSite site : sites) {
                site.getAllEpisodes(session).forEach( episodeTranslationDAO::save);
            }
        }
    }

    void update() {
        try (Session session = dbService.getSession()) {
            EpisodeTranslationDAO episodeTranslationDAO = new EpisodeTranslationDAO(session);
            for (TVSeriesTranslationSite site : sites) {
                site.getNewEpisodes(session).forEach(episodeTranslationDAO::save);
            }
        }
    }


}

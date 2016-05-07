package data.dao;

import data.datasets.Episode;
import data.datasets.EpisodeTranslation;
import data.datasets.Season;
import data.datasets.Translation;
import org.hibernate.Session;

import java.util.List;

/**
 * DAO для EpisodeTranslation
 */
public class EpisodeTranslationDAO extends DAO<EpisodeTranslation>{

    public EpisodeTranslationDAO(Session session) {
        super(session);
    }

    @Override
    public EpisodeTranslation get(long id) {
        return currentSession.get(EpisodeTranslation.class, id);
    }

    public EpisodeTranslation get(long episodeId, long translationId)
    {
        try {
            return (EpisodeTranslation) currentSession
                    .createSQLQuery("select * from episode_translation " +
                            "where episode_id = " + episodeId +
                            " and translation_id = " + translationId + ";")
                    .addEntity(EpisodeTranslation.class)
                    .list()
                    .get(0);
        }
        catch (NullPointerException | IndexOutOfBoundsException e)
        {
            return null;
        }
    }


    public EpisodeTranslation get(Episode episode, Translation translation)
    {
        try {
            return get(episode.getId(), translation.getId());
        } catch (NullPointerException e) {
            return null;
        }
    }


    public EpisodeTranslation get(String tvSeriesName, int seasonNumber, int episodeNumber,long translationId)
    {
        EpisodeDAO episodeDAO = new EpisodeDAO(currentSession);
        try {
            return get(episodeDAO.get(tvSeriesName, seasonNumber, episodeNumber).getId(), translationId);
        } catch (NullPointerException e) {
            return null;
        }
    }


    public EpisodeTranslation get(String tvSeriesName, int seasonNumber, int episodeNumber,Translation translation)
    {
        try {
            return get(tvSeriesName, seasonNumber, episodeNumber, translation.getId());
        } catch (NullPointerException e) {
            return null;
        }
    }


    public EpisodeTranslation get(Season season, int episodeNumber, long translationId)
    {
        try {
            return get(season.getTvSeries().getName(), season.getSeasonNumber(), episodeNumber, translationId);
        } catch (NullPointerException e) {
            return null;
        }
    }


    public EpisodeTranslation get(Season season, int episodeNumber, Translation translation)
    {
        try {
            return get(season, episodeNumber, translation.getId());
        } catch (NullPointerException e) {
            return null;
        }
    }



    @Override
    public List<EpisodeTranslation> getAll() {
        return currentSession.createSQLQuery("select * from episode_translation;")
                .addEntity(EpisodeTranslation.class)
                .list();
    }

    @Override
    public void save(EpisodeTranslation episodeTranslation) {
        currentSession.save(episodeTranslation);
    }

    @Override
    public void delete(EpisodeTranslation episodeTranslation) {
        currentSession.createSQLQuery("delete from episode_translation where id = " + episodeTranslation.getId() + ";").executeUpdate();
    }


    public void delete(long episodeId, long translationId)
    {
        currentSession
                .createSQLQuery("delete from episode_translation " +
                        "where episode_id = " + episodeId +
                        " and translation_id = " + translationId + ";")
                .executeUpdate();
    }


    public void delete(Episode episode, Translation translation)
    {
        delete(episode.getId(), translation.getId());
    }


    public void delete(String tvSeriesName, int seasonNumber, int episodeNumber,long translationId)
    {
        EpisodeDAO episodeDAO = new EpisodeDAO(currentSession);
        delete(episodeDAO.get(tvSeriesName, seasonNumber, episodeNumber).getId(), translationId);
    }


    public void delete(String tvSeriesName, int seasonNumber, int episodeNumber,Translation translation)
    {
        delete(tvSeriesName, seasonNumber, episodeNumber, translation.getId());
    }


    public void delete(Season season, int episodeNumber, long translationId)
    {
        delete(season.getTvSeries().getName(), season.getSeasonNumber(), episodeNumber, translationId);
    }


    public void delete(Season season, int episodeNumber, Translation translation)
    {
        delete(season, episodeNumber, translation.getId());
    }


    @Override
    public void deleteAll()
    {
        currentSession.createSQLQuery("delete from episode_translation;").executeUpdate();
    }
}

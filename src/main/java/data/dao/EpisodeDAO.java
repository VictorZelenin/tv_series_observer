package data.dao;

import data.datasets.Episode;
import data.datasets.Season;
import data.datasets.TVSeries;
import org.hibernate.Session;

import java.util.List;

/**
 * DAO для Episode
 */
public class EpisodeDAO extends DAO<Episode>{

    public EpisodeDAO(Session session)
    {
        super(session);
    }

    @Override
    public Episode get(long id) {
        return currentSession.get(Episode.class, id);
    }


    public Episode get(long seasonId, int episodeNumber)
    {
        try {
            return (Episode) currentSession
                    .createSQLQuery("select * from tv_episode " +
                            "where season_id = " + seasonId +
                            " and episode_number = " + episodeNumber + ";")
                    .addEntity(Episode.class)
                    .list()
                    .get(0);
        }
        catch (NullPointerException | IndexOutOfBoundsException e)
        {
            return null;
        }
    }

    public Episode get(Season season, int episodeNumber)
    {
        return get(season.getId(), episodeNumber);
    }

    public Episode get(TVSeries tvSeries, int seasonNumber, int episodeNumber)
    {
        SeasonDAO seasonDAO = new SeasonDAO(currentSession);
        return get(seasonDAO.get(tvSeries, seasonNumber), episodeNumber);
    }

    public Episode get(String tvSeriesName, int seasonNumber, int episodeNumber)
    {
        SeasonDAO seasonDAO = new SeasonDAO(currentSession);
        return get(seasonDAO.get(tvSeriesName, seasonNumber), episodeNumber);
    }

    @Override
    public List<Episode> getAll() {
        return currentSession.createSQLQuery("select * from tv_episode;")
                .addEntity(Episode.class)
                .list();
    }

    @Override
    public void save(Episode episode) {
        currentSession.save(episode);
    }

    @Override
    public void delete(Episode episode) {
        currentSession.createSQLQuery("delete from tv_episode where id = " + episode.getId() + ";").executeUpdate();
    }

    public void delete(long seasonId, int episodeNumber)
    {
        currentSession
                .createSQLQuery("delete from tv_episode " +
                        "where season_id = " + seasonId +
                        " and episode_number = " + episodeNumber + ";")
                .executeUpdate();
    }

    public void delete(Season season, int episodeNumber)
    {
        delete(season.getId(), episodeNumber);
    }

    public void delete(TVSeries tvSeries, int seasonNumber, int episodeNumber)
    {
        delete(new SeasonDAO(currentSession).get(tvSeries, seasonNumber), episodeNumber);
    }

    public void delete(String tvSeriesName, int seasonNumber, int episodeNumber)
    {
        delete(new SeasonDAO(currentSession).get(tvSeriesName, seasonNumber), episodeNumber);
    }

    @Override
    public void deleteAll() {
        currentSession.createSQLQuery("delete from tv_episode;").executeUpdate();
    }
}

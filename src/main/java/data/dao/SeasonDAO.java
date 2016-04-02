package data.dao;

import data.datasets.Season;
import data.datasets.TVSeries;
import org.hibernate.Session;

import java.util.List;

/**
 * DAO для Season
 */
public class SeasonDAO extends DAO<Season>{

    public SeasonDAO(Session session)
    {
        super(session);
    }

    @Override
    public Season get(long id) {
        return currentSession.get(Season.class, id);
    }

    public Season get(long tvSeriesId, int seasonNumber)
    {
        try {
            return (Season) currentSession
                    .createSQLQuery("select * from season " +
                            "where tv_series_id = " + tvSeriesId +
                            " and season_number = " + seasonNumber + ";")
                    .addEntity(Season.class)
                    .list()
                    .get(0);
        }
        catch (NullPointerException | IndexOutOfBoundsException e)
        {
            return null;
        }
    }

    public Season get(TVSeries tvSeries, int seasonNumber)
    {
        return get(tvSeries.getId(), seasonNumber);
    }

    public Season get(String tvSeriesName, int seasonNumber)
    {
        return get(new TVSeriesDAO(currentSession).get(tvSeriesName), seasonNumber);
    }

    @Override
    public List<Season> getAll() {
        return currentSession.createSQLQuery("select * from season;")
                .addEntity(Season.class)
                .list();
    }

    @Override
    public void save(Season season) {
        currentSession.save(season);
    }

    @Override
    public void delete(Season season) {
        currentSession.createSQLQuery("delete from season where id = " + season.getId() + ";").executeUpdate();
    }

    public void delete(long tvSeriesId, int seasonNumber)
    {
        currentSession
                .createSQLQuery("delete from season " +
                        "where tv_series_id = " + tvSeriesId +
                        " and season_number = " + seasonNumber + ";")
                .executeUpdate();
    }

    public void delete(TVSeries tvSeries, int seasonNumber)
    {
        delete(tvSeries.getId(), seasonNumber);
    }

    public void delete(String tvSeriesName, int seasonNumber)
    {
        delete(new TVSeriesDAO(currentSession).get(tvSeriesName), seasonNumber);
    }

    @Override
    public void deleteAll() {
        currentSession.createSQLQuery("delete from season;").executeUpdate();
    }
}

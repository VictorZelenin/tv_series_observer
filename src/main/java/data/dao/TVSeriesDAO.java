package data.dao;

import data.datasets.TVSeries;
import org.hibernate.Session;

import java.util.List;

/**
 * DAO для TVSeries
 */
public class TVSeriesDAO extends DAO<TVSeries>{

    public TVSeriesDAO(Session session)
    {
        super(session);
    }

    @Override
    public TVSeries get(long id) {
        return currentSession.get(TVSeries.class, id);
    }

    public TVSeries get(String name)
    {
        try {
            return (TVSeries) currentSession.createSQLQuery("select * from tv_series where name = '" + name + "';")
                    .addEntity(TVSeries.class)
                    .list()
                    .get(0);
        }
        catch (NullPointerException | IndexOutOfBoundsException e)
        {
            return null;
        }
    }

    @Override
    public List<TVSeries> getAll() {
        return currentSession.createSQLQuery("select * from tv_series")
                .addEntity(TVSeries.class)
                .list();
    }

    @Override
    public void save(TVSeries tvSeries) {
        currentSession.save(tvSeries);
    }


    @Override
    public void delete(TVSeries tvSeries) {
        currentSession.createSQLQuery("delete from tv_series where id = " + tvSeries.getId() + ";").executeUpdate();
    }

    public void delete(String name)
    {
        currentSession.createSQLQuery("delete from tv_series where name = '" + name + "';").executeUpdate();
    }

    @Override
    public void deleteAll() {
        currentSession.createSQLQuery("delete from tv_series;").executeUpdate();
    }
}

package data.dao;

import data.datasets.Episode;
import data.datasets.Translation;
import org.hibernate.Session;

import java.util.List;

/**
 * DAO для Translation
 */
public class TranslationDAO extends DAO<Translation>{

    public TranslationDAO(Session session) {
        super(session);
    }

    @Override
    public Translation get(long id) {
        return currentSession.get(Translation.class, id);
    }


    public Translation get(String type)
    {
        try {
            return (Translation) currentSession
                    .createSQLQuery("select * from translation where type = '" + type + "';")
                    .addEntity(Translation.class)
                    .list()
                    .get(0);
        }
        catch (NullPointerException | IndexOutOfBoundsException e)
        {
            return null;
        }
    }

    @Override
    public List<Translation> getAll() {
        return currentSession.createSQLQuery("select * from episode_translation;")
                .addEntity(Episode.class)
                .list();
    }

    @Override
    public void save(Translation translation) {
        currentSession.save(translation);
    }

    @Override
    public void delete(Translation translation) {
        currentSession.createSQLQuery("delete from translation where id = " + translation.getId() + ";").executeUpdate();
    }

    public void delete(String type)
    {
        currentSession.createSQLQuery("delete from translation where type = '" + type + "';").executeUpdate();
    }

    @Override
    public void deleteAll() {
        currentSession.createSQLQuery("delete from tv_episode;").executeUpdate();
    }
}

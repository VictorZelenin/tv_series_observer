package data.dao;

import data.datasets.Country;
import org.hibernate.Session;

import java.util.List;

/**
 * DAO for country
 */
public class CountryDAO extends DAO<Country> {

    public CountryDAO(Session session)
    {
        super(session);
    }

    public Country get(long id)
    {
        return  currentSession.get(Country.class, id);
    }

    public Country get(String name)
    {
        try {
            return (Country) currentSession.createSQLQuery("select * from country where name = '" + name + "';")
                    .addEntity(Country.class)
                    .list()
                    .get(0);
        }
        catch (NullPointerException | IndexOutOfBoundsException e)
        {
            return null;
        }
    }

    public List<Country> getAll()
    {
        return currentSession.createSQLQuery("select * from country")
                .addEntity(Country.class)
                .list();
    }

    public void save(Country country)
    {
        currentSession.save(country);
    }


    public void update(Country country)
    {
        currentSession.createSQLQuery("update country set name = '" + country.getName() +
                "', language='" + country.getLanguage() +
                "' where id =" + country.getId() + ";")
                .executeUpdate();
    }

    public void delete(Country country)
    {
        currentSession.createSQLQuery("delete from country where id = " + country.getId() + ";").executeUpdate();
    }

    public void delete(String name)
    {
        currentSession.createSQLQuery("delete from country where name = '" + name + "';").executeUpdate();
    }

    public void deleteAll()
    {
        currentSession.createSQLQuery("delete from country;").executeUpdate();
    }


}

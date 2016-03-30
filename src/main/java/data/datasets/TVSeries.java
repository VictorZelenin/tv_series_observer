package data.datasets;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Описывает определённый сериал
 * Имя, страна сериала, список сезонов (при необходимости)
 */
@Entity
@Table(name = "tv_series")
public class TVSeries  implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "country_id", nullable = false)
    private long countryId;


    private List<Season> seasons;//все сезоны данного сериала

    public TVSeries()
    {

    }

    public TVSeries(String name, Country country)
    {
        this.name = name;
        this.countryId = country.getId();
    }

    public TVSeries(String name, long countryId) {
        this.name = name;
        this.countryId = countryId;
    }

    public List<Season> getSeasons()//
    {
        synchronized (seasons) {
            if (seasons != null)
                return seasons;

            //ленивое получение
        }
        return seasons;
    }
}

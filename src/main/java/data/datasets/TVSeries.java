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

    @OneToMany(mappedBy = "tvSeries", fetch = FetchType.LAZY)
    private List<Season> seasons;//все сезоны данного сериала

    @ManyToOne
    @JoinColumn(name = "country_id", referencedColumnName="id")
    private Country country;

    public TVSeries()
    {

    }

    public TVSeries(String name, Country country)
    {
        this.name = name;
        this.country = country;
    }

    public List<Season> getSeasons() {
        seasons.size();
        return seasons;
    }

    public String getName() {
        return name;
    }

    public Country getCountry() {
        return country;
    }

    public long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}

package data.datasets;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Описывает определённый сериал
 * Имя, страна сериала, список сезонов (при необходимости)
 */
@Entity
@Table(name = "tv_series")
public class TVSeries implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "begin_date")
    @Temporal(TemporalType.DATE)
    private Date beginDate;

    @Column(name = "imdb")
    private Float rating;

    @OneToMany(mappedBy = "tvSeries", fetch = FetchType.LAZY)
    private List<Season> seasons;//все сезоны данного сериала

    @ManyToOne
    @JoinColumn(name = "country_id", referencedColumnName = "id")
    private Country country;

    public TVSeries() {

    }

    public TVSeries(String name, Country country) {
        this.name = name;
        this.country = country;
    }

    public TVSeries(String name, Country country, Date beginDate, float rating) {
        this.name = name;
        this.country = country;
        this.beginDate = beginDate;
        this.rating = rating;
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

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    @Override
    public int hashCode() {
        return (int) id;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TVSeries))
            return false;
        else
            return ((TVSeries) obj).getId() == getId();
    }

    @Override
    public String toString() {
        return getName();
    }
}

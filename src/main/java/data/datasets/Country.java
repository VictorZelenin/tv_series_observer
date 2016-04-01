package data.datasets;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Объект, описывающий страну
 *
 */
@Entity
@Table(name = "country")
public class Country implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "language")
    private String language;



    @OneToMany(mappedBy = "country", fetch = FetchType.LAZY)
    private List<TVSeries> tvSeries;


    public Country()
    {}

    public Country(String name, String language) {
        this.name = name;
        this.language = language;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<TVSeries> getTvSeries() {
        tvSeries.size();
        return tvSeries;
    }
}

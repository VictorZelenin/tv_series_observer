package data.datasets;

import javax.persistence.*;
import java.io.Serializable;
import java.net.URL;
import java.util.List;

/**
 * Тип перевода/ озвучки, ссылка на сайт озвучки
 */
@Entity
@Table(name = "translation")
public class Translation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "type", unique = true)
    private String type;

    @Column(name = "reference")
    private String reference = "";

    @OneToMany(mappedBy = "translation", fetch = FetchType.LAZY)
    private List<EpisodeTranslation> episodeTranslation;

    public Translation() {
    }

    public Translation(String type, String reference) {
        this.type = type;
        this.reference = reference;
    }


    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }


    public String getReference() {
        return reference;
    }


    public void setReference(String reference) {
        this.reference = reference;
    }


    public long getId() {
        return id;
    }

    public List<EpisodeTranslation> getEpisodeTranslation() {
        episodeTranslation.size();
        return episodeTranslation;
    }


    @Override
    public int hashCode() {
        return (int)id;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Translation))
            return false;
        else
            return ((Translation)obj).getId() == getId();
    }

    @Override
    public String toString() {
        return getType();
    }

}

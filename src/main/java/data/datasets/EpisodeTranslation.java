package data.datasets;

import javax.persistence.*;
import java.io.Serializable;
import java.net.URL;
import java.util.Date;

/**
 * Перевод (озвучка) определённого эпизода
 * Хранит основную ссылку, объект-эпизод, переводчика
 */
@Entity
@Table(name = "episode_translation", uniqueConstraints = { @UniqueConstraint(columnNames = {"episode_id", "translation_id"})})
public class EpisodeTranslation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;


    @Column(name = "reference")
    private String reference;

    @Column(name = "date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;


    @ManyToOne
    @JoinColumn(name = "episode_id", referencedColumnName="id")
    private Episode episode;

    @ManyToOne
    @JoinColumn(name = "translation_id", referencedColumnName="id")
    private Translation translation;

    public EpisodeTranslation() {
        date = new Date();
    }

    public EpisodeTranslation(String reference, Episode episode, Translation translation) {
        this.reference = reference;
        this.episode = episode;
        this.translation = translation;
        date = new Date();
    }

    public EpisodeTranslation(String reference, Episode episode, Translation translation, Date date) {
        this.reference = reference;
        this.episode = episode;
        this.translation = translation;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public String getPartialReference() {
        return reference;
    }

    public void setPartialReference(String reference) {
        this.reference = reference;
    }

    public String getAbsoluteReference()
    {
        return translation.getReference() + reference;
    }

    public Episode getEpisode() {
        return episode;
    }

    public void setEpisode(Episode episode) {
        this.episode = episode;
    }

    public Translation getTranslation() {
        return translation;
    }

    public void setTranslation(Translation translation) {
        this.translation = translation;
    }

    public Season getSeason()
    {
        return episode.getSeason();
    }

    public TVSeries getTVSeries()
    {
        return getSeason().getTvSeries();
    }

    public Country getCountry()
    {
        return getTVSeries().getCountry();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    @Override
    public int hashCode() {
        return (int)id;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof EpisodeTranslation))
            return false;
        else
            return ((EpisodeTranslation)obj).getId() == getId();
    }

    @Override
    public String toString() {
        return getEpisode().toString() + "\ntranslation: " + getTranslation().toString();
    }

}

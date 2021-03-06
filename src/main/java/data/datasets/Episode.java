package data.datasets;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Эпизод — одна серия
 * Номер (серии), сезон, список переводов
 */
@Entity
@Table(name = "tv_episode", uniqueConstraints = { @UniqueConstraint(columnNames = {"season_id", "episode_number"})})
public class Episode implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "episode_number", nullable = false)
    private int number;



    @ManyToOne
    @JoinColumn(name = "season_id", referencedColumnName="id")
    private Season season;

    @OneToMany(mappedBy = "episode", fetch = FetchType.LAZY)
    private List<EpisodeTranslation> episodeTranslations;

    public Episode() {
    }


    public Episode(int number, Season season) {
        this.number = number;
        this.season = season;
    }

    public long getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Season getSeason() {
        return season;
    }

    public TVSeries getTVSeries()
    {
        return season.getTvSeries();
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public List<EpisodeTranslation> getEpisodeTranslations() {
        episodeTranslations.size();
        return episodeTranslations;
    }

    @Override
    public int hashCode() {
        return (int)id;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Episode))
            return false;
        else
            return ((Episode)obj).getId() == getId();
    }

    @Override
    public String toString() {
        return getSeason().toString() + "\nepisode " + getNumber();
    }
}

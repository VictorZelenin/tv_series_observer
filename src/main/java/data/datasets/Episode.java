package data.datasets;

import javax.persistence.*;
import java.io.Serializable;

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


    @Column(name = "season_id", nullable = false)
    private long seasonId;


    private Season season;


    public Episode() {
    }


    public Episode(int number, Season season) {
        this.number = number;
        this.season = season;
    }

    public Episode(int number, long seasonId) {
        this.number = number;
        this.seasonId = seasonId;
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

    public long getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(long seasonId) {
        this.seasonId = seasonId;
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

}

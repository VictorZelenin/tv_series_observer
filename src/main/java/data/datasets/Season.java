package data.datasets;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Сезон определённого сериала
 * Хранит объект сериала, номер (этого сезона), список серий
 */

@Entity
@Table(name = "season", uniqueConstraints = { @UniqueConstraint(columnNames = {"tv_series_id", "season_number"})})
public class Season  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "tv_series_id", nullable = false)
    private long tvSeriesId;

    @Column(name = "season_number", nullable = false)
    private int seasonNumber;

    @Column(name = "season_image" )
    private byte[] image;


    private TVSeries tvSeries;//объект, описывающий сериал


    private List<Episode> episodes;//список эпизодов сезона


    public Season() {
    }


    public Season(long tvSeriesId, int seasonNumber) {
        this.tvSeriesId = tvSeriesId;
        this.seasonNumber = seasonNumber;
    }


    public Season(long tvSeriesId, int seasonNumber, byte[] image) {
        this.tvSeriesId = tvSeriesId;
        this.seasonNumber = seasonNumber;
        this.image = image;
    }



    public long getTvSeriesId() {
        return tvSeriesId;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(int seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public TVSeries getTvSeries() {
        return tvSeries;
    }

    public void setTvSeries(TVSeries tvSeries) {
        this.tvSeries = tvSeries;
    }


    public List<Episode> getEpisodes() {
        return episodes;
    }
}

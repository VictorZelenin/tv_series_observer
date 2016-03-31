package data.datasets;

import javax.imageio.ImageIO;
import javax.persistence.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.IOException;
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

    @Column(name = "season_number", nullable = false)
    private int seasonNumber;

    @Column(name = "season_image")
    @Lob
    private byte[] image;


    @ManyToOne
    @JoinColumn(name = "tv_series_id", referencedColumnName="id")
    private TVSeries tvSeries;//объект, описывающий сериал


    @OneToMany(mappedBy = "season", fetch = FetchType.LAZY)
    private List<Episode> episodes;//список эпизодов сезона


    public Season() {
    }

    public Season(int seasonNumber, TVSeries tvSeries, byte[] image) {
        this.seasonNumber = seasonNumber;
        this.tvSeries = tvSeries;
        this.image = image;
    }

    public Season(int seasonNumber, TVSeries tvSeries) {
        this.seasonNumber = seasonNumber;
        this.tvSeries = tvSeries;
    }


    public int getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(int seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public BufferedImage getImage() throws IOException{
        return ImageIO.read(new ByteArrayInputStream(image));
    }

    public void setImage(BufferedImage image) {
        this.image = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
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

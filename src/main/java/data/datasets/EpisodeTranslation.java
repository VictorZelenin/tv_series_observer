package data.datasets;

import javax.persistence.*;
import java.io.Serializable;
import java.net.URL;

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

    @ManyToOne
    @JoinColumn(name = "episode_id", referencedColumnName="id")
    private Episode episode;

    @ManyToOne
    @JoinColumn(name = "translation_id", referencedColumnName="id")
    private Translation translation;

    public EpisodeTranslation() {
    }

    public EpisodeTranslation(String reference, Episode episode, Translation translation) {
        this.reference = reference;
        this.episode = episode;
        this.translation = translation;
    }


    public long getId() {
        return id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
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


}

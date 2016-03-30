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

    @Column(name = "translation_id", nullable = false)
    private long translationId;

    @Column(name = "episode_id", nullable = false)
    private long episodeId;

    private Episode episode;
    private Translation translation;

    public EpisodeTranslation() {
    }


    public EpisodeTranslation(String reference, long episodeId, long translationId) {
        this.reference = reference;
        this.translationId = translationId;
        this.episodeId = episodeId;
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

    public long getTranslationId() {
        return translationId;
    }

    public void setTranslationId(long translationId) {
        this.translationId = translationId;
    }

    public long getEpisodeId() {
        return episodeId;
    }

    public void setEpisodeId(long episodeId) {
        this.episodeId = episodeId;
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

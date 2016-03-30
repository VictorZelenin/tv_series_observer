package data.datasets;

import java.io.Serializable;
import java.net.URL;

/**
 * Перевод (озвучка) определённого эпизода
 * Хранит основную ссылку, объект-эпизод, переводчика
 */
public class EpisodeTranslation implements Serializable {
    private URL reference;
    private Episode episode;
    private Translation translation;


}

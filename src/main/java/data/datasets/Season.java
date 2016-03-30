package data.datasets;

import java.io.Serializable;
import java.util.List;

/**
 * Сезон определённого сериала
 * Хранит объект сериала, номер (этого сезона), список серий
 */
public class Season  implements Serializable {
    private TVSeries tvSeries;
    private int number;
    private List<Episode> episodes;

}

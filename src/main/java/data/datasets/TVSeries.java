package data.datasets;

import java.io.Serializable;
import java.util.List;

/**
 * Описывает определённый сериал
 * Имя, страна сериала, список сезонов (при необходимости)
 */
public class TVSeries  implements Serializable{
    private int id;
    private String name;
    private List<Season> seasons;

}

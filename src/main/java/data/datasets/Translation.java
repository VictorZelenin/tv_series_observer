package data.datasets;

import java.io.Serializable;
import java.net.URL;

/**
 * Тип перевода/ озвучки, ссылка на сайт озвучки
 */
public class Translation implements Serializable {
    private String type;
    private URL reference;

}

package data.datasets;

import java.io.Serializable;

/**
 * Эпизод — одна серия
 * Номер (серии), сезон, список переводов
 */
public class Episode implements Serializable{
    private int number;
    private Season season;
}

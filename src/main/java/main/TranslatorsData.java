package main;

import data.DBService;
import data.dao.TranslationDAO;
import data.datasets.Translation;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import java.util.HashMap;
import java.util.Map;

public class TranslatorsData{

    /**
     * Translators that will be set first.
     */
    private static final Map<String, String> TRANSLATORS = new HashMap<>();
    static {
        TRANSLATORS.put("rus_sub", "http://amovies.org/serials-rus-sub/");
        TRANSLATORS.put("Alternative Production", "http://altpro.tv/");
        TRANSLATORS.put("NewStudio", "http://newstudio.tv/");
        TRANSLATORS.put("LostFilm", "http://www.lostfilm.tv/");
        TRANSLATORS.put("ColdFilm", "http://coldfilm.ru/");
    }

    void fillWithBasicData() {
        try (Session session = DBService.getInstance().getSession()) {
            TranslationDAO translationDAO = new TranslationDAO(session);

            for (Map.Entry<String, String> translator: TRANSLATORS.entrySet()) {
                try {
                    translationDAO.save(new Translation(translator.getKey(), translator.getValue()));
                } catch (ConstraintViolationException e) {
                    e.printStackTrace();//there will be logging someday
                }
            }
        }
    }
}

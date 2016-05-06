package main;


import data.DBService;
import data.dao.CountryDAO;
import data.datasets.Country;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import java.util.HashMap;
import java.util.Map;

public class CountriesData{

    /**
     * Basic countries that have to be added
     */
    private static final Map<String, String> COUNTRIES = new HashMap<>();
    static {
        COUNTRIES.put("UK", "English");
        COUNTRIES.put("USA", "English");
        COUNTRIES.put("Russia", "Russian");
        COUNTRIES.put("Ukraine", "Ukrainian");
    }

    void fillWithBasicData() {
        try (Session session = DBService.getInstance().getSession()) {
            CountryDAO countryDAO = new CountryDAO(session);
            for (Map.Entry<String, String> country: COUNTRIES.entrySet()) {
                try {
                    countryDAO.save(new Country(country.getKey(), country.getValue()));
                } catch (ConstraintViolationException e) {
                    e.printStackTrace(); // Logging need to be added here
                }
            }
        }
    }

}

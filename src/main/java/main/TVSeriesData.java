package main;


import data.DBService;
import data.dao.CountryDAO;
import data.dao.TVSeriesDAO;
import data.datasets.TVSeries;
import main.exception.CantFindObjectException;
import main.exception.ConnectionProblemsException;
import main.exception.DataParsingException;
import main.exception.ObjectAlreadyExistException;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TVSeriesData {

    /**
     * URL for search a TV Series by name on imdb.
     * <p>
     * Note that it has a string parameter to String.format() method:
     * TV Series name in utf-8.
     */
    public static final String IMDB_SEARCH_URL = "http://www.imdb.com/find?ref_=nv_sr_fn&q=%s&s=all";

    /**
     * List of TV series names that will be added first.
     */
    private static final List<String> BASIC_TV_SERIES = new ArrayList<>();

    static {
        BASIC_TV_SERIES.add("Флэш");
        BASIC_TV_SERIES.add("Готэм");
        BASIC_TV_SERIES.add("Форс-мажоры");
        BASIC_TV_SERIES.add("Побег");
        BASIC_TV_SERIES.add("Лютер");
        BASIC_TV_SERIES.add("Игра престолов");
        BASIC_TV_SERIES.add("Доктор Хаус");
        BASIC_TV_SERIES.add("Люцифер");
        BASIC_TV_SERIES.add("Декстер");
        BASIC_TV_SERIES.add("Шерлок");
    }


    void fillWithBasicData() throws CantFindObjectException,
            ConnectionProblemsException, DataParsingException {
        for (String tvSeriesName : BASIC_TV_SERIES) {
            try {
                addNewTVSeries(tvSeriesName);
            } catch (ObjectAlreadyExistException e) {
                e.printStackTrace(); // add logging here
            }
        }
    }

    public void addNewTVSeries(String name) throws CantFindObjectException,
            ObjectAlreadyExistException, ConnectionProblemsException, DataParsingException {
        String imdbUrl = tvSeriesImdbUrl(name);

        TVSeries tvSeries = getTVSeriesInfoFromImdb(imdbUrl, name);

        try(Session session = DBService.getInstance().getSession()) {
            new TVSeriesDAO(session).save(tvSeries);
        }
    }

    public String tvSeriesImdbUrl(String name) throws CantFindObjectException, ConnectionProblemsException {
        String imdbURL;
        try {
            String url = String.format(IMDB_SEARCH_URL, urlEncoding(name));
            Document document = Jsoup.connect(url).get();
            imdbURL = document.getElementsByClass("findResult").first()
                    .select("a").attr("abs:href");
        } catch (IOException e) {
            throw new ConnectionProblemsException();
        } catch (NullPointerException e) {
            throw new CantFindObjectException(name);
        }

        return imdbURL;
    }

    public TVSeries getTVSeriesInfoFromImdb(String imdbUrl, String name)
            throws ConnectionProblemsException, CantFindObjectException, DataParsingException {
        TVSeries tvSeries;

        try {
            tvSeries = new TVSeries();
            Document document = Jsoup.connect(imdbUrl).get();

            tvSeries.setName(name);

            // Get string with date from site
            String stringDate = document.getElementById("titleDetails")
                    .getElementsByClass("txt-block").get(3).text();

            // Get date from received string
            Pattern pattern = Pattern.compile("(Release Date: (.*?\\d{4}))");
            Matcher matcher = pattern.matcher(stringDate);
            if (matcher.find())
                stringDate = matcher.group(2);

            // Parsing to Date
            DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
            Date date = dateFormat.parse(stringDate);

            tvSeries.setBeginDate(date);

            // Get rating
            float rating = Float.parseFloat(
                    document.select(".ratingValue")
                            .first().select("span").first().text()
            );
            tvSeries.setRating(rating);

            // Get country
            String countryName = document.getElementById("titleDetails")
                    .getElementsByClass("txt-block").get(1).select("a")
                    .first().text();

            try (Session session = DBService.getInstance().getSession()){
                tvSeries.setCountry(new CountryDAO(session).get(countryName));
            } catch (ConstraintViolationException e) {
                throw new CantFindObjectException(countryName);
            }

        } catch (IOException e) {
            throw new ConnectionProblemsException(imdbUrl);
        } catch (NullPointerException e) {
            throw new CantFindObjectException(imdbUrl);
        } catch (ParseException | NumberFormatException e) {
            throw new DataParsingException();
        }

        return tvSeries;
    }

    public String urlEncoding(String message) throws UnsupportedEncodingException {
        return URLEncoder.encode(message, "UTF8");
    }

}

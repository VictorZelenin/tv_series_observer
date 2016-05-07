package main.sites;


import data.dao.*;
import data.datasets.*;
import javafx.util.Pair;
import main.TVSeriesData;
import main.exception.CantFindObjectException;
import main.exception.ConnectionProblemsException;
import main.exception.DataParsingException;
import main.exception.ObjectAlreadyExistException;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AmoviesRusSub implements TVSeriesTranslationSite {
    /**
     * Link on pages with TV Series from site
     * <p>
     * Note that link expects page number
     * like a parameter of String.format().
     */
    private static final String PAGES_LINK = "http://amovies.org/serials-rus-sub/page/%d/";
    private static final String TRANSLATION = "rus_sub";
    private static final String TRANSLATION_REFERENCE = "http://amovies.org/";

    @Override
    public List<EpisodeTranslation> getAllEpisodes(Session session)
            throws ConnectionProblemsException, CantFindObjectException {
        return getEpisodes(session, true);
    }

    private List<EpisodeTranslation> getEpisodes(Session session, boolean getAll)
            throws ConnectionProblemsException, CantFindObjectException {
        List<EpisodeTranslation> episodeTranslationList = new ArrayList<>();

        int pageNumber = 1;
        List<EpisodeTranslation> episodesFromPage;
        boolean isNew = false;
        EpisodeTranslationDAO episodeTranslationDAO = new EpisodeTranslationDAO(session);
        EpisodeTranslation bufferEpisodeTranslation = null;
        Translation translation = new TranslationDAO(session).get(TRANSLATION);
        if (translation == null)
            throw new CantFindObjectException(TRANSLATION);

        Pattern seasonPattern = Pattern.compile("(\\d+)(?= сезон)");
        Matcher seasonMatcher;
        Pattern episodeNumberPattern = Pattern.compile("(\\d+)(?= серия)");
        Matcher episodeNumberMatcher;

        try {
            Document document = Jsoup.connect(String.format(PAGES_LINK, pageNumber)).get();

            mainCicle:
            while (true) {
                Elements tvSeriesElements = document.select("#dle-content").first().children();
                for (Element tvSeriesElement : tvSeriesElements) {
                    // can do it in parallel streams
                    if (tvSeriesElement.nodeName().equals("li")) {
                        String stringTvSeriesLink = tvSeriesElement.select("a").attr("abs:href");
                        String tvSeriesName = tvSeriesElement.select("a").text();

                        int seasonNumber = 0;
                        seasonMatcher = seasonPattern.matcher(tvSeriesElement.select("span").text());
                        if (seasonMatcher.find()) {
                            seasonNumber = Integer.parseInt(seasonMatcher.group());
                        }
                        int episodeNumber = 0;
                        episodeNumberMatcher = episodeNumberPattern.matcher(tvSeriesElement.select("span").text());
                        if (episodeNumberMatcher.find()) {
                            episodeNumber = Integer.parseInt(episodeNumberMatcher.group());
                        }

                        bufferEpisodeTranslation = episodeTranslationDAO.get(tvSeriesName, seasonNumber, episodeNumber, translation);

                        isNew = bufferEpisodeTranslation == null;

                        if (!isNew && !getAll) {
                            break mainCicle;
                        }

                        try {
                            if (getAll) {
                                episodesFromPage = getEpisodesFromPage(session, stringTvSeriesLink);
                            } else {
                                episodesFromPage = getLastEpisodesFromPage(session, stringTvSeriesLink);
                            }
                        } catch (DataParsingException e) {
                            continue;
                        }
                        episodeTranslationList.addAll(episodesFromPage);
                    }
                }
                pageNumber++;
                document = Jsoup.connect(String.format(PAGES_LINK, pageNumber)).get();
                if (pageNumber > 1000)
                    break;
            }
        } catch (HttpStatusException e) {
            //this is the true break of the cycle
            return episodeTranslationList;
        } catch (IOException e) {
            throw new ConnectionProblemsException(String.format(PAGES_LINK, pageNumber));
        }
        return episodeTranslationList;
    }

    public List<EpisodeTranslation> getEpisodesFromPage(Session session, String tvSeriesLink)
            throws ConnectionProblemsException, DataParsingException, CantFindObjectException {
        TVSeriesDAO tvSeriesDAO = new TVSeriesDAO(session);
        SeasonDAO seasonDAO = new SeasonDAO(session);
        EpisodeDAO episodeDAO = new EpisodeDAO(session);
        Translation translation = new TranslationDAO(session).get(TRANSLATION);
        if (translation == null) {
            throw new CantFindObjectException(TRANSLATION);
        }

        List<EpisodeTranslation> episodes = new ArrayList<>();

        try {
            String tvSeriesName;
            int seasonNumber = 0;

            Document document = Jsoup.connect(tvSeriesLink).get();
            tvSeriesName = document.select("h1").first().text();


            Pattern getTvSeriesPattern = Pattern.compile("(.*) (\\d+) сезон.*");
            Matcher getTvSeriesMatcher = getTvSeriesPattern.matcher(tvSeriesName);
            if (getTvSeriesMatcher.find()) {
                tvSeriesName = getTvSeriesMatcher.group(1);
                seasonNumber = Integer.parseInt(getTvSeriesMatcher.group(2));
            }
            TVSeries tvSeries = tvSeriesDAO.get(tvSeriesName);
            //If tvSeries is not in database — return
            if (tvSeries == null) {
                return episodes;
            }

            Season season = seasonDAO.get(tvSeries, seasonNumber);
            //If season is not in database — create and add
            if (season == null) {
                //Image may be later
                seasonDAO.save(new Season(seasonNumber, tvSeries));
                season = seasonDAO.get(tvSeries, seasonNumber);
            }

            String[] episodesString = document.select(".arhive_news").first().select("noindex").first().text().split("[|]");
            List<Episode> episodeList = new ArrayList<>();
            Episode bufferEpisode;
            int episodeNumber;

            Pattern getEpisodeNumberPattern = Pattern.compile("((\\d+) серия)");
            Matcher getEpisodeNumberMatcher;
            for (String episodeString : episodesString) {
                getEpisodeNumberMatcher = getEpisodeNumberPattern.matcher(episodeString);
                if (getEpisodeNumberMatcher.find()) {
                    episodeNumber = Integer.parseInt(getEpisodeNumberMatcher.group(2));
                    bufferEpisode = episodeDAO.get(season, episodeNumber);
                    if (bufferEpisode == null) {
                        episodeDAO.save(new Episode(episodeNumber, season));
                        bufferEpisode = episodeDAO.get(season, episodeNumber);
                    }

                    episodeList.add(bufferEpisode);
                }
            }

            for (Episode episode : episodeList) {
                episodes.add(new EpisodeTranslation(
                        tvSeriesLink.substring(TRANSLATION_REFERENCE.length()), episode, translation, new Date()));
            }

        } catch (IOException e) {
            throw new ConnectionProblemsException();
        } catch (IllegalStateException | NumberFormatException e) {
            throw new DataParsingException();
        }
        return episodes;
    }

    @Override
    public List<EpisodeTranslation> getNewEpisodes(Session session)
            throws ConnectionProblemsException, CantFindObjectException {
        return getEpisodes(session, false);
    }

    public List<EpisodeTranslation> getLastEpisodesFromPage(Session session, String tvSeriesLink)
            throws ConnectionProblemsException, DataParsingException, CantFindObjectException {
        List<EpisodeTranslation> newEpisodes = new ArrayList<>();
        List<EpisodeTranslation> allEpisodes = getEpisodesFromPage(session, tvSeriesLink);
        Collections.reverse(allEpisodes);

        EpisodeTranslationDAO episodeTranslationDAO = new EpisodeTranslationDAO(session);
        for (EpisodeTranslation episodeTranslation: allEpisodes) {
            if (episodeTranslationDAO.get(episodeTranslation.getEpisode(), episodeTranslation.getTranslation()) != null) {
                break;
            } else {
                newEpisodes.add(episodeTranslation);
            }
        }

        return newEpisodes;
    }
}

package main;


import main.exception.CantFindObjectException;
import main.exception.ConnectionProblemsException;
import main.exception.DataParsingException;

import java.util.Date;

public class DataUploader {

    /**
     * Time in milliseconds between update function calling
     */
    private long updatingPeriod = 30 * 60 * 1000;

    private TVSeriesData tvSeriesData;

    private CountriesData countriesData;

    private TranslatorsData translatorsData;

    private EpisodesTranslationsData episodesTranslationsData;

    private Updater updater;


    public DataUploader(boolean fillWithBasicData)
            throws CantFindObjectException, ConnectionProblemsException, DataParsingException {
        if (fillWithBasicData) {
            fillWithBasicData();
        }

        createDataObjects();

        updater = new Updater();
        updater.start();
    }

    private void createDataObjects() {
        tvSeriesData = new TVSeriesData();
        countriesData = new CountriesData();
        translatorsData = new TranslatorsData();
        episodesTranslationsData = new EpisodesTranslationsData();
    }

    public long getUpdatingPeriod() {
        return updatingPeriod;
    }

    public void setUpdatingPeriod(long updatingPeriod) {
        this.updatingPeriod = updatingPeriod;
    }

    public void fillWithBasicData() throws CantFindObjectException, ConnectionProblemsException, DataParsingException {
        countriesData.fillWithBasicData();
        translatorsData.fillWithBasicData();
        tvSeriesData.fillWithBasicData();
        episodesTranslationsData.fillWithBasicData();
    }

    public void update() {
        episodesTranslationsData.update();
    }

    public void stopUpdating() {
        updater.interrupt();
    }

    private class Updater extends Thread {

        @Override
        public void run() {
            try {
                sleep(updatingPeriod);
            } catch (InterruptedException e) {
                return;
            }
            while (!isInterrupted()) {
                try {
                    long timeBeforeUpdate = new Date().getTime();

                    update();

                    long timeAfterUpdate = new Date().getTime();

                    long sleepTime = updatingPeriod - (timeAfterUpdate - timeBeforeUpdate);
                    if (sleepTime > 0)
                        sleep(sleepTime);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }
}

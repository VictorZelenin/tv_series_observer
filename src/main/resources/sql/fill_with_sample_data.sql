INSERT INTO country (name, language)
    VALUES
      ('USA', 'eng'),
      ('UK', 'eng'),
      ('Ukraine', 'ukr'),
      ('Switzerland', 'deu'),
      ('Russia', 'rus');


INSERT INTO translation (type, reference)
    VALUES
      ('rus_subs',''),
      ('original',''),
      ('NewStudio','http://newstudio.tv/'),
      ('AltPro','http://altpro.tv/'),
      ('ColdFilm','http://coldfilm.ru/'),
      ('Kerob','http://coldfilm.ru/');


INSERT INTO tv_series (name, country_id, begin_date, imdb)
    SELECT 'Suits', id, '2012-05-12', 8.2 FROM country WHERE name = 'USA';

INSERT INTO tv_series (name, country_id, begin_date, imdb)
    SELECT 'Luther', id, null, 8.0 FROM country WHERE name = 'UK';

INSERT INTO tv_series (name, country_id, begin_date, imdb)
  SELECT 'Flash', id, '2015-02-03', 7.3  FROM country WHERE name = 'USA';

INSERT INTO tv_series (name, country_id, begin_date, imdb)
  SELECT 'Интерны', id, '2011-02-16', 6.8 FROM country WHERE name = 'Russia';

INSERT INTO tv_series (name, country_id, begin_date, imdb)
  SELECT 'Sherlock', id, '2009-04-01', null FROM country WHERE name = 'UK';


INSERT INTO season (tv_series_id, season_number)
    SELECT id, 4 FROM tv_series WHERE name = 'Suits';

INSERT INTO season (tv_series_id, season_number)
  SELECT id, 5 FROM tv_series WHERE name = 'Suits';

INSERT INTO season (tv_series_id, season_number)
  SELECT id, 1 FROM tv_series WHERE name = 'Luther';

INSERT INTO season (tv_series_id, season_number)
  SELECT id, 2 FROM tv_series WHERE name = 'Flash';




INSERT INTO tv_episode(season_id, episode_number)
  SELECT season.id, 15
  FROM season JOIN tv_series
      ON tv_series.id = season.tv_series_id
  WHERE tv_series.name = 'Suits' AND season.season_number = 5;

INSERT INTO tv_episode(season_id, episode_number)
  SELECT season.id, 8
  FROM season JOIN tv_series
      ON tv_series.id = season.tv_series_id
  WHERE tv_series.name = 'Suits' AND season.season_number = 5;

INSERT INTO tv_episode(season_id, episode_number)
  SELECT season.id, 4
  FROM season JOIN tv_series
      ON tv_series.id = season.tv_series_id
  WHERE tv_series.name = 'Suits' AND season.season_number = 4;

INSERT INTO tv_episode(season_id, episode_number)
  SELECT season.id, 6
  FROM season JOIN tv_series
      ON tv_series.id = season.tv_series_id
  WHERE tv_series.name = 'Luther' AND season.season_number = 1;

INSERT INTO tv_episode(season_id, episode_number)
  SELECT season.id, 16
  FROM season JOIN tv_series
      ON tv_series.id = season.tv_series_id
  WHERE tv_series.name = 'Flash' AND season.season_number = 2;

INSERT INTO tv_episode(season_id, episode_number)
  SELECT season.id, 17
  FROM season JOIN tv_series
      ON tv_series.id = season.tv_series_id
  WHERE tv_series.name = 'Flash' AND season.season_number = 2;



INSERT INTO episode_translation (episode_id, translation_id, reference, date)
  SELECT tv_episode.id, translation.id ,'viewtopic.php?t=17925', '2015-07-17'
  FROM translation, tv_episode JOIN  season
      ON tv_episode.season_id = season.id
    JOIN tv_series
      ON season.tv_series_id = tv_series.id
  WHERE tv_series.name = 'Suits'
        AND season.season_number = 5
        AND tv_episode.episode_number = 15
        AND translation.type = 'NewStudio';

INSERT INTO episode_translation (episode_id, translation_id, reference, date)
  SELECT tv_episode.id, translation.id ,'viewtopic.php?t=15489', '2015-07-25 20:38:40'
  FROM translation, tv_episode JOIN  season
      ON tv_episode.season_id = season.id
    JOIN tv_series
      ON season.tv_series_id = tv_series.id
  WHERE tv_series.name = 'Suits'
        AND season.season_number = 5
        AND tv_episode.episode_number = 8
        AND translation.type = 'NewStudio';

INSERT INTO episode_translation (episode_id, translation_id, reference, date)
  SELECT tv_episode.id, translation.id ,'viewtopic.php?t=13911', '2015-07-25 20:40:40'
  FROM translation, tv_episode JOIN  season
      ON tv_episode.season_id = season.id
    JOIN tv_series
      ON season.tv_series_id = tv_series.id
  WHERE tv_series.name = 'Suits'
        AND season.season_number = 4
        AND tv_episode.episode_number = 4
        AND translation.type = 'NewStudio';

INSERT INTO episode_translation (episode_id, translation_id, reference, date)
  SELECT tv_episode.id, translation.id ,'viewtopic.php?t=2529', '2015-07-25 20:45:46'
  FROM translation, tv_episode JOIN  season
      ON tv_episode.season_id = season.id
    JOIN tv_series
      ON season.tv_series_id = tv_series.id
  WHERE tv_series.name = 'Luther'
        AND season.season_number = 1
        AND tv_episode.episode_number = 6
        AND translation.type = 'NewStudio';

INSERT INTO episode_translation (episode_id, translation_id, reference, date)
  SELECT tv_episode.id, translation.id ,'news/flehsh_2_sezon_16_serija_smotret_onlajn/2016-03-23-4099', '2015-07-25 20:45:46'
  FROM translation, tv_episode JOIN  season
      ON tv_episode.season_id = season.id
    JOIN tv_series
      ON season.tv_series_id = tv_series.id
  WHERE tv_series.name = 'Flash'
        AND season.season_number = 2
        AND tv_episode.episode_number = 16
        AND translation.type = 'ColdFilm';

INSERT INTO episode_translation (episode_id, translation_id, reference, date)
  SELECT tv_episode.id, translation.id ,'news/flehsh_2_sezon_17_serija_smotret_onlajn/2016-03-30-4157', '2016-02-14 14:00:15'
  FROM translation, tv_episode JOIN  season
      ON tv_episode.season_id = season.id
    JOIN tv_series
      ON season.tv_series_id = tv_series.id
  WHERE tv_series.name = 'Flash'
        AND season.season_number = 2
        AND tv_episode.episode_number = 17
        AND translation.type = 'ColdFilm';

INSERT INTO episode_translation (episode_id, translation_id, reference, date)
  SELECT tv_episode.id, translation.id ,'190-fleshthe-flash-2-sezon.html', '2015-07-25 20:47:46'
  FROM translation, tv_episode JOIN  season
      ON tv_episode.season_id = season.id
    JOIN tv_series
      ON season.tv_series_id = tv_series.id
  WHERE tv_series.name = 'Flash'
        AND season.season_number = 2
        AND tv_episode.episode_number = 16
        AND translation.type = 'AltPro';

INSERT INTO episode_translation (episode_id, translation_id, reference, date)
  SELECT tv_episode.id, translation.id ,'190-fleshthe-flash-2-sezon.html', '2016-02-14 04:20:15'
  FROM translation, tv_episode JOIN  season
      ON tv_episode.season_id = season.id
    JOIN tv_series
      ON season.tv_series_id = tv_series.id
  WHERE tv_series.name = 'Flash'
        AND season.season_number = 2
        AND tv_episode.episode_number = 17
        AND translation.type = 'AltPro';

INSERT INTO episode_translation (episode_id, translation_id, reference, date)
  SELECT tv_episode.id, translation.id ,'http://amovies.org/serials-rus-sub/2608-fors-mazhory.html', '2016-02-12 04:20:15'
  FROM translation, tv_episode JOIN  season
      ON tv_episode.season_id = season.id
    JOIN tv_series
      ON season.tv_series_id = tv_series.id
  WHERE tv_series.name = 'Suits'
        AND season.season_number = 5
        AND tv_episode.episode_number = 16
        AND translation.type = 'rus_subs';

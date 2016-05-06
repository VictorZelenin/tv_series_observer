CREATE INDEX imdb_tv_series_index ON tv_series(imdb);--таблица обновляется редко, поле может использоваться при поиске часто; под вопросом
CREATE INDEX date_tv_series_index ON tv_series(begin_date);--таблица обновляется редко, поле может использоваться при поиске часто; под вопросом
CREATE INDEX name_tv_series_index ON tv_series(name);--таблица обновляется редко, поле используется часто
CREATE INDEX date_episode_translation_index ON episode_translation(date);--обновляется часто, но используется ещё чаще (для обновления данных в приложении)
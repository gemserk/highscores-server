# --- !Ups

CREATE SEQUENCE game_seq START WITH 1 INCREMENT BY 1 CACHE 1;

CREATE TABLE games ( id bigint NOT NULL, apikey character varying(255), name character varying(255));

CREATE SEQUENCE guestplayer_seq START WITH 1 INCREMENT BY 1 CACHE 1;

CREATE SEQUENCE leaderboard_seq START WITH 1 INCREMENT BY 1 CACHE 1;

CREATE TABLE leaderboards ( id bigint NOT NULL, name character varying(255), game_id bigint);

CREATE SEQUENCE score_seq START WITH 1 INCREMENT BY 1 CACHE 1;

CREATE TABLE scores ( id bigint NOT NULL, data character varying(255), day integer NOT NULL, month integer NOT NULL, scope integer NOT NULL, score bigint NOT NULL, week integer NOT NULL, year integer NOT NULL, leaderboard_id bigint NOT NULL, user_id bigint NOT NULL);

CREATE SEQUENCE user_seq START WITH 1 INCREMENT BY 1 CACHE 1;

CREATE TABLE users ( id bigint NOT NULL, guest boolean NOT NULL, name character varying(255), privatekey character varying(255), userid bigint);

CREATE TABLE usersequencehelper ( id bigint NOT NULL);

ALTER TABLE games ADD CONSTRAINT games_pkey PRIMARY KEY (id);

ALTER TABLE games ADD CONSTRAINT games_apikey UNIQUE (apikey);
ALTER TABLE games ADD CONSTRAINT games_name UNIQUE (name);


ALTER TABLE leaderboards ADD CONSTRAINT leaderboards_game_id_name_key UNIQUE (game_id, name);

ALTER TABLE leaderboards ADD CONSTRAINT leaderboards_pkey PRIMARY KEY (id);

ALTER TABLE scores ADD CONSTRAINT scores_pkey PRIMARY KEY (id);

ALTER TABLE users ADD CONSTRAINT users_pkey PRIMARY KEY (id);

ALTER TABLE users ADD CONSTRAINT users_userid_key UNIQUE (userid);

ALTER TABLE usersequencehelper ADD CONSTRAINT usersequencehelper_pkey PRIMARY KEY (id);

ALTER TABLE leaderboards ADD CONSTRAINT fkaf9d88964429c99e FOREIGN KEY (game_id) REFERENCES games(id);

ALTER TABLE scores ADD CONSTRAINT fkc9e4942147140efe FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE scores ADD CONSTRAINT fkc9e4942148bf5ef6 FOREIGN KEY (leaderboard_id) REFERENCES leaderboards(id);

-- scores by user
CREATE INDEX scores_by_user ON scores (user_id);

-- scores all
CREATE INDEX scores_all ON scores (leaderboard_id, scope, score desc);

-- scores by month
CREATE INDEX scores_by_month ON scores (leaderboard_id, scope, month, score desc);
-- scores by week
CREATE INDEX scores_by_week ON scores (leaderboard_id, scope, week, score desc);
-- scores by day
CREATE INDEX scores_by_day ON scores (leaderboard_id, scope, day, score desc);

# --- !Downs

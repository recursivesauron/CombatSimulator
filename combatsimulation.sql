\c combatsimulation

CREATE TABLE characters (
    id          SERIAL          NOT NULL,
    name        varchar(255)    NOT NULL,
    class       varchar(255)    NOT NULL,
    ally        int             NOT NULL,
    hp_limit    int             NOT NULL,
    mana_limit  int             DEFAULT NULL,
    rage_limit  int             DEFAULT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE moves (
    id          SERIAL          NOT NULL,
    type        varchar(255)    NOT NULL,
    name        varchar(255)    NOT NULL,
    value       int             NOT NULL,
    mana_cost   int             NOT NULL,
    rage_cost   int             NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE enemy_sets(
    source_id   int     NOT NULL,
    target_id   int     NOT NULL,
    PRIMARY KEY(source_id, target_id),
    CONSTRAINT fk_character_source FOREIGN KEY(source_id) REFERENCES characters(id),
    CONSTRAINT fk_character_target FOREIGN KEY(target_id) REFERENCES characters(id)   
);

CREATE TABLE ally_sets(
    source_id   int     NOT NULL,
    ally_id     int     NOT NULL,
    PRIMARY KEY(source_id, ally_id),
    CONSTRAINT fk_character_source FOREIGN KEY(source_id) REFERENCES characters(id),
    CONSTRAINT fk_character_ally FOREIGN KEY(ally_id) REFERENCES characters(id)   
);

CREATE TABLE move_sets(
    character_id    int     NOT NULL,
    move_id         int     NOT NULL,
    PRIMARY KEY(character_id, move_id),
    CONSTRAINT fk_character_source FOREIGN KEY(character_id) REFERENCES characters(id),
    CONSTRAINT fk_move_id FOREIGN KEY(move_id) REFERENCES moves(id)
);
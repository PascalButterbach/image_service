DROP TABLE IF EXISTS "images";

CREATE TABLE "images"
(
    image_id       SERIAL PRIMARY KEY,
    user_id        INT       not null,
    path           TEXT      not null,
    path_thumbnail TEXT      not null,
    path_original  TEXT,
    created        timestamp not null,
    changed        timestamp,
    deleted        boolean DEFAULT false
);

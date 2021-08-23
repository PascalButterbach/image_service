DROP TABLE IF EXISTS "images";

CREATE TABLE "images"
(
    iamge_id  SERIAL PRIMARY KEY,
    user_id   INT       UNIQUE,
    filePath_min TEXT      not null,
    filePath TEXT      not null,
    filePath_max TEXT      not null,
    created   timestamp not null,
    changed   timestamp,
    deleted    boolean   not null
);

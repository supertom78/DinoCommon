CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create table dino
(
    guid                                   UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name                                varchar(255),
    species                                varchar(255),
    status                                 varchar(255),
    parc                                   varchar(255)
);
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create table dinotype
(
    guid                                   UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    species                                varchar(255),
    family                                 varchar(255),
    era                                    varchar(255),
    parc                                   varchar(255)
);
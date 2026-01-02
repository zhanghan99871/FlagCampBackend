create table if not exists cities
(
    id         serial
        constraint cities_pkey
            primary key,
    name       text not null,
    country    text,
    created_at timestamp default CURRENT_TIMESTAMP
);

create table if not exists users
(
    id            bigserial
        constraint users_pkey
            primary key,
    email         text                                   not null
        constraint users_email_key
            unique,
    password_hash text                                   not null,
    display_name  text,
    avatar_url    text,
    created_at    timestamp with time zone default now() not null,
    updated_at    timestamp with time zone default now() not null
);

create table if not exists pois
(
    id              bigserial
        constraint pois_pkey
            primary key,
    provider        text                     default 'google'::text not null,
    provider_poi_id text,
    city_id         bigint
        constraint pois_city_id_fkey
            references cities
            on delete set null,
    name            text                                            not null,
    type            text,
    rating          numeric(2, 1),
    rating_count    integer,
    price_level     smallint,
    address         text,
    phone           text,
    website         text,
    lat             double precision                                not null,
    lng             double precision                                not null,
    extra           jsonb                    default '{}'::jsonb    not null,
    created_at      timestamp with time zone default now()          not null,
    updated_at      timestamp with time zone default now()          not null,
    constraint pois_provider_provider_poi_id_key
        unique (provider, provider_poi_id)
);

create index if not exists idx_pois_city_id
    on pois (city_id);

create index if not exists idx_pois_type
    on pois (type);

create index if not exists idx_pois_rating
    on pois (rating desc);

create index if not exists idx_pois_name_trgm
    on pois using gin (name gin_trgm_ops);

create index if not exists idx_pois_extra_gin
    on pois using gin (extra);

create table if not exists itineraries
(
    id                 bigserial
        constraint itineraries_pkey
            primary key,
    user_id            bigint                                           not null
        constraint itineraries_user_id_fkey
            references users
            on delete cascade,
    city_id            bigint                                           not null
        constraint itineraries_city_id_fkey
            references cities
            on delete restrict,
    title              text                     default 'My Trip'::text not null,
    duration_days      smallint                                         not null
        constraint itineraries_duration_days_check
            check ((duration_days >= 1) AND (duration_days <= 15)),
    current_version_id bigint,
    created_at         timestamp with time zone default now()           not null,
    updated_at         timestamp with time zone default now()           not null
);

create index if not exists idx_itineraries_user_id
    on itineraries (user_id);

create index if not exists idx_itineraries_city_id
    on itineraries (city_id);

create table if not exists itinerary_versions
(
    id           bigserial
        constraint itinerary_versions_pkey
            primary key,
    itinerary_id bigint                                 not null
        constraint itinerary_versions_itinerary_id_fkey
            references itineraries
            on delete cascade,
    version_no   integer                                not null,
    name         text,
    is_published boolean                  default false not null,
    data         jsonb                                  not null,
    created_at   timestamp with time zone default now() not null,
    updated_at   timestamp with time zone default now() not null,
    constraint itinerary_versions_itinerary_id_version_no_key
        unique (itinerary_id, version_no)
);

alter table itineraries
    add constraint fk_itineraries_current_version
        foreign key (current_version_id) references itinerary_versions
            on delete set null;

create index if not exists idx_itinerary_versions_itinerary_id
    on itinerary_versions (itinerary_id);

create index if not exists idx_itinerary_versions_data_gin
    on itinerary_versions using gin (data);

create table if not exists user_favorites
(
    user_id    bigint                                 not null
        constraint user_favorites_user_id_fkey
            references users
            on delete cascade,
    poi_id     bigint                                 not null
        constraint user_favorites_poi_id_fkey
            references pois
            on delete cascade,
    note       text,
    created_at timestamp with time zone default now() not null,
    constraint user_favorites_pkey
        primary key (user_id, poi_id)
);

create index if not exists idx_user_favorites_poi_id
    on user_favorites (poi_id);

create table if not exists poi_recommendations
(
    id         bigserial
        constraint poi_recommendations_pkey
            primary key,
    city_id    bigint                                 not null
        constraint poi_recommendations_city_id_fkey
            references cities
            on delete cascade,
    poi_id     bigint                                 not null
        constraint poi_recommendations_poi_id_fkey
            references pois
            on delete cascade,
    category   text,
    rank       integer                                not null
        constraint ck_poi_reco_rank_positive
            check (rank > 0),
    score      numeric(8, 4),
    reason     text,
    created_at timestamp with time zone default now() not null,
    updated_at timestamp with time zone default now() not null,
    constraint uq_poi_reco_city_category_rank
        unique (city_id, category, rank),
    constraint uq_poi_reco_city_category_poi
        unique (city_id, category, poi_id)
);

create index if not exists idx_poi_reco_city_category_rank
    on poi_recommendations (city_id, category, rank);

create index if not exists idx_poi_reco_city_category_poi
    on poi_recommendations (city_id, category, poi_id);


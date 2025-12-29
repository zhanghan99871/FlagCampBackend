DROP TABLE IF EXISTS itinerary_versions CASCADE;
DROP TABLE IF EXISTS itineraries CASCADE;
DROP TABLE IF EXISTS pois CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS cities CASCADE;
DROP TABLE IF EXISTS city CASCADE;

CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE TABLE cities
(
    id         BIGSERIAL PRIMARY KEY NOT NULL,
    name       TEXT                  NOT NULL,
    country    TEXT,
    lat        DOUBLE PRECISION,
    lng        DOUBLE PRECISION,
    created_at TIMESTAMPTZ DEFAULT now(),
    updated_at TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE users
(
    id           BIGSERIAL PRIMARY KEY NOT NULL,
    email        TEXT UNIQUE            NOT NULL,
    password_hash TEXT                 NOT NULL,
    display_name TEXT,
    avatar_url   TEXT,
    created_at   TIMESTAMPTZ DEFAULT now(),
    updated_at   TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE pois
(
    id            BIGSERIAL PRIMARY KEY NOT NULL,
    provider      TEXT                  NOT NULL DEFAULT 'google',
    provider_poi_id TEXT,
    city_id       BIGINT,
    name          TEXT                  NOT NULL,
    type          TEXT,
    rating        NUMERIC(2, 1),
    rating_count  INTEGER,
    price_level   SMALLINT,
    address       TEXT,
    phone         TEXT,
    website       TEXT,
    lat           DOUBLE PRECISION     NOT NULL,
    lng           DOUBLE PRECISION     NOT NULL,
    extra         JSONB                NOT NULL DEFAULT '{}'::jsonb,
    created_at    TIMESTAMPTZ DEFAULT now(),
    updated_at    TIMESTAMPTZ DEFAULT now(),
    CONSTRAINT fk_pois_city FOREIGN KEY (city_id) REFERENCES cities (id) ON DELETE SET NULL,
    UNIQUE (provider, provider_poi_id)
);

CREATE TABLE itineraries
(
    id                BIGSERIAL PRIMARY KEY NOT NULL,
    user_id           BIGINT                NOT NULL,
    city_id           BIGINT                NOT NULL,
    title             TEXT                  NOT NULL DEFAULT 'My Trip',
    duration_days     SMALLINT              NOT NULL,
    current_version_id BIGINT,
    created_at        TIMESTAMPTZ DEFAULT now(),
    updated_at        TIMESTAMPTZ DEFAULT now(),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_itineraries_city FOREIGN KEY (city_id) REFERENCES cities (id) ON DELETE RESTRICT,
    CHECK (duration_days BETWEEN 1 AND 15)
);

CREATE TABLE itinerary_versions
(
    id           BIGSERIAL PRIMARY KEY NOT NULL,
    itinerary_id BIGINT                NOT NULL,
    version_no   INTEGER               NOT NULL,
    name         TEXT,
    is_published BOOLEAN               NOT NULL DEFAULT false,
    data         JSONB                 NOT NULL,
    created_at   TIMESTAMPTZ DEFAULT now(),
    updated_at   TIMESTAMPTZ DEFAULT now(),
    CONSTRAINT fk_itinerary FOREIGN KEY (itinerary_id) REFERENCES itineraries (id) ON DELETE CASCADE,
    UNIQUE (itinerary_id, version_no)
);

CREATE INDEX idx_pois_city_id ON pois (city_id);
CREATE INDEX idx_pois_type ON pois (type);
CREATE INDEX idx_pois_rating ON pois (rating DESC);
CREATE INDEX idx_pois_name_trgm ON pois USING gin (name gin_trgm_ops);
CREATE INDEX idx_pois_extra_gin ON pois USING gin (extra);
CREATE INDEX idx_itineraries_user_id ON itineraries (user_id);
CREATE INDEX idx_itineraries_city_id ON itineraries (city_id);
CREATE INDEX idx_itinerary_versions_itinerary_id ON itinerary_versions (itinerary_id);
CREATE INDEX idx_itinerary_versions_data_gin ON itinerary_versions USING gin (data);

INSERT INTO cities (name, country, lat, lng)
VALUES ('New York City', 'United States', 40.7128, -74.0060);

INSERT INTO pois (provider, provider_poi_id, city_id, name, type, rating, rating_count, price_level, address, phone, website, lat, lng, extra)
VALUES
    ('google', 'ChIJMetropolitanMuseum', (SELECT id FROM cities WHERE name = 'New York City'), 'The Metropolitan Museum of Art', 'museum', 4.7, 125000, 2, '1000 5th Ave, New York, NY 10028', '+1 212-535-7710', 'https://www.metmuseum.org', 40.7794, -73.9632, '{"open_now": true, "opening_hours": {"monday": "10:00-17:00", "tuesday": "10:00-17:00", "wednesday": "10:00-17:00", "thursday": "10:00-17:00", "friday": "10:00-21:00", "saturday": "10:00-21:00", "sunday": "10:00-17:00"}}'::jsonb),
    ('google', 'ChIJMoMA', (SELECT id FROM cities WHERE name = 'New York City'), 'The Museum of Modern Art', 'museum', 4.6, 98000, 2, '11 W 53rd St, New York, NY 10019', '+1 212-708-9400', 'https://www.moma.org', 40.7614, -73.9776, '{"open_now": true, "opening_hours": {"monday": "10:30-17:30", "tuesday": "10:30-17:30", "wednesday": "10:30-17:30", "thursday": "10:30-17:30", "friday": "10:30-20:00", "saturday": "10:30-20:00", "sunday": "10:30-17:30"}}'::jsonb),
    ('google', 'ChIJGuggenheim', (SELECT id FROM cities WHERE name = 'New York City'), 'Solomon R. Guggenheim Museum', 'museum', 4.5, 45000, 2, '1071 5th Ave, New York, NY 10128', '+1 212-423-3500', 'https://www.guggenheim.org', 40.7831, -73.9590, '{"open_now": true, "opening_hours": {"monday": "11:00-18:00", "tuesday": "11:00-18:00", "wednesday": "11:00-18:00", "thursday": "11:00-18:00", "friday": "11:00-18:00", "saturday": "11:00-18:00", "sunday": "11:00-18:00"}}'::jsonb),
    ('google', 'ChIJStatueOfLiberty', (SELECT id FROM cities WHERE name = 'New York City'), 'Statue of Liberty', 'landmark', 4.7, 200000, 1, 'New York, NY 10004', '+1 212-363-3200', 'https://www.nps.gov/stli', 40.6892, -74.0445, '{"open_now": true, "tags": ["monument", "national_park", "unesco"]}'::jsonb),
    ('google', 'ChIJEmpireState', (SELECT id FROM cities WHERE name = 'New York City'), 'Empire State Building', 'landmark', 4.6, 180000, 2, '350 5th Ave, New York, NY 10118', '+1 212-736-3100', 'https://www.esbnyc.com', 40.7484, -73.9857, '{"open_now": true, "tags": ["skyscraper", "observation_deck"]}'::jsonb),
    ('google', 'ChIJTimesSquare', (SELECT id FROM cities WHERE name = 'New York City'), 'Times Square', 'landmark', 4.3, 150000, 2, 'Manhattan, NY 10036', NULL, NULL, 40.7580, -73.9855, '{"open_now": true, "tags": ["entertainment", "shopping", "theater"]}'::jsonb),
    ('google', 'ChIJC BrooklynBridge', (SELECT id FROM cities WHERE name = 'New York City'), 'Brooklyn Bridge', 'landmark', 4.8, 95000, 0, 'New York, NY 10038', NULL, NULL, 40.7061, -73.9969, '{"open_now": true, "tags": ["bridge", "walking", "photography"]}'::jsonb),
    ('google', 'ChIJCentralPark', (SELECT id FROM cities WHERE name = 'New York City'), 'Central Park', 'park', 4.8, 250000, 0, 'New York, NY', NULL, 'https://www.centralparknyc.org', 40.7829, -73.9654, '{"open_now": true, "tags": ["nature", "recreation", "walking", "jogging"]}'::jsonb),
    ('google', 'ChIJHighLine', (SELECT id FROM cities WHERE name = 'New York City'), 'The High Line', 'park', 4.6, 120000, 0, 'New York, NY 10011', '+1 212-500-6035', 'https://www.thehighline.org', 40.7480, -74.0048, '{"open_now": true, "tags": ["elevated_park", "art", "walking"]}'::jsonb),
    ('google', 'ChIJLeBernardin', (SELECT id FROM cities WHERE name = 'New York City'), 'Le Bernardin', 'food', 4.8, 3500, 4, '155 W 51st St, New York, NY 10019', '+1 212-554-1515', 'https://www.le-bernardin.com', 40.7614, -73.9816, '{"open_now": false, "tags": ["fine_dining", "french", "seafood", "michelin_star"]}'::jsonb),
    ('google', 'ChIJPeterLuger', (SELECT id FROM cities WHERE name = 'New York City'), 'Peter Luger Steak House', 'food', 4.5, 8500, 4, '178 Broadway, Brooklyn, NY 11249', '+1 718-387-7400', 'https://www.peterluger.com', 40.7087, -73.9627, '{"open_now": false, "tags": ["steakhouse", "american", "cash_only"]}'::jsonb),
    ('google', 'ChIJJoeShanghai', (SELECT id FROM cities WHERE name = 'New York City'), 'Joe''s Shanghai', 'food', 4.2, 12000, 2, '9 Pell St, New York, NY 10013', '+1 212-233-8888', NULL, 40.7146, -73.9981, '{"open_now": true, "tags": ["chinese", "dumplings", "soup_dumplings"]}'::jsonb),
    ('google', 'ChIJKatzDeli', (SELECT id FROM cities WHERE name = 'New York City'), 'Katz''s Delicatessen', 'food', 4.4, 28000, 2, '205 E Houston St, New York, NY 10002', '+1 212-254-2246', 'https://www.katzsdelicatessen.com', 40.7224, -73.9875, '{"open_now": true, "tags": ["deli", "pastrami", "sandwiches", "iconic"]}'::jsonb),
    ('google', 'ChIJShakeShack', (SELECT id FROM cities WHERE name = 'New York City'), 'Shake Shack', 'food', 4.3, 45000, 2, 'Multiple locations', NULL, 'https://www.shakeshack.com', 40.7489, -73.9850, '{"open_now": true, "tags": ["burgers", "fast_casual", "shakes"]}'::jsonb),
    ('google', 'ChIJBroadway', (SELECT id FROM cities WHERE name = 'New York City'), 'Broadway', 'entertainment', 4.7, 180000, 3, 'New York, NY 10036', NULL, 'https://www.broadway.com', 40.7614, -73.9776, '{"open_now": true, "tags": ["theater", "musicals", "shows"]}'::jsonb),
    ('google', 'ChIJRadioCity', (SELECT id FROM cities WHERE name = 'New York City'), 'Radio City Music Hall', 'entertainment', 4.6, 55000, 3, '1260 6th Ave, New York, NY 10020', '+1 212-465-6741', 'https://www.radiocity.com', 40.7596, -73.9797, '{"open_now": true, "tags": ["concert_hall", "rockettes", "events"]}'::jsonb);

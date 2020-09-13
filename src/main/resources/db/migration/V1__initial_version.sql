create extension if not exists "uuid-ossp";

create table band (
    id uuid primary key default uuid_generate_v4(),
    name varchar(100) not null unique,
    singer varchar(100),
    established DATE not null
);

create table album (
    id uuid primary key default uuid_generate_v4(),
    name varchar(100) not null,
    year DATE not null,
    band_id uuid not null references band(id) on delete cascade
);

create table track (
    id uuid primary key default uuid_generate_v4(),
    name varchar(100) not null,
    duration real not null,
    album_id uuid not null references album(id) on delete cascade
);

insert into band(id, name, singer, established) values
    ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Papa Roach', 'Jacoby Dakota Shaddix', date '1993-1-1'),
    ('af7b5a22-b35d-42e4-9d20-f1613966f057', 'Red Hot Chili Peppers', 'Anthony Kiedis', date '1983-1-1');

insert into album(id, name, year, band_id) values
    ('7be2f3ea-994b-4ba2-b6a6-60da916ca61c', 'Getting away with murder', date '2004-7-31', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11'),
    ('27d3198e-a547-4322-a3eb-3c86459e194d','Californication', date '1999-6-8', 'af7b5a22-b35d-42e4-9d20-f1613966f057');

insert into track (id, name, duration, album_id) values
    ('2bac8810-c89e-4a1f-9efb-216d18bc9975', 'Getting away with murder', 3.1, '7be2f3ea-994b-4ba2-b6a6-60da916ca61c'),
    ('c676b46f-2bf7-4e89-b258-af0ed6744d58', 'Not listening', 3.09, '7be2f3ea-994b-4ba2-b6a6-60da916ca61c'),
    ('d505e645-3b96-4e9b-aeca-fd6801d9eeb9','Around the world', 3.59, '27d3198e-a547-4322-a3eb-3c86459e194d'),
    ('30b978fb-5963-4ca9-a6b8-3a7389cb7708', 'Get on top', 3.18, '27d3198e-a547-4322-a3eb-3c86459e194d'),
    ('4a8f3435-3ee5-46be-a96f-d19fbef9ac99', 'Easily', 3.51, '27d3198e-a547-4322-a3eb-3c86459e194d');

CREATE DATABASE bandVenues;
CREATE TABLE bands (id serial PRIMARY KEY, name varchar, genre varchar, phone varchar);
CREATE TABLE venues (id serial PRIMARY KEY, name varchar, address varchar, phone varchar);
CREATE TABLE bands_venues (id serial PRIMARY KEY, band_id int, venue_id int);
CREATE DATABASE bandvenues_test WITH bandvenues;

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  svenseemann
 * Created: 24.02.2018
 */

DROP TABLE IF EXISTS industries;
DROP SEQUENCE IF EXISTS industries_id_seq;
CREATE SEQUENCE industries_id_seq;
CREATE TABLE industries (
    id INTEGER DEFAULT NEXTVAL('industries_id_seq'),
    name VARCHAR(255),
    primary key (id)
);

DROP TABLE IF EXISTS interests;
DROP SEQUENCE IF EXISTS interests_id_seq;
CREATE SEQUENCE interests_id_seq;
CREATE TABLE interests (
    id INTEGER DEFAULT NEXTVAL('interests_id_seq'),
    description VARCHAR(255),
    name VARCHAR(255),
    primary key (id)
);

DROP TABLE IF EXISTS jobs;
DROP SEQUENCE IF EXISTS jobs_id_seq;
CREATE SEQUENCE jobs_id_seq;
CREATE TABLE jobs (
    id INTEGER DEFAULT NEXTVAL('jobs_id_seq'),
    employer VARCHAR(255),
    industry VARCHAR(255),
    title VARCHAR(255),
    user_id INTEGER,
    primary key (id)
);

DROP TABLE IF EXISTS location;
DROP SEQUENCE IF EXISTS location_id_seq;
CREATE SEQUENCE location_id_seq;
CREATE TABLE location (
    id INTEGER  DEFAULT NEXTVAL('location_id_seq'),
    city VARCHAR(255),
    country VARCHAR(255),
    employer VARCHAR(255),
    zip VARCHAR(255),
    primary key (id)
);

DROP TABLE IF EXISTS scr_user;
DROP SEQUENCE IF EXISTS scr_user_id_seq;
CREATE SEQUENCE scr_user_id_seq;
CREATE TABLE scr_user (
    id INTEGER  DEFAULT NEXTVAL('scr_user_id_seq'),
    forename VARCHAR(255),
    lastname VARCHAR(255),
    location bytea,
    mail VARCHAR(255),
    password VARCHAR(255),
    roles bytea,
    skills bytea,
    username VARCHAR(255),
    primary key (id)
);

DROP TABLE IF EXISTS scr_user_jobs;
CREATE TABLE scr_user_jobs (
    application_user_id INTEGER not null,
    jobs_id INTEGER not null
);
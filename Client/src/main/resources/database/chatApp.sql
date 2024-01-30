drop database if exists ;
create database chatApp;
use chatApp;
create table users(
    userId varchar(20),
    userName varchar(20),
    passWord varchar(100)
   );
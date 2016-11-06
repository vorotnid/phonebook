# Web application "Phonebook"

Application is compiled by version of Java 8. Maven 3 uses for building a project.

Application "Phonebook" contains five pages: register, login, main page with contact list and search by first name,
last name and phone number and form to add new contact, page to edit existing contact and error page.
Only authenticated users can access the system. Non-authorized users redirects to login page.

Technology stack: Spring (Boot, Security, Mvc, Jdbc, DI), JUnit, Mockito, Thymeleaf, Maven, MySql, Bootstrap.

Application starts with settings in .property file directory which must be passed as an argument.

Application implements two data storage types - file storage or database.
You must select on of those in application.properties file by setting dataStorage.type propery. Set "database" value for database and "fileStorage" value for file storage.

Property file contains database settings and directory for file storage.

SQL queries to create tables:

create table user (
id bigint unique not null auto_increment,
username varchar(50) not null unique,
password varchar(50) not null,
first_name varchar (50) not null,
last_name varchar(50) not null,
middle_name varchar(50) not null,
primary key (id));

create table contact (
id bigint unique not null auto_increment,
first_name varchar (50) not null,
last_name varchar(50) not null,
middle_name varchar(50) not null,
cell_phone varchar(15) not null unique,
home_phone varchar(15),
address varchar(120),
email varchar(40),
user_id bigint not null,
primary key(id),
foreign key (user_id) references user(id) );
DROP DATABASE IF EXISTS moviedb;
CREATE DATABASE moviedb;
USE moviedb;

CREATE TABLE IF NOT EXISTS movies(
id VARCHAR(10) NOT NULL PRIMARY KEY,
title VARCHAR(100) NOT NULL,
year INT NOT NULL,
director VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS stars(
id VARCHAR(10) NOT NULL PRIMARY KEY,
name VARCHAR(100) NOT NULL,
birthYear INT
);

CREATE TABLE IF NOT EXISTS stars_in_movies(
starId VARCHAR(10) NOT NULL,
movieId VARCHAR(10) NOT NULL,
FOREIGN KEY (starId) REFERENCES stars(id) ON DELETE CASCADE,
FOREIGN KEY (movieId) REFERENCES movies(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS genres(
id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
name VARCHAR(32) NOT NULL
);

CREATE TABLE IF NOT EXISTS employees(
email varchar(5) primary key,
password varchar(20) not null,
fullname varchar(100)
);

CREATE TABLE IF NOT EXISTS genres_in_movies(
genreId INT NOT NULL,
movieId VARCHAR(10) NOT NULL,
FOREIGN KEY (genreId) REFERENCES genres(id) ON DELETE CASCADE,
FOREIGN KEY (movieId) REFERENCES movies(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS creditcards(
id VARCHAR(20) NOT NULL PRIMARY KEY,
firstName VARCHAR(50) NOT NULL,
lastName VARCHAR(50) NOT NULL,
expiration DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS customers(
id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
firstName VARCHAR(50) NOT NULL,
lastName VARCHAR(50) NOT NULL,
ccId VARCHAR(20) NOT NULL,
address VARCHAR(200) NOT NULL,
email VARCHAR(50) NOT NULL, 
password VARCHAR(20) NOT NULL,
FOREIGN KEY (ccId) REFERENCES creditcards(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS sales(
id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
customerId INT NOT NULL,
movieId VARCHAR(10) NOT NULL,
saleDate DATE NOT NULL,
FOREIGN KEY (customerId) REFERENCES customers(id) ON DELETE CASCADE,
FOREIGN KEY (movieId) REFERENCES movies(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS ratings(
movieId VARCHAR(10) NOT NULL,
rating FLOAT NOT NULL,
numVotes INT NOT NULL,
FOREIGN KEY (movieId) REFERENCES movies(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS employees(
email varchar(50) primary key,
password varchar(20) not null,
fullname varchar(100)
);

drop procedure if exists addMovie;



delimiter $$

create procedure addMovie( 

IN movieTitle varchar(100), 

IN movieYear int, 

IN movieDirector varchar(100), 

IN starName varchar(100), 

IN genreName varchar(32),

IN movieMID varchar(10),

IN starMID varchar(10),

OUT movieMessage varchar(100),

OUT starMessage varchar(100),

OUT genreMessage varchar(100))


begin



declare genresID int default 0;

declare genresMID int default 0;

declare starsID int default 0;

declare genre_count int default 0;

declare movie_count int default 0;

declare star_count int default 0;



set movie_count = 

(select count(*) from movies

join stars_in_movies on stars_in_movies.movieId = movies.id

join genres_in_movies on genres_in_movies.movieId = movies.id

join genres on genres.id = genres_in_movies.genreId

join stars on stars.id = stars_in_movies.starId

where movies.title = movieTitle and movies.year = movieYear and movies.director = movieDirector);

  
   
IF movie_count > 0 THEN
		  
set movieMessage = "Movie already exists, no changes to database made.";
	 
else
     
insert into movies(id, title, year, director)
	 
values(movieMID, movieTitle, movieYear, movieDirector);


end if;



select count(*), stars.id

into star_count, @starsID

from stars

where stars.name = starName;



if star_count = 0 and movie_count = 0 then

insert into stars(id, name, birthYear)

values(starMID, starName, null);


insert into stars_in_movies(starId, movieId)

values(starMID, movieMID);

set starMessage = "Star name updated";

end if;



if star_count > 0 and movie_count = 0 then

insert into stars_in_movies(starId, movieId)

values(@starsID, movieMID);

set starMessage = "Star name updated";

end if;



select count(*), genres.id

into genre_count, @genresID

from genres 
where genres.name = genreName;



set @genresMID = (select max(id) + 1 from genres);



if genre_count = 0 and movie_count = 0 then

insert into genres(id, name)

values(@genresMID, genreName);

insert into genres_in_movies(genreId, movieId)

values(@genresMID, movieMID);

set genreMessage = "Genre name updated";

end if;





if genre_count > 0 and movie_count = 0 then

insert into genres_in_movies(genreId, movieId)

values(@genresID, movieMID);

set genreMessage = "Genre name updated";

end if;




if movie_count > 0 then

set movieMessage = "Movie already exists, no changes to database made.";

else

set movieMessage = "Movie has been added to the database.";

end if;




end$$



delimiter ;



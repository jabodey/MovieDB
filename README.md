
MAIN PAGE:
group by movies.id having movies.title LIKE letter% to find titles with starting letter from mainPage
group by movies.id having genreNames (group_concat(distinct(genres.name)) LIKE %genre% to find genres containing genre from mainPage

IN SEARCH FUNCTION:
movies.title LIKE %title% find titles containing input title string
movies.year LIKE %year% find year containing input year string
movies.director LIKE %director% find director containing input director string
group by movies.id having starNames LIKE %name% find input star name from list of star names for movie 
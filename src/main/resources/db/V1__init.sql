CREATE TABLE IF NOT EXISTS user(
    id UUID primary key not null, 
    name varchar(256) not null,
    date_joined DATE not null
    );

CREATE TABLE IF NOT EXISTS library(
    id UUID primary key not null,
    user_id UUID primary key not null references user(id),
    title varchar(256) not null,
    lib_desc varchar(512) not null,
);

CREATE TABLE IF NOT EXISTS book(
    id UUID primary key not null,
    title varchar(256) not null,
    author varchar(256) not null,
);

CREATE TABLE IF NOT EXISTS page(
    page_num int not null,
    book_id UUID not null references book(id),
    contents varchar(2048) not null,
    primary key (page_num, book_id)
);

CREATE TABLE IF NOT EXISTS library_book_bridge(
    library_id UUID not null references library(id),
    book_id UUID not null references book(id),
    primary key (library_id, book_id)
);
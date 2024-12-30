create sequence book_seq start with 1 increment by 50;
create table book (id bigint not null, title varchar(255), isbn varchar(255), primary key (id));
truncate table comments cascade;
select setval('comments_id_seq', 1, false);

truncate table news cascade;
select setval('news_id_seq', 1, false);

truncate table authors cascade;
select setval('authors_id_seq', 1, false);

insert into authors (name)
values ('Ben Brown');

insert into news (time, title, text, author_id)
values ('2024-02-20T11:17:15', 'news title', 'news text', 1);

insert into comments (time, text, username, news_id, author_id)
values ('2024-02-21T20:15:13', 'comment text', 'Piter Parker', 1, 1);

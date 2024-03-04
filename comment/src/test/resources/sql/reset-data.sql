truncate table comments cascade;
select setval('comments_id_seq', 1, false);

truncate table news cascade;
select setval('news_id_seq', 1, false);

truncate table authors cascade;
select setval('authors_id_seq', 1, false);

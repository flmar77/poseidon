insert into bid(account, type, bidQuantity)
values ('accountTest', 'typeTest', 1.11);
insert into curve(curveId, term, value)
values (1, 1.0, 1.0);
insert into rating(orderNumber, moodysRating, sandPrating, fitchRating)
values (1, 'a', 'b', 'c');
insert into rule(name, description, json, template, sqlStr, sqlPart)
values ('ruleTest', 'a', 'b', 'c', 'd', 'e');
insert into trade(account, type, buyQuantity)
values ('accountTest', 'a', 1.11);
DROP TABLE IF EXISTS bidlist CASCADE;
CREATE TABLE bidlist
(
    bidListId    INTEGER     NOT NULL AUTO_INCREMENT,
    account      VARCHAR(30) NOT NULL,
    type         VARCHAR(30) NOT NULL,
    bidQuantity  DOUBLE,
    askQuantity  DOUBLE,
    bid          DOUBLE,
    ask          DOUBLE,
    benchmark    VARCHAR(125),
    bidListDate  TIMESTAMP,
    commentary   VARCHAR(125),
    security     VARCHAR(125),
    status       VARCHAR(10),
    trader       VARCHAR(125),
    book         VARCHAR(125),
    creationName VARCHAR(125),
    creationDate TIMESTAMP,
    revisionName VARCHAR(125),
    revisionDate TIMESTAMP,
    dealName     VARCHAR(125),
    dealType     VARCHAR(125),
    sourceListId VARCHAR(125),
    side         VARCHAR(125),

    PRIMARY KEY (bidListId)
);

DROP TABLE IF EXISTS curvepoint CASCADE;
CREATE TABLE curvepoint
(
    id           INTEGER NOT NULL AUTO_INCREMENT,
    curveId      INTEGER,
    asOfDate     TIMESTAMP,
    term         DOUBLE,
    value        DOUBLE,
    creationDate TIMESTAMP,

    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS rating CASCADE;
CREATE TABLE rating
(
    id           INTEGER NOT NULL AUTO_INCREMENT,
    moodysRating VARCHAR(125),
    sandPrating  VARCHAR(125),
    fitchRating  VARCHAR(125),
    orderNumber  INTEGER,

    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS rulename CASCADE;
CREATE TABLE rulename
(
    id          INTEGER NOT NULL AUTO_INCREMENT,
    name        VARCHAR(125),
    description VARCHAR(125),
    json        VARCHAR(125),
    template    VARCHAR(512),
    sqlStr      VARCHAR(125),
    sqlPart     VARCHAR(125),

    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS trade CASCADE;
CREATE TABLE trade
(
    tradeId      INTEGER     NOT NULL AUTO_INCREMENT,
    account      VARCHAR(30) NOT NULL,
    type         VARCHAR(30) NOT NULL,
    buyQuantity  DOUBLE,
    sellQuantity DOUBLE,
    buyPrice     DOUBLE,
    sellPrice    DOUBLE,
    tradeDate    TIMESTAMP,
    security     VARCHAR(125),
    status       VARCHAR(10),
    trader       VARCHAR(125),
    benchmark    VARCHAR(125),
    book         VARCHAR(125),
    creationName VARCHAR(125),
    creationDate TIMESTAMP,
    revisionName VARCHAR(125),
    revisionDate TIMESTAMP,
    dealName     VARCHAR(125),
    dealType     VARCHAR(125),
    sourceListId VARCHAR(125),
    side         VARCHAR(125),

    PRIMARY KEY (tradeId)
);

DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE users
(
    id       INTEGER NOT NULL AUTO_INCREMENT,
    userName VARCHAR(125),
    password VARCHAR(125),
    fullName VARCHAR(125),
    role     VARCHAR(125),

    PRIMARY KEY (id)
);

insert into Users(fullname, username, password, role)
values ('Administrator', 'admin', '$2y$10$HDwKmPe5eemXFtwk0myEa.lKfIr7rem6arw8DfmnharShY4qVKSQq', 'ADMIN');
insert into Users(fullname, username, password, role)
values ('User', 'user', '$2y$10$9AJN38g1qI1DDRyC/dDFzeYqG2.S4O/AUIEcNMw/omAZwQ.eakqLu', 'USER');
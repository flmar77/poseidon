DROP TABLE IF EXISTS bid CASCADE;
CREATE TABLE bid
(
    id           INTEGER     NOT NULL AUTO_INCREMENT,
    account      VARCHAR(30) NOT NULL UNIQUE,
    type         VARCHAR(30) NOT NULL,
    bidQuantity  DOUBLE      NOT NULL,
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

    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS curve CASCADE;
CREATE TABLE curve
(
    id           INTEGER NOT NULL AUTO_INCREMENT,
    curveId      INTEGER NOT NULL UNIQUE,
    term         DOUBLE  NOT NULL,
    value        DOUBLE  NOT NULL,
    asOfDate     TIMESTAMP,
    creationDate TIMESTAMP,

    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS rating CASCADE;
CREATE TABLE rating
(
    id           INTEGER      NOT NULL AUTO_INCREMENT,
    orderNumber  INTEGER      NOT NULL UNIQUE,
    moodysRating VARCHAR(125) NOT NULL,
    sandPrating  VARCHAR(125) NOT NULL,
    fitchRating  VARCHAR(125) NOT NULL,

    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS rule CASCADE;
CREATE TABLE rule
(
    id          INTEGER      NOT NULL AUTO_INCREMENT,
    name        VARCHAR(125) NOT NULL UNIQUE,
    description VARCHAR(125) NOT NULL,
    json        VARCHAR(125) NOT NULL,
    template    VARCHAR(512) NOT NULL,
    sqlStr      VARCHAR(125) NOT NULL,
    sqlPart     VARCHAR(125) NOT NULL,

    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS trade CASCADE;
CREATE TABLE trade
(
    id           INTEGER     NOT NULL AUTO_INCREMENT,
    account      VARCHAR(30) NOT NULL UNIQUE,
    type         VARCHAR(30) NOT NULL,
    buyQuantity  DOUBLE      NOT NULL,
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

    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS user CASCADE;
CREATE TABLE user
(
    id       INTEGER      NOT NULL AUTO_INCREMENT,
    userName VARCHAR(125) NOT NULL UNIQUE,
    password VARCHAR(125) NOT NULL,
    fullName VARCHAR(125) NOT NULL,
    role     VARCHAR(125) NOT NULL,

    PRIMARY KEY (id)
);

insert into user(fullName, userName, password, role)
values ('Administrator', 'admin', '$2y$10$HDwKmPe5eemXFtwk0myEa.lKfIr7rem6arw8DfmnharShY4qVKSQq', 'ADMIN');
insert into user(fullName, userName, password, role)
values ('User', 'user', '$2y$10$9AJN38g1qI1DDRyC/dDFzeYqG2.S4O/AUIEcNMw/omAZwQ.eakqLu', 'USER');
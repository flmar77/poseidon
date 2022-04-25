DROP TABLE IF EXISTS bid CASCADE;
CREATE TABLE bid
(
    id           INTEGER       NOT NULL AUTO_INCREMENT,
    account      VARCHAR(255)  NOT NULL UNIQUE,
    type         VARCHAR(255)  NOT NULL,
    bidQuantity  DOUBLE(10, 2) NOT NULL CHECK ( bidQuantity >= 0 ),
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
    id           INTEGER       NOT NULL AUTO_INCREMENT,
    curveId      INTEGER(8)    NOT NULL UNIQUE CHECK ( curveId >= 0 ),
    term         DOUBLE(10, 2) NOT NULL,
    value        DOUBLE(10, 2) NOT NULL,
    asOfDate     TIMESTAMP,
    creationDate TIMESTAMP,

    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS rating CASCADE;
CREATE TABLE rating
(
    id           INTEGER      NOT NULL AUTO_INCREMENT,
    orderNumber  INTEGER(8)   NOT NULL UNIQUE CHECK ( orderNumber >= 0),
    moodysRating VARCHAR(255) NOT NULL,
    sandPrating  VARCHAR(255) NOT NULL,
    fitchRating  VARCHAR(255) NOT NULL,

    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS rule CASCADE;
CREATE TABLE rule
(
    id          INTEGER      NOT NULL AUTO_INCREMENT,
    name        VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(255) NOT NULL,
    json        VARCHAR(255) NOT NULL,
    template    VARCHAR(255) NOT NULL,
    sqlStr      VARCHAR(255) NOT NULL,
    sqlPart     VARCHAR(255) NOT NULL,

    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS trade CASCADE;
CREATE TABLE trade
(
    id           INTEGER       NOT NULL AUTO_INCREMENT,
    account      VARCHAR(255)  NOT NULL UNIQUE,
    type         VARCHAR(255)  NOT NULL,
    buyQuantity  DOUBLE(10, 2) NOT NULL,
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
    userName VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    fullName VARCHAR(255) NOT NULL,
    role     VARCHAR(255) NOT NULL,

    PRIMARY KEY (id)
);

insert into user(fullName, userName, password, role)
values ('Administrator', 'admin', '$2y$10$HDwKmPe5eemXFtwk0myEa.lKfIr7rem6arw8DfmnharShY4qVKSQq', 'ADMIN');
insert into user(fullName, userName, password, role)
values ('User', 'user', '$2y$10$9AJN38g1qI1DDRyC/dDFzeYqG2.S4O/AUIEcNMw/omAZwQ.eakqLu', 'USER');
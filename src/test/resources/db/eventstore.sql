CREATE SCHEMA eventstore;

CREATE TABLE eventstore.events(
    ID IDENTITY NOT NULL PRIMARY KEY,
    REFERENCE VARCHAR(255) NOT NULL,
    EVENTGROUP VARCHAR(100) NOT NULL,
    EVENT VARCHAR(100) NOT NULL,
    AGGREGATE VARCHAR(255) NOT NULL,
    SEQUENCE INT NOT NULL ,
    VERSION INT NOT NULL,
    PAYLOAD TEXT,
    CREATED DATE,
    CREATOR VARCHAR(255)
)
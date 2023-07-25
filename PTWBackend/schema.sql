-- Create database
CREATE DATABASE PTW;

USE PTW;

-- Create table User
CREATE TABLE User (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    company VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uc_email UNIQUE (email)
);

-- Create table Request
CREATE TABLE Request (
    id INT NOT NULL AUTO_INCREMENT,
    type VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    company VARCHAR(255) NOT NULL,
    equipment VARCHAR(255) NOT NULL,
    startdate TIMESTAMP NOT NULL, 
    enddate TIMESTAMP NOT NULL,
    locations VARCHAR(255) NOT NULL,
    comment VARCHAR(255),
    status VARCHAR(255) NOT NULL,
    user_email VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_email) REFERENCES User (email)
);

CREATE TABLE Approval (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    company VARCHAR(255) NOT NULL,
    workarea VARCHAR(255) NOT NULL,
    ppe VARCHAR(255) NOT NULL,
    precaution VARCHAR(255) NOT NULL,
    PTW_Id INT NOT NULL,
    Approval_email VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (PTW_Id) REFERENCES Request (id),
    FOREIGN KEY (Approval_email) REFERENCES User (email)
);


DROP SCHEMA IF EXISTS `lms` ;
CREATE SCHEMA IF NOT EXISTS `lms` ;
USE `lms` ;

-- -----------------------------------------------------
-- Table `lms`.`Book`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lms`.`Book` ;

CREATE TABLE IF NOT EXISTS `lms`.`Book` (
  `ISBN` VARCHAR(13) NOT NULL,
  `Title` VARCHAR(255) NOT NULL,
  `Publisher` VARCHAR(255) NULL DEFAULT NULL,
  `Edition` VARCHAR(255) NULL DEFAULT NULL,
  `PublicationYear` VARCHAR(255) NULL DEFAULT NULL,
  `GoogleBooksID` VARCHAR(255) NULL DEFAULT NULL,
  `NoOfPages` INT(11) NULL DEFAULT NULL,
  `Description` VARCHAR(5119) NULL,
  PRIMARY KEY (`ISBN`));


-- -----------------------------------------------------
-- Table `lms`.`ApprovedBook`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lms`.`ApprovedBook` ;

CREATE TABLE IF NOT EXISTS `lms`.`ApprovedBook` (
  `ISBN` VARCHAR(255) NOT NULL,
  INDEX `fk_ApprovedBookISBN` (`ISBN` ASC),
  PRIMARY KEY (`ISBN`),
  CONSTRAINT `fk_ApprovedBookISBN`
    FOREIGN KEY (`ISBN`)
    REFERENCES `lms`.`Book` (`ISBN`));


-- -----------------------------------------------------
-- Table `lms`.`AvailableBook`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lms`.`AvailableBook` ;

CREATE TABLE IF NOT EXISTS `lms`.`AvailableBook` (
  `ISBN` VARCHAR(255) NOT NULL,
  `TotalCopies` INT(255) NOT NULL,
  `CallNumber` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`ISBN`),
  CONSTRAINT `fk_AvailableBookISBN`
    FOREIGN KEY (`ISBN`)
    REFERENCES `lms`.`Book` (`ISBN`));


-- -----------------------------------------------------
-- Table `lms`.`RequestedBook`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lms`.`RequestedBook` ;

CREATE TABLE IF NOT EXISTS `lms`.`RequestedBook` (
  `ISBN` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`ISBN`),
  CONSTRAINT `fk_RequestedBookISBN`
    FOREIGN KEY (`ISBN`)
    REFERENCES `lms`.`Book` (`ISBN`));


-- -----------------------------------------------------
-- Table `lms`.`Author`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lms`.`Author` ;

CREATE TABLE IF NOT EXISTS `lms`.`Author` (
  `ISBN` VARCHAR(255) NOT NULL,
  `AuthorName` VARCHAR(255) NOT NULL,
  INDEX `fk_Author` (`ISBN` ASC),
  CONSTRAINT `fk_Author`
    FOREIGN KEY (`ISBN`)
    REFERENCES `lms`.`Book` (`ISBN`));


-- -----------------------------------------------------
-- Table `lms`.`BookAvailabilityNotification`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lms`.`BookAvailabilityNotification` ;

CREATE TABLE IF NOT EXISTS `lms`.`BookAvailabilityNotification` (
  `ISBN` VARCHAR(255) NOT NULL,
  `UserID` VARCHAR(255) NOT NULL,
  INDEX `fk_BookAvailabilityISBN` (`ISBN` ASC),
  CONSTRAINT `fk_BookAvailabilityISBN`
    FOREIGN KEY (`ISBN`)
    REFERENCES `lms`.`AvailableBook` (`ISBN`));


-- -----------------------------------------------------
-- Table `lms`.`BookRequest`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lms`.`BookRequest` ;

CREATE TABLE IF NOT EXISTS `lms`.`BookRequest` (
  `ISBN` VARCHAR(255) NOT NULL,
  `UserID` VARCHAR(255) NOT NULL,
  `UserEmail` VARCHAR(255) NULL DEFAULT NULL,
  INDEX `fk_RequestBookISBN` (`ISBN` ASC),
  CONSTRAINT `fk_RequestBookISBN`
    FOREIGN KEY (`ISBN`)
    REFERENCES `lms`.`RequestedBook` (`ISBN`));


-- -----------------------------------------------------
-- Table `lms`.`Category`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lms`.`Category` ;

CREATE TABLE IF NOT EXISTS `lms`.`Category` (
  `ISBN` VARCHAR(255) NOT NULL,
  `CategoryName` VARCHAR(255) NOT NULL,
  INDEX `fk_Catagory` (`ISBN` ASC),
  CONSTRAINT `fk_Catagory`
    FOREIGN KEY (`ISBN`)
    REFERENCES `lms`.`Book` (`ISBN`));


-- -----------------------------------------------------
-- Table `lms`.`Issue`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lms`.`Issue` ;

CREATE TABLE IF NOT EXISTS `lms`.`Issue` (
  `IssueID` INT NOT NULL AUTO_INCREMENT,
  `BookISBN` VARCHAR(255) NOT NULL,
  `UserID` VARCHAR(255) NOT NULL,
  `UserEmail` VARCHAR(255) NULL,
  `IssueDate` VARCHAR(255) NOT NULL,
  `IssuerID` VARCHAR(255) NOT NULL,
  `IssuerEmail` VARCHAR(225) NOT NULL,
  `ReturnedDate` VARCHAR(255) NULL DEFAULT NULL,
  INDEX `fk_Book` (`BookISBN` ASC),
  PRIMARY KEY (`IssueID`),
  CONSTRAINT `fk_Book`
    FOREIGN KEY (`BookISBN`)
    REFERENCES `lms`.`AvailableBook` (`ISBN`));


-- -----------------------------------------------------
-- Table `lms`.`Reissue`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lms`.`Reissue` ;

CREATE TABLE IF NOT EXISTS `lms`.`Reissue` (
  `IssueID` INT NOT NULL,
  `ReissueDate` VARCHAR(255) NOT NULL,
  `ReissuerID` VARCHAR(225) NOT NULL,
  `ReissuerEmail` VARCHAR(45) NOT NULL,
  INDEX `fk_Reissue_idx` (`IssueID` ASC),
  CONSTRAINT `fk_Reissue`
    FOREIGN KEY (`IssueID`)
    REFERENCES `lms`.`Issue` (`IssueID`));


-- -----------------------------------------------------
-- Table `lms`.`Staff`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lms`.`Staff` ;

CREATE TABLE IF NOT EXISTS `lms`.`Staff` (
  `emailid` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`emailid`));

DROP DATABASE IF EXISTS HomeQuest;
CREATE DATABASE HomeQuest;
USE HomeQuest;

-- Buyers table
CREATE TABLE Buyers (
	BuyerID INT PRIMARY KEY AUTO_INCREMENT
);

-- Sellers table
CREATE TABLE Sellers (
    SellerID INT PRIMARY KEY AUTO_INCREMENT,
    FullName VARCHAR(100) NOT NULL,
    Email VARCHAR(100) NOT NULL UNIQUE,
    DateOfBirth DATE,
    ConsentLocation BOOLEAN,
    /*ProfilePhoto,*/
    PhoneNumber VARCHAR(15),
    UserType ENUM('Free', 'Gold') DEFAULT 'Free'
);

-- Properties table
CREATE TABLE Properties (
    PropertyID INT PRIMARY KEY AUTO_INCREMENT,
    SellerID INT,
    Location VARCHAR(255) NOT NULL,
    Price DECIMAL(10, 2) NOT NULL,
    Size INT,
    NumberOfRooms INT,
    PropertyType ENUM('Apartment', 'House', 'Condo', 'Land'),
    IsForRent BOOLEAN,
    RentDuration INT,
    VerificationStatus BOOLEAN,
    FOREIGN KEY (SellerID) REFERENCES Sellers(SellerID)
);

-- PropertyVerification table
CREATE TABLE PropertyVerification (
    VerificationID INT PRIMARY KEY AUTO_INCREMENT,
    PropertyID INT,
    VerificationStatus ENUM('Pending', 'Verified', 'Rejected') DEFAULT 'Pending',
    FOREIGN KEY (PropertyID) REFERENCES Properties(PropertyID)
);

-- Transactions table
CREATE TABLE Transactions (
    TransactionID INT PRIMARY KEY AUTO_INCREMENT,
    SellerID INT,
    PropertyID INT,
    Amount DECIMAL(10, 2),
    PaymentMethod ENUM('Weekly', 'Monthly', 'Quaterly', 'Yearly'),
    TransactionDate DATETIME,
    FOREIGN KEY (SellerID) REFERENCES Sellers(SellerID)
);

-- Review Table
CREATE TABLE Reviews (
	ReviewID INT PRIMARY KEY AUTO_INCREMENT,
    PropertyID INT,
    ReviewDate DATETIME,
    ReviewText TEXT,
    ReviewLike BOOLEAN,
    FOREIGN KEY (PropertyID) REFERENCES Properties(PropertyID)
);
 /*	
-- Likes table
CREATE TABLE Likes (
    LikeID INT PRIMARY KEY AUTO_INCREMENT,
    PropertyID INT,
    LikeDate DATETIME,
    FOREIGN KEY (PropertyID) REFERENCES Properties(PropertyID)
);

-- Comments table
CREATE TABLE Comments (
    CommentID INT PRIMARY KEY AUTO_INCREMENT,
    PropertyID INT,
    CommentText TEXT,
    CommentDate DATETIME,
    FOREIGN KEY (PropertyID) REFERENCES Properties(PropertyID)
);*/
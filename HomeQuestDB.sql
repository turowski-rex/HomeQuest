DROP DATABASE IF EXISTS HomeQuest;
CREATE DATABASE HomeQuest;
USE HomeQuest;

-- Buyers table
CREATE TABLE Buyers (
	BuyerID INT PRIMARY KEY AUTO_INCREMENT,
    SessionID VARCHAR(225) -- no login for buyer so sessionID required
);

-- Sellers table
CREATE TABLE Sellers (
    SellerID INT PRIMARY KEY AUTO_INCREMENT,
    FullName VARCHAR(100) NOT NULL,
    Email VARCHAR(100) NOT NULL UNIQUE,
    Password VARCHAR(100) NOT NULL,
    DateOfBirth DATE,
    ConsentLocation BOOLEAN,
    ProfilePhoto VARCHAR(225), -- stored as path of photo
    PhoneNumber VARCHAR(15),
    UserType ENUM('Free', 'Gold') DEFAULT 'Free'
    
);

-- Properties table
CREATE TABLE Properties (
    PropertyID INT PRIMARY KEY AUTO_INCREMENT,
    SellerID INT,
    PropertyName VARCHAR(100) NOT NULL,
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

CREATE TABLE PropertyImages (
    ImageID INT PRIMARY KEY AUTO_INCREMENT,
    PropertyID INT,
    ImagePath VARCHAR(255) NOT NULL,
    FOREIGN KEY (PropertyID) REFERENCES Properties(PropertyID)
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
    TransactionStatus BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (SellerID) REFERENCES Sellers(SellerID),
    FOREIGN KEY (PropertyID) REFERENCES Properties(PropertyID)
);

-- Review Table
CREATE TABLE Reviews (
	ReviewID INT PRIMARY KEY AUTO_INCREMENT,
    PropertyID INT,
    ReviewDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ReviewText TEXT,
    ReviewLike BOOLEAN,
    FOREIGN KEY (PropertyID) REFERENCES Properties(PropertyID)
);

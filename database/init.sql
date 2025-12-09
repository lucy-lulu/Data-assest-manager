-- database
CREATE DATABASE DA;

-- use the new database
USE DA;

-- Create Asset Table
CREATE TABLE Asset (
assetId INT AUTO_INCREMENT PRIMARY KEY,
assetName VARCHAR(100) NOT NULL,
format VARCHAR(50),
createTime DATETIME DEFAULT CURRENT_TIMESTAMP,
author VARCHAR(100),
description TEXT,
tag VARCHAR(100),
canvasLink TEXT,
url VARCHAR(255),
studentType VARCHAR(50),
subjectNumber VARCHAR(50),
visitCount INT DEFAULT 0
);

-- Teacher Table
CREATE TABLE Teacher (
    teacherId INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    passwordHash VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL
);

-- Recently Viewed Table
CREATE TABLE RecentlyViewed (
    recentlyViewedId INT AUTO_INCREMENT PRIMARY KEY,
    teacherId INT,
    assetId INT,
    FOREIGN KEY (teacherId) REFERENCES Teacher(teacherId),
    FOREIGN KEY (assetId) REFERENCES Asset(assetId) ON DELETE SET NULL
);

-- Search History Table
CREATE TABLE SearchHistory (
    searchHistoryId INT AUTO_INCREMENT PRIMARY KEY,
    teacherId INT,
    searchString VARCHAR(100) NOT NULL,
    FOREIGN KEY (teacherId) REFERENCES Teacher(teacherId)
);

-- Administrator Table
CREATE TABLE Administrator (
    adminId INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    passwordHash VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL,
    reportGenerationSetting TEXT,
    classificationModeSetting TEXT
);

-- Feedback Table
CREATE TABLE Feedback (
    feedbackId INT AUTO_INCREMENT PRIMARY KEY,
    teacherId INT,
    assetId INT,
    description TEXT,
    sendTime DATETIME,
    status VARCHAR(100),
    FOREIGN KEY (teacherId) REFERENCES Teacher(teacherId),
    FOREIGN KEY (assetId) REFERENCES Asset(assetId) ON DELETE CASCADE
);

-- Report Table
CREATE TABLE Report (
    reportId INT AUTO_INCREMENT PRIMARY KEY,
    adminId INT,
    mostPopular TEXT,
    categoryStatus TEXT,
    systemDetail TEXT,
    FOREIGN KEY (adminId) REFERENCES Administrator(adminId)
);

-- Category Table
CREATE TABLE Category (
    categoryId INT AUTO_INCREMENT PRIMARY KEY,
    categoryName VARCHAR(50) NOT NULL,
    teacherId INT,
    FOREIGN KEY (teacherId) REFERENCES Teacher(teacherId)
);

-- Classification Table
CREATE TABLE Classification (
    classificationId INT AUTO_INCREMENT PRIMARY KEY,
    assetId INT,
    categoryId INT,
    FOREIGN KEY (assetId) REFERENCES Asset(assetId) ON DELETE CASCADE,
    FOREIGN KEY (categoryId) REFERENCES Category(categoryId)
);

-- ClassifiedAsset View
CREATE VIEW ClassifiedAsset AS
SELECT classificationId, teacherId, cl.categoryId, categoryName, cl.assetId, assetName, format, createTime, author, description, tag, canvasLink, url, studentType, subjectNumber, visitCount
FROM Classification cl
JOIN Category ca ON cl.categoryId = ca.categoryId
JOIN Asset a ON cl.assetId = a.assetId;

-- Teacher Uploaded Asset Table
CREATE TABLE TeacherUploaded (
    TeacherUploadedId INT AUTO_INCREMENT PRIMARY KEY,
    teacherId INT,
    assetId INT,
    FOREIGN KEY (teacherId) REFERENCES Teacher(teacherId),
    FOREIGN KEY (assetId) REFERENCES Asset(assetId) ON DELETE CASCADE
);

-- Teacher Collected Asset Table
CREATE TABLE TeacherCollected (
    TeacherCollectedId INT AUTO_INCREMENT PRIMARY KEY,
    teacherId INT,
    assetId INT,
    FOREIGN KEY (teacherId) REFERENCES Teacher(teacherId),
    FOREIGN KEY (assetId) REFERENCES Asset(assetId) ON DELETE SET NULL
);

-- CollectedAsset View
CREATE VIEW CollectedView AS
SELECT TeacherCollectedId, tc.teacherId, userName, tc.assetId, assetName, format, createTime, author, description, tag, canvasLink, url, studentType, subjectNumber, visitCount
FROM TeacherCollected tc
JOIN Teacher t ON tc.teacherId = t.teacherId
JOIN Asset a ON tc.assetId = a.assetId;

-- SubTeacher Table
CREATE TABLE SubTeacher (
    subTeacherId INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    passwordHash VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL
);
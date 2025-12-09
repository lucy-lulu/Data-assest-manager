-- Use the DigitalAssetManager database
USE DA;

-- Insert Assets
INSERT INTO Asset (assetName, format, author, description, tag, canvasLink, url, studentType, subjectNumber, visitCount) VALUES
('Asset1', 'PDF', 'Author1', 'Description of Asset1', 'tag1', 'http://canvaslink.com/asset1', 'http://url.com/asset1', 'Undergraduate', 'SUBJ001', 5),
('Asset2', 'Video', 'Author2', 'Description of Asset2', 'tag2', 'http://canvaslink.com/asset2', 'http://url.com/asset2', 'Postgraduate', 'SUBJ002', 3),
('Asset3', 'Image', 'Author3', 'Description of Asset3', 'tag3', 'http://canvaslink.com/asset3', 'http://url.com/asset3', 'Undergraduate', 'SUBJ003', 10);

-- Insert Admins and Teachers
INSERT INTO Administrator (username, passwordHash, email, reportGenerationSetting, classificationModeSetting) VALUES
('adminUser', '0b14d501a594442a01c6859541bcb3e8164d183d32937b851835442f69d5c94e', 'admin@example.com', 'weekly', 'autoTagging');
INSERT INTO Teacher (username, passwordHash, email) VALUES
('teacherUser', '0b14d501a594442a01c6859541bcb3e8164d183d32937b851835442f69d5c94e', 'teacher@example.com');
INSERT INTO SubTeacher (username, passwordHash, email) VALUES
('subTeacherUser', '0b14d501a594442a01c6859541bcb3e8164d183d32937b851835442f69d5c94e', 'subteacher@example.com');

-- Insert Recently Viewed and Search History
INSERT INTO RecentlyViewed (teacherId, assetId) VALUES
('1', '1'),
('1', '2');

INSERT INTO SearchHistory (teacherId, searchString) VALUES
('1', 'Asset'),
('1', 'Asset1');

-- Insert Feedback
INSERT INTO Feedback (teacherId, assetId, description, sendTime, status) VALUES
(1, 1, 'Great asset for learning!', NOW(), 'In Progress'),
(1, 2, 'Very informative video.', NOW(), 'Done');

-- Insert Reports
INSERT INTO Report (adminId, mostPopular, categoryStatus, systemDetail) VALUES
(1, 'Asset1', 'Active', 'Detailed report of asset usage.');

-- Insert Category
INSERT INTO Category (categoryName, teacherId) VALUES
('category1', 1),
('category2', 1);

-- Insert Classification
INSERT INTO Classification (assetId, categoryId) VALUES
(1, 1),
(1, 2),
(2, 1);

-- Insert TeacherUploaded
INSERT INTO TeacherUploaded (teacherId, assetId) VALUES
(1, 1),
(1, 2);



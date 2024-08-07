-- Set the schema for the current session
SET search_path TO project2;

-- Drop existing tables if they exist
DROP TABLE IF EXISTS Users, Educators, Quizzes, ChoiceSelections, QuizQuestions, QuestionChoices, Courses, Enrollments, QuizAttempts CASCADE;

-- Drop existing casts if they exist
drop cast if exists (varchar AS user_role);

-- Drop existing types if they exist
DROP TYPE IF EXISTS user_role;
DROP TYPE IF EXISTS pay_status;

-- Create types
CREATE TYPE user_role AS ENUM('student', 'educator');
CREATE TYPE	pay_status AS ENUM('pending', 'completed', 'cancelled');

-- Create cast for types
CREATE CAST (varchar AS user_role) WITH INOUT AS implicit;

-- Create tables
CREATE TABLE Users (
    user_id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    role user_role NOT NULL
);

CREATE TABLE Educators (
    educator_id INT PRIMARY KEY,
    degree_level VARCHAR(255),
    degree_major VARCHAR(255),
    alma_mater VARCHAR(255),
    year VARCHAR(255),
    FOREIGN KEY (educator_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

CREATE TABLE Courses (
    course_id SERIAL PRIMARY KEY,
    educator_id INT,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(100),
    price NUMERIC(10, 2),
    img_url text default 'https://www.fourpaws.com/-/media/Project/OneWeb/FourPaws/Images/articles/cat-corner/cats-that-dont-shed/siamese-cat.jpg',
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (educator_id) REFERENCES Educators(educator_id) ON DELETE SET NULL
);

CREATE TABLE Enrollments (
    enrollment_id SERIAL PRIMARY KEY,
    student_id INT,
    course_id INT,
    enrollment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    payment_status VARCHAR(255) NOT NULL,
    enrollment_status BOOLEAN NOT NULL DEFAULT FALSE,
    course_rating int default 3,
    course_review VARCHAR(255),
    FOREIGN KEY (student_id) REFERENCES Users(user_id) ON DELETE SET NULL,
    FOREIGN KEY (course_id) REFERENCES Courses(course_id) ON DELETE SET NULL
);

CREATE TABLE Quizzes (
    quiz_id SERIAL PRIMARY KEY,
    course_id INT,
    title VARCHAR(255) NOT NULL,
    timer INT,
    attempts_allowed INT,
    open bool,
    FOREIGN KEY (course_id) REFERENCES Courses(course_id) ON DELETE CASCADE
);

CREATE TABLE QuizQuestions (
    question_id SERIAL PRIMARY KEY,
    quiz_id INT,
    question_text TEXT NOT NULL,
    FOREIGN KEY (quiz_id) REFERENCES Quizzes(quiz_id) ON DELETE CASCADE
);

CREATE TABLE QuestionChoices (
    choice_id SERIAL PRIMARY KEY,
    question_id  INT,
    correct bool,
    text VARCHAR(255) NOT NULL,
    FOREIGN KEY (question_id) REFERENCES QuizQuestions(question_id) ON DELETE CASCADE
);

CREATE TABLE QuizAttempts (
    attempt_id SERIAL PRIMARY KEY,
    student_id INT,
    quiz_id INT,
    attempt_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    score NUMERIC(5,2) check (score <= 100),
    FOREIGN KEY (student_id) REFERENCES Users(user_id),
    FOREIGN KEY (quiz_id) REFERENCES Quizzes(quiz_id)
);

CREATE TABLE ChoiceSelections (
        choice_id int REFERENCES QuestionChoices(choice_id) NOT NULL,
        attempt_id int REFERENCES QuizAttempts (attempt_id) NOT NULL,
        PRIMARY KEY(choice_id, attempt_id)
);

-- Sample Data

INSERT INTO Users (email, password, first_name, last_name, role) VALUES
('john.doe@example.com', '482C811DA5D5B4BC6D497FFA98491E38', 'John', 'Doe', 'student'),
('jane.smith@example.com', '482C811DA5D5B4BC6D497FFA98491E38', 'Jane', 'Smith', 'student'),
('alice.johnson@example.com', '482C811DA5D5B4BC6D497FFA98491E38', 'Alice', 'Johnson', 'educator'),
('bob.brown@example.com', '482C811DA5D5B4BC6D497FFA98491E38', 'Bob', 'Brown', 'educator');

INSERT INTO Educators (educator_id, degree_level, degree_major, alma_mater, year) VALUES
((SELECT user_id FROM Users WHERE email='alice.johnson@example.com'), 'PhD', 'Computer Science', 'MIT', '2015'),
((SELECT user_id FROM Users WHERE email='bob.brown@example.com'), 'Masters', 'Mathematics', 'Harvard', '2010');

INSERT INTO Courses (educator_id, title, description, category, price) VALUES
((SELECT educator_id FROM Educators WHERE educator_id=(SELECT user_id FROM Users WHERE email='alice.johnson@example.com')), 'Introduction to Programming', 'A beginner course on programming.', 'Computer Science', 49.99),
((SELECT educator_id FROM Educators WHERE educator_id=(SELECT user_id FROM Users WHERE email='alice.johnson@example.com')), 'Data Structures', 'Learn about different data structures.', 'Computer Science', 79.99),
((SELECT educator_id FROM Educators WHERE educator_id=(SELECT user_id FROM Users WHERE email='bob.brown@example.com')), 'Calculus I', 'An introductory course on calculus.', 'Mathematics', 59.99),
((SELECT educator_id FROM Educators WHERE educator_id=(SELECT user_id FROM Users WHERE email='bob.brown@example.com')), 'Statistics', 'Basic statistics concepts and techniques.', 'Mathematics', 69.99);

INSERT INTO Enrollments (student_id, course_id, payment_status, enrollment_status, course_review) VALUES
((SELECT user_id FROM Users WHERE email='john.doe@example.com'), (SELECT course_id FROM Courses WHERE title='Introduction to Programming'), 'completed', TRUE, 'Great course!'),
((SELECT user_id FROM Users WHERE email='john.doe@example.com'), (SELECT course_id FROM Courses WHERE title='Calculus I'), 'completed', TRUE, 'Very informative.'),
((SELECT user_id FROM Users WHERE email='jane.smith@example.com'), (SELECT course_id FROM Courses WHERE title='Data Structures'), 'completed', TRUE, 'Challenging but rewarding.'),
((SELECT user_id FROM Users WHERE email='jane.smith@example.com'), (SELECT course_id FROM Courses WHERE title='Statistics'), 'pending', FALSE, NULL);

INSERT INTO quizzes (course_id, title, timer, attempts_allowed, open)
VALUES
    ( 1, 'Programming JavaScript', 60, 2, 'true' );

INSERT INTO quizzes (course_id, title, timer, attempts_allowed, open)
VALUES
    ( (SELECT course_id FROM courses WHERE title='Introduction to Programming'), 'Programming JavaScript', 60, 2, 'true' );

INSERT INTO quizquestions (quiz_id, question_text)
VALUES
  ( 1, 'How do you create a function in JavaScript?' ),
  ( 1, 'What is the result of typeof NaN?' );

INSERT INTO QuestionChoices (question_id, text, correct)
VALUES
  ( (SELECT question_id FROM quizquestions where question_text='How do you create a function in JavaScript?'), 'def myFunction() {}', 'false' ),
  ( (SELECT question_id FROM quizquestions where question_text='How do you create a function in JavaScript?'), 'create myFunction() {}', 'false'),
  ( (SELECT question_id FROM quizquestions where question_text='How do you create a function in JavaScript?'), 'function myFunction() {}', 'true'),
  ( (SELECT question_id FROM quizquestions where question_text='How do you create a function in JavaScript?'), 'function:myFunction() {}', 'false'),
  ( (SELECT question_id FROM quizquestions where question_text='How do you create a function in JavaScript?'), 'myFunction function() {}', 'false');

INSERT INTO QuestionChoices (question_id, text, correct)
VALUES
  ( (SELECT question_id FROM quizquestions where question_text='What is the result of typeof NaN?'), 'object ', 'false'),
  ( (SELECT question_id FROM quizquestions where question_text='What is the result of typeof NaN?'), 'undefined', 'false' ),
  ( (SELECT question_id FROM quizquestions where question_text='What is the result of typeof NaN?'), 'NaN', 'false'),
  ( (SELECT question_id FROM quizquestions where question_text='What is the result of typeof NaN?'), 'null', 'false'),
  ( (SELECT question_id FROM quizquestions where question_text='What is the result of typeof NaN?'), 'number', 'true');

-- Select statements
select * from users;
select * from educators;
select * from courses;
select * from enrollments;
select * from quizzes;
select * from quizattempts;
select * from quizquestions;

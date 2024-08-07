-- Set the schema for the current session
SET search_path TO project2;

select * from users;
select * from courses;
select * from quizzes;
select * from quizquestions;
select * from questionchoices;

INSERT INTO quizzes (course_id, title, timer, attempts_allowed, open) 
VALUES
    ( 1, 'Programming JavaScript', 60, 2, 'true' );

INSERT INTO quizzes (course_id, title, timer, attempts_allowed, open) 
VALUES
    ( (SELECT course_id FROM courses WHERE title='Introduction to Programming'), 'Programming JavaScript', 60, 2, 'true' );

INSERT INTO quizquestions (quiz_id, question_text) 
VALUES
  ( (SELECT quiz_id FROM quizzes WHERE title='Programming JavaScript'), 'How do you create a function in JavaScript?' ),
  ( (SELECT quiz_id FROM quizzes WHERE title='Programming JavaScript'), 'What is the result of typeof NaN?' );

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

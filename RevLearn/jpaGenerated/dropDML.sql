
    alter table if exists project2.quizattempts 
       drop constraint if exists FKfhhkmlk6b2sstt3e2gy3drwny;

    alter table if exists project2.quizattempts 
       drop constraint if exists FKrysyev381nhi9uhtaoxjfo46j;

    drop table if exists project2.choiceselections cascade;

    drop table if exists project2.courses cascade;

    drop table if exists project2.educator cascade;

    drop table if exists project2.enrollments cascade;

    drop table if exists project2.questionchoices cascade;

    drop table if exists project2.quizattempts cascade;

    drop table if exists project2.quizquestions cascade;

    drop table if exists project2.quizzes cascade;

    drop table if exists project2.users cascade;

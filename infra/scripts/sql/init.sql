CREATE TABLE users (
  user_id serial PRIMARY KEY,
  username VARCHAR (50) NOT NULL,
  course VARCHAR (50),
  email VARCHAR (50),
  age INT2
);

INSERT INTO users (user_id, username, course, email, age)
VALUES
    ('1', 'Oleg', 'QA junior', 'oleg@otus.test', '17'),
    ('2', 'Ivan', 'QA middle', 'ivan@otus.com', '25'),
    ('3', 'Alex', 'QA senior', 'alex@otus.net', '60');
select * from users;
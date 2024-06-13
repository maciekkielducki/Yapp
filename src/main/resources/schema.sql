DROP TABLE IF EXISTS users, posts, comments, post_likes, comment_likes;
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(255),
                       last_name VARCHAR(255),
                       email VARCHAR(255),
                       password VARCHAR(255),
                       avatar_color VARCHAR(255)
);

CREATE TABLE posts (
                       id SERIAL PRIMARY KEY,
                       user_id BIGINT REFERENCES users(id),
                       content TEXT,
                       likes_count INTEGER DEFAULT 0,
                       comments_count INTEGER DEFAULT 0,
                       image_url VARCHAR(255),
                       date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE comments (
                          id SERIAL PRIMARY KEY,
                          user_id BIGINT REFERENCES users(id),
                          post_id BIGINT REFERENCES posts(id),
                          content TEXT,
                          likes_count INTEGER DEFAULT 0,
                          date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE post_likes (
                            id SERIAL PRIMARY KEY,
                            post_id BIGINT REFERENCES posts(id),
                            user_id BIGINT REFERENCES users(id)
);

CREATE TABLE comment_likes (
                               id SERIAL PRIMARY KEY,
                               comment_id BIGINT REFERENCES comments(id),
                               user_id BIGINT REFERENCES users(id)
);

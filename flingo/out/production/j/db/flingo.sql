CREATE DATABASE flingo;
USE flingo;

CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL
);

CREATE TABLE flashcards (
    card_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    front_text VARCHAR(255) NOT NULL,
    back_text VARCHAR(255) NOT NULL,
    language_direction ENUM('EN_HI', 'HI_EN') NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
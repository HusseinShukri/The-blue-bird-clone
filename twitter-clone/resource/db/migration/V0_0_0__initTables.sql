CREATE TABLE User (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL,
    is_deleted BOOLEAN NOT NULL
);

CREATE TABLE Tweet (
   id INT PRIMARY KEY AUTO_INCREMENT,
   user_id INT NOT NULL,
   content TEXT NOT NULL,
   created_at DATETIME NOT NULL,
   is_deleted BOOLEAN NOT NULL,
   FOREIGN KEY (user_id) REFERENCES User(id)
);

CREATE TABLE Follower (
  follower_id INT NOT NULL,
  following_id INT NOT NULL,
  created_at DATETIME NOT NULL,
  PRIMARY KEY (follower_id, following_id),
  FOREIGN KEY (follower_id) REFERENCES User(id),
  FOREIGN KEY (following_id) REFERENCES User(id)
);

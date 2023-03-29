CREATE TABLE role(
   id serial PRIMARY KEY,
   name VARCHAR (255) UNIQUE NOT NULL
);

CREATE TABLE account (
	id serial PRIMARY KEY,
	username VARCHAR (255) UNIQUE NOT NULL,
	password VARCHAR (511) NOT NULL,
    email VARCHAR (255) UNIQUE NOT NULL,
    balance BIGINT NOT NULL
);

CREATE TABLE account_role (
  account_id INT NOT NULL,
  role_id INT NOT NULL,
  PRIMARY KEY (account_id, role_id),
  FOREIGN KEY (role_id) REFERENCES role (id),
  FOREIGN KEY (account_id) REFERENCES account (id)
);

INSERT INTO role (name) VALUES ('ROLE_ADMIN');
INSERT INTO role (name) VALUES ('ROLE_USER');

INSERT INTO account (username, password, email, balance)
VALUES (
        'admin',
        '$2a$10$ELH7ZujJZ2YDjU8rKOgTZe4qDGEmyk4ySiQVCo05k8NtDEx84hVxe',
        'admin@admin.com',
        1000000
        );

INSERT INTO account_role (account_id, role_id) VALUES (1, 1);
INSERT INTO account_role (account_id, role_id) VALUES (1, 2);
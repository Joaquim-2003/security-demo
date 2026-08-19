CREATE TABLE sys_user (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          username VARCHAR(50) NOT NULL UNIQUE,
                          password VARCHAR(100) NOT NULL
);

CREATE TABLE sys_role (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          role_code VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE sys_user_role (
                               user_id BIGINT NOT NULL,
                               role_id BIGINT NOT NULL
);

CREATE TABLE sys_permission (
                                id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                permission_code VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE sys_role_permission (
                                     role_id BIGINT NOT NULL,
                                     permission_id BIGINT NOT NULL
);
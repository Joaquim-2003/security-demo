INSERT INTO sys_user (username, password)
VALUES
    ('admin', '123456'),
    ('user', '123456');

INSERT INTO sys_role (role_code)
VALUES
    ('ADMIN'),
    ('USER');

INSERT INTO sys_user_role (user_id, role_id)
VALUES
    (1, 1),
    (2, 2);

INSERT INTO sys_permission (permission_code)
VALUES
    ('user:list'),
    ('user:add'),
    ('user:delete');

INSERT INTO sys_role_permission (role_id, permission_id)
VALUES
    (1, 1),
    (1, 2),
    (1, 3),
    (2, 1);
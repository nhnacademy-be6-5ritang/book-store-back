# 회원 상태 초기 데이터
INSERT INTO users_statuses (user_status_name)
VALUES ('ACTIVE'),
       ('DORMANT'),
       ('WITHDRAW');

# 회원 등급 초기 데이터
INSERT INTO users_grades (user_grade_name, user_grade_min_amount, user_grade_max_amount, user_grade_point_rate)
VALUES ('REGULAR', 0, 100000, 1.5),
       ('ROYAL', 100000, 300000, 3),
       ('GRAND', 300000, 1000000, 5),
       ('PRESTIGE', 1000000, 9999999999999999, 10);

# 회원 권한 초기 데이터
INSERT INTO roles (role_name)
VALUES ('MEMBER'),
       ('MEMBER_ADMIN'),
       ('BOOK_ADMIN'),
       ('ORDER_ADMIN'),
       ('HEAD_ADMIN');

# 회원 포인트 적립 정책 초기 데이터
INSERT INTO point_earning_policies (point_earning_amount, point_earning_policy_type, point_earning_policy_status)
VALUES (5000, 'SIGN_UP', 'ACTIVE'),
       (200, 'REVIEW', 'ACTIVE'),
       (500, 'PHOTO_REVIEW', 'ACTIVE'),
       (1.5, 'ORDER_REGULAR', 'ACTIVE'),
       (3, 'ORDER_ROYAL', 'ACTIVE'),
       (5, 'ORDER_GRAND', 'ACTIVE'),
       (10, 'ORDER_PRESTIGE', 'ACTIVE');

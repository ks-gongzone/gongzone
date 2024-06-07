-- 0.CREATE DB/USER
DROP DATABASE IF EXISTS ks51team02db;

CREATE DATABASE ks51team02db;

DROP USER IF EXISTS 'ks51team02id'@'%';

CREATE USER 'ks51team02id'@'%' IDENTIFIED BY 'ks51team02pw';

GRANT ALL PRIVILEGES ON ks51team02db.* TO 'ks51team02id'@'%';

FLUSH PRIVILEGES;


-- 1.CREATE TABLE
USE ks51team02db;

-- 회원 권한 테이블
CREATE TABLE member_level
(
    m_level      INT         NOT NULL COMMENT '권한코드',
    m_level_name VARCHAR(10) NOT NULL COMMENT '권한명',
    PRIMARY KEY (m_level)
) COMMENT '회원 권한 테이블';

-- 회원 정보 테이블
CREATE TABLE member
(
    m_no    VARCHAR(10) NOT NULL COMMENT '회원고유번호',
    m_level INT         NOT NULL COMMENT '권한코드',
    m_id    VARCHAR(50) NOT NULL COMMENT '아이디',
    m_pw    VARCHAR(50) NOT NULL COMMENT '비밀번호',
    m_name  VARCHAR(20) NOT NULL COMMENT '이름',
    m_email VARCHAR(20) NOT NULL COMMENT '이메일',
    PRIMARY KEY (m_no),
    UNIQUE KEY (m_id),
    FOREIGN KEY (m_level) REFERENCES member_level (m_level)
) COMMENT '회원 정보 테이블';


-- 2. INSERT DATA
INSERT INTO member_level(m_level, m_level_name)
VALUES (1, '일반회원'),
       (2, '관리자');

INSERT INTO member(m_no, m_level, m_id, m_pw, m_name, m_email)
VALUES ('M000001', 2, 'admin', 'admin', '관리자1', 'admin@gongzone.com'),
       ('M000002', 1, 'id001', 'pw001', '홍길동', 'hong@gongzone.com');

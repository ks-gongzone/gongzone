--
--
--  !! 주의: 실행시 DB 초기화됩니다 !!
--
--			최종 수정 - 240612
--
--


-- 0. DB 제거, 생성
DROP DATABASE IF EXISTS ks51team02db;
CREATE DATABASE ks51team02db;

-- DROP USER IF EXISTS 'ks51team02id';
-- CREATE USER 'ks51team02id'@'%' IDENTIFIED BY 'ks51team02pw';
-- 
-- GRANT ALL PRIVILEGES ON ks51team02db.* TO 'ks51team02id'@'%';
-- FLUSH PRIVILEGES;


-- 1. 테이블 생성
USE ks51team02db;

CREATE TABLE type_code
(
    type_code        VARCHAR(20) NOT NULL COMMENT '유형코드', -- PK
    code_group       VARCHAR(10) NOT NULL COMMENT '코드대분류',
    code_group_sub   VARCHAR(10) NOT NULL COMMENT '코드중분류',
    code_description VARCHAR(50) NOT NULL COMMENT '코드설명',
    PRIMARY KEY (type_code)
) COMMENT ='유형코드';

CREATE TABLE status_code
(
    status_code      VARCHAR(20) NOT NULL COMMENT '상태코드', -- PK
    code_group       VARCHAR(10) NOT NULL COMMENT '코드대분류',
    code_group_sub   VARCHAR(10) NOT NULL COMMENT '코드중분류',
    code_description VARCHAR(50) NOT NULL COMMENT '코드설명',
    PRIMARY KEY (status_code)
) COMMENT ='상태코드';

CREATE TABLE member_level
(
    m_level      INT         NOT NULL COMMENT '권한코드', -- PK
    m_level_name VARCHAR(10) NOT NULL COMMENT '권한명',
    PRIMARY KEY (m_level)
) COMMENT ='회원권한';

CREATE TABLE member
(
    m_no        VARCHAR(10) NOT NULL COMMENT '회원고유번호',         -- PK
    m_level     INT         NOT NULL DEFAULT 1 COMMENT '권한코드', -- FK ref: member_level
    m_id        VARCHAR(50) NULL UNIQUE COMMENT '아이디',
    m_pw        VARCHAR(50) NULL COMMENT '비밀번호',
    m_name      VARCHAR(20) NOT NULL COMMENT '이름',
    m_email     VARCHAR(20) NOT NULL COMMENT '이메일',
    m_phone     VARCHAR(20) NOT NULL COMMENT '전화번호',
    m_gender    CHAR(1)     NOT NULL COMMENT '성별',
    m_addr      VARCHAR(20) NULL COMMENT '주소',
    m_birthday  DATE        NULL COMMENT '생년월일',
    m_nick      VARCHAR(20) NULL COMMENT '별명',
    status_code VARCHAR(20) NOT NULL COMMENT '회원상태',           -- FK ref: status_code
    PRIMARY KEY (m_no)
) COMMENT ='회원목록';

CREATE TABLE token
(
    token_no          INT AUTO_INCREMENT COMMENT '토큰고유번호',    -- PK
    m_no              VARCHAR(10)  NOT NULL COMMENT '회원고유번호', -- FK ref: member
    token_type        VARCHAR(20)  NOT NULL COMMENT '토큰타입',
    token_value_acc   VARCHAR(500) NOT NULL COMMENT '토큰값(액세스)',
    token_value_ref   VARCHAR(500) NOT NULL COMMENT '토큰값(리프레쉬)',
    token_expires_acc DATE         NOT NULL COMMENT '만료일(액세스)',
    token_expires_ref DATE         NOT NULL COMMENT '만료일(리프레쉬)',
    token_last_update DATE         NOT NULL COMMENT '최근수정일',
    PRIMARY KEY (token_no)
) COMMENT ='소셜로그인토큰';

CREATE TABLE login
(
    login_no       INT AUTO_INCREMENT COMMENT '로그인기록고유번호', -- PK
    m_no           VARCHAR(10) NOT NULL COMMENT '회원고유번호',  -- FK ref: member
    type_code      VARCHAR(20) NOT NULL COMMENT '로그인유형',
    login_browser  VARCHAR(20) NOT NULL COMMENT '사용브라우저',
    login_in_time  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '로그인시도일시',
    login_out_time TIMESTAMP   NULL     DEFAULT NULL COMMENT '세션종료일시',
    status_code    VARCHAR(20) NOT NULL COMMENT '로그인상태',   -- PK ref: status_code
    PRIMARY KEY (login_no)
) COMMENT ='로그인기록';

CREATE TABLE note
(
    note_no        INT AUTO_INCREMENT COMMENT '쪽지고유번호',      -- PK
    m_no           VARCHAR(10)  NOT NULL COMMENT '보낸회원고유번호', -- FK ref: member
    m_target_no    VARCHAR(10)  NOT NULL COMMENT '수신회원고유번호', -- FK ref: member
    note_body      VARCHAR(500) NOT NULL COMMENT '쪽지내용',
    note_send_time TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '보낸일시',
    note_read_time TIMESTAMP    NULL     DEFAULT NULL COMMENT '읽은일시',
    status_code    VARCHAR(20)  NOT NULL COMMENT '쪽지상태',     -- FK ref: status_code
    PRIMARY KEY (note_no)
) COMMENT ='쪽지내역';

CREATE TABLE alert
(
    alert_no       INT AUTO_INCREMENT COMMENT '알림기록고유번호',      -- PK
    m_no           VARCHAR(10)  NOT NULL COMMENT '알림수신회원고유번호', -- FK ref: member
    type_code      VARCHAR(20)  NOT NULL COMMENT '알림유형',
    alert_detail   VARCHAR(500) NOT NULL COMMENT '알림상세메시지',
    alert_uptime   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '알림보낸일시',
    alert_readtime TIMESTAMP    NULL     DEFAULT NULL COMMENT '알림읽은일시',
    status_code    VARCHAR(20)  NOT NULL COMMENT '알림상태',       -- FK ref: status_code
    PRIMARY KEY (alert_no)
) COMMENT ='최근알림내역';

CREATE TABLE alert_allow
(
    alert_allow_no  INT AUTO_INCREMENT COMMENT '알림수신여부고유번호', -- PK
    m_no            VARCHAR(10) NOT NULL COMMENT '회원고유번호',   -- FK ref: member
    allow_sms       BOOLEAN     NOT NULL DEFAULT FALSE COMMENT 'SMS 수신여부',
    allow_email     BOOLEAN     NOT NULL DEFAULT FALSE COMMENT 'email 수신여부',
    allow_marketing BOOLEAN     NOT NULL DEFAULT FALSE COMMENT '광고성정보 수신여부',
    allow_member    BOOLEAN     NOT NULL DEFAULT FALSE COMMENT '회원알림 수신여부',
    allow_note      BOOLEAN     NOT NULL DEFAULT FALSE COMMENT '쪽지알림 수신여부',
    allow_bulletin  BOOLEAN     NOT NULL DEFAULT FALSE COMMENT '게시글알림 수신여부',
    allow_party     BOOLEAN     NOT NULL DEFAULT FALSE COMMENT '파티알림 수신여부',
    PRIMARY KEY (alert_allow_no)
) COMMENT ='알림수신여부';

CREATE TABLE alert_allow_changes
(
    alert_change_no     INT AUTO_INCREMENT COMMENT '수신여부변경고유번호',   -- PK
    alert_allow_no      INT         NOT NULL COMMENT '알림수신여부고유번호', -- FK ref: alert_allow
    type_code           VARCHAR(20) NOT NULL COMMENT '수신여부변경유형',   -- FK ref: type_code
    allow_change_before BOOLEAN     NOT NULL COMMENT '수신여부변경전',
    allow_change_after  BOOLEAN     NOT NULL COMMENT '수신여부변경후',
    allow_change_date   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '수신여부변경일시',
    PRIMARY KEY (alert_change_no)
) COMMENT ='알림수신여부변경내역';

CREATE TABLE follow
(
    follow_no   INT AUTO_INCREMENT COMMENT '회원팔로우고유번호',   -- PK
    m_no        VARCHAR(10) NOT NULL COMMENT '팔로우한회원번호',  -- FK ref: member
    m_target_no VARCHAR(10) NOT NULL COMMENT '팔로우대상회원번호', -- FK ref: member
    follow_date TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '팔로우일시',
    PRIMARY KEY (follow_no)
) COMMENT ='회원팔로우목록';

CREATE TABLE block
(
    block_no    INT AUTO_INCREMENT COMMENT '회원차단고유번호',   -- PK
    m_no        VARCHAR(10) NOT NULL COMMENT '차단한회원번호',  -- FK ref: member
    m_target_no VARCHAR(10) NOT NULL COMMENT '차단대상회원번호', -- FK ref: member
    block_date  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '차단일시',
    PRIMARY KEY (block_no)
) COMMENT ='회원차단목록';

CREATE TABLE member_quit
(
    m_quit_no          INT AUTO_INCREMENT COMMENT '탈퇴기록고유번호',  -- PK
    m_no               VARCHAR(10)  NOT NULL COMMENT '회원고유번호', -- FK ref: member
    type_code          VARCHAR(20)  NOT NULL COMMENT '탈퇴유형',   -- FK ref: type_code
    quit_reason_detail VARCHAR(200) NULL COMMENT '탈퇴상세사유',
    quit_date          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '탈퇴일시',
    PRIMARY KEY (m_quit_no)
) COMMENT ='탈퇴회원목록';

CREATE TABLE member_sleep
(
    m_sleep_no   INT AUTO_INCREMENT COMMENT '휴면기록고유번호', -- PK
    m_no         VARCHAR(10) NOT NULL COMMENT '회원고유번호', -- FK ref: member
    m_last_login TIMESTAMP   NOT NULL COMMENT '마지막로그인일시',
    m_sleep_date TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '휴면전환일시',
    PRIMARY KEY (m_sleep_no)
) COMMENT ='휴면회원목록';

CREATE TABLE member_punish
(
    m_punish_no       INT AUTO_INCREMENT COMMENT '제재기록고유번호',     -- PK
    m_no              VARCHAR(10)  NOT NULL COMMENT '제재회원고유번호',  -- FK ref: member
    m_admin_no        VARCHAR(10)  NOT NULL COMMENT '제재관리자고유번호', -- FK ref: member
    type_code         VARCHAR(20)  NOT NULL COMMENT '제재유형',      -- FK ref: type_code
    punish_reason     VARCHAR(500) NOT NULL COMMENT '제재사유상세',
    punish_start_date TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '제재일시',
    punish_period     TIMESTAMP    NOT NULL COMMENT '제재기간',
    punish_end_period TIMESTAMP    NOT NULL COMMENT '제재해제예정일시',
    status_code       VARCHAR(20)  NOT NULL COMMENT '제재상태',      -- FK ref: status_code
    PRIMARY KEY (m_punish_no)
) COMMENT ='제재회원목록';

CREATE TABLE member_report
(
    m_report_no     INT AUTO_INCREMENT COMMENT '신고기록고유번호',      -- PK
    m_no            VARCHAR(10)  NOT NULL COMMENT '신고한회원고유번호',  -- FK ref: member
    m_target_no     VARCHAR(10)  NOT NULL COMMENT '신고대상회원고유번호', -- FK ref: member
    type_code       VARCHAR(20)  NOT NULL COMMENT '신고유형',       -- FK ref: type_code
    m_report_reason VARCHAR(500) NOT NULL COMMENT '신고사유상세',
    m_report_date   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '신고일시',
    status_code     VARCHAR(20)  NOT NULL COMMENT '신고접수상태',     -- FK ref: status_code
    PRIMARY KEY (m_report_no)
) COMMENT ='회원신고내역';

CREATE TABLE member_question
(
    m_question_no   INT AUTO_INCREMENT COMMENT '문의고유번호',    -- PK
    m_no            VARCHAR(10)  NOT NULL COMMENT '회원고유번호', -- FK ref: member
    type_code       VARCHAR(20)  NOT NULL COMMENT '문의유형',   -- FK ref: type_code
    m_question_body VARCHAR(500) NOT NULL COMMENT '문의내용',
    m_question_date TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '문의일시',
    status_code     VARCHAR(20)  NOT NULL COMMENT '문의접수상태', -- FK ref: status_code
    PRIMARY KEY (m_question_no)
) COMMENT ='문의내역';

CREATE TABLE announce
(
    announce_no    INT AUTO_INCREMENT COMMENT '관리자게시글고유번호',      -- PK
    m_admin_no     VARCHAR(10)   NOT NULL COMMENT '게시글작성자(관리자)', -- FK ref: member
    type_code      VARCHAR(20)   NOT NULL COMMENT '관리자게시글유형',    -- FK ref: type_code
    announce_title VARCHAR(50)   NOT NULL COMMENT '게시글제목',
    announce_body  VARCHAR(1000) NOT NULL COMMENT '게시글본문',
    announce_date  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
    PRIMARY KEY (announce_no)
) COMMENT ='관리자게시글목록';

CREATE TABLE member_point
(
    m_point_no VARCHAR(10) NOT NULL COMMENT '회원보유포인트고유번호', -- PK
    m_no       VARCHAR(10) NOT NULL COMMENT '회원고유번호',      -- FK ref: member
    m_point    INT         NOT NULL DEFAULT 0 COMMENT '포인트잔액',
    PRIMARY KEY (m_point_no)
) COMMENT ='회원보유포인트';

CREATE TABLE point_history
(
    point_history_no     VARCHAR(10) NOT NULL COMMENT '포인트증감내역고유번호', -- PK
    m_point_no           VARCHAR(10) NOT NULL COMMENT '회원보유포인트고유번호', -- FK ref: member
    type_code            VARCHAR(20) NOT NULL COMMENT '증감유형',        -- FK ref: type_code
    point_history_before INT         NOT NULL COMMENT '보유포인트(변동전)',
    point_history_change INT         NOT NULL COMMENT '포인트변동량',
    point_history_after  INT         NOT NULL COMMENT '보유포인트(변동후)',
    point_history_date   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '변동일시',
    status_code          VARCHAR(20) NOT NULL COMMENT '처리상태',        -- FK ref: status_code
    PRIMARY KEY (point_history_no)
) COMMENT ='포인트증감내역';

CREATE TABLE point_payment
(
    pay_no           INT AUTO_INCREMENT COMMENT '포인트충전고유번호',     -- PK
    point_history_no VARCHAR(10) NOT NULL COMMENT '포인트증감내역고유번호', -- FK ref: point_history
    pay_type         VARCHAR(20) NOT NULL COMMENT '결제방법',
    pay_date         TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '요청일시',
    status_code      VARCHAR(20) NOT NULL COMMENT '처리상태',        -- FK ref: status_code
    PRIMARY KEY (pay_no)
) COMMENT ='포인트충전내역';

CREATE TABLE point_payment_info
(
    pay_info_no       INT AUTO_INCREMENT COMMENT '포인트충전상세번호',    -- PK
    pay_no            INT          NOT NULL COMMENT '포인트충전고유번호', -- FK ref: point_payment
    pay_method        VARCHAR(20)  NOT NULL COMMENT '지불수단',
    pay_amount        INT          NOT NULL COMMENT '지불금액',
    pay_unique_id     VARCHAR(500) NOT NULL COMMENT '주문고유ID',
    pay_order_name    VARCHAR(20)  NOT NULL COMMENT '주문상품이름',
    pay_customer_name VARCHAR(20)  NOT NULL COMMENT '결제자이름',
    pay_date          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '결제일시',
    pay_success_yn    BOOLEAN      NOT NULL COMMENT '결제성공여부',
    pay_success_url   VARCHAR(500) NOT NULL COMMENT '콜백주소(성공시)',
    pay_fail_url      VARCHAR(500) NOT NULL COMMENT '콜백주소(실패시)',
    PRIMARY KEY (pay_info_no)
) COMMENT ='포인트충전상세';

CREATE TABLE point_withdraw
(
    withdraw_no      INT AUTO_INCREMENT COMMENT '포인트인출고유번호',     -- PK
    point_history_no VARCHAR(10) NOT NULL COMMENT '포인트증감내역고유번호', -- FK ref: point_history
    withdraw_bank    VARCHAR(10) NOT NULL COMMENT '지급은행',
    withdraw_account VARCHAR(20) NOT NULL COMMENT '지급계좌',
    withdraw_name    VARCHAR(20) NOT NULL COMMENT '예금주',
    withdraw_amount  INT         NOT NULL COMMENT '지급금액',
    withdraw_date    TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '요청일시',
    status_code      VARCHAR(20) NOT NULL COMMENT '처리상태',        -- FK ref: status_code
    PRIMARY KEY (withdraw_no)
) COMMENT ='포인트인출내역';

CREATE TABLE board
(
    b_no           VARCHAR(10)  NOT NULL COMMENT '게시글고유번호', -- PK
    m_no           VARCHAR(10)  NOT NULL COMMENT '게시글작성자',  -- FK ref: member
    b_title        VARCHAR(50)  NOT NULL COMMENT '게시글 제목',
    b_body         VARCHAR(500) NOT NULL COMMENT '게시글 내용',
    b_date         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
    b_period       TIMESTAMP    NOT NULL COMMENT '모집기한',
    b_view_count   INT          NOT NULL DEFAULT 0 COMMENT '게시글조회수',
    b_wish_count   INT          NOT NULL DEFAULT 0 COMMENT '게시글찜수',
    b_report_count INT          NOT NULL DEFAULT 0 COMMENT '게시글신고횟수',
    status_code    VARCHAR(20)  NOT NULL COMMENT '게시글상태',   -- FK ref: status_code
    PRIMARY KEY (b_no)
) COMMENT ='게시글';

CREATE TABLE location
(
    location_no     INT AUTO_INCREMENT COMMENT '위치고유번호',        -- PK
    b_no            VARCHAR(10)     NOT NULL COMMENT '게시글고유번호', -- FK ref: board
    location_do     VARCHAR(10)     NOT NULL COMMENT '위치(도)',
    location_si     VARCHAR(10)     NOT NULL COMMENT '위치(시/군)',
    location_gu     VARCHAR(10)     NOT NULL COMMENT '위치(구)',
    location_dong   VARCHAR(10)     NOT NULL COMMENT '위치(동)',
    location_detail VARCHAR(50)     NULL COMMENT '상세주소',
    location_x      DECIMAL(10, 10) NOT NULL COMMENT '위치(x좌표)',
    location_y      DECIMAL(10, 10) NOT NULL COMMENT '위치(y좌표)',
    PRIMARY KEY (location_no)
) COMMENT ='위치';

CREATE TABLE board_image
(
    b_image_no     INT AUTO_INCREMENT COMMENT '이미지고유번호',    -- PK
    b_no           VARCHAR(10)  NOT NULL COMMENT '게시글고유번호', -- FK ref: board
    b_image_source VARCHAR(500) NOT NULL COMMENT '이미지 소스',
    PRIMARY KEY (b_image_no)
) COMMENT ='이미지';

CREATE TABLE reply
(
    reply_no           INT AUTO_INCREMENT COMMENT '댓글고유번호',     -- PK
    b_no               VARCHAR(10)  NOT NULL COMMENT '게시글고유번호', -- FK ref: board
    m_no               VARCHAR(10)  NOT NULL COMMENT '게시자고유번호', -- FK ref: member
    reply_body         VARCHAR(200) NOT NULL COMMENT '댓글내용',
    reply_date         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
    reply_report_count INT          NOT NULL DEFAULT 0 COMMENT '신고횟수',
    status_code        VARCHAR(20)  NOT NULL COMMENT '댓글 상태',   -- FK ref: status_code
    PRIMARY KEY (reply_no)
) COMMENT ='댓글목록';

CREATE TABLE wishlist
(
    wish_no   INT AUTO_INCREMENT COMMENT '찜고유번호',      -- PK
    b_no      VARCHAR(10) NOT NULL COMMENT '게시글고유번호',  -- FK ref: board
    m_no      VARCHAR(10) NOT NULL COMMENT '찜한회원고유번호', -- FK ref: member
    wish_date TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '찜한일시',
    PRIMARY KEY (wish_no)
) COMMENT ='찜목록';

CREATE TABLE category
(
    c_code   VARCHAR(20) NOT NULL COMMENT '카테고리코드', -- PK
    c_group1 VARCHAR(10) NOT NULL COMMENT '대분류',
    c_group2 VARCHAR(10) NOT NULL COMMENT '중분류',
    c_group3 VARCHAR(10) NOT NULL COMMENT '소분류',
    PRIMARY KEY (c_code)
) COMMENT ='카테고리';

CREATE TABLE party
(
    p_no            VARCHAR(10)  NOT NULL COMMENT '파티고유번호',  -- PK
    b_no            VARCHAR(10)  NOT NULL COMMENT '게시물고유번호', -- FK ref: board
    c_code          VARCHAR(10)  NOT NULL COMMENT '카테고리',
    p_url           VARCHAR(500) NOT NULL COMMENT '상품URL',
    p_amount        INT          NOT NULL COMMENT '상품총수량',
    p_amount_remain INT          NOT NULL COMMENT '잔여수량',
    p_price         INT          NOT NULL COMMENT '상품총가격',
    p_price_remain  INT          NOT NULL COMMENT '남은금액',
    p_start_date    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '파티시작일시',
    p_end_date      TIMESTAMP    NULL COMMENT '파티종료일시',
    status_code     VARCHAR(20)  NOT NULL COMMENT '파티상태',    -- FK ref: status_code
    PRIMARY KEY (p_no)
) COMMENT ='파티정보';

CREATE TABLE party_request
(
    p_request_no   INT AUTO_INCREMENT COMMENT '파티가입신청고유번호',  -- PK
    p_no           VARCHAR(10) NOT NULL COMMENT '파티고유번호',    -- FK ref: party
    m_no           VARCHAR(10) NOT NULL COMMENT '신청자회원고유번호', -- FK ref: member
    request_amount INT         NOT NULL COMMENT '구매희망수량',
    request_date   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '파티가입신청일시',
    status_code    VARCHAR(20) NOT NULL COMMENT '가입상태',      -- FK ref: status_code
    PRIMARY KEY (p_request_no)
) COMMENT ='파티신청현황';

CREATE TABLE party_member
(
    p_member_no      VARCHAR(10) NOT NULL COMMENT '파티원정보고유번호', -- PK
    p_no             VARCHAR(10) NOT NULL COMMENT '파티고유번호',    -- FK ref: party
    m_no             VARCHAR(10) NOT NULL COMMENT '파티원회원고유번호', -- FK ref: member
    pm_amount        INT         NOT NULL COMMENT '구매예정수량',
    pm_price         INT         NOT NULL COMMENT '결제예정금액',
    member_join_date TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '파티가입일시',
    member_is_leader VARCHAR(10) NOT NULL COMMENT '파티장/파티원',
    PRIMARY KEY (p_member_no)
) COMMENT ='파티원정보';

CREATE TABLE party_purchase
(
    purchase_no INT AUTO_INCREMENT COMMENT '결제현황고유번호', -- PK
    p_no        VARCHAR(10) NOT NULL COMMENT '파티고유번호',
    p_member_no VARCHAR(10) NOT NULL COMMENT '파티원정보고유번호',
    status_code VARCHAR(20) NOT NULL COMMENT '결제상태',   -- FK ref: status_code
    PRIMARY KEY (purchase_no)
) COMMENT ='파티원결제현황';

CREATE TABLE party_purchase_detail
(
    purchase_detail_no INT AUTO_INCREMENT COMMENT '결제상세현황고유번호',    -- PK
    purchase_no        INT         NOT NULL COMMENT '결제현황고유번호',    -- FK ref: party_purchase
    point_history_no   VARCHAR(10) NOT NULL COMMENT '포인트증감내역고유번호', -- FK ref: point_history
    purchase_price     INT         NOT NULL COMMENT '결제예정금액',
    purchase_purchase  INT         NOT NULL DEFAULT 0 COMMENT '결제완료금액',
    purchase_date      TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '결제일시',
    PRIMARY KEY (purchase_detail_no)
) COMMENT ='파티원결제상세현황';

CREATE TABLE party_shipping
(
    shipping_no     INT AUTO_INCREMENT COMMENT '상품배송현황고유번호', -- PK
    p_no            VARCHAR(10) NOT NULL COMMENT '파티고유번호',   -- FK ref: party
    invoice_courier VARCHAR(10) NULL COMMENT '택배사명',
    invoice_no      VARCHAR(20) NULL COMMENT '상품운송장번호',
    add_date        TIMESTAMP   NULL COMMENT '등록일시',
    status_code     VARCHAR(20) NOT NULL COMMENT '배송상태',     -- FK ref: status_code
    PRIMARY KEY (shipping_no)
) COMMENT ='상품배송현황';

CREATE TABLE party_reception
(
    reception_no      INT AUTO_INCREMENT COMMENT '상품수취현황고유번호',   -- PK
    p_no              VARCHAR(10)  NOT NULL COMMENT '파티고유번호',    -- FK ref: party
    p_member_no       VARCHAR(10)  NOT NULL COMMENT '파티원정보고유번호', -- FK ref: party_member
    reception_comment VARCHAR(200) NULL COMMENT '파티코멘트',
    reception_date    TIMESTAMP    NULL COMMENT '수취확인일시',
    status_code       VARCHAR(20)  NOT NULL COMMENT '수취상태',      -- FK ref: status_code
    PRIMARY KEY (reception_no)
) COMMENT ='상품수취현황';

CREATE TABLE party_reception_image
(
    reception_image_no INT AUTO_INCREMENT COMMENT '상품수취이미지고유번호',   -- PK
    reception_no       INT          NOT NULL COMMENT '상품수취현황고유번호', -- FK ref: party_reception
    image_source       VARCHAR(500) NOT NULL COMMENT '이미지소스',
    image_upload_date  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '업로드일시',
    PRIMARY KEY (reception_image_no)
) COMMENT ='상품수취이미지';

CREATE TABLE party_settlement
(
    p_settle_no INT AUTO_INCREMENT COMMENT '파티정산고유번호',    -- PK
    p_no        VARCHAR(10) NOT NULL COMMENT '파티고유번호',    -- FK ref: party
    p_member_no VARCHAR(10) NOT NULL COMMENT '파티원정보고유번호', -- FK ref: party_member
    status_code VARCHAR(20) NOT NULL COMMENT '정산상태',      -- FK ref: status_code
    PRIMARY KEY (p_settle_no)
) COMMENT ='파티정산현황';

CREATE TABLE party_settlement_detail
(
    settle_detail_no        INT AUTO_INCREMENT COMMENT '정산상세현황고유번호',    -- PK
    p_settle_no             INT         NOT NULL COMMENT '파티정산고유번호',    -- FK ref: party_settlement
    point_history_no        VARCHAR(10) NOT NULL COMMENT '포인트증감내역고유번호', -- FK ref: point_history
    p_settle_expected_price INT         NOT NULL COMMENT '정산예정금액',
    p_settle_complete_price INT         NOT NULL DEFAULT 0 COMMENT '정산완료금액',
    p_settle_date           TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '정산일시',
    PRIMARY KEY (settle_detail_no)
) COMMENT ='파티정산상세현황';



-- 2. 참조 관계 설정
-- member 테이블의 외래 키 설정
ALTER TABLE member
    ADD CONSTRAINT FK_member_level FOREIGN KEY (m_level) REFERENCES member_level (m_level),
    ADD CONSTRAINT FK_member_status FOREIGN KEY (status_code) REFERENCES status_code (status_code);

-- token 테이블의 외래 키 설정
ALTER TABLE token
    ADD CONSTRAINT FK_token_member FOREIGN KEY (m_no) REFERENCES member (m_no);

-- login 테이블의 외래 키 설정
ALTER TABLE login
    ADD CONSTRAINT FK_login_member FOREIGN KEY (m_no) REFERENCES member (m_no),
    ADD CONSTRAINT FK_login_type_code FOREIGN KEY (type_code) REFERENCES type_code (type_code),
    ADD CONSTRAINT FK_login_status FOREIGN KEY (status_code) REFERENCES status_code (status_code);

-- note 테이블의 외래 키 설정
ALTER TABLE note
    ADD CONSTRAINT FK_note_sender FOREIGN KEY (m_no) REFERENCES member (m_no),
    ADD CONSTRAINT FK_note_receiver FOREIGN KEY (m_target_no) REFERENCES member (m_no),
    ADD CONSTRAINT FK_note_status FOREIGN KEY (status_code) REFERENCES status_code (status_code);

-- alert 테이블의 외래 키 설정
ALTER TABLE alert
    ADD CONSTRAINT FK_alert_member FOREIGN KEY (m_no) REFERENCES member (m_no),
    ADD CONSTRAINT FK_alert_type_code FOREIGN KEY (type_code) REFERENCES type_code (type_code),
    ADD CONSTRAINT FK_alert_status FOREIGN KEY (status_code) REFERENCES status_code (status_code);

-- alert_allow 테이블의 외래 키 설정
ALTER TABLE alert_allow
    ADD CONSTRAINT FK_alert_allow_member FOREIGN KEY (m_no) REFERENCES member (m_no);

-- alert_allow_changes 테이블의 외래 키 설정
ALTER TABLE alert_allow_changes
    ADD CONSTRAINT FK_alert_allow_changes_alert_allow FOREIGN KEY (alert_allow_no) REFERENCES alert_allow (alert_allow_no),
    ADD CONSTRAINT FK_alert_allow_changes_type_code FOREIGN KEY (type_code) REFERENCES type_code (type_code);

-- follow 테이블의 외래 키 설정
ALTER TABLE follow
    ADD CONSTRAINT FK_follow_member FOREIGN KEY (m_no) REFERENCES member (m_no),
    ADD CONSTRAINT FK_follow_target_member FOREIGN KEY (m_target_no) REFERENCES member (m_no);

-- block 테이블의 외래 키 설정
ALTER TABLE block
    ADD CONSTRAINT FK_block_member FOREIGN KEY (m_no) REFERENCES member (m_no),
    ADD CONSTRAINT FK_block_target_member FOREIGN KEY (m_target_no) REFERENCES member (m_no);

-- member_quit 테이블의 외래 키 설정
ALTER TABLE member_quit
    ADD CONSTRAINT FK_member_quit_member FOREIGN KEY (m_no) REFERENCES member (m_no),
    ADD CONSTRAINT FK_member_quit_type_code FOREIGN KEY (type_code) REFERENCES type_code (type_code);

-- member_sleep 테이블의 외래 키 설정
ALTER TABLE member_sleep
    ADD CONSTRAINT FK_member_sleep_member FOREIGN KEY (m_no) REFERENCES member (m_no);

-- member_punish 테이블의 외래 키 설정
ALTER TABLE member_punish
    ADD CONSTRAINT FK_member_punish_member FOREIGN KEY (m_no) REFERENCES member (m_no),
    ADD CONSTRAINT FK_member_punish_admin FOREIGN KEY (m_admin_no) REFERENCES member (m_no),
    ADD CONSTRAINT FK_member_punish_type_code FOREIGN KEY (type_code) REFERENCES type_code (type_code),
    ADD CONSTRAINT FK_member_punish_status FOREIGN KEY (status_code) REFERENCES status_code (status_code);

-- member_report 테이블의 외래 키 설정
ALTER TABLE member_report
    ADD CONSTRAINT FK_member_report_member FOREIGN KEY (m_no) REFERENCES member (m_no),
    ADD CONSTRAINT FK_member_report_target_member FOREIGN KEY (m_target_no) REFERENCES member (m_no),
    ADD CONSTRAINT FK_member_report_type_code FOREIGN KEY (type_code) REFERENCES type_code (type_code),
    ADD CONSTRAINT FK_member_report_status FOREIGN KEY (status_code) REFERENCES status_code (status_code);

-- member_question 테이블의 외래 키 설정
ALTER TABLE member_question
    ADD CONSTRAINT FK_member_question_member FOREIGN KEY (m_no) REFERENCES member (m_no),
    ADD CONSTRAINT FK_member_question_type_code FOREIGN KEY (type_code) REFERENCES type_code (type_code),
    ADD CONSTRAINT FK_member_question_status FOREIGN KEY (status_code) REFERENCES status_code (status_code);

-- announce 테이블의 외래 키 설정
ALTER TABLE announce
    ADD CONSTRAINT FK_announce_admin FOREIGN KEY (m_admin_no) REFERENCES member (m_no),
    ADD CONSTRAINT FK_announce_type_code FOREIGN KEY (type_code) REFERENCES type_code (type_code);

-- member_point 테이블의 외래 키 설정
ALTER TABLE member_point
    ADD CONSTRAINT FK_member_point_member FOREIGN KEY (m_no) REFERENCES member (m_no);

-- point_history 테이블의 외래 키 설정
ALTER TABLE point_history
    ADD CONSTRAINT FK_point_history_member_point FOREIGN KEY (m_point_no) REFERENCES member_point (m_point_no),
    ADD CONSTRAINT FK_point_history_type_code FOREIGN KEY (type_code) REFERENCES type_code (type_code),
    ADD CONSTRAINT FK_point_history_status FOREIGN KEY (status_code) REFERENCES status_code (status_code);

-- point_payment 테이블의 외래 키 설정
ALTER TABLE point_payment
    ADD CONSTRAINT FK_point_payment_point_history FOREIGN KEY (point_history_no) REFERENCES point_history (point_history_no),
    ADD CONSTRAINT FK_point_payment_status FOREIGN KEY (status_code) REFERENCES status_code (status_code);

-- point_payment_info 테이블의 외래 키 설정
ALTER TABLE point_payment_info
    ADD CONSTRAINT FK_point_payment_info_point_payment FOREIGN KEY (pay_no) REFERENCES point_payment (pay_no);

-- point_withdraw 테이블의 외래 키 설정
ALTER TABLE point_withdraw
    ADD CONSTRAINT FK_point_withdraw_point_history FOREIGN KEY (point_history_no) REFERENCES point_history (point_history_no),
    ADD CONSTRAINT FK_point_withdraw_status FOREIGN KEY (status_code) REFERENCES status_code (status_code);

-- board 테이블의 외래 키 설정
ALTER TABLE board
    ADD CONSTRAINT FK_board_member FOREIGN KEY (m_no) REFERENCES member (m_no),
    ADD CONSTRAINT FK_board_status FOREIGN KEY (status_code) REFERENCES status_code (status_code);

-- location 테이블의 외래 키 설정
ALTER TABLE location
    ADD CONSTRAINT FK_location_board FOREIGN KEY (b_no) REFERENCES board (b_no);

-- board_image 테이블의 외래 키 설정
ALTER TABLE board_image
    ADD CONSTRAINT FK_board_image_board FOREIGN KEY (b_no) REFERENCES board (b_no);

-- reply 테이블의 외래 키 설정
ALTER TABLE reply
    ADD CONSTRAINT FK_reply_board FOREIGN KEY (b_no) REFERENCES board (b_no),
    ADD CONSTRAINT FK_reply_member FOREIGN KEY (m_no) REFERENCES member (m_no),
    ADD CONSTRAINT FK_reply_status FOREIGN KEY (status_code) REFERENCES status_code (status_code);

-- wishlist 테이블의 외래 키 설정
ALTER TABLE wishlist
    ADD CONSTRAINT FK_wishlist_board FOREIGN KEY (b_no) REFERENCES board (b_no),
    ADD CONSTRAINT FK_wishlist_member FOREIGN KEY (m_no) REFERENCES member (m_no);

-- party 테이블의 외래 키 설정
ALTER TABLE party
    ADD CONSTRAINT FK_party_board FOREIGN KEY (b_no) REFERENCES board (b_no),
    ADD CONSTRAINT FK_party_category FOREIGN KEY (c_code) REFERENCES category (c_code),
    ADD CONSTRAINT FK_party_status FOREIGN KEY (status_code) REFERENCES status_code (status_code);

-- party_request 테이블의 외래 키 설정
ALTER TABLE party_request
    ADD CONSTRAINT FK_party_request_party FOREIGN KEY (p_no) REFERENCES party (p_no),
    ADD CONSTRAINT FK_party_request_member FOREIGN KEY (m_no) REFERENCES member (m_no),
    ADD CONSTRAINT FK_party_request_status FOREIGN KEY (status_code) REFERENCES status_code (status_code);

-- party_member 테이블의 외래 키 설정
ALTER TABLE party_member
    ADD CONSTRAINT FK_party_member_party FOREIGN KEY (p_no) REFERENCES party (p_no),
    ADD CONSTRAINT FK_party_member_member FOREIGN KEY (m_no) REFERENCES member (m_no);

-- party_purchase 테이블의 외래 키 설정
ALTER TABLE party_purchase
    ADD CONSTRAINT FK_party_purchase_party FOREIGN KEY (p_no) REFERENCES party (p_no),
    ADD CONSTRAINT FK_party_purchase_party_member FOREIGN KEY (p_member_no) REFERENCES party_member (p_member_no),
    ADD CONSTRAINT FK_party_purchase_status FOREIGN KEY (status_code) REFERENCES status_code (status_code);

-- party_purchase_detail 테이블의 외래 키 설정
ALTER TABLE party_purchase_detail
    ADD CONSTRAINT FK_party_purchase_detail_party_purchase FOREIGN KEY (purchase_no) REFERENCES party_purchase (purchase_no),
    ADD CONSTRAINT FK_party_purchase_detail_point_history FOREIGN KEY (point_history_no) REFERENCES point_history (point_history_no);

-- party_shipping 테이블의 외래 키 설정
ALTER TABLE party_shipping
    ADD CONSTRAINT FK_party_shipping_party FOREIGN KEY (p_no) REFERENCES party (p_no),
    ADD CONSTRAINT FK_party_shipping_status FOREIGN KEY (status_code) REFERENCES status_code (status_code);

-- party_reception 테이블의 외래 키 설정
ALTER TABLE party_reception
    ADD CONSTRAINT FK_party_reception_party FOREIGN KEY (p_no) REFERENCES party (p_no),
    ADD CONSTRAINT FK_party_reception_party_member FOREIGN KEY (p_member_no) REFERENCES party_member (p_member_no),
    ADD CONSTRAINT FK_party_reception_status FOREIGN KEY (status_code) REFERENCES status_code (status_code);

-- party_reception_image 테이블의 외래 키 설정
ALTER TABLE party_reception_image
    ADD CONSTRAINT FK_party_reception_image_reception FOREIGN KEY (reception_no) REFERENCES party_reception (reception_no);

-- party_settlement 테이블의 외래 키 설정
ALTER TABLE party_settlement
    ADD CONSTRAINT FK_party_settlement_party FOREIGN KEY (p_no) REFERENCES party (p_no),
    ADD CONSTRAINT FK_party_settlement_party_member FOREIGN KEY (p_member_no) REFERENCES party_member (p_member_no),
    ADD CONSTRAINT FK_party_settlement_status FOREIGN KEY (status_code) REFERENCES status_code (status_code);

-- party_settlement_detail 테이블의 외래 키 설정
ALTER TABLE party_settlement_detail
    ADD CONSTRAINT FK_party_settlement_detail_party_settlement FOREIGN KEY (p_settle_no) REFERENCES party_settlement (p_settle_no),
    ADD CONSTRAINT FK_party_settlement_detail_point_history FOREIGN KEY (point_history_no) REFERENCES point_history (point_history_no);


-- 3. 기본 데이터 삽입
INSERT IGNORE INTO type_code (type_code, code_group, code_group_sub, code_description)
VALUES ('T010101', '회원', '로그인', '자사몰'),
       ('T010102', '회원', '로그인', '네이버'),
       ('T010103', '회원', '로그인', '구글'),
       ('T010104', '회원', '로그인', '카카오'),
       ('T010201', '회원', '알림', '공지'),
       ('T010202', '회원', '알림', '광고성'),
       ('T010203', '회원', '알림', '회원'),
       ('T010204', '회원', '알림', '쪽지'),
       ('T010205', '회원', '알림', '게시글'),
       ('T010206', '회원', '알림', '파티'),
       ('T010207', '회원', '알림', 'SMS'),
       ('T010208', '회원', '알림', '이메일'),
       ('T010301', '회원', '탈퇴', '서비스 불만족'),
       ('T010302', '회원', '탈퇴', '기타'),
       ('T010401', '회원', '제재', '부적절한 콘텐츠'),
       ('T010402', '회원', '제재', '사기 및 사기성 행위'),
       ('T010403', '회원', '제재', '스팸 및 악성 행위'),
       ('T010404', '회원', '제재', '지적 재산권 침해'),
       ('T010405', '회원', '제재', '사생활 침해 및 개인정보 보호'),
       ('T010406', '회원', '제재', '사용자 행위 관련'),
       ('T010407', '회원', '제재', '기타'),
       ('T010501', '회원', '신고', '부적절한 콘텐츠'),
       ('T010502', '회원', '신고', '사기 및 사기성 행위'),
       ('T010503', '회원', '신고', '스팸 및 악성 행위'),
       ('T010504', '회원', '신고', '지적 재산권 침해'),
       ('T010505', '회원', '신고', '사생활 침해 및 개인정보 보호'),
       ('T010506', '회원', '신고', '사용자 행위 관련'),
       ('T010507', '회원', '신고', '기타'),
       ('T010601', '회원', '문의', '회원'),
       ('T010602', '회원', '문의', '게시글'),
       ('T010603', '회원', '문의', '파티'),
       ('T010604', '회원', '문의', '기타'),
       ('T020101', '관리자', '게시글', '공지사항'),
       ('T020102', '관리자', '게시글', 'FAQ'),
       ('T020103', '관리자', '게시글', '프로모션'),
       ('T030101', '포인트', '증가', '충전'),
       ('T030102', '포인트', '증가', '프로모션'),
       ('T030103', '포인트', '증가', '환불'),
       ('T030104', '포인트', '증가', '파티 정산'),
       ('T030201', '포인트', '증가(관리자)', '사용자 회수'),
       ('T030202', '포인트', '증가(관리자)', '파티 입금'),
       ('T030301', '포인트', '감소', '인출'),
       ('T030302', '포인트', '감소', '회수'),
       ('T030303', '포인트', '감소', '파티 결제'),
       ('T030401', '포인트', '감소(관리자)', '프로모션지급'),
       ('T030402', '포인트', '감소(관리자)', '사용자 환불'),
       ('T030403', '포인트', '감소(관리자)', '파티장 입금');

INSERT IGNORE INTO status_code (status_code, code_group, code_group_sub, code_description)
VALUES ('S010101', '회원', '목록', '정상'),
       ('S010102', '회원', '목록', '휴면'),
       ('S010103', '회원', '목록', '제재'),
       ('S010104', '회원', '목록', '탈퇴'),
       ('S010201', '회원', '로그인', '로그인 실패'),
       ('S010202', '회원', '로그인', '로그인 중'),
       ('S010203', '회원', '로그인', '로그아웃'),
       ('S010301', '회원', '쪽지', '수신'),
       ('S010302', '회원', '쪽지', '읽음'),
       ('S010303', '회원', '쪽지', '삭제'),
       ('S010401', '회원', '알림', '수신'),
       ('S010402', '회원', '알림', '읽음'),
       ('S010403', '회원', '알림', '삭제'),
       ('S010501', '회원', '제재', '처리 대기중'),
       ('S010502', '회원', '제재', '수감 중'),
       ('S010503', '회원', '제재', '석방 완료'),
       ('S010601', '회원', '신고 접수', '처리 대기중'),
       ('S010602', '회원', '신고 접수', '처리 완료'),
       ('S010603', '회원', '신고 접수', '보류'),
       ('S010701', '회원', '문의', '처리 대기중'),
       ('S010702', '회원', '문의', '처리 완료'),
       ('S010703', '회원', '문의', '보류'),
       ('S030101', '포인트', '증감 내역', '성공'),
       ('S030102', '포인트', '증감 내역', '실패'),
       ('S030201', '포인트', '충전 내역', '성공'),
       ('S030202', '포인트', '충전 내역', '잔액 부족'),
       ('S030203', '포인트', '충전 내역', '결제 취소'),
       ('S030204', '포인트', '충전 내역', '은행 점검'),
       ('S030205', '포인트', '충전 내역', '네트워크 오류'),
       ('S030206', '포인트', '충전 내역', '서비스 오류'),
       ('S030301', '포인트', '인출 내역', '성공'),
       ('S030302', '포인트', '인출 내역', '포인트 부족'),
       ('S030303', '포인트', '인출 내역', '결제 취소'),
       ('S030304', '포인트', '인출 내역', '은행 점검'),
       ('S030305', '포인트', '인출 내역', '네트워크 오류'),
       ('S030306', '포인트', '인출 내역', '서비스 오류'),
       ('S040101', '게시글', '목록', '모집중'),
       ('S040102', '게시글', '목록', '모집 완료'),
       ('S040103', '게시글', '목록', '완료'),
       ('S040104', '게시글', '목록', '임시 차단'),
       ('S040105', '게시글', '목록', '삭제'),
       ('S040106', '게시글', '목록', '기한 만료'),
       ('S050101', '댓글', '목록', '게시 완료'),
       ('S050102', '댓글', '목록', '수정 완료'),
       ('S050103', '댓글', '목록', '임시 차단'),
       ('S050104', '댓글', '목록', '삭제'),
       ('S060101', '파티', '정상', '모집중'),
       ('S060102', '파티', '정상', '모집완료'),
       ('S060103', '파티', '정상', '파티원 결제대기'),
       ('S060104', '파티', '정상', '파티장 결제대기'),
       ('S060105', '파티', '정상', '쇼핑몰 배송중'),
       ('S060106', '파티', '정상', '수취 대기중'),
       ('S060107', '파티', '정상', '정산 대기중'),
       ('S060108', '파티', '정상', '정산 완료'),
       ('S060109', '파티', '비정상', '파티 해제'),
       ('S060110', '파티', '비정상', '기간 만료'),
       ('S060201', '파티', '신청 현황', '수락 대기중'),
       ('S060202', '파티', '신청 현황', '가입완료'),
       ('S060203', '파티', '신청 현황', '거절'),
       ('S060204', '파티', '신청 현황', '본인 취소'),
       ('S060205', '파티', '신청 현황', '강퇴'),
       ('S060301', '파티', '결제', '대기'),
       ('S060302', '파티', '결제', '완료'),
       ('S060303', '파티', '결제', '취소'),
       ('S060401', '파티', '상품 배송', '배송전'),
       ('S060402', '파티', '상품 배송', '배송중'),
       ('S060403', '파티', '상품 배송', '배송 완료'),
       ('S060501', '파티', '상품 수취', '대기'),
       ('S060502', '파티', '상품 수취', '완료'),
       ('S060503', '파티', '상품 수취', '이슈'),
       ('S060601', '파티', '정산', '정산 대기중'),
       ('S060602', '파티', '정산', '정산 완료'),
       ('S060603', '파티', '정산', '환불 요청'),
       ('S060604', '파티', '정산', '환불 완료'),
       ('S060605', '파티', '정산', '오류 발생');

INSERT IGNORE INTO member_level (m_level, m_level_name)
VALUES (1, '일반이용자'),
       (2, '관리자');

INSERT IGNORE INTO category (c_code, c_group1, c_group2, c_group3)
VALUES ('CF0101', '식품', '신선식품', '채소'),
       ('CF0102', '식품', '신선식품', '과일'),
       ('CF0103', '식품', '신선식품', '수산/건어물'),
       ('CF0104', '식품', '신선식품', '정육/계란류'),
       ('CF0105', '식품', '신선식품', '우유/유제품'),
       ('CF0201', '식품', '곡물류', '쌀/잡곡'),
       ('CF0202', '식품', '곡물류', '견과류'),
       ('CF0301', '식품', '반찬류', '김치/반찬'),
       ('CF0302', '식품', '반찬류', '밀키트'),
       ('CF0401', '식품', '가공식품', '면류/통조림'),
       ('CF0402', '식품', '가공식품', '양념/오일'),
       ('CF0403', '식품', '가공식품', '간식/과자'),
       ('CF0404', '식품', '가공식품', '베이커리/잼'),
       ('CF0501', '식품', '음료', '생수/음료'),
       ('CF0502', '식품', '음료', '커피/차'),
       ('CF9901', '식품', '기타', '건강식품');



-- 4. 샘플 데이터 삽입
-- member 테이블에 데이터 삽입
INSERT INTO member (m_no, m_level, m_id, m_pw, m_name, m_email, m_phone, m_gender, m_addr, m_birthday, m_nick, status_code)
VALUES ('M000001', 2, 'admin', 'admin', '관리자', 'admin@example.com', '010-9876-5432', 'F', '서울시 강북구', '1985-05-05', '관리자', 'S010101'),
       ('M000002', 1, 'user2', 'password2', '홍길동', 'hong@example.com', '010-1234-5678', 'M', '서울시 강남구', '1980-01-01', '길동', 'S010101'),
       ('M000003', 1, 'user3', 'password3', '이순신', 'lee@example.com', '010-5678-1234', 'M', '서울시 중구', '1975-03-15', '순신', 'S010102'),
       ('M000004', 1, 'user4', 'password4', '김유신', 'kim@example.com', '010-1111-2222', 'M', '경기도 성남시', '1982-02-10', '유신', 'S010101'),
       ('M000005', 1, 'user5', 'password5', '박혁거세', 'park@example.com', '010-3333-4444', 'M', '부산시 해운대구', '1979-05-15', '혁거세', 'S010101'),
       ('M000006', 1, 'user6', 'password6', '강감찬', 'kang@example.com', '010-5555-6666', 'M', '광주시 남구', '1985-08-20', '감찬', 'S010102'),
       ('M000007', 1, 'user7', 'password7', '을지문덕', 'ulji@example.com', '010-7777-8888', 'M', '대구시 중구', '1983-03-03', '문덕', 'S010101'),
       ('M000008', 1, 'user8', 'password8', '연개소문', 'yeon@example.com', '010-9999-0000', 'M', '대전시 서구', '1984-04-04', '개소문', 'S010103'),
       ('M000009', 1, 'user9', 'password9', '계백', 'gye@example.com', '010-1212-3434', 'M', '울산시 북구', '1980-05-05', '백', 'S010104'),
       ('M000010', 1, 'user10', 'password10', '최영', 'choi@example.com', '010-5656-7878', 'M', '충주시 충주읍', '1981-06-06', '영', 'S010101');

-- member_point 테이블에 데이터 삽입
INSERT IGNORE INTO member_point (m_point_no, m_no)
VALUES ('MP000001', 'M000001'),
       ('MP000002', 'M000002'),
       ('MP000003', 'M000003'),
       ('MP000004', 'M000004'),
       ('MP000005', 'M000005'),
       ('MP000006', 'M000006'),
       ('MP000007', 'M000007'),
       ('MP000008', 'M000008'),
       ('MP000009', 'M000009'),
       ('MP000010', 'M000010');

-- point_history 테이블에 데이터 삽입
INSERT IGNORE INTO point_history (point_history_no, m_point_no, type_code, point_history_before, point_history_change, point_history_after, status_code)
VALUES ('PH000001', 'MP000002', 'T030101', 0, 4000, 4000, 'S030101'),
       ('PH000002', 'MP000002', 'T030303', 4000, -4000, 0, 'S030101'),
       ('PH000003', 'MP000003', 'T030101', 0, 3000, 3000, 'S030101'),
       ('PH000004', 'MP000003', 'T030303', 3000, -3000, 0, 'S030101'),
       ('PH000005', 'MP000004', 'T030101', 0, 1000, 1000, 'S030101'),
       ('PH000006', 'MP000004', 'T030303', 1000, 0, 1000, 'S030102'),
       ('PH000007', 'MP000004', 'T030303', 1000, -1000, 0, 'S030101'),
       ('PH000008', 'MP000001', 'T030104', 0, 8000, 8000, 'S030101'),
       ('PH000009', 'MP000001', 'T030301', 8000, -8000, 0, 'S030101');

-- point_payment 테이블에 데이터 삽입
INSERT IGNORE INTO point_payment (pay_no, point_history_no, pay_type, status_code)
VALUES (1, 'PH000001', '토스페이', 'S030201'),
       (2, 'PH000003', '토스페이', 'S030201'),
       (3, 'PH000005', '토스페이', 'S030201');

-- ponit_payment_info 테이블에 데이터 삽입
INSERT IGNORE INTO point_payment_info (pay_no, pay_method, pay_amount, pay_unique_id, pay_order_name, pay_customer_name, pay_success_yn, pay_success_url, pay_fail_url)
VALUES (1, '카드', 4000, '?', '상품명1', '홍길동', 1, '?', '?'),
       (2, '카드', 3000, '?', '상품명1', '이순신', 1, '?', '?'),
       (3, '카드', 1000, '?', '상품명1', '김유신', 1, '?', '?');

-- point_withdraw 테이블에 데이터 삽입
INSERT IGNORE INTO point_withdraw (point_history_no, withdraw_bank, withdraw_account, withdraw_name, withdraw_amount, status_code)
VALUES ('PH000009', '국민', '6516854846471', '홍길동', 8000, 'S030301');

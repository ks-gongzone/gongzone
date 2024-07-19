--
--
--  !! 주의: 실행시 DB 초기화됩니다 !!
--
--			최종 수정 - 240719
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


CREATE TABLE IF NOT EXISTS `type_code` (
  `type_code` varchar(20) NOT NULL COMMENT '유형코드',
  `code_group` varchar(10) NOT NULL COMMENT '코드대분류',
  `code_group_sub` varchar(10) NOT NULL COMMENT '코드중분류',
  `code_description` varchar(50) NOT NULL COMMENT '코드설명',
  PRIMARY KEY (`type_code`)
) COMMENT='유형코드';


CREATE TABLE IF NOT EXISTS `status_code` (
  `status_code` varchar(20) NOT NULL COMMENT '상태코드',
  `code_group` varchar(10) NOT NULL COMMENT '코드대분류',
  `code_group_sub` varchar(10) NOT NULL COMMENT '코드중분류',
  `code_description` varchar(50) NOT NULL COMMENT '코드설명',
  PRIMARY KEY (`status_code`)
) COMMENT='상태코드';


CREATE TABLE IF NOT EXISTS `member_level` (
  `m_level` int NOT NULL COMMENT '권한코드',
  `m_level_name` varchar(10) NOT NULL COMMENT '권한명',
  PRIMARY KEY (`m_level`)
) COMMENT='회원권한';


CREATE TABLE IF NOT EXISTS `member` (
  `m_no` varchar(10) NOT NULL COMMENT '회원고유번호',
  `m_level` int NOT NULL DEFAULT '1' COMMENT '권한코드',
  `m_id` varchar(50) NOT NULL COMMENT '아이디',
  `m_pw` varchar(50) DEFAULT NULL COMMENT '비밀번호',
  `m_name` varchar(20) NOT NULL COMMENT '이름',
  `m_email` varchar(40) NOT NULL COMMENT '이메일',
  `m_phone` varchar(20) NOT NULL COMMENT '전화번호',
  `m_gender` char(1) NOT NULL COMMENT '성별',
  `m_addr` varchar(100) DEFAULT NULL COMMENT '주소',
  `m_birthday` date DEFAULT NULL COMMENT '생년월일',
  `m_nick` varchar(20) DEFAULT NULL COMMENT '별명',
  `status_code` varchar(20) NOT NULL COMMENT '회원상태',
  PRIMARY KEY (`m_no`),
  UNIQUE KEY `m_id` (`m_id`),
  KEY `FK_member_level` (`m_level`),
  KEY `FK_member_status` (`status_code`),
  CONSTRAINT `FK_member_level` FOREIGN KEY (`m_level`) REFERENCES `member_level` (`m_level`),
  CONSTRAINT `FK_member_status` FOREIGN KEY (`status_code`) REFERENCES `status_code` (`status_code`)
) COMMENT='회원목록';


CREATE TABLE IF NOT EXISTS `token` (
  `token_no` int NOT NULL AUTO_INCREMENT COMMENT '토큰고유번호',
  `m_no` varchar(10) NOT NULL COMMENT '회원고유번호',
  `token_type` varchar(20) NOT NULL COMMENT '토큰타입',
  `token_value_acc` varchar(500) NOT NULL COMMENT '토큰값(액세스)',
  `token_value_ref` varchar(500) DEFAULT NULL COMMENT '토큰값(리프레쉬)',
  `token_expires_acc` date NOT NULL COMMENT '만료일(액세스)',
  `token_expires_ref` date DEFAULT NULL COMMENT '만료일(리프레쉬)',
  `token_last_update` date NOT NULL COMMENT '최근수정일',
  PRIMARY KEY (`token_no`),
  KEY `FK_token_member` (`m_no`),
  CONSTRAINT `FK_token_member` FOREIGN KEY (`m_no`) REFERENCES `member` (`m_no`)
) COMMENT='소셜로그인토큰';


CREATE TABLE IF NOT EXISTS `login` (
  `login_no` int NOT NULL AUTO_INCREMENT COMMENT '로그인기록고유번호',
  `m_no` varchar(10) NOT NULL COMMENT '회원고유번호',
  `type_code` varchar(20) NOT NULL COMMENT '로그인유형',
  `login_browser` varchar(20) NOT NULL COMMENT '사용브라우저',
  `login_in_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '로그인시도일시',
  `login_out_time` timestamp NULL DEFAULT NULL COMMENT '세션종료일시',
  `status_code` varchar(20) NOT NULL COMMENT '로그인상태',
  PRIMARY KEY (`login_no`),
  KEY `FK_login_member` (`m_no`),
  KEY `FK_login_type_code` (`type_code`),
  KEY `FK_login_status` (`status_code`),
  CONSTRAINT `FK_login_member` FOREIGN KEY (`m_no`) REFERENCES `member` (`m_no`),
  CONSTRAINT `FK_login_status` FOREIGN KEY (`status_code`) REFERENCES `status_code` (`status_code`),
  CONSTRAINT `FK_login_type_code` FOREIGN KEY (`type_code`) REFERENCES `type_code` (`type_code`)
) COMMENT='로그인기록';


CREATE TABLE IF NOT EXISTS `note` (
  `note_no` int NOT NULL AUTO_INCREMENT COMMENT '쪽지고유번호',
  `m_no` varchar(10) NOT NULL COMMENT '보낸회원고유번호',
  `m_target_no` varchar(10) NOT NULL COMMENT '수신회원고유번호',
  `note_body` varchar(500) NOT NULL COMMENT '쪽지내용',
  `note_send_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '보낸일시',
  `note_read_time` timestamp NULL DEFAULT NULL COMMENT '읽은일시',
  `status_code` varchar(20) NOT NULL COMMENT '쪽지상태',
  `status_code_target` varchar(20) NOT NULL COMMENT '수신회원 쪽지상태',
  PRIMARY KEY (`note_no`),
  KEY `FK_note_sender` (`m_no`),
  KEY `FK_note_receiver` (`m_target_no`),
  KEY `FK_note_status` (`status_code`),
  KEY `FK_note_status_target` (`status_code_target`),
  CONSTRAINT `FK_note_receiver` FOREIGN KEY (`m_target_no`) REFERENCES `member` (`m_no`),
  CONSTRAINT `FK_note_sender` FOREIGN KEY (`m_no`) REFERENCES `member` (`m_no`),
  CONSTRAINT `FK_note_status` FOREIGN KEY (`status_code`) REFERENCES `status_code` (`status_code`),
  CONSTRAINT `FK_note_status_target` FOREIGN KEY (`status_code_target`) REFERENCES `status_code` (`status_code`)
) COMMENT='쪽지내역';


CREATE TABLE IF NOT EXISTS `alert` (
  `alert_no` int NOT NULL AUTO_INCREMENT COMMENT '알림기록고유번호',
  `m_no` varchar(10) NOT NULL COMMENT '알림수신회원고유번호',
  `type_code` varchar(20) NOT NULL COMMENT '알림유형',
  `alert_detail` varchar(500) NOT NULL COMMENT '알림상세메시지',
  `alert_uptime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '알림보낸일시',
  `alert_readtime` timestamp NULL DEFAULT NULL COMMENT '알림읽은일시',
  `status_code` varchar(20) NOT NULL DEFAULT 'S010301' COMMENT '알림상태',
  PRIMARY KEY (`alert_no`),
  KEY `FK_alert_member` (`m_no`),
  KEY `FK_alert_type_code` (`type_code`),
  KEY `FK_alert_status` (`status_code`),
  CONSTRAINT `FK_alert_member` FOREIGN KEY (`m_no`) REFERENCES `member` (`m_no`),
  CONSTRAINT `FK_alert_status` FOREIGN KEY (`status_code`) REFERENCES `status_code` (`status_code`),
  CONSTRAINT `FK_alert_type_code` FOREIGN KEY (`type_code`) REFERENCES `type_code` (`type_code`)
) COMMENT='최근알림내역';


CREATE TABLE IF NOT EXISTS `alert_allow` (
  `alert_allow_no` int NOT NULL AUTO_INCREMENT COMMENT '알림수신여부고유번호',
  `m_no` varchar(10) NOT NULL COMMENT '회원고유번호',
  `allow_sms` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'SMS 수신여부',
  `allow_email` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'email 수신여부',
  `allow_marketing` tinyint(1) NOT NULL DEFAULT '0' COMMENT '광고성정보 수신여부',
  `allow_member` tinyint(1) NOT NULL DEFAULT '0' COMMENT '회원알림 수신여부',
  `allow_note` tinyint(1) NOT NULL DEFAULT '0' COMMENT '쪽지알림 수신여부',
  `allow_bulletin` tinyint(1) NOT NULL DEFAULT '0' COMMENT '게시글알림 수신여부',
  `allow_party` tinyint(1) NOT NULL DEFAULT '0' COMMENT '파티알림 수신여부',
  PRIMARY KEY (`alert_allow_no`),
  KEY `FK_alert_allow_member` (`m_no`),
  CONSTRAINT `FK_alert_allow_member` FOREIGN KEY (`m_no`) REFERENCES `member` (`m_no`)
) COMMENT='알림수신여부';


CREATE TABLE IF NOT EXISTS `alert_allow_changes` (
  `alert_change_no` int NOT NULL AUTO_INCREMENT COMMENT '수신여부변경고유번호',
  `alert_allow_no` int NOT NULL COMMENT '알림수신여부고유번호',
  `type_code` varchar(20) NOT NULL COMMENT '수신여부변경유형',
  `allow_change_before` tinyint(1) DEFAULT NULL COMMENT '수신여부변경전',
  `allow_change_after` tinyint(1) NOT NULL COMMENT '수신여부변경후',
  `allow_change_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '수신여부변경일시',
  PRIMARY KEY (`alert_change_no`),
  KEY `FK_alert_allow_changes_alert_allow` (`alert_allow_no`),
  KEY `FK_alert_allow_changes_type_code` (`type_code`),
  CONSTRAINT `FK_alert_allow_changes_alert_allow` FOREIGN KEY (`alert_allow_no`) REFERENCES `alert_allow` (`alert_allow_no`),
  CONSTRAINT `FK_alert_allow_changes_type_code` FOREIGN KEY (`type_code`) REFERENCES `type_code` (`type_code`)
) COMMENT='알림수신여부변경내역';


CREATE TABLE IF NOT EXISTS `follow` (
  `follow_no` int NOT NULL AUTO_INCREMENT COMMENT '회원팔로우고유번호',
  `m_no` varchar(10) NOT NULL COMMENT '팔로우한회원번호',
  `m_target_no` varchar(10) NOT NULL COMMENT '팔로우대상회원번호',
  `follow_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '팔로우일시',
  PRIMARY KEY (`follow_no`),
  KEY `FK_follow_member` (`m_no`),
  KEY `FK_follow_target_member` (`m_target_no`),
  CONSTRAINT `FK_follow_member` FOREIGN KEY (`m_no`) REFERENCES `member` (`m_no`),
  CONSTRAINT `FK_follow_target_member` FOREIGN KEY (`m_target_no`) REFERENCES `member` (`m_no`)
) COMMENT='회원팔로우목록';


CREATE TABLE IF NOT EXISTS `block` (
  `block_no` int NOT NULL AUTO_INCREMENT COMMENT '회원차단고유번호',
  `m_no` varchar(10) NOT NULL COMMENT '차단한회원번호',
  `m_target_no` varchar(10) NOT NULL COMMENT '차단대상회원번호',
  `block_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '차단일시',
  PRIMARY KEY (`block_no`),
  KEY `FK_block_member` (`m_no`),
  KEY `FK_block_target_member` (`m_target_no`),
  CONSTRAINT `FK_block_member` FOREIGN KEY (`m_no`) REFERENCES `member` (`m_no`),
  CONSTRAINT `FK_block_target_member` FOREIGN KEY (`m_target_no`) REFERENCES `member` (`m_no`)
) COMMENT='회원차단목록';


CREATE TABLE IF NOT EXISTS `member_quit` (
  `m_quit_no` int NOT NULL AUTO_INCREMENT COMMENT '탈퇴기록고유번호',
  `m_no` varchar(10) NOT NULL COMMENT '회원고유번호',
  `type_code` varchar(20) NOT NULL COMMENT '탈퇴유형',
  `quit_reason_detail` varchar(200) DEFAULT NULL COMMENT '탈퇴상세사유',
  `quit_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '탈퇴일시',
  PRIMARY KEY (`m_quit_no`),
  KEY `FK_member_quit_member` (`m_no`),
  KEY `FK_member_quit_type_code` (`type_code`),
  CONSTRAINT `FK_member_quit_member` FOREIGN KEY (`m_no`) REFERENCES `member` (`m_no`),
  CONSTRAINT `FK_member_quit_type_code` FOREIGN KEY (`type_code`) REFERENCES `type_code` (`type_code`)
) COMMENT='탈퇴회원목록';


CREATE TABLE IF NOT EXISTS `member_sleep` (
  `m_sleep_no` int NOT NULL AUTO_INCREMENT COMMENT '휴면기록고유번호',
  `m_no` varchar(10) NOT NULL COMMENT '회원고유번호',
  `m_last_login` timestamp NOT NULL COMMENT '마지막로그인일시',
  `m_sleep_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '휴면전환일시',
  PRIMARY KEY (`m_sleep_no`),
  KEY `FK_member_sleep_member` (`m_no`),
  CONSTRAINT `FK_member_sleep_member` FOREIGN KEY (`m_no`) REFERENCES `member` (`m_no`)
) COMMENT='휴면회원목록';


CREATE TABLE IF NOT EXISTS `member_punish` (
  `m_punish_no` int NOT NULL AUTO_INCREMENT COMMENT '제재기록고유번호',
  `m_no` varchar(10) NOT NULL COMMENT '제재회원고유번호',
  `m_admin_no` varchar(10) NOT NULL COMMENT '제재관리자고유번호',
  `type_code` varchar(20) NOT NULL COMMENT '제재유형',
  `punish_reason` varchar(500) NOT NULL COMMENT '제재사유상세',
  `punish_start_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '제재일시',
  `punish_period` varchar(50) NOT NULL DEFAULT '' COMMENT '제재기간',
  `punish_end_period` timestamp NOT NULL COMMENT '제재해제예정일시',
  `status_code` varchar(20) NOT NULL COMMENT '제재상태',
  PRIMARY KEY (`m_punish_no`),
  KEY `FK_member_punish_member` (`m_no`),
  KEY `FK_member_punish_admin` (`m_admin_no`),
  KEY `FK_member_punish_type_code` (`type_code`),
  KEY `FK_member_punish_status` (`status_code`),
  CONSTRAINT `FK_member_punish_admin` FOREIGN KEY (`m_admin_no`) REFERENCES `member` (`m_no`),
  CONSTRAINT `FK_member_punish_member` FOREIGN KEY (`m_no`) REFERENCES `member` (`m_no`),
  CONSTRAINT `FK_member_punish_status` FOREIGN KEY (`status_code`) REFERENCES `status_code` (`status_code`),
  CONSTRAINT `FK_member_punish_type_code` FOREIGN KEY (`type_code`) REFERENCES `type_code` (`type_code`)
) COMMENT='제재회원목록';


CREATE TABLE IF NOT EXISTS `member_report` (
  `m_report_no` int NOT NULL AUTO_INCREMENT COMMENT '신고기록고유번호',
  `m_no` varchar(10) NOT NULL COMMENT '신고한회원고유번호',
  `m_target_no` varchar(10) NOT NULL COMMENT '신고대상회원고유번호',
  `type_code` varchar(20) NOT NULL COMMENT '신고유형',
  `m_report_reason` varchar(500) NOT NULL COMMENT '신고사유상세',
  `m_report_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '신고일시',
  `status_code` varchar(20) NOT NULL COMMENT '신고접수상태',
  PRIMARY KEY (`m_report_no`),
  KEY `FK_member_report_member` (`m_no`),
  KEY `FK_member_report_target_member` (`m_target_no`),
  KEY `FK_member_report_type_code` (`type_code`),
  KEY `FK_member_report_status` (`status_code`),
  CONSTRAINT `FK_member_report_member` FOREIGN KEY (`m_no`) REFERENCES `member` (`m_no`),
  CONSTRAINT `FK_member_report_status` FOREIGN KEY (`status_code`) REFERENCES `status_code` (`status_code`),
  CONSTRAINT `FK_member_report_target_member` FOREIGN KEY (`m_target_no`) REFERENCES `member` (`m_no`),
  CONSTRAINT `FK_member_report_type_code` FOREIGN KEY (`type_code`) REFERENCES `type_code` (`type_code`)
) COMMENT='회원신고내역';


CREATE TABLE IF NOT EXISTS `member_question` (
  `m_question_no` int NOT NULL AUTO_INCREMENT COMMENT '문의고유번호',
  `m_no` varchar(10) NOT NULL COMMENT '회원고유번호',
  `type_code` varchar(20) NOT NULL COMMENT '문의유형',
  `m_question_body` varchar(500) NOT NULL COMMENT '문의내용',
  `m_question_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '문의일시',
  `status_code` varchar(20) NOT NULL COMMENT '문의접수상태',
  PRIMARY KEY (`m_question_no`),
  KEY `FK_member_question_member` (`m_no`),
  KEY `FK_member_question_type_code` (`type_code`),
  KEY `FK_member_question_status` (`status_code`),
  CONSTRAINT `FK_member_question_member` FOREIGN KEY (`m_no`) REFERENCES `member` (`m_no`),
  CONSTRAINT `FK_member_question_status` FOREIGN KEY (`status_code`) REFERENCES `status_code` (`status_code`),
  CONSTRAINT `FK_member_question_type_code` FOREIGN KEY (`type_code`) REFERENCES `type_code` (`type_code`)
) COMMENT='문의내역';


CREATE TABLE IF NOT EXISTS `announce` (
  `announce_no` int NOT NULL AUTO_INCREMENT COMMENT '관리자게시글고유번호',
  `m_admin_no` varchar(10) NOT NULL COMMENT '게시글작성자(관리자)',
  `type_code` varchar(20) NOT NULL COMMENT '관리자게시글유형',
  `announce_title` varchar(50) NOT NULL COMMENT '게시글제목',
  `announce_body` text NOT NULL COMMENT '게시글본문',
  `announce_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
  `announce_view_count` int NOT NULL COMMENT '게시글조회수',
  PRIMARY KEY (`announce_no`),
  KEY `FK_announce_admin` (`m_admin_no`),
  KEY `FK_announce_type_code` (`type_code`),
  CONSTRAINT `FK_announce_admin` FOREIGN KEY (`m_admin_no`) REFERENCES `member` (`m_no`),
  CONSTRAINT `FK_announce_type_code` FOREIGN KEY (`type_code`) REFERENCES `type_code` (`type_code`)
) COMMENT='관리자게시글목록';


CREATE TABLE IF NOT EXISTS `member_point` (
  `m_point_no` varchar(10) NOT NULL COMMENT '회원보유포인트고유번호',
  `m_no` varchar(10) NOT NULL COMMENT '회원고유번호',
  `m_point` int NOT NULL DEFAULT '0' COMMENT '포인트잔액',
  PRIMARY KEY (`m_point_no`),
  KEY `FK_member_point_member` (`m_no`),
  CONSTRAINT `FK_member_point_member` FOREIGN KEY (`m_no`) REFERENCES `member` (`m_no`)
) COMMENT='회원보유포인트';


CREATE TABLE IF NOT EXISTS `point_history` (
  `point_history_no` varchar(10) NOT NULL COMMENT '포인트증감내역고유번호',
  `m_point_no` varchar(10) NOT NULL COMMENT '회원보유포인트고유번호',
  `type_code` varchar(20) NOT NULL COMMENT '증감유형',
  `point_history_before` int NOT NULL COMMENT '보유포인트(변동전)',
  `point_history_change` int NOT NULL COMMENT '포인트변동량',
  `point_history_after` int NOT NULL COMMENT '보유포인트(변동후)',
  `point_history_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '변동일시',
  `status_code` varchar(20) NOT NULL DEFAULT 'S030102' COMMENT '처리상태',
  PRIMARY KEY (`point_history_no`),
  KEY `FK_point_history_member_point` (`m_point_no`),
  KEY `FK_point_history_type_code` (`type_code`),
  KEY `FK_point_history_status` (`status_code`),
  CONSTRAINT `FK_point_history_member_point` FOREIGN KEY (`m_point_no`) REFERENCES `member_point` (`m_point_no`),
  CONSTRAINT `FK_point_history_status` FOREIGN KEY (`status_code`) REFERENCES `status_code` (`status_code`),
  CONSTRAINT `FK_point_history_type_code` FOREIGN KEY (`type_code`) REFERENCES `type_code` (`type_code`)
) COMMENT='포인트증감내역';


CREATE TABLE IF NOT EXISTS `point_payment` (
  `pay_no` int NOT NULL AUTO_INCREMENT COMMENT '포인트충전고유번호',
  `point_history_no` varchar(10) NOT NULL COMMENT '포인트증감내역고유번호',
  `pay_type` varchar(20) NOT NULL COMMENT '결제방법',
  `pay_tx_type` varchar(50) NOT NULL COMMENT '트랜잭션타입',
  `pay_tx_id` varchar(50) NOT NULL COMMENT '결제txid',
  `pay_id` varchar(50) NOT NULL COMMENT '결제id',
  `pay_code` varchar(20) DEFAULT NULL COMMENT '결제코드',
  `pay_message` varchar(50) DEFAULT NULL COMMENT '결제메시지',
  `pay_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '요청일시',
  `status_code` varchar(20) NOT NULL COMMENT '처리상태',
  PRIMARY KEY (`pay_no`),
  KEY `FK_point_payment_point_history` (`point_history_no`),
  KEY `FK_point_payment_status` (`status_code`),
  CONSTRAINT `FK_point_payment_point_history` FOREIGN KEY (`point_history_no`) REFERENCES `point_history` (`point_history_no`),
  CONSTRAINT `FK_point_payment_status` FOREIGN KEY (`status_code`) REFERENCES `status_code` (`status_code`)
) COMMENT='포인트충전내역';


CREATE TABLE IF NOT EXISTS `point_withdraw` (
  `withdraw_no` int NOT NULL AUTO_INCREMENT COMMENT '포인트인출고유번호',
  `point_history_no` varchar(10) NOT NULL COMMENT '포인트증감내역고유번호',
  `withdraw_bank` varchar(10) NOT NULL COMMENT '지급은행',
  `withdraw_account` varchar(20) NOT NULL COMMENT '지급계좌',
  `withdraw_name` varchar(20) NOT NULL COMMENT '예금주',
  `withdraw_amount` int NOT NULL COMMENT '지급금액',
  `withdraw_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '요청일시',
  `status_code` varchar(20) NOT NULL COMMENT '처리상태',
  PRIMARY KEY (`withdraw_no`),
  KEY `FK_point_withdraw_point_history` (`point_history_no`),
  KEY `FK_point_withdraw_status` (`status_code`),
  CONSTRAINT `FK_point_withdraw_point_history` FOREIGN KEY (`point_history_no`) REFERENCES `point_history` (`point_history_no`),
  CONSTRAINT `FK_point_withdraw_status` FOREIGN KEY (`status_code`) REFERENCES `status_code` (`status_code`)
) COMMENT='포인트인출내역';


CREATE TABLE IF NOT EXISTS `board` (
  `b_no` varchar(10) NOT NULL COMMENT '게시글고유번호',
  `m_no` varchar(10) NOT NULL COMMENT '게시글작성자',
  `b_title` varchar(50) NOT NULL COMMENT '게시글 제목',
  `b_body` varchar(500) NOT NULL COMMENT '게시글 내용',
  `b_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
  `b_period` timestamp NOT NULL COMMENT '모집기한',
  `b_view_count` int NOT NULL DEFAULT '0' COMMENT '게시글조회수',
  `b_wish_count` int NOT NULL DEFAULT '0' COMMENT '게시글찜수',
  `b_report_count` int NOT NULL DEFAULT '0' COMMENT '게시글신고횟수',
  `status_code` varchar(20) NOT NULL DEFAULT 'S040101' COMMENT '게시글상태',
  PRIMARY KEY (`b_no`),
  KEY `FK_board_member` (`m_no`),
  KEY `FK_board_status` (`status_code`),
  CONSTRAINT `FK_board_member` FOREIGN KEY (`m_no`) REFERENCES `member` (`m_no`),
  CONSTRAINT `FK_board_status` FOREIGN KEY (`status_code`) REFERENCES `status_code` (`status_code`)
) COMMENT='게시글';


CREATE TABLE IF NOT EXISTS `location` (
  `location_no` int NOT NULL AUTO_INCREMENT COMMENT '위치고유번호',
  `b_no` varchar(10) NOT NULL COMMENT '게시글고유번호',
  `location_do` varchar(10) NOT NULL COMMENT '위치(도)',
  `location_si` varchar(10) NOT NULL COMMENT '위치(시/군)',
  `location_gu` varchar(10) NOT NULL COMMENT '위치(구)',
  `location_dong` varchar(10) NOT NULL COMMENT '위치(동)',
  `location_detail` varchar(50) DEFAULT NULL COMMENT '상세주소',
  `location_x` double NOT NULL DEFAULT '0' COMMENT '위치(x좌표)',
  `location_y` double NOT NULL DEFAULT '0' COMMENT '위치(y좌표)',
  PRIMARY KEY (`location_no`),
  KEY `FK_location_board` (`b_no`),
  CONSTRAINT `FK_location_board` FOREIGN KEY (`b_no`) REFERENCES `board` (`b_no`)
) COMMENT='위치';


CREATE TABLE IF NOT EXISTS `file` (
  `file_no` int NOT NULL AUTO_INCREMENT COMMENT '파일 고유번호',
  `file_original_name` varchar(100) NOT NULL COMMENT '파일 기존 이름',
  `file_new_name` varchar(100) NOT NULL COMMENT '파일 신규 이름',
  `file_path` varchar(500) NOT NULL COMMENT '파일 경로',
  `file_size` int NOT NULL DEFAULT '0' COMMENT '파일 사이즈',
  `file_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '파일 업로드 날짜',
  PRIMARY KEY (`file_no`) USING BTREE
) COMMENT='이미지';


CREATE TABLE IF NOT EXISTS `file_relation` (
  `file_relation_no` int NOT NULL AUTO_INCREMENT COMMENT '파일 관계 고유번호',
  `file_no` int NOT NULL COMMENT '파일고유번호',
  `file_usage` varchar(10) NOT NULL COMMENT '파일 사용처',
  PRIMARY KEY (`file_relation_no`),
  KEY `file_no` (`file_no`),
  CONSTRAINT `file_no` FOREIGN KEY (`file_no`) REFERENCES `file` (`file_no`)
) COMMENT='파일 관계 테이블';


CREATE TABLE IF NOT EXISTS `reply` (
  `reply_no` int NOT NULL AUTO_INCREMENT COMMENT '댓글고유번호',
  `b_no` varchar(10) NOT NULL COMMENT '게시글고유번호',
  `m_no` varchar(10) NOT NULL COMMENT '게시자고유번호',
  `reply_body` varchar(200) NOT NULL COMMENT '댓글내용',
  `reply_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
  `reply_report_count` int NOT NULL DEFAULT '0' COMMENT '신고횟수',
  `status_code` varchar(20) NOT NULL COMMENT '댓글 상태',
  PRIMARY KEY (`reply_no`),
  KEY `FK_reply_board` (`b_no`),
  KEY `FK_reply_member` (`m_no`),
  KEY `FK_reply_status` (`status_code`),
  CONSTRAINT `FK_reply_board` FOREIGN KEY (`b_no`) REFERENCES `board` (`b_no`),
  CONSTRAINT `FK_reply_member` FOREIGN KEY (`m_no`) REFERENCES `member` (`m_no`),
  CONSTRAINT `FK_reply_status` FOREIGN KEY (`status_code`) REFERENCES `status_code` (`status_code`)
) COMMENT='댓글목록';


CREATE TABLE IF NOT EXISTS `wishlist` (
  `wish_no` int NOT NULL AUTO_INCREMENT COMMENT '찜고유번호',
  `b_no` varchar(10) NOT NULL COMMENT '게시글고유번호',
  `m_no` varchar(10) NOT NULL COMMENT '찜한회원고유번호',
  `wish_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '찜한일시',
  PRIMARY KEY (`wish_no`),
  KEY `FK_wishlist_board` (`b_no`),
  KEY `FK_wishlist_member` (`m_no`),
  CONSTRAINT `FK_wishlist_board` FOREIGN KEY (`b_no`) REFERENCES `board` (`b_no`),
  CONSTRAINT `FK_wishlist_member` FOREIGN KEY (`m_no`) REFERENCES `member` (`m_no`)
) COMMENT='찜목록';


CREATE TABLE IF NOT EXISTS `category` (
  `c_code` varchar(20) NOT NULL COMMENT '카테고리코드',
  `c_group1` varchar(10) NOT NULL COMMENT '대분류',
  `c_group2` varchar(10) NOT NULL COMMENT '중분류',
  `c_group3` varchar(10) NOT NULL COMMENT '소분류',
  PRIMARY KEY (`c_code`)
) COMMENT='카테고리';


CREATE TABLE IF NOT EXISTS `party` (
  `p_no` varchar(10) NOT NULL COMMENT '파티고유번호',
  `b_no` varchar(10) NOT NULL COMMENT '게시물고유번호',
  `c_code` varchar(10) NOT NULL COMMENT '카테고리',
  `p_url` varchar(500) NOT NULL COMMENT '상품URL',
  `p_amount` int NOT NULL COMMENT '상품총수량',
  `p_amount_remain` int NOT NULL COMMENT '잔여수량',
  `p_price` int NOT NULL COMMENT '상품총가격',
  `p_price_remain` int NOT NULL COMMENT '남은금액',
  `p_start_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '파티시작일시',
  `p_end_date` timestamp NULL DEFAULT NULL COMMENT '파티종료일시',
  `status_code` varchar(20) NOT NULL DEFAULT 'S060101' COMMENT '파티상태',
  PRIMARY KEY (`p_no`),
  KEY `FK_party_board` (`b_no`),
  KEY `FK_party_category` (`c_code`),
  KEY `FK_party_status` (`status_code`),
  CONSTRAINT `FK_party_board` FOREIGN KEY (`b_no`) REFERENCES `board` (`b_no`),
  CONSTRAINT `FK_party_category` FOREIGN KEY (`c_code`) REFERENCES `category` (`c_code`),
  CONSTRAINT `FK_party_status` FOREIGN KEY (`status_code`) REFERENCES `status_code` (`status_code`)
) COMMENT='파티정보';


CREATE TABLE IF NOT EXISTS `party_request` (
  `p_request_no` int NOT NULL AUTO_INCREMENT COMMENT '파티가입신청고유번호',
  `p_no` varchar(10) NOT NULL COMMENT '파티고유번호',
  `m_no` varchar(10) NOT NULL COMMENT '신청자회원고유번호',
  `request_amount` int NOT NULL COMMENT '구매희망수량',
  `request_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '파티가입신청일시',
  `status_code` varchar(20) NOT NULL COMMENT '가입상태',
  PRIMARY KEY (`p_request_no`),
  KEY `FK_party_request_party` (`p_no`),
  KEY `FK_party_request_member` (`m_no`),
  KEY `FK_party_request_status` (`status_code`),
  CONSTRAINT `FK_party_request_member` FOREIGN KEY (`m_no`) REFERENCES `member` (`m_no`),
  CONSTRAINT `FK_party_request_party` FOREIGN KEY (`p_no`) REFERENCES `party` (`p_no`),
  CONSTRAINT `FK_party_request_status` FOREIGN KEY (`status_code`) REFERENCES `status_code` (`status_code`)
) COMMENT='파티신청현황';


CREATE TABLE IF NOT EXISTS `party_member` (
  `p_member_no` varchar(10) NOT NULL COMMENT '파티원정보고유번호',
  `p_no` varchar(10) NOT NULL COMMENT '파티고유번호',
  `m_no` varchar(10) NOT NULL COMMENT '파티원회원고유번호',
  `pm_amount` int NOT NULL COMMENT '구매예정수량',
  `pm_price` int NOT NULL COMMENT '결제예정금액',
  `member_join_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '파티가입일시',
  `member_is_leader` varchar(10) NOT NULL DEFAULT '파티원' COMMENT '파티장/파티원',
  PRIMARY KEY (`p_member_no`),
  KEY `FK_party_member_party` (`p_no`),
  KEY `FK_party_member_member` (`m_no`),
  CONSTRAINT `FK_party_member_member` FOREIGN KEY (`m_no`) REFERENCES `member` (`m_no`),
  CONSTRAINT `FK_party_member_party` FOREIGN KEY (`p_no`) REFERENCES `party` (`p_no`)
) COMMENT='파티원정보';


CREATE TABLE IF NOT EXISTS `party_purchase` (
  `purchase_no` int NOT NULL AUTO_INCREMENT COMMENT '결제현황고유번호',
  `p_no` varchar(10) NOT NULL COMMENT '파티고유번호',
  `p_member_no` varchar(10) NOT NULL COMMENT '파티원정보고유번호',
  `purchase_price` int NOT NULL COMMENT '결제예정금액',
  `status_code` varchar(20) NOT NULL DEFAULT 'S060301' COMMENT '결제상태',
  PRIMARY KEY (`purchase_no`),
  KEY `FK_party_purchase_party` (`p_no`),
  KEY `FK_party_purchase_party_member` (`p_member_no`),
  KEY `FK_party_purchase_status` (`status_code`),
  CONSTRAINT `FK_party_purchase_party` FOREIGN KEY (`p_no`) REFERENCES `party` (`p_no`),
  CONSTRAINT `FK_party_purchase_party_member` FOREIGN KEY (`p_member_no`) REFERENCES `party_member` (`p_member_no`),
  CONSTRAINT `FK_party_purchase_status` FOREIGN KEY (`status_code`) REFERENCES `status_code` (`status_code`)
) COMMENT='파티원결제현황';


CREATE TABLE IF NOT EXISTS `party_purchase_detail` (
  `purchase_detail_no` int NOT NULL AUTO_INCREMENT COMMENT '결제상세현황고유번호',
  `purchase_no` int NOT NULL COMMENT '결제현황고유번호',
  `point_history_no` varchar(10) NOT NULL COMMENT '포인트증감내역고유번호',
  `purchase_price` int NOT NULL COMMENT '결제완료금액',
  `purchase_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '결제일시',
  PRIMARY KEY (`purchase_detail_no`),
  KEY `FK_party_purchase_detail_party_purchase` (`purchase_no`),
  KEY `FK_party_purchase_detail_point_history` (`point_history_no`),
  CONSTRAINT `FK_party_purchase_detail_party_purchase` FOREIGN KEY (`purchase_no`) REFERENCES `party_purchase` (`purchase_no`),
  CONSTRAINT `FK_party_purchase_detail_point_history` FOREIGN KEY (`point_history_no`) REFERENCES `point_history` (`point_history_no`)
) COMMENT='파티원결제상세현황';


CREATE TABLE IF NOT EXISTS `party_shipping` (
  `shipping_no` int NOT NULL AUTO_INCREMENT COMMENT '상품배송현황고유번호',
  `p_no` varchar(10) NOT NULL COMMENT '파티고유번호',
  `invoice_courier` varchar(10) DEFAULT NULL COMMENT '택배사명',
  `invoice_code` varchar(20) DEFAULT NULL COMMENT '상품운송장번호',
  `add_date` timestamp NULL DEFAULT NULL COMMENT '등록일시',
  `status_code` varchar(20) NOT NULL DEFAULT 'S060401' COMMENT '배송상태',
  PRIMARY KEY (`shipping_no`),
  KEY `FK_party_shipping_party` (`p_no`),
  KEY `FK_party_shipping_status` (`status_code`),
  CONSTRAINT `FK_party_shipping_party` FOREIGN KEY (`p_no`) REFERENCES `party` (`p_no`),
  CONSTRAINT `FK_party_shipping_status` FOREIGN KEY (`status_code`) REFERENCES `status_code` (`status_code`)
) COMMENT='상품배송현황';


CREATE TABLE IF NOT EXISTS `party_reception` (
  `r_no` varchar(10) NOT NULL COMMENT '상품수취현황고유번호',
  `p_no` varchar(10) NOT NULL COMMENT '파티고유번호',
  `p_member_no` varchar(10) NOT NULL COMMENT '파티원정보고유번호',
  `reception_comment` varchar(200) DEFAULT NULL COMMENT '파티코멘트',
  `reception_date` timestamp NULL DEFAULT NULL COMMENT '수취확인일시',
  `status_code` varchar(20) NOT NULL DEFAULT 'S060501' COMMENT '수취상태',
  PRIMARY KEY (`r_no`) USING BTREE,
  KEY `FK_party_reception_party` (`p_no`),
  KEY `FK_party_reception_party_member` (`p_member_no`),
  KEY `FK_party_reception_status` (`status_code`),
  CONSTRAINT `FK_party_reception_party` FOREIGN KEY (`p_no`) REFERENCES `party` (`p_no`),
  CONSTRAINT `FK_party_reception_party_member` FOREIGN KEY (`p_member_no`) REFERENCES `party_member` (`p_member_no`),
  CONSTRAINT `FK_party_reception_status` FOREIGN KEY (`status_code`) REFERENCES `status_code` (`status_code`)
) COMMENT='상품수취현황';


CREATE TABLE IF NOT EXISTS `party_settlement` (
  `p_settle_no` int NOT NULL AUTO_INCREMENT COMMENT '파티정산고유번호',
  `p_no` varchar(10) NOT NULL COMMENT '파티고유번호',
  `p_member_no` varchar(10) NOT NULL COMMENT '파티원정보고유번호',
  `p_settle_price` int NOT NULL COMMENT '정산예정금액',
  `status_code` varchar(20) NOT NULL DEFAULT 'S060601' COMMENT '정산상태',
  PRIMARY KEY (`p_settle_no`),
  KEY `FK_party_settlement_party` (`p_no`),
  KEY `FK_party_settlement_party_member` (`p_member_no`) USING BTREE,
  KEY `FK_party_settlement_status` (`status_code`) USING BTREE,
  CONSTRAINT `FK_party_settlement_party` FOREIGN KEY (`p_no`) REFERENCES `party` (`p_no`),
  CONSTRAINT `FK_party_settlement_party_member` FOREIGN KEY (`p_member_no`) REFERENCES `party_member` (`p_member_no`),
  CONSTRAINT `FK_party_settlement_status` FOREIGN KEY (`status_code`) REFERENCES `status_code` (`status_code`)
) COMMENT='파티정산현황';


CREATE TABLE IF NOT EXISTS `party_settlement_detail` (
  `settle_detail_no` int NOT NULL AUTO_INCREMENT COMMENT '정산상세현황고유번호',
  `p_settle_no` int NOT NULL COMMENT '파티정산고유번호',
  `point_history_no` varchar(10) NOT NULL COMMENT '포인트증감내역고유번호',
  `p_settle_price` int NOT NULL COMMENT '정산완료금액',
  `p_settle_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '정산일시',
  PRIMARY KEY (`settle_detail_no`),
  KEY `FK_party_settlement_detail_party_settlement` (`p_settle_no`),
  KEY `FK_party_settlement_detail_point_history` (`point_history_no`),
  CONSTRAINT `FK_party_settlement_detail_party_settlement` FOREIGN KEY (`p_settle_no`) REFERENCES `party_settlement` (`p_settle_no`),
  CONSTRAINT `FK_party_settlement_detail_point_history` FOREIGN KEY (`point_history_no`) REFERENCES `point_history` (`point_history_no`)
) COMMENT='파티정산상세현황';


CREATE TABLE IF NOT EXISTS `error_log` (
    `error_no` int NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '에러번호',
    `error_name` varchar(255) NOT NULL COMMENT '에러명',
    `error_code` int CHECK (error_code BETWEEN 400 AND 599) NOT NULL COMMENT '에러코드',
    `m_no` INT NULL COMMENT '회원번호',
    `error_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '에러발생시간'
); COMMENT='에러로그';



-- 2. 상수 데이터 삽입
INSERT INTO `member_level` (`m_level`, `m_level_name`) VALUES
	(1, '일반이용자'),
	(2, '관리자');


INSERT INTO `category` (`c_code`, `c_group1`, `c_group2`, `c_group3`) VALUES
	('CF0101', '식품', '신선식품', '채소'),
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


INSERT INTO `type_code` (`type_code`, `code_group`, `code_group_sub`, `code_description`) VALUES
	('T010101', '회원', '로그인', '자사몰'),
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
	('T020101', '관리자', '게시글', '공지'),
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


INSERT INTO `status_code` (`status_code`, `code_group`, `code_group_sub`, `code_description`) VALUES
	('S010101', '회원', '목록', '정상'),
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



-- 3. 트리거
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `after_alert_allow_changes_delete` AFTER DELETE ON `alert_allow_changes` FOR EACH ROW BEGIN
    DECLARE last_sms TINYINT;
    DECLARE last_email TINYINT;
    DECLARE last_marketing TINYINT;
    DECLARE last_member TINYINT;
    DECLARE last_note TINYINT;
    DECLARE last_bulletin TINYINT;
    DECLARE last_party TINYINT;

    -- 롤백을 위한 마지막 상태 조회
    SELECT allow_change_after INTO last_sms
    FROM alert_allow_changes
    WHERE alert_allow_no = OLD.alert_allow_no AND type_code = 'T010207'
    ORDER BY allow_change_date DESC
    LIMIT 1;

    SELECT allow_change_after INTO last_email
    FROM alert_allow_changes
    WHERE alert_allow_no = OLD.alert_allow_no AND type_code = 'T010208'
    ORDER BY allow_change_date DESC
    LIMIT 1;

    SELECT allow_change_after INTO last_marketing
    FROM alert_allow_changes
    WHERE alert_allow_no = OLD.alert_allow_no AND type_code = 'T010202'
    ORDER BY allow_change_date DESC
    LIMIT 1;

    SELECT allow_change_after INTO last_member
    FROM alert_allow_changes
    WHERE alert_allow_no = OLD.alert_allow_no AND type_code = 'T010203'
    ORDER BY allow_change_date DESC
    LIMIT 1;

    SELECT allow_change_after INTO last_note
    FROM alert_allow_changes
    WHERE alert_allow_no = OLD.alert_allow_no AND type_code = 'T010204'
    ORDER BY allow_change_date DESC
    LIMIT 1;

    SELECT allow_change_after INTO last_bulletin
    FROM alert_allow_changes
    WHERE alert_allow_no = OLD.alert_allow_no AND type_code = 'T010205'
    ORDER BY allow_change_date DESC
    LIMIT 1;

    SELECT allow_change_after INTO last_party
    FROM alert_allow_changes
    WHERE alert_allow_no = OLD.alert_allow_no AND type_code = 'T010206'
    ORDER BY allow_change_date DESC
    LIMIT 1;

    -- 롤백
    IF last_sms IS NOT NULL THEN
        UPDATE alert_allow
        SET allow_sms = last_sms
        WHERE alert_allow_no = OLD.alert_allow_no;
    ELSE
        UPDATE alert_allow
        SET allow_sms = 0
        WHERE alert_allow_no = OLD.alert_allow_no;
    END IF;

    IF last_email IS NOT NULL THEN
        UPDATE alert_allow
        SET allow_email = last_email
        WHERE alert_allow_no = OLD.alert_allow_no;
    ELSE
        UPDATE alert_allow
        SET allow_email = 0
        WHERE alert_allow_no = OLD.alert_allow_no;
    END IF;

    IF last_marketing IS NOT NULL THEN
        UPDATE alert_allow
        SET allow_marketing = last_marketing
        WHERE alert_allow_no = OLD.alert_allow_no;
    ELSE
        UPDATE alert_allow
        SET allow_marketing = 0
        WHERE alert_allow_no = OLD.alert_allow_no;
    END IF;

    IF last_member IS NOT NULL THEN
        UPDATE alert_allow
        SET allow_member = last_member
        WHERE alert_allow_no = OLD.alert_allow_no;
    ELSE
        UPDATE alert_allow
        SET allow_member = 0
        WHERE alert_allow_no = OLD.alert_allow_no;
    END IF;

    IF last_note IS NOT NULL THEN
        UPDATE alert_allow
        SET allow_note = last_note
        WHERE alert_allow_no = OLD.alert_allow_no;
    ELSE
        UPDATE alert_allow
        SET allow_note = 0
        WHERE alert_allow_no = OLD.alert_allow_no;
    END IF;

    IF last_bulletin IS NOT NULL THEN
        UPDATE alert_allow
        SET allow_bulletin = last_bulletin
        WHERE alert_allow_no = OLD.alert_allow_no;
    ELSE
        UPDATE alert_allow
        SET allow_bulletin = 0
        WHERE alert_allow_no = OLD.alert_allow_no;
    END IF;

    IF last_party IS NOT NULL THEN
        UPDATE alert_allow
        SET allow_party = last_party
        WHERE alert_allow_no = OLD.alert_allow_no;
    ELSE
        UPDATE alert_allow
        SET allow_party = 0
        WHERE alert_allow_no = OLD.alert_allow_no;
    END IF;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;


SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `after_alert_allow_delete` AFTER DELETE ON `alert_allow` FOR EACH ROW BEGIN
    DECLARE last_sms TINYINT DEFAULT 0;
    DECLARE last_email TINYINT DEFAULT 0;
    DECLARE last_marketing TINYINT DEFAULT 0;
    DECLARE last_member TINYINT DEFAULT 0;
    DECLARE last_note TINYINT DEFAULT 0;
    DECLARE last_bulletin TINYINT DEFAULT 0;
    DECLARE last_party TINYINT DEFAULT 0;

    -- 마지막 상태 조회
    SELECT allow_change_after INTO last_sms
    FROM alert_allow_changes
    WHERE alert_allow_no = OLD.alert_allow_no AND type_code = 'T010207'
    ORDER BY allow_change_date DESC
    LIMIT 1;

    SELECT allow_change_after INTO last_email
    FROM alert_allow_changes
    WHERE alert_allow_no = OLD.alert_allow_no AND type_code = 'T010208'
    ORDER BY allow_change_date DESC
    LIMIT 1;

    SELECT allow_change_after INTO last_marketing
    FROM alert_allow_changes
    WHERE alert_allow_no = OLD.alert_allow_no AND type_code = 'T010202'
    ORDER BY allow_change_date DESC
    LIMIT 1;

    SELECT allow_change_after INTO last_member
    FROM alert_allow_changes
    WHERE alert_allow_no = OLD.alert_allow_no AND type_code = 'T010203'
    ORDER BY allow_change_date DESC
    LIMIT 1;

    SELECT allow_change_after INTO last_note
    FROM alert_allow_changes
    WHERE alert_allow_no = OLD.alert_allow_no AND type_code = 'T010204'
    ORDER BY allow_change_date DESC
    LIMIT 1;

    SELECT allow_change_after INTO last_bulletin
    FROM alert_allow_changes
    WHERE alert_allow_no = OLD.alert_allow_no AND type_code = 'T010205'
    ORDER BY allow_change_date DESC
    LIMIT 1;

    SELECT allow_change_after INTO last_party
    FROM alert_allow_changes
    WHERE alert_allow_no = OLD.alert_allow_no AND type_code = 'T010206'
    ORDER BY allow_change_date DESC
    LIMIT 1;

    -- alert_allow 테이블에 데이터 업데이트
    UPDATE alert_allow
    SET allow_sms = COALESCE(last_sms, 0),
        allow_email = COALESCE(last_email, 0),
        allow_marketing = COALESCE(last_marketing, 0),
        allow_member = COALESCE(last_member, 0),
        allow_note = COALESCE(last_note, 0),
        allow_bulletin = COALESCE(last_bulletin, 0),
        allow_party = COALESCE(last_party, 0)
    WHERE alert_allow_no = OLD.alert_allow_no;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;


SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `after_alert_allow_update` AFTER UPDATE ON `alert_allow` FOR EACH ROW BEGIN
    DECLARE max_id INT;

    -- allow_sms 상태 변경 시
    IF OLD.allow_sms <> NEW.allow_sms THEN
        SELECT COALESCE(MAX(alert_change_no), 0) + 1 INTO max_id FROM alert_allow_changes;
        INSERT INTO alert_allow_changes (alert_change_no, alert_allow_no, type_code, allow_change_before, allow_change_after)
        VALUES (max_id, NEW.alert_allow_no, 'T010207', OLD.allow_sms, NEW.allow_sms);
    END IF;

    -- allow_email 상태 변경 시
    IF OLD.allow_email <> NEW.allow_email THEN
        SELECT COALESCE(MAX(alert_change_no), 0) + 1 INTO max_id FROM alert_allow_changes;
        INSERT INTO alert_allow_changes (alert_change_no, alert_allow_no, type_code, allow_change_before, allow_change_after)
        VALUES (max_id, NEW.alert_allow_no, 'T010208', OLD.allow_email, NEW.allow_email);
    END IF;

    -- allow_marketing 상태 변경 시
    IF OLD.allow_marketing <> NEW.allow_marketing THEN
        SELECT COALESCE(MAX(alert_change_no), 0) + 1 INTO max_id FROM alert_allow_changes;
        INSERT INTO alert_allow_changes (alert_change_no, alert_allow_no, type_code, allow_change_before, allow_change_after)
        VALUES (max_id, NEW.alert_allow_no, 'T010202', OLD.allow_marketing, NEW.allow_marketing);
    END IF;

    -- allow_member 상태 변경 시
    IF OLD.allow_member <> NEW.allow_member THEN
        SELECT COALESCE(MAX(alert_change_no), 0) + 1 INTO max_id FROM alert_allow_changes;
        INSERT INTO alert_allow_changes (alert_change_no, alert_allow_no, type_code, allow_change_before, allow_change_after)
        VALUES (max_id, NEW.alert_allow_no, 'T010203', OLD.allow_member, NEW.allow_member);
    END IF;

    -- allow_note 상태 변경 시
    IF OLD.allow_note <> NEW.allow_note THEN
        SELECT COALESCE(MAX(alert_change_no), 0) + 1 INTO max_id FROM alert_allow_changes;
        INSERT INTO alert_allow_changes (alert_change_no, alert_allow_no, type_code, allow_change_before, allow_change_after)
        VALUES (max_id, NEW.alert_allow_no, 'T010204', OLD.allow_note, NEW.allow_note);
    END IF;

    -- allow_bulletin 상태 변경 시
    IF OLD.allow_bulletin <> NEW.allow_bulletin THEN
        SELECT COALESCE(MAX(alert_change_no), 0) + 1 INTO max_id FROM alert_allow_changes;
        INSERT INTO alert_allow_changes (alert_change_no, alert_allow_no, type_code, allow_change_before, allow_change_after)
        VALUES (max_id, NEW.alert_allow_no, 'T010205', OLD.allow_bulletin, NEW.allow_bulletin);
    END IF;

    -- allow_party 상태 변경 시
    IF OLD.allow_party <> NEW.allow_party THEN
        SELECT COALESCE(MAX(alert_change_no), 0) + 1 INTO max_id FROM alert_allow_changes;
        INSERT INTO alert_allow_changes (alert_change_no, alert_allow_no, type_code, allow_change_before, allow_change_after)
        VALUES (max_id, NEW.alert_allow_no, 'T010206', OLD.allow_party, NEW.allow_party);
    END IF;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;


SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `after_member_insert` AFTER INSERT ON `member` FOR EACH ROW BEGIN
    DECLARE existing_row_count INT;

    -- 멤버 존재하는지 체크
    SELECT COUNT(*)
    INTO existing_row_count
    FROM alert_allow
    WHERE m_no = NEW.m_no;

    -- 존재하지 않을 시 알림 수신 기본값으로 세팅
    IF existing_row_count = 0 THEN
        INSERT INTO alert_allow (m_no, allow_sms, allow_email, allow_marketing, allow_member, allow_note, allow_bulletin, allow_party)
        VALUES (NEW.m_no, 0, 0, 0, 0, 0, 0, 0);
    END IF;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;



-- 4. 샘플 데이터 삽입(임시)
-- 4-1. 회원
INSERT IGNORE INTO member
	(m_no, m_level, m_id, m_pw, m_name, m_email, m_phone, m_gender, m_addr, m_birthday, m_nick, status_code)
VALUES
	('M000001', 2, 'admin', 'admin', '관리자', 'admin@example.com', '010-9876-5432', 'F', '서울시 강북구', '1985-05-05', '관리자', 'S010101'),
	('M000002', 1, 'user2', 'user2', '회원2', 'user2@gongzone.shop', '010-2345-2345', 'M', '전라북도 전주시 덕진구', '1992-02-02', '최초의회원', 'S010101'),
	('M000003', 1, 'hanokfairy', 'hanokfairy', '고윤영', 'hanokfactory@gongzone.shop', '010-1234-5678', 'M', '전라북도 전주시 덕진구', '1990-01-01', '한옥마을요정', 'S010101'),
	('M000004', 1, 'dntjrvv1', 'dntjrvv1', '전우석', 'dntjrvv1@gongzone.shop', '010-4567-4567', 'M', '전라북도 덕진구', '1999-01-01', '바나나맨', 'S010101'),
	('M000005', 1, 'ominho123', 'ominho123', '오민호', 'ominho123@naver.com', '010-2345-4235', 'M', '', NULL, '크랙', 'S010101'),
	('M000006', 1, 'rladmsquf3110', 'rladmsquf3110', '김우연', 'rladmsquf3110@naver.com', '010-3171-6838', 'F', '전', '2005-06-19', '젠키스', 'S010101'),
	('M000007', 1, 'funnymonkey1', 'funnymonkey1', '원숭이', 'funnymonkey1@gongzone.shop', '010-1111-1111', 'M', '아마존 밀림', '2024-07-19', '바나나이스', 'S010101'),
	('M000008', 1, 'crack123', 'crack123', '호민오', 'crack123@google.com', '010-5476-4573', 'M', '전라북도 덕진동', '2024-07-23', '', 'S010101');

INSERT IGNORE INTO member_point
	(m_point_no, m_no)
VALUES
	('MP000001', 'M000001'),
	('MP000002', 'M000002'),
	('MP000003', 'M000003'),
	('MP000004', 'M000004'),
	('MP000005', 'M000005'),
	('MP000006', 'M000006'),
	('MP000007', 'M000007'),
	('MP000008', 'M000008');

-- 4-2. 게시글
INSERT IGNORE INTO `board`
	(`b_no`, `m_no`, `b_title`, `b_body`, `b_date`, `b_period`, `b_view_count`, `b_wish_count`, `b_report_count`, `status_code`)
VALUES
	('B000001', 'M000002', '대량 바나나튀김 250g 12봉 튀긴 슬라이스바나나칩 업소용', '<p>바나나칩 같이 구매하실분 구합니다 맛있어요</p>', '2024-07-19 01:33:05', '2024-08-01 14:59:59', 3, 0, 0, 'S040101');

INSERT IGNORE INTO `file`
	(`file_no`, `file_original_name`, `file_new_name`, `file_path`, `file_size`, `file_date`)
VALUES
	(1, '20240719 이미지004.png', '20240719이미지0043626523243466848.png', '/api/attachement/20240719/20240719이미지0043626523243466848.png', 379038, '2024-07-19 01:34:40');

INSERT IGNORE INTO `file_relation`
	(`file_relation_no`, `file_no`, `file_usage`)
VALUES
	(1, 1, 'B000001');

INSERT IGNORE INTO `location`
	(`location_no`, `b_no`, `location_do`, `location_si`, `location_gu`, `location_dong`, `location_detail`, `location_x`, `location_y`)
VALUES
	(1, 'B000001', '전북특별자치도', '전주시', '덕진구', '덕진동1가', '1401-18 맥도날드 주차장', 35.8438038057573, 127.12310555280546);

-- 4-3. 파티
INSERT IGNORE INTO `party`
	(`p_no`, `b_no`, `c_code`, `p_url`, `p_amount`, `p_amount_remain`, `p_price`, `p_price_remain`, `p_start_date`, `p_end_date`, `status_code`)
VALUES
	('P000001', 'B000001', 'CF0403', 'https://smartstore.naver.com/fbstore/products/9810920546', 12, 8, 57880, 38584, '2024-07-19 01:33:05', NULL, 'S060101');

INSERT IGNORE INTO `party_member`
	(`p_member_no`, `p_no`, `m_no`, `pm_amount`, `pm_price`, `member_join_date`, `member_is_leader`)
VALUES
	('PM000001', 'P000001', 'M000002', 4, 19296, '2024-07-19 01:33:05', '파티장');

DELIMITER $$
-- 알람 상태 변경 시 해당 상태코드 와 함께 변경 전 후 상태 changes 테이블에 데이터 삽입
CREATE TRIGGER after_alert_allow_update
AFTER UPDATE ON alert_allow
FOR EACH ROW
BEGIN
    -- sms 수신 상태변경
    IF OLD.allow_sms <> NEW.allow_sms THEN
        INSERT INTO alert_allow_changes (alert_allow_no, type_code, allow_change_before, allow_change_after)
        VALUES (OLD.alert_allow_no, 'T010207', OLD.allow_sms, NEW.allow_sms);
    END IF;
    -- email 수신 상태변경
    IF OLD.allow_email <> NEW.allow_email THEN
        INSERT INTO alert_allow_changes (alert_allow_no, type_code, allow_change_before, allow_change_after)
        VALUES (OLD.alert_allow_no, 'T010208', OLD.allow_email, NEW.allow_email);
    END IF;
    -- 마케팅 수신 상태변경
    IF OLD.allow_marketing <> NEW.allow_marketing THEN
        INSERT INTO alert_allow_changes (alert_allow_no, type_code, allow_change_before, allow_change_after)
        VALUES (OLD.alert_allow_no, 'T010202', OLD.allow_marketing, NEW.allow_marketing);
    END IF;
    -- 유저 상호작용 알람 수신 상태변경
    IF OLD.allow_member <> NEW.allow_member THEN
        INSERT INTO alert_allow_changes (alert_allow_no, type_code, allow_change_before, allow_change_after)
        VALUES (OLD.alert_allow_no, 'T010203', OLD.allow_member, NEW.allow_member);
    END IF;
    -- 쪽지알람 수신 상태변경
    IF OLD.allow_note <> NEW.allow_note THEN
        INSERT INTO alert_allow_changes (alert_allow_no, type_code, allow_change_before, allow_change_after)
        VALUES (OLD.alert_allow_no, 'T010204', OLD.allow_note, NEW.allow_note);
    END IF;
    -- 게시판 알람 수신 상태변경
    IF OLD.allow_bulletin <> NEW.allow_bulletin THEN
        INSERT INTO alert_allow_changes (alert_allow_no, type_code, allow_change_before, allow_change_after)
        VALUES (OLD.alert_allow_no, 'T010205', OLD.allow_bulletin, NEW.allow_bulletin);
    END IF;
    -- 파티 알람 수신 상태변경
    IF OLD.allow_party <> NEW.allow_party THEN
        INSERT INTO alert_allow_changes (alert_allow_no, type_code, allow_change_before, allow_change_after)
        VALUES (OLD.alert_allow_no, 'T010206', OLD.allow_party, NEW.allow_party);
    END IF;
END$$

DELIMITER ;

DELIMITER $$

CREATE TRIGGER after_alert_allow_update
AFTER UPDATE ON alert_allow
FOR EACH ROW
BEGIN
    DECLARE max_id INT;

    -- allow_sms 상태 변경 시
    IF OLD.allow_sms <> NEW.allow_sms THEN
        SELECT MAX(alert_allow_change_no) INTO max_id FROM alert_allow_changes;
        SET max_id = COALESCE(max_id, 0) + 1;
        INSERT INTO alert_allow_changes (alert_allow_change_no, alert_allow_no, type_code, allow_change_before, allow_change_after)
        VALUES (max_id, NEW.alert_allow_no, 'T010207', OLD.allow_sms, NEW.allow_sms);
    END IF;

    -- allow_email 상태 변경 시
    IF OLD.allow_email <> NEW.allow_email THEN
        SELECT MAX(alert_allow_change_no) INTO max_id FROM alert_allow_changes;
        SET max_id = COALESCE(max_id, 0) + 1;
        INSERT INTO alert_allow_changes (alert_allow_change_no, alert_allow_no, type_code, allow_change_before, allow_change_after)
        VALUES (max_id, NEW.alert_allow_no, 'T010208', OLD.allow_email, NEW.allow_email);
    END IF;

    -- allow_marketing 상태 변경 시
    IF OLD.allow_marketing <> NEW.allow_marketing THEN
        SELECT MAX(alert_allow_change_no) INTO max_id FROM alert_allow_changes;
        SET max_id = COALESCE(max_id, 0) + 1;
        INSERT INTO alert_allow_changes (alert_allow_change_no, alert_allow_no, type_code, allow_change_before, allow_change_after)
        VALUES (max_id, NEW.alert_allow_no, 'T010202', OLD.allow_marketing, NEW.allow_marketing);
    END IF;

    -- allow_member 상태 변경 시
    IF OLD.allow_member <> NEW.allow_member THEN
        SELECT MAX(alert_allow_change_no) INTO max_id FROM alert_allow_changes;
        SET max_id = COALESCE(max_id, 0) + 1;
        INSERT INTO alert_allow_changes (alert_allow_change_no, alert_allow_no, type_code, allow_change_before, allow_change_after)
        VALUES (max_id, NEW.alert_allow_no, 'T010203', OLD.allow_member, NEW.allow_member);
    END IF;

    -- allow_note 상태 변경 시
    IF OLD.allow_note <> NEW.allow_note THEN
        SELECT MAX(alert_allow_change_no) INTO max_id FROM alert_allow_changes;
        SET max_id = COALESCE(max_id, 0) + 1;
        INSERT INTO alert_allow_changes (alert_allow_change_no, alert_allow_no, type_code, allow_change_before, allow_change_after)
        VALUES (max_id, NEW.alert_allow_no, 'T010204', OLD.allow_note, NEW.allow_note);
    END IF;

    -- allow_bulletin 상태 변경 시
    IF OLD.allow_bulletin <> NEW.allow_bulletin THEN
        SELECT MAX(alert_allow_change_no) INTO max_id FROM alert_allow_changes;
        SET max_id = COALESCE(max_id, 0) + 1;
        INSERT INTO alert_allow_changes (alert_allow_change_no, alert_allow_no, type_code, allow_change_before, allow_change_after)
        VALUES (max_id, NEW.alert_allow_no, 'T010205', OLD.allow_bulletin, NEW.allow_bulletin);
    END IF;

    -- allow_party 상태 변경 시
    IF OLD.allow_party <> NEW.allow_party THEN
        SELECT MAX(alert_allow_change_no) INTO max_id FROM alert_allow_changes;
        SET max_id = COALESCE(max_id, 0) + 1;
        INSERT INTO alert_allow_changes (alert_allow_change_no, alert_allow_no, type_code, allow_change_before, allow_change_after)
        VALUES (max_id, NEW.alert_allow_no, 'T010206', OLD.allow_party, NEW.allow_party);
    END IF;
END$$

DELIMITER ;

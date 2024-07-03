DELIMITER $$

CREATE TRIGGER after_alert_allow_changes_delete
AFTER DELETE ON alert_allow_changes
FOR EACH ROW
BEGIN
    DECLARE max_id INT;
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
    ORDER BY change_time DESC
    LIMIT 1;

    SELECT allow_change_after INTO last_email
    FROM alert_allow_changes
    WHERE alert_allow_no = OLD.alert_allow_no AND type_code = 'T010208'
    ORDER BY change_time DESC
    LIMIT 1;

    SELECT allow_change_after INTO last_marketing
    FROM alert_allow_changes
    WHERE alert_allow_no = OLD.alert_allow_no AND type_code = 'T010202'
    ORDER BY change_time DESC
    LIMIT 1;

    SELECT allow_change_after INTO last_member
    FROM alert_allow_changes
    WHERE alert_allow_no = OLD.alert_allow_no AND type_code = 'T010203'
    ORDER BY change_time DESC
    LIMIT 1;

    SELECT allow_change_after INTO last_note
    FROM alert_allow_changes
    WHERE alert_allow_no = OLD.alert_allow_no AND type_code = 'T010204'
    ORDER BY change_time DESC
    LIMIT 1;

    SELECT allow_change_after INTO last_bulletin
    FROM alert_allow_changes
    WHERE alert_allow_no = OLD.alert_allow_no AND type_code = 'T010205'
    ORDER BY change_time DESC
    LIMIT 1;

    SELECT allow_change_after INTO last_party
    FROM alert_allow_changes
    WHERE alert_allow_no = OLD.alert_allow_no AND type_code = 'T010206'
    ORDER BY change_time DESC
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

    -- AUTO_INCREMENT 값 조정
    SELECT MAX(alert_allow_change_no) INTO max_id FROM alert_allow_changes;
    IF max_id IS NOT NULL THEN
        SET max_id = max_id + 1;
        SET @sql = CONCAT('ALTER TABLE alert_allow_changes AUTO_INCREMENT = ', max_id);
        PREPARE stmt FROM @sql;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    END IF;
END$$

DELIMITER ;

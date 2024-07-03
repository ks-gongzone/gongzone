DELIMITER $$

CREATE TRIGGER after_alert_allow_delete
AFTER DELETE ON alert_allow
FOR EACH ROW
BEGIN
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
END$$

DELIMITER ;

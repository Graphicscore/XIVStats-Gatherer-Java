DELIMITER $$
DROP PROCEDURE IF EXISTS `dbplayers`.`SplitMinionsToTable` $$
CREATE
    PROCEDURE `dbplayers`.`SplitMinionsToTable`()
    BEGIN

	DECLARE r_len INTEGER;
        DECLARE r_id INTEGER;
        DECLARE r_name text;
        DECLARE i INT DEFAULT 0;
	DECLARE splitted_name VARCHAR(255);
        DECLARE occurances INT DEFAULT 0;
        DECLARE done INT DEFAULT 0;
        DECLARE cur CURSOR FOR SELECT tblplayers.id,tblplayers.minions FROM tblplayers;
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
	OPEN cur;
            read_loop: LOOP
		FETCH cur INTO r_id,r_name;
                IF done THEN
			LEAVE read_loop;
                END IF;
                SET occurances = (SELECT LENGTH(r_name) - LENGTH(REPLACE(r_name, ',', ''))+1);
		SET i = 1;
		WHILE i <= occurances DO
		  SET splitted_name = (SELECT REPLACE(SUBSTRING(SUBSTRING_INDEX(r_name, ',', i),
			LENGTH(SUBSTRING_INDEX(r_name, ',', i - 1)) + 1), ',', ''));
		  INSERT INTO character_minions VALUES (r_id, NULL, splitted_name);
		  SET i = i + 1;
		END WHILE;
            END LOOP;
        CLOSE cur;
    END$$
DELIMITER ;
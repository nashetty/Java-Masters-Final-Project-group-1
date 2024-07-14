-- DROP DATABASE energy_manager;
CREATE DATABASE energy_manager;

USE energy_manager;

CREATE TABLE energy (
	id INT AUTO_INCREMENT PRIMARY KEY,
	transactionType ENUM('generated', 'used') NOT NULL,
	energyType VARCHAR(50),
	amount_K_W decimal(5,2)
);

-- set energyType to N/A when transactionType is 'used' as it does not
-- really matter what type of energy was used
DELIMITER //
CREATE TRIGGER set_energyType_to_na BEFORE INSERT ON energy
FOR EACH ROW
BEGIN
	IF NEW.transactionType = 'used' THEN
		SET NEW.energyType = 'N/A';
	END IF;
END;
//
DELIMITER ;

INSERT INTO energy
(transactionType, energyType, amount_K_W)
VALUES
('generated', 'wind', 3.5),
('used', NULL, 1.2),
('generated', 'wind', 4.8),
('generated', 'solar', 5.0),
('used', NULL, 2.1),
('used', NULL, 1.5),
('generated', 'wind', 3.0),
('generated', 'solar', 2.7),
('generated', 'wind', 4.0),
('used', NULL, 1.8);

SELECT * FROM energy;
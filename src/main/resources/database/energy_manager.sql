-- DROP DATABASE energy_manager;
CREATE DATABASE energy_manager;

USE energy_manager;

CREATE TABLE energy (
    id INT AUTO_INCREMENT PRIMARY KEY,
    transaction_type ENUM('generated', 'used') NOT NULL,
    energy_type VARCHAR(50),
    amount_K_W decimal(5,2),
    transaction_date DATE  -- format YYYY-MM-DD
);

-- set energyType to N/A when transactionType is 'used' as it does not
-- really matter what type of energy was used
DELIMITER //
CREATE TRIGGER set_energy_type_to_na BEFORE INSERT ON energy
    FOR EACH ROW
BEGIN
    IF NEW.transaction_type = 'used' THEN
		SET NEW.energy_type = 'N/A';
END IF;
END;
//
DELIMITER ;

INSERT INTO energy
(transaction_type, energy_type, amount_K_W, transaction_date)
VALUES
-- January 2024
('generated', 'solar', 4.2, '2024-01-01'),
('generated', 'wind', 3.5, '2024-01-01'),
('used', NULL, 1.1, '2024-01-01'),
('generated', 'solar', 4.5, '2024-01-15'),
('generated', 'wind', 3.5, '2024-01-15'),
('used', NULL, 1.2, '2024-01-15'),

-- February 2024
('generated', 'solar', 5.0, '2024-02-01'),
('generated', 'wind', 4.8, '2024-02-01'),
('used', NULL, 2.1, '2024-02-01'),
('generated', 'solar', 4.9, '2024-02-15'),
('generated', 'wind', 3.8, '2024-02-15'),
('used', NULL, 1.5, '2024-02-15'),

-- March 2024
('generated', 'solar', 3.5, '2024-03-01'),
('generated', 'wind', 2.9, '2024-03-01'),
('used', NULL, 1.7, '2024-03-01'),
('generated', 'solar', 2.7, '2024-03-15'),
('generated', 'wind', 3.0, '2024-03-15'),
('used', NULL, 1.6, '2024-03-15'),

-- April 2024
('generated', 'solar', 4.0, '2024-04-01'),
('generated', 'wind', 3.2, '2024-04-01'),
('used', NULL, 2.0, '2024-04-01'),
('generated', 'solar', 3.9, '2024-04-15'),
('generated', 'wind', 3.4, '2024-04-15'),
('used', NULL, 1.7, '2024-04-15'),

-- May 2024
('generated', 'solar', 5.0, '2024-05-01'),
('generated', 'wind', 4.5, '2024-05-01'),
('used', NULL, 2.3, '2024-05-01'),
('generated', 'solar', 4.8, '2024-05-15'),
('generated', 'wind', 4.0, '2024-05-15'),
('used', NULL, 2.1, '2024-05-15'),

-- June 2024
('generated', 'solar', 6.0, '2024-06-01'),
('generated', 'wind', 5.5, '2024-06-01'),
('used', NULL, 3.0, '2024-06-01'),
('generated', 'solar', 5.8, '2024-06-15'),
('generated', 'wind', 4.0, '2024-06-15'),
('used', NULL, 2.5, '2024-06-15'),

-- July 2024
('generated', 'solar', 6.5, '2024-07-01'),
('generated', 'wind', 6.0, '2024-07-01'),
('used', NULL, 3.2, '2024-07-01'),
('generated', 'solar', 6.3, '2024-07-15'),
('generated', 'wind', 5.5, '2024-07-15'),
('used', NULL, 3.1, '2024-07-15');

SELECT * FROM energy;

-- all transactions (for all dates) filtered by transaction type
SELECT SUM(amount_K_W) AS total_kW, transaction_type FROM energy WHERE transaction_type = 'used';
SELECT SUM(amount_K_W) AS total_kW, transaction_type FROM energy WHERE transaction_type = 'generated';
SELECT SUM(amount_K_W) AS total_kW, transaction_type FROM energy GROUP BY transaction_type;

-- transactions filtered by transaction type and by month
SELECT SUM(amount_K_W) AS total_kW, transaction_type, MONTH(transaction_date) AS Month FROM energy GROUP BY transaction_type, Month;
-- GGCU Utility Management System schema, sample data, procedures, triggers and views
-- For MySQL (XAMPP)

DROP DATABASE IF EXISTS ggcu;
CREATE DATABASE ggcu;
USE ggcu;

SET FOREIGN_KEY_CHECKS=0;

-- Core lookup: utility types
CREATE TABLE utilities (
  id INT AUTO_INCREMENT PRIMARY KEY,
  code VARCHAR(32) NOT NULL UNIQUE,
  name VARCHAR(64) NOT NULL
) ENGINE=InnoDB;

-- Users & Roles (administrative users)
CREATE TABLE roles (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(64) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(64) NOT NULL UNIQUE,
  full_name VARCHAR(128),
  role_id INT,
  email VARCHAR(128),
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- Customers (household, business, gov)
CREATE TABLE customers (
  id INT AUTO_INCREMENT PRIMARY KEY,
  account_number VARCHAR(64) NOT NULL UNIQUE,
  name VARCHAR(128) NOT NULL,
  customer_type ENUM('household','business','government') DEFAULT 'household',
  contact_phone VARCHAR(32),
  contact_email VARCHAR(128),
  address TEXT,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- Meters registered to customers. A customer can have multiple meters across utilities.
CREATE TABLE meters (
  id INT AUTO_INCREMENT PRIMARY KEY,
  meter_number VARCHAR(64) NOT NULL UNIQUE,
  customer_id INT NOT NULL,
  utility_id INT NOT NULL,
  installation_date DATE,
  status ENUM('active','inactive','decommissioned') DEFAULT 'active',
  notes TEXT,
  FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE,
  FOREIGN KEY (utility_id) REFERENCES utilities(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Periodic meter readings
CREATE TABLE meter_readings (
  id INT AUTO_INCREMENT PRIMARY KEY,
  meter_id INT NOT NULL,
  reading_date DATE NOT NULL,
  reading_value DECIMAL(12,3) NOT NULL,
  recorded_by INT,
  recorded_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY (meter_id, reading_date),
  FOREIGN KEY (meter_id) REFERENCES meters(id) ON DELETE CASCADE,
  FOREIGN KEY (recorded_by) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- Tariff plans (one plan may apply to many meters/customers)
CREATE TABLE tariff_plans (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(128) NOT NULL,
  utility_id INT NOT NULL,
  effective_from DATE NOT NULL,
  effective_to DATE DEFAULT '9999-12-31',
  fixed_charge DECIMAL(12,2) DEFAULT 0,
  CONSTRAINT uq_plan UNIQUE(utility_id,name,effective_from),
  FOREIGN KEY (utility_id) REFERENCES utilities(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Tariff slabs for plans. Use max_unit = NULL for 'above' slab (infinite)
CREATE TABLE tariff_slabs (
  id INT AUTO_INCREMENT PRIMARY KEY,
  plan_id INT NOT NULL,
  min_unit DECIMAL(12,3) NOT NULL,
  max_unit DECIMAL(12,3),
  rate_per_unit DECIMAL(12,4) NOT NULL,
  notes VARCHAR(255),
  FOREIGN KEY (plan_id) REFERENCES tariff_plans(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Bills (master) and Bill items (line items per utility / meter)
CREATE TABLE bills (
  id INT AUTO_INCREMENT PRIMARY KEY,
  bill_number VARCHAR(64) NOT NULL UNIQUE,
  customer_id INT NOT NULL,
  bill_date DATE NOT NULL,
  due_date DATE NOT NULL,
  total_amount DECIMAL(12,2) DEFAULT 0,
  paid_amount DECIMAL(12,2) DEFAULT 0,
  status ENUM('issued','partial','paid','overdue') DEFAULT 'issued',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE bill_items (
  id INT AUTO_INCREMENT PRIMARY KEY,
  bill_id INT NOT NULL,
  meter_id INT NOT NULL,
  utility_id INT NOT NULL,
  consumption DECIMAL(12,3) NOT NULL,
  amount DECIMAL(12,2) NOT NULL,
  fixed_charge DECIMAL(12,2) DEFAULT 0,
  description VARCHAR(256),
  FOREIGN KEY (bill_id) REFERENCES bills(id) ON DELETE CASCADE,
  FOREIGN KEY (meter_id) REFERENCES meters(id) ON DELETE CASCADE,
  FOREIGN KEY (utility_id) REFERENCES utilities(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Payments
CREATE TABLE payments (
  id INT AUTO_INCREMENT PRIMARY KEY,
  bill_id INT NOT NULL,
  payment_date DATE NOT NULL,
  amount DECIMAL(12,2) NOT NULL,
  method ENUM('cash','card','online','cheque') DEFAULT 'cash',
  reference VARCHAR(128),
  recorded_by INT,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (bill_id) REFERENCES bills(id) ON DELETE CASCADE,
  FOREIGN KEY (recorded_by) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- Simple audit/log table for events
CREATE TABLE event_logs (
  id INT AUTO_INCREMENT PRIMARY KEY,
  event_type VARCHAR(64),
  ref_id INT,
  details TEXT,
  created_by INT,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB;

SET FOREIGN_KEY_CHECKS=1;

-- Sample data
INSERT INTO utilities (code,name) VALUES
('ELEC','Electricity'),
('WATR','Water'),
('GAS','Gas');

INSERT INTO roles (name) VALUES ('admin'),('meter_reader'),('cashier'),('manager');

INSERT INTO users (username,full_name,role_id,email) VALUES
('alice','Alice Admin',1,'alice@example.org'),
('bob','Bob Reader',2,'bob@example.org'),
('carol','Carol Cashier',3,'carol@example.org'),
('dave','Dave Manager',4,'dave@example.org');

INSERT INTO customers (account_number,name,customer_type,contact_phone,address) VALUES
('ACC-0001','Green Meadows - House 1','household','+123456789','Block A, Green Meadows'),
('ACC-0002','Green Meadows - House 2','household','+123456788','Block B, Green Meadows'),
('ACC-BIZ-01','Tech Park Office 1','business','+123456700','Unit 1, Tech Park');

-- Create meter entries
-- Locate utility ids
SET @elec_id = (SELECT id FROM utilities WHERE code='ELEC');
SET @watr_id = (SELECT id FROM utilities WHERE code='WATR');
SET @gas_id = (SELECT id FROM utilities WHERE code='GAS');

INSERT INTO meters (meter_number,customer_id,utility_id,installation_date) VALUES
('MTR-E-0001',1,@elec_id,'2020-01-01'),
('MTR-W-0001',1,@watr_id,'2020-01-01'),
('MTR-E-0002',2,@elec_id,'2021-05-01'),
('MTR-E-0003',3,@elec_id,'2019-03-15');

-- Tariff plans and slabs
INSERT INTO tariff_plans (name,utility_id,effective_from,fixed_charge) VALUES
('Residential Green Tariff',@elec_id,'2020-01-01',50.00),
('Residential Water Standard',@watr_id,'2020-01-01',20.00),
('Commercial Electricity',@elec_id,'2020-01-01',100.00);

SET @res_elec_plan = (SELECT id FROM tariff_plans WHERE name='Residential Green Tariff');
SET @res_watr_plan = (SELECT id FROM tariff_plans WHERE name='Residential Water Standard');
SET @biz_elec_plan = (SELECT id FROM tariff_plans WHERE name='Commercial Electricity');

-- Slabs for residential electricity (kWh)
INSERT INTO tariff_slabs (plan_id,min_unit,max_unit,rate_per_unit) VALUES
(@res_elec_plan,0,50,0.05),
(@res_elec_plan,50,150,0.10),
(@res_elec_plan,150,9999999,0.20);

-- Slabs for water (m3)
INSERT INTO tariff_slabs (plan_id,min_unit,max_unit,rate_per_unit) VALUES
(@res_watr_plan,0,10,0.50),
(@res_watr_plan,10,30,1.00),
(@res_watr_plan,30,9999999,2.00);

-- Commercial electricity slabs
INSERT INTO tariff_slabs (plan_id,min_unit,max_unit,rate_per_unit) VALUES
(@biz_elec_plan,0,100,0.12),
(@biz_elec_plan,100,500,0.18),
(@biz_elec_plan,500,9999999,0.25);

-- Sample meter readings: for simplicity monthly snapshots
INSERT INTO meter_readings (meter_id,reading_date,reading_value,recorded_by) VALUES
((SELECT id FROM meters WHERE meter_number='MTR-E-0001'),'2025-10-01',1000.000,2),
((SELECT id FROM meters WHERE meter_number='MTR-E-0001'),'2025-11-01',1080.000,2),
((SELECT id FROM meters WHERE meter_number='MTR-E-0002'),'2025-10-01',500.000,2),
((SELECT id FROM meters WHERE meter_number='MTR-E-0002'),'2025-11-01',580.000,2),
((SELECT id FROM meters WHERE meter_number='MTR-E-0003'),'2025-10-01',20000.000,2),
((SELECT id FROM meters WHERE meter_number='MTR-E-0003'),'2025-11-01',20150.000,2),
((SELECT id FROM meters WHERE meter_number='MTR-W-0001'),'2025-10-01',120.000,2),
((SELECT id FROM meters WHERE meter_number='MTR-W-0001'),'2025-11-01',128.000,2);

-- UDF: calculate charge given consumption and plan_id
DELIMITER $$
CREATE FUNCTION calculate_charge(p_consumption DECIMAL(12,3), p_plan_id INT)
RETURNS DECIMAL(12,2)
DETERMINISTIC
BEGIN
  DECLARE v_total DECIMAL(12,2) DEFAULT 0;
  SELECT COALESCE(SUM(GREATEST(LEAST(COALESCE(max_unit, p_consumption), p_consumption) - min_unit, 0) * rate_per_unit),0)
  INTO v_total
  FROM tariff_slabs
  WHERE plan_id = p_plan_id;

  RETURN ROUND(v_total,2);
END$$
DELIMITER ;

-- Stored procedure: generate bills for a billing period (end_date is the reading date to use)
DELIMITER $$
CREATE PROCEDURE sp_generate_bills(p_period_start DATE, p_period_end DATE)
BEGIN
  DECLARE done INT DEFAULT 0;
  DECLARE v_meter INT;
  DECLARE v_customer INT;
  DECLARE v_utility INT;
  DECLARE v_prev_read DECIMAL(12,3);
  DECLARE v_curr_read DECIMAL(12,3);
  DECLARE v_consumption DECIMAL(12,3);
  DECLARE v_amount DECIMAL(12,2);
  DECLARE v_fixed DECIMAL(12,2);
  DECLARE v_plan INT;
  DECLARE cur CURSOR FOR SELECT m.id, m.customer_id, m.utility_id FROM meters m WHERE m.status='active';
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

  OPEN cur;
  read_loop: LOOP
    FETCH cur INTO v_meter, v_customer, v_utility;
    IF done THEN
      LEAVE read_loop;
    END IF;

    -- get the latest reading at or before p_period_end
    SELECT reading_value INTO v_curr_read FROM meter_readings
    WHERE meter_id = v_meter AND reading_date = p_period_end LIMIT 1;

    -- get the previous reading before period_start (or earlier reading)
    SELECT reading_value INTO v_prev_read FROM meter_readings
    WHERE meter_id = v_meter AND reading_date < p_period_start
    ORDER BY reading_date DESC LIMIT 1;

    IF v_curr_read IS NULL OR v_prev_read IS NULL THEN
      -- if we don't have both readings, skip billing for this meter
      ITERATE read_loop;
    END IF;

    SET v_consumption = v_curr_read - v_prev_read;
    IF v_consumption < 0 THEN
      SET v_consumption = 0; -- guard
    END IF;

    -- determine applicable tariff plan: pick plan by utility and effective date
    SELECT id, fixed_charge INTO v_plan, v_fixed FROM tariff_plans
    WHERE utility_id = v_utility AND effective_from <= p_period_end AND effective_to >= p_period_end
    ORDER BY effective_from DESC LIMIT 1;

    IF v_plan IS NULL THEN
      SET v_amount = 0;
    ELSE
      SET v_amount = calculate_charge(v_consumption, v_plan);
    END IF;

    -- Create or append to bill for the customer for this period: Bill number = DATE + customer
    SET @bill_no = CONCAT(DATE_FORMAT(p_period_end,'%Y%m'),'-',LPAD(v_customer,6,'0'));

    -- Insert or update bill master
    INSERT INTO bills (bill_number,customer_id,bill_date,due_date,total_amount,paid_amount,status)
    VALUES (@bill_no, v_customer, p_period_end, DATE_ADD(p_period_end, INTERVAL 21 DAY), v_amount + IFNULL(v_fixed,0),0,'issued')
    ON DUPLICATE KEY UPDATE total_amount = VALUES(total_amount), status='issued';

    SET @bill_id = (SELECT id FROM bills WHERE bill_number=@bill_no LIMIT 1);

    -- Insert bill item (one per meter)
    INSERT INTO bill_items (bill_id,meter_id,utility_id,consumption,amount,fixed_charge,description)
    VALUES (@bill_id, v_meter, v_utility, v_consumption, v_amount, IFNULL(v_fixed,0), CONCAT('Auto bill for meter ', v_meter, ' period ', p_period_start, ' to ', p_period_end));

    -- log event
    INSERT INTO event_logs (event_type,ref_id,details,created_by) VALUES ('bill_generated', @bill_id, CONCAT('Meter=',v_meter,',cons=',v_consumption,',amt=',v_amount), NULL);

  END LOOP;
  CLOSE cur;
END$$
DELIMITER ;

-- Trigger: after payment insert, update bill paid_amount and status
DELIMITER $$
CREATE TRIGGER trg_after_payment_insert
AFTER INSERT ON payments
FOR EACH ROW
BEGIN
  UPDATE bills
  SET paid_amount = paid_amount + NEW.amount,
      status = CASE
        WHEN paid_amount + NEW.amount >= total_amount THEN 'paid'
        WHEN paid_amount + NEW.amount > 0 THEN 'partial'
        ELSE status
      END
  WHERE id = NEW.bill_id;

  INSERT INTO event_logs (event_type,ref_id,details,created_by) VALUES ('payment_recorded', NEW.bill_id, CONCAT('Payment ', NEW.amount, ' via ', NEW.method), NEW.recorded_by);
END$$
DELIMITER ;

-- Views for reports
CREATE VIEW vw_outstanding_balances AS
SELECT c.id AS customer_id, c.account_number, c.name, SUM(b.total_amount - b.paid_amount) AS outstanding
FROM customers c
JOIN bills b ON b.customer_id = c.id
WHERE b.total_amount > b.paid_amount
GROUP BY c.id;

CREATE VIEW vw_revenue_by_utility_month AS
SELECT ui.name AS utility, DATE_FORMAT(p.payment_date,'%Y-%m') AS yyyy_mm, SUM(p.amount) AS revenue
FROM payments p
JOIN bill_items bi ON bi.bill_id = p.bill_id
JOIN utilities ui ON ui.id = bi.utility_id
GROUP BY ui.id, yyyy_mm;

CREATE VIEW vw_top_consumers AS
SELECT c.id AS customer_id, c.name, SUM(bi.consumption * bi.amount/NULLIF(bi.consumption,0)) AS approx_spend, SUM(bi.consumption) AS total_consumption
FROM customers c
JOIN bills b ON b.customer_id = c.id
JOIN bill_items bi ON bi.bill_id = b.id
GROUP BY c.id ORDER BY approx_spend DESC LIMIT 50;

CREATE VIEW vw_defaulters AS
SELECT c.id AS customer_id, c.name, SUM(b.total_amount - b.paid_amount) AS outstanding, COUNT(b.id) AS unpaid_invoices
FROM customers c
JOIN bills b ON b.customer_id = c.id
WHERE b.total_amount > b.paid_amount AND b.due_date < CURDATE()
GROUP BY c.id HAVING outstanding > 0 ORDER BY outstanding DESC;

-- Stored procedure to get revenue by utility between two dates (payments)
DELIMITER $$
CREATE PROCEDURE sp_revenue_between_dates(p_start DATE, p_end DATE)
BEGIN
  SELECT ui.name AS utility, COALESCE(SUM(p.amount),0) AS revenue
  FROM utilities ui
  LEFT JOIN bill_items bi ON bi.utility_id = ui.id
  LEFT JOIN payments p ON p.bill_id = bi.bill_id
    AND (p.payment_date BETWEEN p_start AND p_end)
  GROUP BY ui.id
  ORDER BY revenue DESC;
END$$
DELIMITER ;

-- Example usage:
-- CALL sp_revenue_between_dates('2025-10-01','2025-11-30');

-- Example: generate bills for Oct to Nov 2025 period (generate using readings at 2025-11-01, previous reading < 2025-10-01)
-- Note: adjust dates to match real reading dates in meter_readings table.

-- To generate bills, run:
-- CALL sp_generate_bills('2025-10-01','2025-11-01');

-- Example: record a payment
-- INSERT INTO payments (bill_id,payment_date,amount,method,recorded_by) VALUES (1, '2025-11-10', 100.00, 'cash', 3);

-- End of script

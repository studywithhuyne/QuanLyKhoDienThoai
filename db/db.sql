DROP DATABASE IF EXISTS qlkh;
CREATE DATABASE qlkh CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE qlkh;

-- =======================================================
-- 1. CẤU TRÚC BẢNG (STRUCTURE)
-- =======================================================

-- 1. accounts
CREATE TABLE accounts (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(32) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    role        ENUM('admin', 'staff') NOT NULL DEFAULT 'staff',
    fullname    VARCHAR(32),
    last_login  DATETIME,
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 2. attributes
CREATE TABLE attributes (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(50) NOT NULL UNIQUE
);

-- 3. brands
CREATE TABLE brands (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(50) NOT NULL UNIQUE
);

-- 4. categories
CREATE TABLE categories (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(50) NOT NULL UNIQUE
);

-- 5. suppliers
CREATE TABLE suppliers (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(50) NOT NULL UNIQUE
);

-- 6. attribute_options
CREATE TABLE attribute_options (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    attribute_id    INT NOT NULL,
    value           VARCHAR(50) NOT NULL,
    FOREIGN KEY (attribute_id) REFERENCES attributes(id) ON DELETE CASCADE
);

-- 7. import_receipts
CREATE TABLE import_receipts (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    supplier_id     INT NOT NULL,
    staff_id        INT NOT NULL,
    total_amount    DOUBLE NOT NULL DEFAULT 0,
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (supplier_id) REFERENCES suppliers(id),
    FOREIGN KEY (staff_id)    REFERENCES accounts(id)
);

-- 8. invoices
CREATE TABLE invoices (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    staff_id        INT NOT NULL,
    total_amount    DOUBLE NOT NULL DEFAULT 0,
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (staff_id) REFERENCES accounts(id)
);

-- 9. products
CREATE TABLE products (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    brand_id        INT NOT NULL,
    category_id     INT NOT NULL,
    name            VARCHAR(255) NOT NULL UNIQUE,
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (brand_id)    REFERENCES brands(id),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- 10. skus
CREATE TABLE skus (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    product_id  INT NOT NULL,
    code        VARCHAR(25) NOT NULL UNIQUE,
    price       DOUBLE NOT NULL DEFAULT 0,
    stock       INT DEFAULT 0,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

-- 11. attribute_option_sku
CREATE TABLE attribute_option_sku (
    sku_id                  INT NOT NULL,
    attribute_option_id     INT NOT NULL,
    PRIMARY KEY (sku_id, attribute_option_id),
    FOREIGN KEY (sku_id)              REFERENCES skus(id) ON DELETE CASCADE,
    FOREIGN KEY (attribute_option_id) REFERENCES attribute_options(id) ON DELETE CASCADE
);

-- 12. import_details
CREATE TABLE import_details (
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    import_receipt_id   INT NOT NULL,
    product_id          INT NOT NULL,
    sku_id              INT NOT NULL,
    quantity            INT NOT NULL CHECK (quantity > 0),
    FOREIGN KEY (import_receipt_id) REFERENCES import_receipts(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id)        REFERENCES products(id),
    FOREIGN KEY (sku_id)            REFERENCES skus(id)
);

-- 13. phone_imeis
CREATE TABLE phone_imeis (
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    sku_id              INT NOT NULL,
    import_receipt_id   INT NOT NULL,
    imei                VARCHAR(50) NOT NULL UNIQUE,
    status              ENUM('available', 'sold', 'warranty', 'defective') NOT NULL DEFAULT 'available',
    created_at          DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sku_id)            REFERENCES skus(id),
    FOREIGN KEY (import_receipt_id) REFERENCES import_receipts(id)
);

-- 14. invoice_details
CREATE TABLE invoice_details (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    invoice_id  INT NOT NULL,
    sku_id      INT NOT NULL,
    quantity    INT NOT NULL DEFAULT 1,
    imei_id     INT DEFAULT NULL,
    FOREIGN KEY (invoice_id) REFERENCES invoices(id) ON DELETE CASCADE,
    FOREIGN KEY (sku_id)     REFERENCES skus(id),
    FOREIGN KEY (imei_id)    REFERENCES phone_imeis(id)
);


-- =======================================================
-- 2. DỮ LIỆU MẪU (DUMMY DATA)
-- =======================================================

-- Categories
INSERT INTO categories (id, name) VALUES
(1, 'Điện thoại'),
(2, 'Cáp sạc'),
(3, 'Cường lực'),
(4, 'Sạc dự phòng'),
(5, 'Củ sạc'),
(6, 'Loa');

-- Brands
INSERT INTO brands (name) VALUES
('Apple'), ('Samsung'), ('Xiaomi'), ('Oppo'),
('Anker'), ('Baseus'), ('Belkin'), ('Sony'), ('Ugreen');

-- Suppliers
INSERT INTO suppliers (name) VALUES
('FPT Synnex'), ('Viettel Store'), ('CellphoneS B2B'),
('Anker Vietnam'), ('Baseus Official'), ('Ugreen Vietnam');

-- Accounts
INSERT INTO accounts (username, password, fullname, role) VALUES
('admin', '1', 'Quản lý', 'admin'),
('jerry', '1', 'Jerry',   'staff');


-- =======================================================
-- 3. ATTRIBUTE & OPTIONS
-- =======================================================

INSERT INTO attributes (id, name) VALUES
(1, 'ram'), (2, 'rom'), (3, 'color'), (4, 'length'),
(5, 'wattage'), (6, 'power_bank'), (7, 'compatibility');

INSERT INTO attribute_options (id, attribute_id, value) VALUES
-- RAM
(1, 1, '8GB'), (2, 1, '12GB'), (3, 1, '16GB'), (4, 1, '24GB'),
-- ROM
(5, 2, '128GB'), (6, 2, '256GB'), (7, 2, '512GB'), (8, 2, '1TB'),
-- COLOR
(9, 3, 'Natural Titanium'), (10, 3, 'Phantom Black'), (11, 3, 'Ceramic White'),
(12, 3, 'Deep Purple'), (13, 3, 'Graphite'), (14, 3, 'Gold'),
-- LENGTH
(15, 4, '1m'), (16, 4, '2m'), (17, 4, '3m'),
-- WATTAGE
(18, 5, '30W'), (19, 5, '45W'), (20, 5, '65W'), (21, 5, '100W'), (22, 5, '140W'),
-- POWER BANK CAPACITY
(23, 6, '10.000mAh'), (24, 6, '20.000mAh'), (25, 6, '24.000mAh'),
-- COMPATIBILITY
(30, 7, 'Model: iPhone 17 Pro Max'),
(31, 7, 'Model: Samsung S26 Ultra'),
(32, 7, 'Port: USB-C (Type-C)'),
(33, 7, 'Port: Lightning'),
(34, 7, 'Tech: MagSafe');


-- =======================================================
-- 4. PRODUCTS & SKUS
-- =======================================================

INSERT INTO products (id, brand_id, category_id, name) VALUES
-- Smartphones
(1, 1, 1, 'iPhone 17 Pro Max'),
(2, 2, 1, 'Samsung Galaxy S26 Ultra'),
(3, 3, 1, 'Xiaomi 15 Ultra'),
-- Cables
(4, 5, 2, 'Anker PowerLine III Flow USB-C'),
(5, 6, 2, 'Baseus Crystal Shine Cable'),
-- Screen Protectors
(6, 7, 3, 'Belkin UltraGlass 3 for iPhone'),
-- Power Banks
(7, 5, 4, 'Anker 737 PowerCore 24K'),
(8, 6, 4, 'Baseus Blade 2 Ultra Slim'),
-- Adapters
(9, 9, 5, 'Ugreen Nexode 140W GaN'),
(10, 5, 5, 'Anker Nano IV 30W');

INSERT INTO skus (id, product_id, code, price, stock) VALUES
-- iPhone 17 PM
(1, 1, 'IP17PM-1TB-NAT',  52990000, 5),   -- Natural Titanium
(2, 1, 'IP17PM-512-BLK',  46990000, 8),   -- Phantom Black
-- Samsung S26 Ultra
(3, 2, 'SS-S26U-1TB-BLK', 44990000, 5),
(4, 2, 'SS-S26U-512-WHT', 38990000, 10),
-- Cables
(5, 4, 'ANK-FLOW-1M',     290000,   50),  -- Anker Flow 1m
(6, 5, 'BAS-CRY-2M',      150000,   60),  -- Baseus 2m
-- Screen Protector
(7, 6, 'BEL-GLS-IP17',    990000,   40),  -- Belkin Kính
-- Power Bank
(8, 7, 'ANK-737-24K',     3500000,  15),  -- Anker 737
(9, 8, 'BAS-BLD-10K',     1200000,  20),  -- Baseus Blade
-- Adapter
(10, 9, 'UGR-140W',       1890000,  30);  -- Ugreen 140W


-- =======================================================
-- 5. MAPPING
-- =======================================================

INSERT INTO attribute_option_sku (sku_id, attribute_option_id) VALUES
-- SKU 1 (iPhone 17 PM Natural): ram 12gb(2), rom 1tb(8), color natural(9)
(1, 2), (1, 8), (1, 9),
-- SKU 2 (iPhone 17 PM Black): ram 12gb(2), rom 512gb(7), color black(10)
(2, 2), (2, 7), (2, 10),
-- SKU 3 (S26U Black): ram 16gb(3), rom 1tb(8), color black(10)
(3, 3), (3, 8), (3, 10),
-- SKU 5 (Cáp Anker 1m): length 1m(15), port usb-c(32), wattage 100w(21)
(5, 15), (5, 32), (5, 21),
-- SKU 7 (Kính Belkin): model iphone 17 pm(30)
(7, 30),
-- SKU 8 (Anker 737): cap 24k(25), wattage 140w(22), port usb-c(32)
(8, 25), (8, 22), (8, 32),
-- SKU 10 (Ugreen 140W): wattage 140w(22), port usb-c(32)
(10, 22), (10, 32);


-- =======================================================
-- 6. GIAO DỊCH MẪU
-- =======================================================

-- Nhập kho
INSERT INTO import_receipts (id, supplier_id, staff_id, total_amount, created_at) VALUES
(1, 1, 2, 1500000000, '2026-01-02 08:00:00');

INSERT INTO import_details (import_receipt_id, product_id, sku_id, quantity) VALUES
(1, 1, 1, 5),
(1, 1, 2, 8);

-- Tạo IMEI
INSERT INTO phone_imeis (sku_id, import_receipt_id, imei, status) VALUES
(1, 1, '999988887777001', 'available'),
(1, 1, '999988887777002', 'sold');

-- Xuất kho (Bán hàng)
INSERT INTO invoices (id, staff_id, total_amount, created_at) VALUES
(1, 2, 56490000, '2026-01-05 10:30:00');

-- Bán 1 iPhone (kèm IMEI) + 1 Sạc Anker (Không IMEI)
INSERT INTO invoice_details (invoice_id, sku_id, quantity, imei_id) VALUES
(1, 1, 1, 2),    -- iPhone
(1, 8, 1, NULL); -- Sạc dự phòng
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
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(50) NOT NULL UNIQUE,
    is_deleted  BOOLEAN NOT NULL DEFAULT FALSE
);

-- 4. categories
CREATE TABLE categories (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(50) NOT NULL UNIQUE,
    is_deleted  BOOLEAN NOT NULL DEFAULT FALSE
);

-- 5. suppliers
CREATE TABLE suppliers (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(50) NOT NULL UNIQUE,
    phone   VARCHAR(20),
    email   VARCHAR(100),
    address VARCHAR(255)
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
    is_deleted      BOOLEAN NOT NULL DEFAULT FALSE,
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

-- 15. category_attribute
CREATE TABLE category_attribute (
    category_id     INT NOT NULL,
    attribute_id    INT NOT NULL,
    PRIMARY KEY (category_id, attribute_id),
    FOREIGN KEY (category_id)  REFERENCES categories(id) ON DELETE CASCADE,
    FOREIGN KEY (attribute_id) REFERENCES attributes(id) ON DELETE CASCADE
);

-- 16. logs
CREATE TABLE logs (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    user_id     INT NOT NULL,
    action      VARCHAR(50) NOT NULL,
    details     VARCHAR(500),
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES accounts(id)
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
('Anker'), ('Baseus'), ('Belkin'), ('Sony'), ('Ugreen'),
('JBL'), ('Realme'), ('Vivo');

-- Suppliers
INSERT INTO suppliers (name, phone, email, address) VALUES
('FPT Synnex', '1900 6600', 'sales@fptdistribution.com.vn', '52-54 Lê Đại Hành, Q.11, TP.HCM'),
('Viettel Store', '1800 8123', 'cskh@viettelstore.vn', '285 Cách Mạng Tháng 8, Q.10, TP.HCM'),
('CellphoneS B2B', '1800 2097', 'b2b@cellphones.com.vn', '315 Nguyễn Văn Trỗi, Q.Tân Bình, TP.HCM'),
('Anker Vietnam', '028 3975 8888', 'support@anker.vn', '141 Điện Biên Phủ, Q.Bình Thạnh, TP.HCM'),
('Baseus Official', '028 3636 8888', 'info@baseus.vn', '78 Lê Lai, Q.1, TP.HCM'),
('Ugreen Vietnam', '028 3822 8899', 'sales@ugreen.vn', '100 Nguyễn Thị Minh Khai, Q.3, TP.HCM'),
('Thế Giới Di Động', '1800 1060', 'cskh@thegioididong.com', '128 Trần Quang Khải, Q.1, TP.HCM'),
('Điện Máy Xanh', '1800 1061', 'hotro@dienmayxanh.com', '456 Cách Mạng Tháng 8, Q.3, TP.HCM');

-- Accounts
INSERT INTO accounts (username, password, fullname, role) VALUES
('admin', '1', 'Quản lý', 'admin'),
('jerry', '1', 'Jerry', 'staff'),
('hh', '1', 'Hòa Hộp', 'staff'),
('gh', '1', 'Gia Huy', 'staff');


-- =======================================================
-- 3. ATTRIBUTE & OPTIONS
-- =======================================================

INSERT INTO attributes (id, name) VALUES
(1, 'ram'), (2, 'rom'), (3, 'color'), (4, 'length'),
(5, 'wattage'), (6, 'power_bank'), (7, 'compatibility');

-- Liên kết Category - Attribute
INSERT INTO category_attribute (category_id, attribute_id) VALUES
-- Điện thoại (1): ram, rom, color
(1, 1), (1, 2), (1, 3),
-- Cáp sạc (2): length, compatibility
(2, 4), (2, 7),
-- Cường lực (3): compatibility
(3, 7),
-- Sạc dự phòng (4): power_bank, wattage, compatibility
(4, 5), (4, 6), (4, 7),
-- Củ sạc (5): wattage, compatibility
(5, 5), (5, 7),
-- Loa (6): color, wattage
(6, 3), (6, 5);

INSERT INTO attribute_options (id, attribute_id, value) VALUES
-- RAM
(1, 1, '8GB'), (2, 1, '12GB'), (3, 1, '16GB'), (4, 1, '24GB'),
-- ROM
(5, 2, '128GB'), (6, 2, '256GB'), (7, 2, '512GB'), (8, 2, '1TB'),
-- COLOR
(9, 3, 'Natural Titanium'), (10, 3, 'Phantom Black'), (11, 3, 'Ceramic White'),
(12, 3, 'Deep Purple'), (13, 3, 'Graphite'), (14, 3, 'Gold'),
(35, 3, 'Blue'), (36, 3, 'Green'), (37, 3, 'Red'), (38, 3, 'Silver'),
-- LENGTH
(15, 4, '1m'), (16, 4, '2m'), (17, 4, '3m'),
-- WATTAGE
(18, 5, '30W'), (19, 5, '45W'), (20, 5, '65W'), (21, 5, '100W'), (22, 5, '140W'),
(39, 5, '20W'), (40, 5, '15W'), (41, 5, '5W'),
-- POWER BANK CAPACITY
(23, 6, '10.000mAh'), (24, 6, '20.000mAh'), (25, 6, '24.000mAh'),
(42, 6, '5.000mAh'), (43, 6, '30.000mAh'),
-- COMPATIBILITY
(30, 7, 'Model: iPhone 17 Pro Max'),
(31, 7, 'Model: Samsung S26 Ultra'),
(32, 7, 'Port: USB-C (Type-C)'),
(33, 7, 'Port: Lightning'),
(34, 7, 'Tech: MagSafe'),
(44, 7, 'Model: Xiaomi 15 Ultra'),
(45, 7, 'Universal');


-- =======================================================
-- 4. PRODUCTS & SKUS
-- =======================================================

INSERT INTO products (id, brand_id, category_id, name) VALUES
-- Smartphones (category 1)
(1, 1, 1, 'iPhone 17 Pro Max'),
(2, 2, 1, 'Samsung Galaxy S26 Ultra'),
(3, 3, 1, 'Xiaomi 15 Ultra'),
(11, 1, 1, 'iPhone 17 Pro'),
(12, 2, 1, 'Samsung Galaxy S26'),
(13, 4, 1, 'Oppo Find X8 Pro'),
-- Cables (category 2)
(4, 5, 2, 'Anker PowerLine III Flow USB-C'),
(5, 6, 2, 'Baseus Crystal Shine Cable'),
(14, 9, 2, 'Ugreen USB-C to Lightning Cable'),
-- Screen Protectors (category 3)
(6, 7, 3, 'Belkin UltraGlass 3 for iPhone'),
(15, 6, 3, 'Baseus Tempered Glass Samsung'),
-- Power Banks (category 4)
(7, 5, 4, 'Anker 737 PowerCore 24K'),
(8, 6, 4, 'Baseus Blade 2 Ultra Slim'),
(16, 3, 4, 'Xiaomi Power Bank 3 Ultra'),
-- Adapters (category 5)
(9, 9, 5, 'Ugreen Nexode 140W GaN'),
(10, 5, 5, 'Anker Nano IV 30W'),
(17, 1, 5, 'Apple 20W USB-C Power Adapter'),
(18, 2, 5, 'Samsung 45W Super Fast Charger'),
-- Speakers (category 6)
(19, 10, 6, 'JBL Flip 6'),
(20, 8, 6, 'Sony SRS-XB13');

INSERT INTO skus (id, product_id, code, price, stock) VALUES
-- iPhone 17 Pro Max (product 1)
(1, 1, 'IP17PM-1TB-NAT',  52990000, 5),   -- Natural Titanium 1TB
(2, 1, 'IP17PM-512-BLK',  46990000, 8),   -- Phantom Black 512GB
(21, 1, 'IP17PM-256-WHT', 42990000, 12),  -- Ceramic White 256GB
-- Samsung Galaxy S26 Ultra (product 2)
(3, 2, 'SS-S26U-1TB-BLK', 44990000, 5),   -- Black 1TB
(4, 2, 'SS-S26U-512-WHT', 38990000, 10),  -- White 512GB
(22, 2, 'SS-S26U-256-GRN', 34990000, 15), -- Green 256GB
-- Xiaomi 15 Ultra (product 3)
(23, 3, 'XM-15U-512-BLK', 29990000, 8),   -- Black 512GB
(24, 3, 'XM-15U-256-WHT', 25990000, 10),  -- White 256GB
-- iPhone 17 Pro (product 11)
(25, 11, 'IP17P-256-NAT', 35990000, 10),  -- Natural 256GB
(26, 11, 'IP17P-512-BLK', 39990000, 7),   -- Black 512GB
-- Samsung Galaxy S26 (product 12)
(27, 12, 'SS-S26-256-BLU', 24990000, 20), -- Blue 256GB
(28, 12, 'SS-S26-128-BLK', 21990000, 15), -- Black 128GB
-- Oppo Find X8 Pro (product 13)
(29, 13, 'OPPO-FX8P-512', 28990000, 6),   -- 512GB
-- Cables (products 4, 5, 14)
(5, 4, 'ANK-FLOW-1M',     290000,   50),  -- Anker Flow 1m
(30, 4, 'ANK-FLOW-2M',    390000,   40),  -- Anker Flow 2m
(6, 5, 'BAS-CRY-2M',      150000,   60),  -- Baseus 2m
(31, 5, 'BAS-CRY-1M',     120000,   80),  -- Baseus 1m
(32, 14, 'UGR-CL-1M',     250000,   45),  -- Ugreen USB-C to Lightning 1m
-- Screen Protectors (products 6, 15)
(7, 6, 'BEL-GLS-IP17',    990000,   40),  -- Belkin iPhone
(33, 15, 'BAS-GLS-SS26',  590000,   50),  -- Baseus Samsung
-- Power Banks (products 7, 8, 16)
(8, 7, 'ANK-737-24K',     3500000,  15),  -- Anker 737 24K
(9, 8, 'BAS-BLD-10K',     1200000,  20),  -- Baseus Blade 10K
(34, 8, 'BAS-BLD-20K',    1800000,  18),  -- Baseus Blade 20K
(35, 16, 'XM-PB3-20K',    890000,   30),  -- Xiaomi 20K
-- Adapters (products 9, 10, 17, 18)
(10, 9, 'UGR-140W',       1890000,  30),  -- Ugreen 140W
(36, 10, 'ANK-NANO-30W',  590000,   50),  -- Anker Nano 30W
(37, 17, 'APL-20W',       590000,   40),  -- Apple 20W
(38, 18, 'SS-45W',        890000,   35),  -- Samsung 45W
-- Speakers (products 19, 20)
(39, 19, 'JBL-FLIP6-BLU', 2990000,  12),  -- JBL Flip 6 Blue
(40, 19, 'JBL-FLIP6-RED', 2990000,  10),  -- JBL Flip 6 Red
(41, 20, 'SONY-XB13-BLK', 1290000,  25),  -- Sony XB13 Black
(42, 20, 'SONY-XB13-BLU', 1290000,  20);  -- Sony XB13 Blue


-- =======================================================
-- 5. MAPPING (SKU - ATTRIBUTE OPTIONS)
-- =======================================================

INSERT INTO attribute_option_sku (sku_id, attribute_option_id) VALUES
-- SKU 1 (iPhone 17 PM Natural 1TB): ram 12gb(2), rom 1tb(8), color natural(9)
(1, 2), (1, 8), (1, 9),
-- SKU 2 (iPhone 17 PM Black 512GB): ram 12gb(2), rom 512gb(7), color black(10)
(2, 2), (2, 7), (2, 10),
-- SKU 21 (iPhone 17 PM White 256GB): ram 12gb(2), rom 256gb(6), color white(11)
(21, 2), (21, 6), (21, 11),
-- SKU 3 (S26U Black 1TB): ram 16gb(3), rom 1tb(8), color black(10)
(3, 3), (3, 8), (3, 10),
-- SKU 4 (S26U White 512GB): ram 16gb(3), rom 512gb(7), color white(11)
(4, 3), (4, 7), (4, 11),
-- SKU 22 (S26U Green 256GB): ram 12gb(2), rom 256gb(6), color green(36)
(22, 2), (22, 6), (22, 36),
-- SKU 23 (Xiaomi 15U Black 512GB): ram 16gb(3), rom 512gb(7), color black(10)
(23, 3), (23, 7), (23, 10),
-- SKU 24 (Xiaomi 15U White 256GB): ram 12gb(2), rom 256gb(6), color white(11)
(24, 2), (24, 6), (24, 11),
-- SKU 25 (iPhone 17P Natural 256GB): ram 8gb(1), rom 256gb(6), color natural(9)
(25, 1), (25, 6), (25, 9),
-- SKU 26 (iPhone 17P Black 512GB): ram 8gb(1), rom 512gb(7), color black(10)
(26, 1), (26, 7), (26, 10),
-- SKU 27 (S26 Blue 256GB): ram 8gb(1), rom 256gb(6), color blue(35)
(27, 1), (27, 6), (27, 35),
-- SKU 28 (S26 Black 128GB): ram 8gb(1), rom 128gb(5), color black(10)
(28, 1), (28, 5), (28, 10),
-- SKU 29 (Oppo Find X8P 512GB): ram 16gb(3), rom 512gb(7), color black(10)
(29, 3), (29, 7), (29, 10),
-- SKU 5 (Cáp Anker 1m): length 1m(15), port usb-c(32), wattage 100w(21)
(5, 15), (5, 32), (5, 21),
-- SKU 30 (Cáp Anker 2m): length 2m(16), port usb-c(32), wattage 100w(21)
(30, 16), (30, 32), (30, 21),
-- SKU 6 (Baseus 2m): length 2m(16), port usb-c(32)
(6, 16), (6, 32),
-- SKU 31 (Baseus 1m): length 1m(15), port usb-c(32)
(31, 15), (31, 32),
-- SKU 32 (Ugreen USB-C to Lightning): length 1m(15), port lightning(33)
(32, 15), (32, 33),
-- SKU 7 (Kính Belkin iPhone): model iphone 17 pm(30)
(7, 30),
-- SKU 33 (Kính Baseus Samsung): model samsung s26(31)
(33, 31),
-- SKU 8 (Anker 737 24K): cap 24k(25), wattage 140w(22), port usb-c(32)
(8, 25), (8, 22), (8, 32),
-- SKU 9 (Baseus Blade 10K): cap 10k(23), wattage 65w(20), port usb-c(32)
(9, 23), (9, 20), (9, 32),
-- SKU 34 (Baseus Blade 20K): cap 20k(24), wattage 65w(20), port usb-c(32)
(34, 24), (34, 20), (34, 32),
-- SKU 35 (Xiaomi 20K): cap 20k(24), wattage 45w(19), port usb-c(32)
(35, 24), (35, 19), (35, 32),
-- SKU 10 (Ugreen 140W): wattage 140w(22), port usb-c(32)
(10, 22), (10, 32),
-- SKU 36 (Anker Nano 30W): wattage 30w(18), port usb-c(32)
(36, 18), (36, 32),
-- SKU 37 (Apple 20W): wattage 20w(39), port usb-c(32)
(37, 39), (37, 32),
-- SKU 38 (Samsung 45W): wattage 45w(19), port usb-c(32)
(38, 19), (38, 32),
-- SKU 39 (JBL Flip 6 Blue): color blue(35), wattage 30w(18)
(39, 35), (39, 18),
-- SKU 40 (JBL Flip 6 Red): color red(37), wattage 30w(18)
(40, 37), (40, 18),
-- SKU 41 (Sony XB13 Black): color black(10), wattage 5w(41)
(41, 10), (41, 41),
-- SKU 42 (Sony XB13 Blue): color blue(35), wattage 5w(41)
(42, 35), (42, 41);


-- =======================================================
-- 6. GIAO DỊCH MẪU (IMPORT/EXPORT)
-- =======================================================

-- NHẬP KHO
INSERT INTO import_receipts (id, supplier_id, staff_id, total_amount, created_at) VALUES
(1, 1, 2, 529900000, '2026-01-02 08:00:00'),
(2, 2, 2, 449900000, '2026-01-05 09:30:00'),
(3, 4, 3, 29000000, '2026-01-08 10:00:00'),
(4, 7, 3, 89900000, '2026-01-10 14:00:00'),
(5, 3, 2, 59800000, '2026-01-15 11:30:00');

INSERT INTO import_details (import_receipt_id, product_id, sku_id, quantity) VALUES
-- Phiếu 1: Nhập iPhone 17 Pro Max
(1, 1, 1, 5),   -- 5 x IP17PM 1TB Natural
(1, 1, 2, 8),   -- 8 x IP17PM 512GB Black
(1, 1, 21, 12), -- 12 x IP17PM 256GB White
-- Phiếu 2: Nhập Samsung S26 Ultra
(2, 2, 3, 5),   -- 5 x S26U 1TB Black
(2, 2, 4, 10),  -- 10 x S26U 512GB White
(2, 2, 22, 15), -- 15 x S26U 256GB Green
-- Phiếu 3: Nhập phụ kiện Anker
(3, 4, 5, 50),  -- 50 x Cáp Anker 1m
(3, 4, 30, 40), -- 40 x Cáp Anker 2m
(3, 7, 8, 15),  -- 15 x Anker PowerBank 24K
-- Phiếu 4: Nhập điện thoại Xiaomi
(4, 3, 23, 8),  -- 8 x Xiaomi 15U 512GB Black
(4, 3, 24, 10), -- 10 x Xiaomi 15U 256GB White
-- Phiếu 5: Nhập loa
(5, 19, 39, 12), -- 12 x JBL Flip 6 Blue
(5, 19, 40, 10), -- 10 x JBL Flip 6 Red
(5, 20, 41, 25), -- 25 x Sony XB13 Black
(5, 20, 42, 20); -- 20 x Sony XB13 Blue

-- IMEI cho điện thoại (mỗi SKU điện thoại có vài IMEI)
INSERT INTO phone_imeis (sku_id, import_receipt_id, imei, status) VALUES
-- iPhone 17 PM 1TB Natural (SKU 1)
(1, 1, '352789100000001', 'available'),
(1, 1, '352789100000002', 'available'),
(1, 1, '352789100000003', 'sold'),
(1, 1, '352789100000004', 'available'),
(1, 1, '352789100000005', 'available'),
-- iPhone 17 PM 512GB Black (SKU 2)
(2, 1, '352789100000011', 'available'),
(2, 1, '352789100000012', 'sold'),
(2, 1, '352789100000013', 'available'),
(2, 1, '352789100000014', 'available'),
(2, 1, '352789100000015', 'available'),
(2, 1, '352789100000016', 'available'),
(2, 1, '352789100000017', 'sold'),
(2, 1, '352789100000018', 'available'),
-- iPhone 17 PM 256GB White (SKU 21)
(21, 1, '352789100000021', 'available'),
(21, 1, '352789100000022', 'available'),
(21, 1, '352789100000023', 'available'),
(21, 1, '352789100000024', 'sold'),
(21, 1, '352789100000025', 'available'),
-- Samsung S26U 1TB Black (SKU 3)
(3, 2, '352789200000001', 'available'),
(3, 2, '352789200000002', 'available'),
(3, 2, '352789200000003', 'sold'),
(3, 2, '352789200000004', 'available'),
(3, 2, '352789200000005', 'available'),
-- Samsung S26U 512GB White (SKU 4)
(4, 2, '352789200000011', 'available'),
(4, 2, '352789200000012', 'available'),
(4, 2, '352789200000013', 'available'),
(4, 2, '352789200000014', 'sold'),
(4, 2, '352789200000015', 'available'),
-- Samsung S26U 256GB Green (SKU 22)
(22, 2, '352789200000021', 'available'),
(22, 2, '352789200000022', 'available'),
(22, 2, '352789200000023', 'available'),
-- Xiaomi 15U 512GB Black (SKU 23)
(23, 4, '352789300000001', 'available'),
(23, 4, '352789300000002', 'sold'),
(23, 4, '352789300000003', 'available'),
(23, 4, '352789300000004', 'available'),
-- Xiaomi 15U 256GB White (SKU 24)
(24, 4, '352789300000011', 'available'),
(24, 4, '352789300000012', 'available'),
(24, 4, '352789300000013', 'sold'),
(24, 4, '352789300000014', 'available');

-- XUẤT KHO (Phiếu xuất)
INSERT INTO invoices (id, staff_id, total_amount, created_at) VALUES
(1, 2, 56490000, '2026-01-05 10:30:00'),
(2, 3, 93980000, '2026-01-10 14:30:00'),
(3, 2, 47280000, '2026-01-12 09:15:00'),
(4, 4, 6470000, '2026-01-18 16:00:00'),
(5, 3, 85970000, '2026-01-20 11:45:00'),
(6, 2, 4180000, '2026-01-25 15:30:00');

-- Chi tiết xuất kho
INSERT INTO invoice_details (invoice_id, sku_id, quantity, imei_id) VALUES
-- Phiếu 1: Bán 1 iPhone 17PM + 1 Sạc Anker
(1, 1, 1, 3),      -- iPhone với IMEI 3
(1, 8, 1, NULL),   -- Sạc dự phòng
-- Phiếu 2: Bán 2 iPhone 17PM Black
(2, 2, 1, 7),      -- iPhone với IMEI 7
(2, 2, 1, 12),     -- iPhone với IMEI 12 (id=7)
-- Phiếu 3: Bán 1 Samsung S26U + phụ kiện
(3, 3, 1, 21),     -- Samsung với IMEI
(3, 5, 2, NULL),   -- 2 x Cáp Anker
(3, 7, 1, NULL),   -- 1 x Kính cường lực
-- Phiếu 4: Bán phụ kiện
(4, 5, 5, NULL),   -- 5 x Cáp Anker 1m
(4, 6, 10, NULL),  -- 10 x Cáp Baseus 2m
(4, 36, 3, NULL),  -- 3 x Củ sạc Anker 30W
-- Phiếu 5: Bán 1 Xiaomi + 1 iPhone 17PM White
(5, 23, 1, 32),    -- Xiaomi với IMEI
(5, 21, 1, 18),    -- iPhone với IMEI
-- Phiếu 6: Bán loa
(6, 39, 1, NULL),  -- 1 x JBL Flip 6 Blue
(6, 41, 1, NULL);  -- 1 x Sony XB13 Black


-- =======================================================
-- 7. LOGS (Nhật ký hoạt động)
-- =======================================================

INSERT INTO logs (user_id, action, details, created_at) VALUES
-- Tháng 1/2026
(1, 'Đăng nhập', 'Đăng nhập thành công', '2026-01-02 07:30:00'),
(1, 'Thêm sản phẩm', 'Thêm sản phẩm: iPhone 17 Pro Max', '2026-01-02 07:45:00'),
(1, 'Thêm sản phẩm', 'Thêm sản phẩm: Samsung Galaxy S26 Ultra', '2026-01-02 08:00:00'),
(2, 'Đăng nhập', 'Đăng nhập thành công', '2026-01-02 08:00:00'),
(2, 'Tạo phiếu nhập', 'Phiếu nhập #1 - FPT Synnex - 529.900.000₫', '2026-01-02 08:15:00'),
(1, 'Đăng xuất', 'Đăng xuất thành công', '2026-01-02 12:00:00'),

(2, 'Đăng nhập', 'Đăng nhập thành công', '2026-01-05 09:00:00'),
(2, 'Tạo phiếu nhập', 'Phiếu nhập #2 - Viettel Store - 449.900.000₫', '2026-01-05 09:30:00'),
(2, 'Xuất kho', 'Phiếu xuất #1 - 56.490.000₫', '2026-01-05 10:30:00'),
(2, 'Đăng xuất', 'Đăng xuất thành công', '2026-01-05 18:00:00'),

(3, 'Đăng nhập', 'Đăng nhập thành công', '2026-01-08 09:45:00'),
(3, 'Tạo phiếu nhập', 'Phiếu nhập #3 - Anker Vietnam - 29.000.000₫', '2026-01-08 10:00:00'),
(3, 'Đăng xuất', 'Đăng xuất thành công', '2026-01-08 17:30:00'),

(3, 'Đăng nhập', 'Đăng nhập thành công', '2026-01-10 13:30:00'),
(3, 'Tạo phiếu nhập', 'Phiếu nhập #4 - Thế Giới Di Động - 89.900.000₫', '2026-01-10 14:00:00'),
(3, 'Xuất kho', 'Phiếu xuất #2 - 93.980.000₫', '2026-01-10 14:30:00'),
(3, 'Đăng xuất', 'Đăng xuất thành công', '2026-01-10 18:30:00'),

(2, 'Đăng nhập', 'Đăng nhập thành công', '2026-01-12 08:30:00'),
(2, 'Xuất kho', 'Phiếu xuất #3 - 47.280.000₫', '2026-01-12 09:15:00'),
(2, 'Sửa sản phẩm', 'Cập nhật giá: Anker PowerLine III Flow', '2026-01-12 10:30:00'),
(2, 'Đăng xuất', 'Đăng xuất thành công', '2026-01-12 17:00:00'),

(2, 'Đăng nhập', 'Đăng nhập thành công', '2026-01-15 11:00:00'),
(2, 'Tạo phiếu nhập', 'Phiếu nhập #5 - CellphoneS B2B - 59.800.000₫', '2026-01-15 11:30:00'),
(2, 'Đăng xuất', 'Đăng xuất thành công', '2026-01-15 18:00:00'),

(1, 'Đăng nhập', 'Đăng nhập thành công', '2026-01-18 08:00:00'),
(1, 'Thêm tài khoản', 'Thêm tài khoản: anna', '2026-01-18 08:30:00'),
(4, 'Đăng nhập', 'Đăng nhập thành công', '2026-01-18 15:30:00'),
(4, 'Xuất kho', 'Phiếu xuất #4 - 6.470.000₫', '2026-01-18 16:00:00'),
(4, 'Đăng xuất', 'Đăng xuất thành công', '2026-01-18 18:00:00'),
(1, 'Đăng xuất', 'Đăng xuất thành công', '2026-01-18 19:00:00'),

(3, 'Đăng nhập', 'Đăng nhập thành công', '2026-01-20 11:00:00'),
(3, 'Xuất kho', 'Phiếu xuất #5 - 85.970.000₫', '2026-01-20 11:45:00'),
(3, 'Xóa IMEI', 'Xóa IMEI: 352789100456792 (lỗi nhập)', '2026-01-20 14:15:00'),
(3, 'Đăng xuất', 'Đăng xuất thành công', '2026-01-20 18:00:00'),

(2, 'Đăng nhập', 'Đăng nhập thành công', '2026-01-25 14:00:00'),
(2, 'Xuất kho', 'Phiếu xuất #6 - 4.180.000₫', '2026-01-25 15:30:00'),
(2, 'Sửa SKU', 'Cập nhật tồn kho: JBL Flip 6 Blue', '2026-01-25 16:00:00'),
(2, 'Đăng xuất', 'Đăng xuất thành công', '2026-01-25 18:30:00'),

(1, 'Đăng nhập', 'Đăng nhập thành công', '2026-01-30 09:00:00'),
(1, 'Thêm thương hiệu', 'Thêm thương hiệu: JBL', '2026-01-30 09:15:00'),
(1, 'Thêm thương hiệu', 'Thêm thương hiệu: Realme', '2026-01-30 09:20:00'),
(1, 'Thêm nhà cung cấp', 'Thêm NCC: Điện Máy Xanh', '2026-01-30 09:30:00'),
(1, 'Đăng xuất', 'Đăng xuất thành công', '2026-01-30 17:00:00');
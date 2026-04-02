CREATE DATABASE IF NOT EXISTS shopping_cart_localization
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE shopping_cart_localization;

CREATE TABLE IF NOT EXISTS cart_records (
    id INT AUTO_INCREMENT PRIMARY KEY,
    total_items INT NOT NULL,
    total_cost DOUBLE NOT NULL,
    language VARCHAR(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS cart_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cart_record_id INT,
    item_number INT NOT NULL,
    price DOUBLE NOT NULL,
    quantity INT NOT NULL,
    subtotal DOUBLE NOT NULL,
    FOREIGN KEY (cart_record_id) REFERENCES cart_records(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS localization_strings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    `key` VARCHAR(100) NOT NULL,
    value VARCHAR(255) NOT NULL,
    language VARCHAR(10) NOT NULL
);

INSERT INTO localization_strings (`key`, value, language) VALUES
('prompt.num.items', 'Enter the number of items to purchase:', 'en'),
('prompt.item.price', 'Enter the price for item', 'en'),
('prompt.item.quantity', 'Enter the quantity for item', 'en'),
('label.total.cost', 'Total cost', 'en'),
('app.title', 'Shopping Cart Application', 'en'),
('label.language', 'Language:', 'en'),
('button.create', 'Create Items', 'en'),
('button.calculate', 'Calculate Total', 'en'),
('label.item.number', 'Item', 'en'),
('label.footer', 'SEP2 - Week 2 Assignment - JavaFX Shopping Cart', 'en'),
('label.item.total', 'Item Total:', 'en'),
('error.empty.input', 'Please enter a number of items', 'en'),
('error.invalid.number', 'Invalid number entered', 'en'),
('error.max.items', 'Maximum 100 items allowed', 'en'),
('error.no.items', 'Please create items first', 'en'),
('error.negative.price', 'Price cannot be negative', 'en'),
('error.negative.quantity', 'Quantity cannot be negative', 'en'),
('error.empty.field', 'This field cannot be empty', 'en'),
('error.invalid.price', 'Invalid price format', 'en'),
('error.invalid.quantity', 'Invalid quantity format', 'en');

INSERT INTO localization_strings (`key`, value, language) VALUES
('prompt.num.items', 'Syötä ostettavien tuotteiden määrä:', 'fi'),
('prompt.item.price', 'Syötä tuotteen hinta', 'fi'),
('prompt.item.quantity', 'Syötä tuotteen määrä', 'fi'),
('label.total.cost', 'Kokonaishinta', 'fi'),
('app.title', 'Ostoskärryn sovellus', 'fi'),
('label.language', 'Kieli:', 'fi'),
('button.create', 'Luo tuotteet', 'fi'),
('button.calculate', 'Laske yhteensä', 'fi'),
('label.item.number', 'Tuote', 'fi'),
('label.footer', 'SEP2 - Viikko 2 -tehtävä - JavaFX Ostoskärry', 'fi'),
('label.item.total', 'Tuotteen summa:', 'fi'),
('error.empty.input', 'Anna tuotteiden määrä', 'fi'),
('error.invalid.number', 'Virheellinen numero', 'fi'),
('error.max.items', 'Enintään 100 tuotetta sallittu', 'fi'),
('error.no.items', 'Luo ensin tuotteet', 'fi'),
('error.negative.price', 'Hinta ei voi olla negatiivinen', 'fi'),
('error.negative.quantity', 'Määrä ei voi olla negatiivinen', 'fi'),
('error.empty.field', 'Tämä kenttä ei voi olla tyhjä', 'fi'),
('error.invalid.price', 'Virheellinen hintamuoto', 'fi'),
('error.invalid.quantity', 'Virheellinen määrämuoto', 'fi');

INSERT INTO localization_strings (`key`, value, language) VALUES
('prompt.num.items', 'Ange antalet varor att köpa:', 'sv'),
('prompt.item.price', 'Ange priset för varan', 'sv'),
('prompt.item.quantity', 'Ange mängden varor', 'sv'),
('label.total.cost', 'Total kostnad', 'sv'),
('app.title', 'Shopping Cart-applikation', 'sv'),
('label.language', 'Språk:', 'sv'),
('button.create', 'Skapa varor', 'sv'),
('button.calculate', 'Beräkna totalt', 'sv'),
('label.item.number', 'Produkt', 'sv'),
('label.footer', 'SEP2 - Vecka 2 - Uppgift - JavaFX Shopping Cart', 'sv'),
('label.item.total', 'Produkt total:', 'sv'),
('error.empty.input', 'Ange antalet produkter', 'sv'),
('error.invalid.number', 'Ogiltigt nummer', 'sv'),
('error.max.items', 'Max 100 produkter tillåtna', 'sv'),
('error.no.items', 'Skapa produkter först', 'sv'),
('error.negative.price', 'Priset kan inte vara negativt', 'sv'),
('error.negative.quantity', 'Antalet kan inte vara negativt', 'sv'),
('error.empty.field', 'Detta fält kan inte vara tomt', 'sv'),
('error.invalid.price', 'Ogiltigt prisformat', 'sv'),
('error.invalid.quantity', 'Ogiltigt antalsformat', 'sv');

INSERT INTO localization_strings (`key`, value, language) VALUES
('prompt.num.items', '購入する商品の数を入力してください:', 'ja'),
('prompt.item.price', '商品の価格を入力してください', 'ja'),
('prompt.item.quantity', '商品の数量を入力してください', 'ja'),
('label.total.cost', '合計金額', 'ja'),
('app.title', 'ショッピングカートアプリケーション', 'ja'),
('label.language', '言語:', 'ja'),
('button.create', 'アイテム作成', 'ja'),
('button.calculate', '合計計算', 'ja'),
('label.item.number', 'アイテム', 'ja'),
('label.footer', 'SEP2 - 第2週 - デジタルショッピングカート JavaFX', 'ja'),
('label.item.total', 'アイテム合計:', 'ja'),
('error.empty.input', '商品数を入力してください', 'ja'),
('error.invalid.number', '無効な番号', 'ja'),
('error.max.items', '最大100個まで', 'ja'),
('error.no.items', '最初に商品を作成してください', 'ja'),
('error.negative.price', '価格は負できません', 'ja'),
('error.negative.quantity', '数量は負できません', 'ja'),
('error.empty.field', 'このフィールドは空にできません', 'ja'),
('error.invalid.price', '無効な価格形式', 'ja'),
('error.invalid.quantity', '無効な数量形式', 'ja');

INSERT INTO localization_strings (`key`, value, language) VALUES
('prompt.num.items', 'أدخل عدد المنتجات المراد شرائها:', 'ar'),
('prompt.item.price', 'أدخل سعر المنتج', 'ar'),
('prompt.item.quantity', 'أدخل كمية المنتج', 'ar'),
('label.total.cost', 'التكلفة الإجمالية', 'ar'),
('app.title', 'تطبيق سلة الشراء', 'ar'),
('label.language', 'اللغة:', 'ar'),
('button.create', 'إنشاء المنتجات', 'ar'),
('button.calculate', 'حساب المجموع', 'ar'),
('label.item.number', 'المنتج', 'ar'),
('label.footer', 'تكليف أساسي آسية - أسبوع ثاني - تطبيق سلة الشراء JavaFX', 'ar'),
('label.item.total', 'مجموع المنتج:', 'ar'),
('error.empty.input', 'أدخل عدد المنتجات', 'ar'),
('error.invalid.number', 'رقم غير صالح', 'ar'),
('error.max.items', 'الحد الأقصى 100 منتج', 'ar'),
('error.no.items', 'قم بإنشاء المنتجات أولاً', 'ar'),
('error.negative.price', 'السعر لا يمكن أن يكون سالباً', 'ar'),
('error.negative.quantity', 'الكمية لا يمكن أن تكون سالبة', 'ar'),
('error.empty.field', 'هذا الحقل لا يمكن أن يكون فارغاً', 'ar'),
('error.invalid.price', 'تنسيق سعر غير صالح', 'ar'),
('error.invalid.quantity', 'تنسيق كمية غير صالح', 'ar');

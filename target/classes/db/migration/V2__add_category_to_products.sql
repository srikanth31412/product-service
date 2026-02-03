ALTER TABLE products
    ADD COLUMN category VARCHAR(100) NOT NULL DEFAULT 'GENERAL';

CREATE INDEX idx_products_category ON products(category);

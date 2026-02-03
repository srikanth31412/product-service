ALTER TABLE products
    ADD COLUMN reorder_level INTEGER;

CREATE INDEX idx_products_reorder_level
    ON products(reorder_level);

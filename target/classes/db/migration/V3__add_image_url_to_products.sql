ALTER TABLE products
    ADD COLUMN image_url VARCHAR(500);

CREATE INDEX idx_products_image_url
    ON products(image_url);

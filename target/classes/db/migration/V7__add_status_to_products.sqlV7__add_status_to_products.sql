-- file: product-service/src/main/resources/db/migration/V7__add_status_to_products.sql

-- add column with default
ALTER TABLE products
    ADD COLUMN status VARCHAR(50) DEFAULT 'ACTIVE';

-- backfill existing rows
UPDATE products
SET status = 'ACTIVE'
WHERE status IS NULL;

-- enforce NOT NULL
ALTER TABLE products
    ALTER COLUMN status SET NOT NULL;

-- ensure default for future inserts
ALTER TABLE products
    ALTER COLUMN status SET DEFAULT 'ACTIVE';

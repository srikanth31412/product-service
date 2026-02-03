CREATE TABLE products (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          sku VARCHAR(50) NOT NULL UNIQUE,
                          name VARCHAR(150) NOT NULL,
                          description TEXT,
                          category VARCHAR(50) NOT NULL,
                          price DECIMAL(10,2) NOT NULL,
                          quantity INT NOT NULL,
                          reorder_level INT NOT NULL,
                          status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
                          image_url VARCHAR(255),
                          created_at TIMESTAMP NOT NULL,
                          updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_products_name ON products(name);
CREATE INDEX idx_products_sku ON products(sku);
CREATE INDEX idx_products_category ON products(category);
CREATE INDEX idx_products_status ON products(status);

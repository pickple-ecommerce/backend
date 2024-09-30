-- Vendor Table
CREATE TABLE p_vendors (
                           vendor_id UUID PRIMARY KEY,
                           vendor_name VARCHAR(255) NOT NULL,
                           vendor_address VARCHAR(255),
                           user_id BIGINT REFERENCES p_users(user_id)
);

-- Product Table
CREATE TABLE p_products (
                            product_id UUID PRIMARY KEY,
                            product_name VARCHAR NOT NULL,
                            description TEXT,
                            product_price DECIMAL(10, 2) NOT NULL,
                            product_image VARCHAR,
                            is_public BOOLEAN DEFAULT TRUE,
                            vendor_id UUID REFERENCES p_vendors(vendor_id)
);

-- Stock Table
CREATE TABLE p_stocks (
                          stock_id UUID PRIMARY KEY,
                          stock_quantity BIGINT NOT NULL,
                          product_id UUID REFERENCES p_products(product_id)
);

-- Order Table
CREATE TABLE p_orders (
                          order_id UUID PRIMARY KEY,
                          order_status VARCHAR(50) CHECK (order_status IN ('PENDING', 'COMPLETED', 'CANCELED')),
                          order_price DECIMAL(10, 2) NOT NULL,
                          user_id BIGINT REFERENCES p_users(user_id),
                          delivery_id UUID
);

-- Order Details Table
CREATE TABLE p_order_details (
                                 order_detail_id UUID PRIMARY KEY,
                                 total_price DECIMAL(10, 2) NOT NULL,
                                 order_quantity BIGINT NOT NULL,
                                 product_id UUID REFERENCES p_products(product_id)
);

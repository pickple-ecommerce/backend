-- Insert dummy data into p_vendors
INSERT INTO p_vendors (vendor_id, vendor_name, vendor_address, user_id)
VALUES
    ('123e4567-e89b-12d3-a456-426614174000', 'Vendor A', '123 Main St', 1),
    ('123e4567-e89b-12d3-a456-426614174001', 'Vendor B', '456 Side St', 2),
    ('123e4567-e89b-12d3-a456-426614174002', 'Vendor C', '789 High St', 3);

-- Insert dummy data into p_products
INSERT INTO p_products (product_id, product_name, description, product_price, product_image, is_public)
VALUES
    ('223e4567-e89b-12d3-a456-426614174000', 'Product A1', 'Description for Product A1', 29.99, 'productA1.jpg', TRUE),
    ('223e4567-e89b-12d3-a456-426614174001', 'Product B1', 'Description for Product B1', 49.99, 'productB1.jpg', TRUE),
    ('223e4567-e89b-12d3-a456-426614174002', 'Product C1', 'Description for Product C1', 19.99, 'productC1.jpg', TRUE);

-- Insert dummy data into p_orders
INSERT INTO p_orders (order_id, order_status, amount, user_id, payment_id, delivery_id)
VALUES
    ('323e4567-e89b-12d3-a456-426614174000', 'PENDING', 79.98, 1, '423e4567-e89b-12d3-a456-426614174000', '523e4567-e89b-12d3-a456-426614174000'),
    ('323e4567-e89b-12d3-a456-426614174001', 'COMPLETED', 99.98, 2, '423e4567-e89b-12d3-a456-426614174001', '523e4567-e89b-12d3-a456-426614174001'),
    ('323e4567-e89b-12d3-a456-426614174002', 'CANCELED', 19.99, 3, '423e4567-e89b-12d3-a456-426614174002', '523e4567-e89b-12d3-a456-426614174002');

-- Insert dummy data into p_order_details
INSERT INTO p_order_details (order_detail_id, total_price, order_quantity, product_id, order_id)
VALUES
    -- Order 1: Two products
    ('623e4567-e89b-12d3-a456-426614174000', 29.99, 1, '223e4567-e89b-12d3-a456-426614174000', '323e4567-e89b-12d3-a456-426614174000'),
    ('623e4567-e89b-12d3-a456-426614174003', 49.99, 1, '223e4567-e89b-12d3-a456-426614174001', '323e4567-e89b-12d3-a456-426614174000'),

    -- Order 2: Two products
    ('623e4567-e89b-12d3-a456-426614174001', 49.99, 1, '223e4567-e89b-12d3-a456-426614174001', '323e4567-e89b-12d3-a456-426614174001'),
    ('623e4567-e89b-12d3-a456-426614174004', 49.99, 1, '223e4567-e89b-12d3-a456-426614174002', '323e4567-e89b-12d3-a456-426614174001'),

    -- Order 3: One product
    ('623e4567-e89b-12d3-a456-426614174002', 19.99, 1, '223e4567-e89b-12d3-a456-426614174002', '323e4567-e89b-12d3-a456-426614174002');

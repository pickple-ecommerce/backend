-- Keyspace 생성
CREATE KEYSPACE IF NOT EXISTS "delivery_keyspace"
    WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};


CREATE TABLE IF NOT EXISTS "delivery_keyspace".p_deliveries
(
    delivery_id          UUID PRIMARY KEY,
    order_id             UUID,
    carrier_id           text,
    carrier_name         text,
    delivery_type        text,
    delivery_status      text,
    delivery_requirement text,
    tracking_number      text,
    recipient_name       text,
    recipient_address    text,
    recipient_contact    text,

    -- Audit fields
    created_at           timestamp,
    created_by           text,
    updated_at           timestamp,
    updated_by           text,

);

CREATE TABLE IF NOT EXISTS "delivery_keyspace".p_delivery_details
(
    delivery_id  UUID,
    delivery_detail_time timestamp,
    delivery_detail_status      text,
    delivery_detail_description  text,

    -- Audit fields
    created_at   timestamp,
    created_by   text,
    updated_at   timestamp,
    updated_by   text,

    PRIMARY KEY (delivery_id, delivery_detail_time)
);

CREATE TABLE IF NOT EXISTS "delivery_keyspace".p_deliveries_deleted
(
    delivery_id          UUID PRIMARY KEY,
    order_id             UUID,
    carrier_id           text,
    carrier_name         text,
    delivery_type        text,
    delivery_status      text,
    delivery_requirement text,
    tracking_number      text,
    recipient_name       text,
    recipient_address    text,
    recipient_contact    text,

    -- Audit fields
    created_at           timestamp,
    created_by           text,
    updated_at           timestamp,
    updated_by           text,
    deleted_at           timestamp,
    deleted_by           text
);

CREATE TABLE IF NOT EXISTS "delivery_keyspace".p_delivery_details_deleted
(
    delivery_id              UUID,
    delivery_detail_time     timestamp,
    delivery_detail_status   text,
    delivery_detail_description text,

    -- Audit fields
    created_at               timestamp,
    created_by               text,
    updated_at               timestamp,
    updated_by               text,
    deleted_at               timestamp,
    deleted_by               text,

    PRIMARY KEY (delivery_id, delivery_detail_time)
);
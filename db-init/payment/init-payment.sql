-- Table: public.p_payments

-- DROP TABLE IF EXISTS public.p_payments;

CREATE TABLE IF NOT EXISTS public.p_payments
(
    payment_id uuid NOT NULL,
    order_id uuid,
    amount double precision,
    method character varying COLLATE pg_catalog."default",
    status character varying COLLATE pg_catalog."default",
    approval_number character varying COLLATE pg_catalog."default",
    is_delete boolean,
    created_at timestamp without time zone,
    created_by character varying COLLATE pg_catalog."default",
    updated_at timestamp without time zone,
    updated_by character varying COLLATE pg_catalog."default",
    deleted_at timestamp without time zone,
    deleted_by character varying COLLATE pg_catalog."default",
    CONSTRAINT p_payments_pkey PRIMARY KEY (payment_id)
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.p_payments
    OWNER to pickple;
create table if not exists p_notifications (
    notification_id UUID primary key,
    channel_id UUID,
    username varchar,
    category varchar,
    subject varchar,
    content text,
    sender varchar,
    status varchar
);

create table if not exists p_notification_channels (
    id UUID primary key DEFAULT uuid_generate_v4(),
    name varchar
);
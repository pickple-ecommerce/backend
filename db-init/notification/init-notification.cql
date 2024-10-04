create keyspace if not exists "notification_keyspace"
    with replication = {'class':'SimpleStrategy', 'replication_factor': 1};

create table if not exists "notification_keyspace".p_notifications
(
    notification_id UUID primary key,
    channel_id UUID,
    user_id bigint,
    type varchar,
    text varchar,
    status varchar
);

create table if not exists "notification_keyspace".p_notification_channels
(
    id UUID primary key,
    channel_id varchar,
    name varchar,
    type varchar
);
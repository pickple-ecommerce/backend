-- uuid-ossp 확장 활성화
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create table if not exists p_notification_channels (
    id UUID primary key,
    name varchar,
    description text,
    is_delete boolean
);

create table if not exists p_notifications (
    notification_id UUID primary key,
    channel_id UUID,
    username varchar,
    category varchar,
    subject varchar,
    content text,
    sender varchar,
    status varchar,
    foreign key (channel_id) references p_notification_channels(id)
);

-- 알림 채널에 초기 데이터 삽입 (UUID 자동 생성)
INSERT INTO p_notification_channels VALUES (uuid_generate_v4(), 'Email', 'Notification channel for email alerts', false);


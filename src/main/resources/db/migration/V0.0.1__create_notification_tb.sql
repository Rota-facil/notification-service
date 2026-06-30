CREATE TABLE IF NOT EXISTS notifications_tb (
    notification_id UUID PRIMARY KEY,
    recipient_id UUID NOT NULL,
    recipient_type VARCHAR(40) NOT NULL,
    notification_type VARCHAR(40) NOT NULL,
    title TEXT NOT NULL,
    message TEXT NOT NULL,
    target_type VARCHAR(40) NOT NULL,
    target_id UUID NOT NULL,
    priority VARCHAR(10) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
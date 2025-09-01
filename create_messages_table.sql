-- Create messages table if it doesn't exist
CREATE TABLE IF NOT EXISTS messages (
    message_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sender_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    sent_at DATETIME,
    created_at DATETIME,
    updated_at DATETIME,
    is_read BOOLEAN DEFAULT FALSE,
    
    INDEX idx_sender_id (sender_id),
    INDEX idx_receiver_id (receiver_id),
    INDEX idx_created_at (created_at),
    INDEX idx_conversation (sender_id, receiver_id),
    INDEX idx_unread (receiver_id, is_read)
);

-- Insert some test data (optional)
-- INSERT INTO messages (sender_id, receiver_id, content, sent_at, created_at, updated_at, is_read) VALUES
-- (1, 2, 'Hello, this is a test message', NOW(), NOW(), NOW(), FALSE),
-- (2, 1, 'Hi there! This is a reply', NOW(), NOW(), NOW(), FALSE),
-- (1, 3, 'Another test message', NOW(), NOW(), NOW(), FALSE);

-- Verify table structure
DESCRIBE messages;
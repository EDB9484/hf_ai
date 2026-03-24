CREATE TABLE IF NOT EXISTS member (
    id BIGSERIAL PRIMARY KEY,
    joined_at TIMESTAMP NOT NULL,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    customer_id VARCHAR(40) NOT NULL UNIQUE,
    birth_date VARCHAR(10) NOT NULL,
    gender VARCHAR(1) NOT NULL,
    member_status VARCHAR(30) NOT NULL,
    converted_at TIMESTAMP NULL,
    restriction VARCHAR(30) NOT NULL,
    inflow_channel VARCHAR(50) NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_member_joined_at ON member (joined_at DESC);
CREATE INDEX IF NOT EXISTS idx_member_name ON member (name);
CREATE INDEX IF NOT EXISTS idx_member_phone ON member (phone);
CREATE INDEX IF NOT EXISTS idx_member_birth_date ON member (birth_date);
CREATE INDEX IF NOT EXISTS idx_member_member_status ON member (member_status);
CREATE INDEX IF NOT EXISTS idx_member_restriction ON member (restriction);

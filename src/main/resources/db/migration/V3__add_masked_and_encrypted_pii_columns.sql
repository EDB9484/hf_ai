ALTER TABLE member
    ADD COLUMN IF NOT EXISTS masked_name VARCHAR(100),
    ADD COLUMN IF NOT EXISTS encrypted_name TEXT,
    ADD COLUMN IF NOT EXISTS masked_phone VARCHAR(20),
    ADD COLUMN IF NOT EXISTS encrypted_phone TEXT;

UPDATE member
SET masked_name = COALESCE(masked_name, name),
    encrypted_name = COALESCE(encrypted_name, 'ENC:' || name),
    masked_phone = COALESCE(masked_phone, phone),
    encrypted_phone = COALESCE(encrypted_phone, 'ENC:' || phone)
WHERE masked_name IS NULL
   OR encrypted_name IS NULL
   OR masked_phone IS NULL
   OR encrypted_phone IS NULL;

ALTER TABLE member
    ALTER COLUMN masked_name SET NOT NULL,
    ALTER COLUMN encrypted_name SET NOT NULL,
    ALTER COLUMN masked_phone SET NOT NULL,
    ALTER COLUMN encrypted_phone SET NOT NULL;

CREATE INDEX IF NOT EXISTS idx_member_masked_name ON member (masked_name);
CREATE INDEX IF NOT EXISTS idx_member_masked_phone ON member (masked_phone);

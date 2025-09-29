-- Migration: Add isActive column to drinks table
-- Author: FratTab Development Team
-- Date: 2025-09-29
-- Description: Adds isActive boolean column to track active/inactive drinks

BEGIN;

-- Check if the column already exists before adding it
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 
        FROM information_schema.columns 
        WHERE table_name = 'drink' 
        AND column_name = 'is_active'
    ) THEN
        -- Add the isActive column with default value true
        ALTER TABLE drink 
        ADD COLUMN is_active BOOLEAN DEFAULT true NOT NULL;
        
        -- Add index for performance on active drinks queries
        CREATE INDEX IF NOT EXISTS idx_drink_is_active 
        ON drink(is_active);
        
        -- Log the migration
        RAISE NOTICE 'Added is_active column to drink table with default value true';
    ELSE
        RAISE NOTICE 'Column is_active already exists in drink table';
    END IF;
END $$;

-- Ensure all existing records have is_active = true
UPDATE drink 
SET is_active = true 
WHERE is_active IS NULL OR is_active IS FALSE;

-- Add table comment
COMMENT ON TABLE drink IS 'Stores drink information including name, quantity, price, and active status';
COMMENT ON COLUMN drink.is_active IS 'Boolean flag indicating if the drink is currently active and available for ordering';

COMMIT;

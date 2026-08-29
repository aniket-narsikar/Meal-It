-- Database Migration Script for Meal-It Application

-- 1. Status Enum Typo Fix Migration
-- Updates existing records in food_order table storing food_status enum strings
UPDATE food_order SET food_status = 'RECEIVED' WHERE food_status = 'RECIVED';
UPDATE food_order SET food_status = 'PREPARING' WHERE food_status = 'PREPRING';

-- 2. Password Storage Notice:
-- Passwords saved via POST /user/save or POST /auth/login will be stored as BCrypt hashes.
-- Legacy plain-text passwords will automatically be migrated to BCrypt hash upon successful user authentication via POST /auth/login.

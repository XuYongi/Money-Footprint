-- Drop tables if they exist
DROP TABLE IF EXISTS specification CASCADE;
DROP TABLE IF EXISTS advertisement CASCADE;
DROP TABLE IF EXISTS stockpile CASCADE;
DROP TABLE IF EXISTS payment CASCADE;
DROP TABLE IF EXISTS wallet CASCADE;
DROP TABLE IF EXISTS account CASCADE;
DROP TABLE IF EXISTS product CASCADE;

-- Create account table
CREATE TABLE IF NOT EXISTS account
(
    id        SERIAL PRIMARY KEY,
    username  VARCHAR(50),
    password  VARCHAR(100),
    name      VARCHAR(50),
    avatar    VARCHAR(100),
    telephone VARCHAR(20),
    email     VARCHAR(100),
    location  VARCHAR(100)
    );
CREATE INDEX idx_account_username ON account(username);

-- Create wallet table
CREATE TABLE IF NOT EXISTS wallet
(
    id         SERIAL PRIMARY KEY,
    money      DECIMAL,
    account_id INTEGER REFERENCES account(id) ON DELETE CASCADE
    );

-- Create product table
CREATE TABLE IF NOT EXISTS product
(
    id          SERIAL PRIMARY KEY,
    title       VARCHAR(50),
    price       DECIMAL,
    rate        FLOAT,
    description VARCHAR(8000),
    cover       VARCHAR(100),
    detail      VARCHAR(100)
    );
CREATE INDEX idx_product_title ON product(title);

-- Create stockpile table
CREATE TABLE IF NOT EXISTS stockpile
(
    id         SERIAL PRIMARY KEY,
    amount     INTEGER,
    frozen     INTEGER,
    product_id INTEGER REFERENCES product(id) ON DELETE CASCADE
    );

-- Create specification table
CREATE TABLE IF NOT EXISTS specification
(
    id         SERIAL PRIMARY KEY,
    item       VARCHAR(50),
    value      VARCHAR(100),
    product_id INTEGER REFERENCES product(id) ON DELETE CASCADE
    );

-- Create advertisement table
CREATE TABLE IF NOT EXISTS advertisement
(
    id         SERIAL PRIMARY KEY,
    image      VARCHAR(100),
    product_id INTEGER REFERENCES product(id) ON DELETE CASCADE
    );

-- Create payment table
CREATE TABLE IF NOT EXISTS payment
(
    id           SERIAL PRIMARY KEY,
    pay_id       VARCHAR(100),
    create_time  TIMESTAMP,
    total_price  DECIMAL,
    expires      INTEGER NOT NULL,
    payment_link VARCHAR(300),
    pay_state    VARCHAR(20)
    );

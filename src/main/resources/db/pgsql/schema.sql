-- 创建用户表
CREATE TABLE IF NOT EXISTS user_t (
                                      id SERIAL PRIMARY KEY, -- 用户ID，主键，自增
                                      username VARCHAR(50) NOT NULL UNIQUE, -- 用户名，非空且唯一
    password VARCHAR(50) NOT NULL, -- 密码，非空
    email VARCHAR(50) -- 邮箱，可选
    );

-- 创建账目类型表
CREATE TABLE IF NOT EXISTS account_type_t (
                                              id SERIAL PRIMARY KEY, -- 账目类型ID，主键，自增
                                              name VARCHAR(50) NOT NULL, -- 账目类型名称，非空
    user_id INTEGER NOT NULL, -- 用户ID，非空
    level INTEGER NOT NULL CHECK (level IN (1, 2)), -- 账目类型等级，非空，1表示一级分类，2表示二级分类
    parent_id INTEGER, -- 父级账目类型ID，可选，如果是一级分类则为空，如果是二级分类则填写对应的一级分类ID
    category INTEGER NOT NULL CHECK (category IN (0, 1)) -- 账目类型类别，非空，0表示支出，1表示收入
    );

-- 创建账本表
CREATE TABLE IF NOT EXISTS ledger_t (
                                        id SERIAL PRIMARY KEY, -- 账本ID，主键，自增
                                        name VARCHAR(50) NOT NULL, -- 账本名称，非空
    creator_id INTEGER NOT NULL -- 创建者ID，非空
    );

-- 创建账本成员表
CREATE TABLE IF NOT EXISTS ledger_member_t (
                                               ledger_id INTEGER NOT NULL, -- 账本ID，非空
                                               user_id INTEGER NOT NULL, -- 用户ID，非空
                                               PRIMARY KEY (ledger_id, user_id) -- 联合主键，由账本ID和用户ID组成
    );

-- 创建账单表
CREATE TABLE IF NOT EXISTS bill_t (
                                      id SERIAL PRIMARY KEY, -- 账单ID，主键，自增
                                      amount NUMERIC(10,2) NOT NULL, -- 金额，非空，保留两位小数
    account_type_id INTEGER NOT NULL, -- 账目类型ID，非空
    ledger_id INTEGER NOT NULL, -- 账本ID，非空
    user_id INTEGER NOT NULL, -- 用户ID，非空
    date DATE NOT NULL, -- 日期，非空
    remark VARCHAR(200) -- 备注，可选
    );


-- 给用户表添加审计字段
ALTER TABLE user_t ADD COLUMN create_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP; -- 创建时间，非空，默认为当前时间
ALTER TABLE user_t ADD COLUMN create_user VARCHAR(50) NOT NULL DEFAULT CURRENT_USER; -- 创建人，非空，默认为当前用户
ALTER TABLE user_t ADD COLUMN update_time TIMESTAMP WITH TIME ZONE; -- 修改时间，可选
ALTER TABLE user_t ADD COLUMN update_user VARCHAR(50); -- 修改人，可选

-- 给账目类型表添加审计字段
ALTER TABLE account_type_t ADD COLUMN create_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP; -- 创建时间，非空，默认为当前时间
ALTER TABLE account_type_t ADD COLUMN create_user VARCHAR(50) NOT NULL DEFAULT CURRENT_USER; -- 创建人，非空，默认为当前用户
ALTER TABLE account_type_t ADD COLUMN update_time TIMESTAMP WITH TIME ZONE; -- 修改时间，可选
ALTER TABLE account_type_t ADD COLUMN update_user VARCHAR(50); -- 修改人，可选

-- 给账本表添加审计字段
ALTER TABLE ledger_t ADD COLUMN create_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP; -- 创建时间，非空，默认为当前时间
ALTER TABLE ledger_t ADD COLUMN create_user VARCHAR(50) NOT NULL DEFAULT CURRENT_USER; -- 创建人，非空，默认为当前用户
ALTER TABLE ledger_t ADD COLUMN update_time TIMESTAMP WITH TIME ZONE; -- 修改时间，可选
ALTER TABLE ledger_t ADD COLUMN update_user VARCHAR(50); -- 修改人，可选

-- 给账本成员表添加审计字段
ALTER TABLE ledger_member_t ADD COLUMN create_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP; -- 创建时间，非空，默认为当前时间
ALTER TABLE ledger_member_t ADD COLUMN create_user VARCHAR(50) NOT NULL DEFAULT CURRENT_USER; -- 创建人，非空，默认为当前用户
ALTER TABLE ledger_member_t ADD COLUMN update_time TIMESTAMP WITH TIME ZONE; -- 修改时间，可选
ALTER TABLE ledger_member_t ADD COLUMN update_user VARCHAR(50); -- 修改人，可选

-- 给账单表添加审计字段
ALTER TABLE bill_t ADD COLUMN create_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP; -- 创建时间，非空，默认为当前时间
ALTER TABLE bill_t ADD COLUMN create_user VARCHAR(50) NOT NULL DEFAULT CURRENT_USER; -- 创建人，非空，默认为当前用户
ALTER TABLE bill_t ADD COLUMN update_time TIMESTAMP WITH TIME ZONE; -- 修改时间，可选
ALTER TABLE bill_t ADD COLUMN update_user VARCHAR(50); -- 修改人，可选


-- 在 user 表中添加一个名为 account_setting 的 JSONB 类型的字段
ALTER TABLE user_t ADD COLUMN account_setting JSONB COMMENT '用户自定义的账目层级结构的JSON数据';

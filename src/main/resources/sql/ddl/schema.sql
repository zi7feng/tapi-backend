# DROP DATABASE TAPI;

CREATE DATABASE IF NOT EXISTS TAPI;

USE TAPI;

# drop table tapi.User;
-- user table
create table if not exists tapi.User
(
    id             bigint auto_increment comment 'id' primary key,
    user_name       varchar(256)                           null comment 'User Name',
    user_account    varchar(256)                           not null comment 'User Account',
    user_avatar     varchar(1024)                          null comment 'User Avatar',
    email          varchar(256)                           null comment 'email',
    user_role       varchar(256) default 'user'            not null comment 'user Role：user / admin',
    user_password   varchar(512)                           null comment 'user password',
    balance        bigint       default 30                not null comment 'user balance, 30 by default on signing up',
    status         tinyint      default 0                 not null comment 'account status（0- normal 1- blocked）',
    create_time     datetime     default CURRENT_TIMESTAMP not null comment 'create time',
    update_time     datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'update time',
    is_delete       tinyint      default 0                 not null comment 'is delete? 1- deleted',
    constraint uni_userAccount
        unique (user_account)
)
    comment 'User table';

-- Interface information
CREATE TABLE IF NOT EXISTS InterfaceInfo
(
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'primary key',
    name VARCHAR(256) NOT NULL COMMENT 'name',
    description VARCHAR(256) NULL COMMENT 'description',
    url VARCHAR(512) NOT NULL COMMENT 'interface url',
    request_params TEXT NOT NULL COMMENT 'request parameters',
    request_header TEXT NULL COMMENT 'request header',
    response_header TEXT NULL COMMENT 'response header',
    status INT DEFAULT 0 NOT NULL COMMENT 'interface status (0 - closed, 1 - open)',
    method VARCHAR(256) NOT NULL COMMENT 'request method',
    user_id BIGINT NOT NULL COMMENT 'creator user id',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT 'creation time',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'update time',
    is_delete TINYINT DEFAULT 0 NOT NULL COMMENT 'deletion status (0 - not deleted, 1 - deleted)',
    PRIMARY KEY (id)
) COMMENT 'interface information';

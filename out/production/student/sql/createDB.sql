CREATE DATABASE IF NOT EXISTS STUDENT;
USE STUDENT;
CREATE TABLE IF NOT EXISTS `stu`
(
    `id`       BIGINT      NOT NULL AUTO_INCREMENT,
    `name`     VARCHAR(20) NOT NULL,
    `sexual`   VARCHAR(10) NOT NULL,
    `birthday` DATE,
    `classid`  VARCHAR(20) NOT NULL,
    `chin`     INT DEFAULT 0,
    `math`     INT DEFAULT 0,
    `eng`      INT DEFAULT 0,
    `phys`     INT DEFAULT 0,
    `chem`     INT DEFAULT 0,
    `bio`      INT DEFAULT 0,
    `pol`      INT DEFAULT 0,
    `his`      INT DEFAULT 0,
    `geo`      INT DEFAULT 0,
    `sumscore` INT DEFAULT 0,
    PRIMARY KEY (`id`),
    UNIQUE KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1000001
  DEFAULT CHARSET = utf8;
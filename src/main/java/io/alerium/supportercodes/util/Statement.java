package io.alerium.supportercodes.util;

public final class Statement {

    // Statements
    public static final String UPDATE_CREATOR_UUID_DATA = "INSERT INTO `%s`.`%s` (uuid, supporters, support_code_uses) VALUES ('%s', %d, %d) ON DUPLICATE KEY UPDATE supporters=%d, support_code_uses=%d;";
    public static final String UPDATE_SUPPORTER_UUID_DATA = "INSERT INTO `%s`.`%s` (uuid, supported_creator, supporter_since) VALUES ('%s', '%s', %d) ON DUPLICATE KEY UPDATE supported_creator='%s', supporter_since=%d;";

    public static final String DELETE_UUID_DATA = "DELETE FROM `%s`.`%s` WHERE uuid='%s';";
    public static final String SELECT_ALL_FROM_TABLE = "SELECT * FROM `%s`.`%s`";

    public static final String SETUP_DATABASE = "CREATE DATABASE IF NOT EXISTS `%s`;";
    public static final String SETUP_CREATOR_TABLE = "CREATE TABLE IF NOT EXISTS `%s`.`creators` (`uuid` VARCHAR(36) NOT NULL, `supporters` LONG NOT NULL, `support_code_uses` LONG NOT NULL, PRIMARY KEY (`uuid`));";
    public static final String SETUP_SUPPORTER_TABLE = "CREATE TABLE IF NOT EXISTS `%s`.`supporters` (`uuid` VARCHAR(36) NOT NULL, `supported_creator` VARCHAR(36) NULL, `supporter_since` LONG NOT NULL, PRIMARY KEY (`uuid`));";

}

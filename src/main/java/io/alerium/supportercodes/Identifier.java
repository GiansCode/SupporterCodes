package io.alerium.supportercodes;

public final class Identifier {

    // Table names
    public static final String CREATOR_TABLE = "creators";
    public static final String SUPPORTER_TABLE = "supporters";

    // Database update interval path
    public static final String SAVE_DELAY_PATH = "database.saveDelay";

    // Commands
    public static final String BASE_COMMAND = "support";
    public static final String STOP_SUB_COMMAND = "stop";
    public static final String CLEAR_SUB_COMMAND = "clear";
    public static final String HANDLE_SUB_COMMAND = "handle";
    public static final String RELOAD_SUB_COMMAND = "reload";
    public static final String CREATOR_MANAGE_COMMAND = "creator";
    public static final String CREATOR_ADD_COMMAND = "add";
    public static final String CREATOR_REMOVE_COMMAND = "remove";

    // Command Permissions
    public static final String BASE_PERMISSION = "supportercodes.command.main";
    public static final String STOP_PERMISSION = "supportercodes.command.stop";
    public static final String HANDLE_PERMISSION = "supportercodes.command.handle";
    public static final String SUPPORT_PERMISSION = "supportercodes.command.support";
    public static final String SUPPORT_FORCE_PERMISSION = "supportercodes.command.support.force";
    public static final String RELOAD_PERMISSION = "supportercodes.command.reload";
    public static final String CREATOR_ADD_PERMISSION = "supportercodes.command.manage.add";
    public static final String CREATOR_REMOVE_PERMISSION = "supportercodes.command.manage.remove";

    // Message Paths
    public static final String USER_IS_A_CREATOR = "user-is-a-creator";
    public static final String USER_IS_SUPPORTING_CREATOR = "user-is-supporting-creator";
    public static final String USER_IS_NOT_SUPPORT_A_CREATOR = "user-is-not-supporting-a-creator";
    public static final String USER_STOPPED_SUPPORTING_CREATOR = "user-stopped-supporting-creator";
    public static final String INVALID_CREATOR = "invalid-creator";
    public static final String INVALID_FORCE_PLAYER = "invalid-force-player";
    public static final String CREATOR_FORCE_CREATOR = "creator-force-creator";
    public static final String ALREADY_EXISTENT_WRAPPER = "user-wrapper-already-exists";
    public static final String ADDED_A_CREATOR = "added-a-creator";
    public static final String REMOVED_CREATOR = "removed-creator";

    public static final String RELOAD_PLUGIN = "reloaded-plugin";

    public static final String MISSING_PERMISSION = "missing-permission";
    public static final String NON_CONSOLE_COMMAND = "non-console-command";
    public static final String NON_EXISTENT_COMMAND = "non-existent-command";
    public static final String WRONG_COMMAND_USAGE = "wrong-command-usage";

}

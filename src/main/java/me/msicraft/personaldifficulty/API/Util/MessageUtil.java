package me.msicraft.personaldifficulty.API.Util;

import me.msicraft.personaldifficulty.PersonalDifficulty;

public class MessageUtil {

    public static String getPermissionErrorMessage() {
        String s = null;
        if (PersonalDifficulty.messageConfig.getConfig().contains("Permission-Error")) {
            s = PersonalDifficulty.messageConfig.getConfig().getString("Permission-Error");
        }
        return s;
    }

}

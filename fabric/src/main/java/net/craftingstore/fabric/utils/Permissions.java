package net.craftingstore.fabric.utils;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Objects;
import java.util.function.Predicate;

public class Permissions {
    public static Predicate<ServerCommandSource> require(String permission) {
        Objects.requireNonNull(permission, "permission");
        return source -> {
            Objects.requireNonNull(source.getEntity());
            User playerLP = LuckPermsProvider.get().getUserManager().getUser(source.getEntity().getUuid());
            if (playerLP == null) {
                return false;
            }
            return playerLP.getCachedData().getPermissionData().checkPermission(permission).asBoolean();
        };
    }
}

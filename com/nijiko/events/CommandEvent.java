package com.nijiko.events;

import com.nijiko.CLI;
import com.nijiko.Messaging;
import com.nijiko.Misc;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import java.util.ArrayList;

import static com.nijikokun.bukkit.Permissions.Permissions.version;

/**
 * Created by deide on 2016-09-28.
 */
public class CommandEvent implements Listener {
    private Permissions plugin;
    private CLI Commands;
    private PermissionHandler Security;

    public CommandEvent(Permissions plugin) {
        this.plugin = plugin;

        this.Commands = new CLI();
        Commands.add("/pr|perms", "Reload Permissions.");
        Commands.add("/pr|perms -reload|-r +world:all", "Reload World.");

        Security = Permissions.getHandler();
    }

    @EventHandler
    public void onPlayerCommand(PlayerChatEvent event) {
        final Player player = event.getPlayer();
        String message = event.getMessage();

        // Save player.
        Messaging.save(player);

        // Commands
        Commands.save(message);

        // Parsing / Checks
        String base = Commands.base();
        String command = Commands.command();
        ArrayList<Object> variables = Commands.parse();

        if (base != null) {
            if (Misc.isEither(base, "pr", "perms")) {
                if (command == null) {
                    Messaging.send("&7-------[ &fPermissions&7 ]-------");
                    Messaging.send("&7Currently running version: &f" + version);

                    if (Security.permission(player, "permissions.reload")) {
                        Messaging.send("&7Reload with: &f/pr reload");
                    }

                    Messaging.send("&7-------[ &fPermissions&7 ]-------");

                    return;
                }

                if(Misc.isEither(command, "reload", "-r")) {
                    if (Security.permission(player, "permissions.reload")) {
                        String world = Commands.getString("world");

                        if (world.equalsIgnoreCase("all")) {
                            Security.reload();
                            Messaging.send(ChatColor.GRAY + "[Permissions] Reload completed.");
                        } else {
                            if(Security.reload(world)) {
                                Messaging.send(ChatColor.GRAY + "[Permissions] Reload of "+ world +" completed.");
                            } else {
                                Messaging.send("&7[Permissions] "+ world +" does not exist!");
                            }
                        }
                    }

                    return;
                }
            }
        }
    }
}

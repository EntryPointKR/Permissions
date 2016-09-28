package com.nijikokun.bukkit.Permissions;

import com.nijiko.Misc;
import com.nijiko.configuration.ConfigurationHandler;
import com.nijiko.configuration.DefaultConfiguration;
import com.nijiko.events.CommandEvent;
import com.nijiko.permissions.Control;
import com.nijiko.permissions.PermissionHandler;
import org.bukkit.Server;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

/**
 * Permissions 1.x & Code from iConomy 2.x
 * Copyright (C) 2011  Nijikokun <nijikokun@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Permissions Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Permissions Public License for more details.
 *
 * You should have received a copy of the GNU Permissions Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
public class Permissions extends JavaPlugin {
    /*
     * Loggery Foggery
     */

    public static final Logger log = Logger.getLogger("Minecraft");

    /*
     * Central Data pertaining directly to the plugin name & versioning.
     */
    public static String name = "Permissions";
    public static String codename = "Handler";
    public static String version = "2.1";
    /**
     * Controller for permissions and security.
     */
    public static PermissionHandler Security;
    /**
     * Miscellaneous object for various functions that don't belong anywhere else
     */
    public static Misc Misc = new Misc();

    /*
     * Internal Properties controllers
     */
    private DefaultConfiguration config;

    /*
     * Server
     */
    public static Server Server = null;

    private String DefaultWorld = "";

    public void onDisable() {
        log.info("[" + name + "] version [" + version + "] (" + codename + ") Disabled.");
    }

    public Permissions() {
        new File("plugins" + File.separator + "Permissions" + File.separator).mkdirs();

        PropertyHandler server = new PropertyHandler("server.properties");
        DefaultWorld = server.getString("level-name");

        // Attempt
        if (!(new File(getDataFolder(), DefaultWorld + ".yml").exists())) {
            Misc.touch(DefaultWorld + ".yml");
        }

        Configuration configure = YamlConfiguration.loadConfiguration(new File(getDataFolder(), DefaultWorld + ".yml"));

        // Gogo
        this.config = new ConfigurationHandler(configure);

        // Setup Permission
        setupPermissions();

        // Enabled
        log.info("[" + name + "] version [" + version + "] (" + codename + ") was Initialized.");
    }

    public void onEnable() {
        // Server
        Server = getServer();

        // Register
        registerEvents();

        // Enabled
        log.info("[" + name + "] version [" + version + "] (" + codename + ") loaded");
    }

    private void registerEvents() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new CommandEvent(this), this);
    }

    /**
     * Alternative method of grabbing Permissions.Security
     * <br /><br />
     * <blockquote><pre>
     * Permissions.getHandler()
     * </pre></blockquote>
     *
     * @return PermissionHandler
     */
    public static PermissionHandler getHandler() {
        return Security;
    }

    public void setupPermissions() {
        Security = new Control(YamlConfiguration.loadConfiguration(new File("plugins" + File.separator + "Permissions", DefaultWorld + ".yml")));
        Security.setDefaultWorld(DefaultWorld);
        Security.setDirectory(new File("plugins" + File.separator + "Permissions"));
        Security.load();
    }
}

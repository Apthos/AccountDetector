package net.apthos.phobos;

import net.apthos.phobos.files.FileUtils;
import net.apthos.phobos.mysql.MySQL;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class AccountDetector extends JavaPlugin implements Listener {

    private static AccountDetector instance;
    private MySQL SQL;

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
        instance = this;
        FileUtils.createFiles();
        SQL = new MySQL();
    }

    @Override
    public void onDisable() {
    }

    public static AccountDetector getInstance(){
        return instance;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        //Todo: write code to see if user exists in database.
    }
}

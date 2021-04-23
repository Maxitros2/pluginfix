package me.maxitros.fix;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import me.maxitros.dbapi.api.CallBack;
import me.maxitros.fix.api.PublicCallback;
import me.maxitros.fix.runnables.ChangedLocRunnable;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public class FixPlugin extends JavaPlugin implements Listener {
    static FixPlugin plugin;
    static Essentials essentials;
    public static FixPlugin getInstance(){
        return plugin;
    }
    public static Essentials getEssentials(){
        return essentials;
    }
    public void onEnable() {
        plugin = this;
        essentials = (Essentials) Bukkit.getPluginManager().getPlugin("EssentialsX");
        Bukkit.getPluginManager().registerEvents(this, this);
    }
    @EventHandler
    public void OnPlayerTeleport(PlayerTeleportEvent e)
    {
        Player p = e.getPlayer();
        if(p.hasPermission("fixplugin.tp")||p.isOp())
            return;
        if(e.getCause()!=PlayerTeleportEvent.TeleportCause.PLUGIN)
            return;
        Location teleportto = e.getTo();
        Location startlocation = p.getLocation();
        p.sendMessage(new TextComponent(ChatColor.YELLOW + "Начинаю телепортацию, не двигайтесь 3 секунды!"));
        PublicCallback callBack = new PublicCallback() {
            @Override
            public void call(Object o, Throwable throwable) {
                super.call(o,throwable);
                boolean aBoolean = (Boolean)o;
                if(aBoolean){
                    p.teleport(teleportto);
                }else{
                    p.sendMessage(new TextComponent(ChatColor.RED + "Отмена телепортации!"));
                }
            }
        };
        BukkitTask scheduler = Bukkit.getScheduler().runTaskTimer(this,
                new ChangedLocRunnable(callBack, p,startlocation, p.getNoDamageTicks()),20, 60 );
        e.setCancelled(true);
    }
    @EventHandler
    public void HomeCommand(AsyncPlayerChatEvent e)
    {
        if(e.getMessage().equals("/home")){
            Player p = e.getPlayer();
            User user = essentials.getUser(p);
            for(String s : user.getHomes()){
                try {
                    if(!user.getHome(s).getWorld().equals(Bukkit.getServer().getWorld("world"))){
                        user.delHome(s);
                        p.sendMessage(new TextComponent("Удален дом "+ s + " т.к. находится в невозможной локации"));
                }
                }catch (Exception exception){};
            }
        }
    }
    @EventHandler
    public void OnBlockPlace(PlayerBucketEmptyEvent e)
    {
        Player p = e.getPlayer();
        if(!e.getBucket().equals(Material.LAVA_BUCKET))
            return;
        if(essentials.getUser(p).isVanished()){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void OnInventoryInteract(InventoryOpenEvent e)
    {
        if(e.getInventory().contains(Material.BOOK_AND_QUILL))
        {
            e.getInventory().remove(Material.BOOK_AND_QUILL);
        }
    }
}

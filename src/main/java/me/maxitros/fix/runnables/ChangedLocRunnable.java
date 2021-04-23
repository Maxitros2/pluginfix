package me.maxitros.fix.runnables;

import me.maxitros.dbapi.api.CallBack;
import me.maxitros.fix.api.PublicCallback;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ChangedLocRunnable implements Runnable {
    private final PublicCallback callBack;
    private final Player player;
    private final Location location;
    private final int ticks;

    public ChangedLocRunnable(PublicCallback callback, Player player, Location location, int ticks)
    {
        this.callBack = callback;
        this.player = player;
        this.location = location;
        this.ticks = ticks;
    }
    @Override
    public void run() {
        if(callBack.object!=null){
            if(!(Boolean)callBack.object){
                return;
            }
        }
        if(player.getLocation()!=location||player.getNoDamageTicks()!=ticks){
            callBack.call(false, null);
        }
        else {
            callBack.call(true, null);
        }
    }
}

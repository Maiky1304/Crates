package com.github.maiky1304.crates.utils.menu;

import com.github.maiky1304.crates.CratesPlugin;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

@Getter
@RequiredArgsConstructor
public abstract class MenuAnimation extends BukkitRunnable {

    private final CratesPlugin instance;
    private final Menu menu;
    private final int tickTime;
    private final Runnable runnable;

    private BukkitTask task = null; // If null task has not started yet

    @Override
    public void run() {
        this.render();
    }

    /**
     * Finish the animation
     */
    public void finish() {
        if (this.task != null) this.task.cancel();
        this.runnable.run();
    }

    /**
     * Starts the bukkit runnable
     */
    public void start() {
        this.task = this.runTaskTimer(this.instance, 0, tickTime);
    }

    public abstract void render();

}

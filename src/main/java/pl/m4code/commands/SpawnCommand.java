package pl.m4code.commands;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;
import pl.m4code.Main;
import pl.m4code.commands.api.CommandAPI;
import pl.m4code.utils.TextUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpawnCommand extends CommandAPI implements Listener {
    private final Map<Player, Integer> teleportTasks = new HashMap<>();

    public SpawnCommand() {
        super(
                "spawn",
                "",
                "",
                "/spawn",
                List.of("sp")
        );
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        FileConfiguration config = Main.getInstance().getConfig();

        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("m4code.spawn.reload")) {
                sendMessage(sender, config.getString("messages.no_permission"));
                return;
            }
            Main.getInstance().reloadConfig();
            sendMessage(sender, config.getString("messages.reload"));
            return;
        }

        if (!(sender instanceof Player player)) {
            sendMessage(sender, config.getString("messages.only_players"));
            return;
        }

        if (teleportTasks.containsKey(player)) {
            sendMessage(player, config.getString("messages.already_teleporting"));
            return;
        }

        if (!config.contains("spawn.world") || !config.contains("spawn.x") || !config.contains("spawn.y") || !config.contains("spawn.z")) {
            sendMessage(player, config.getString("messages.spawn_not_set"));
            return;
        }

        if (player.hasPermission("m4code.bypass")) {
            teleportPlayer(player);
        } else {
            startTeleport(player);
        }
    }

    private void startTeleport(Player player) {
        FileConfiguration config = Main.getInstance().getConfig();

        if (!Main.getInstance().isEnabled()) {
            sendMessage(player, config.getString("messages.plugin_disabled"));
            return;
        }

        int teleportTime = config.getInt("counter.time");
        String title = TextUtil.fixColor(config.getString("counter.title_constant"));
        final int[] taskId = new int[1]; // Use an array to hold the taskId
        taskId[0] = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
            int timeLeft = teleportTime;

            @Override
            public void run() {
                if (timeLeft <= 0) {
                    teleportPlayer(player);
                    teleportTasks.remove(player);
                    Bukkit.getScheduler().cancelTask(taskId[0]);
                } else {
                    String subtitle = config.getString("counter.title").replace("%time%", String.valueOf(timeLeft));
                    player.sendTitle(title, TextUtil.fixColor(subtitle), 0, 20, 0);
                    player.playSound(player.getLocation(), Sound.valueOf(config.getString("sounds.teleport_countdown")), 1.0f, 1.0f);
                    timeLeft--;
                }
            }
        }, 0L, 20L);

        teleportTasks.put(player, taskId[0]);
    }

    private void teleportPlayer(Player player) {
        FileConfiguration config = Main.getInstance().getConfig();
        String worldName = config.getString("spawn.world");
        double x = config.getDouble("spawn.x");
        double y = config.getDouble("spawn.y");
        double z = config.getDouble("spawn.z");
        float yaw = (float) config.getDouble("spawn.yaw");
        float pitch = (float) config.getDouble("spawn.pitch");

        Location spawnLocation = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
        player.teleport(spawnLocation);
        sendMessage(player, config.getString("messages.teleport_success"));

        String title = TextUtil.fixColor(config.getString("counter.title_constant"));

        // Send subtitle message
        player.sendTitle(title, TextUtil.fixColor(config.getString("messages.teleport_success")), 10, 30, 20);
        player.playSound(player.getLocation(), Sound.valueOf(config.getString("sounds.teleport_success")), 1.0f, 1.0f);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (teleportTasks.containsKey(player)) {
            Location from = event.getFrom();
            Location to = event.getTo();
            if (from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ()) {
                cancelTeleport(player);
            }
        }
    }

    private void cancelTeleport(Player player) {
        FileConfiguration config = Main.getInstance().getConfig();
        int taskId = teleportTasks.remove(player);
        Bukkit.getScheduler().cancelTask(taskId);
        String title = TextUtil.fixColor(config.getString("counter.title_constant"));
        player.sendTitle(title, TextUtil.fixColor(config.getString("messages.teleport_cancelled")), 10, 30, 20);
        sendMessage(player, config.getString("messages.teleport_cancelled"));
        player.playSound(player.getLocation(), Sound.valueOf(config.getString("sounds.teleport_cancelled")), 1.0f, 1.0f);
    }

    private void sendMessage(CommandSender sender, String message) {
        if (sender instanceof Player player) {
            player.sendTitle("", TextUtil.fixColor(message), 10, 30, 20);
        }
        TextUtil.sendMessage(sender, message);
    }

    @Override
    public List<String> tab(@NonNull Player player, @NotNull @NonNull String[] args) {
        return null;
    }
}
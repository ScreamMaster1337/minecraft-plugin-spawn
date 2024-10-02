package pl.m4code.commands;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.m4code.Main;
import pl.m4code.commands.api.CommandAPI;
import pl.m4code.utils.TextUtil;

import java.util.List;

public class SetSpawnCommand extends CommandAPI {

    public SetSpawnCommand() {
        super(
                "setspawn",
                "",
                "",
                "/setspawn",
                List.of("ssp")
        );
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            TextUtil.sendMessage(sender, "&cPodana komenda jest dostępna tylko dla graczy!");
            return;
        }

        if (!player.hasPermission("m4code.setspawn")) {
            TextUtil.sendMessage(player, "&cNie masz uprawnień do używania tej komendy!");
            return;
        }

        setSpawnLocation(player);
    }

    private void setSpawnLocation(Player player) {
        Location location = player.getLocation();
        FileConfiguration config = Main.getInstance().getConfig();

        config.set("spawn.world", location.getWorld().getName());
        config.set("spawn.x", location.getX());
        config.set("spawn.y", location.getY());
        config.set("spawn.z", location.getZ());
        config.set("spawn.yaw", location.getYaw());
        config.set("spawn.pitch", location.getPitch());

        Main.getInstance().saveConfig();

        TextUtil.sendMessage(player, "&aSpawn został ustawiony na twojej obecnej pozycji!");
    }

    @Override
    public List<String> tab(@NonNull Player player, @NotNull @NonNull String[] args) {
        return null;
    }
}
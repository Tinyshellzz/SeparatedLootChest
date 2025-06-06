package tcc.youajing.tcctools.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import tcc.youajing.tcctools.TccTools;

import java.util.Random;

import static tcc.youajing.tcctools.ObjectPool.mcPlayerMapper;

/**
 * 新玩家欢迎
 * 给新玩家一个床
 */
public class PlayerJoinListener implements Listener {
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPlayedBefore()) {
            // Cancel the default join message
            event.setJoinMessage(null);

            // 随机选择一种床颜色
            Material randomBedColor = getRandomBedColor();
            // 给玩家添加随机颜色的床
            player.getInventory().addItem(new ItemStack(randomBedColor, 1));
            // 欢迎消息
            Component welcomeMessage = miniMessage.deserialize("<bold><rainbow>大萌新『<underlined>" + player.getName() + "</underlined>』驾到，通通闪开!!!</rainbow></bold>");
            // 发送欢迎消息给所有在线玩家
            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.sendMessage(welcomeMessage));

            TextComponent msg2 = new TextComponent(ChatColor.GREEN + ">>点击欢迎玩新家<<" + player.getName());
            msg2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/welcome " + player.getName()));
            // 发送欢迎消息给所有在线玩家
            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.sendMessage(msg2));
        }

        // 创建玩家数据
        mcPlayerMapper.update_player_name(player);
    }

    private Material getRandomBedColor() {
        Material[] bedColors = {
                Material.WHITE_BED,
                Material.ORANGE_BED,
                Material.MAGENTA_BED,
                Material.LIGHT_BLUE_BED,
                Material.YELLOW_BED,
                Material.LIME_BED,
                Material.PINK_BED,
                Material.GRAY_BED,
                Material.LIGHT_GRAY_BED,
                Material.CYAN_BED,
                Material.PURPLE_BED,
                Material.BLUE_BED,
                Material.BROWN_BED,
                Material.GREEN_BED,
                Material.RED_BED,
                Material.BLACK_BED
        };
        Random random = new Random();
        return bedColors[random.nextInt(bedColors.length)];
    }
}

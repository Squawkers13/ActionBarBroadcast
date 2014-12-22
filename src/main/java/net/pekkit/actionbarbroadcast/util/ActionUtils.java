/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2014 Squawkers13 <Squawkers13@pekkit.net>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.pekkit.actionbarbroadcast.util;

import java.lang.reflect.Field;
import net.minecraft.server.v1_8_R1.ChatSerializer;
import net.minecraft.server.v1_8_R1.EnumTitleAction;
import net.minecraft.server.v1_8_R1.IChatBaseComponent;
import net.minecraft.server.v1_8_R1.PacketPlayOutChat;
import net.minecraft.server.v1_8_R1.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R1.PlayerConnection;
import org.bukkit.ChatColor;

import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ActionUtils {

    public static void sendTitle(Player player, String title, String subtitle) {
        CraftPlayer craftplayer = (CraftPlayer) player;
        PlayerConnection connection = craftplayer.getHandle().playerConnection;
        IChatBaseComponent titleJSON = ChatSerializer.a("{'text': '" + ChatColor.translateAlternateColorCodes('&', title).replace("'", "\'") + "'}");
        PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(EnumTitleAction.TITLE, titleJSON, Constants.TITLE_FADE, Constants.TITLE_STAY, Constants.TITLE_FADE);
        connection.sendPacket(titlePacket);
    }

    public static void sendActionBar(Player p, String msg) {
        IChatBaseComponent cbc = ChatSerializer.a("{\"text\": \"" + ChatColor.translateAlternateColorCodes('&', msg) + "\"}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(ppoc);
    }
    
    public static void sendHeaderAndFooter(Player p, String head, String foot) {
        CraftPlayer craftplayer = (CraftPlayer) p;
        PlayerConnection connection = craftplayer.getHandle().playerConnection;
        IChatBaseComponent header = ChatSerializer.a("{'color': '" + "', 'text': '" + ChatColor.translateAlternateColorCodes('&', head).replace("'", "\'") + "'}");
        IChatBaseComponent footer = ChatSerializer.a("{'color': '" + "', 'text': '" + ChatColor.translateAlternateColorCodes('&', foot).replace("'", "\'") + "'}");
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();

        try {
            Field headerField = packet.getClass().getDeclaredField("a");
            headerField.setAccessible(true);
            headerField.set(packet, header);
            headerField.setAccessible(!headerField.isAccessible());

            Field footerField = packet.getClass().getDeclaredField("b");
            footerField.setAccessible(true);
            footerField.set(packet, footer);
            footerField.setAccessible(!footerField.isAccessible());
        } catch (Exception e) {
            e.printStackTrace();
        }
        connection.sendPacket(packet);
    }
}

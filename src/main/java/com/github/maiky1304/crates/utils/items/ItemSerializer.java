package com.github.maiky1304.crates.utils.items;

import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@UtilityClass
public class ItemSerializer {

    /**
     * Serialize an item to Base64
     * @param itemStack
     * @return base64 string or null
     */
    public static String toBase64(ItemStack itemStack) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOut = new BukkitObjectOutputStream(out);

            dataOut.writeObject(itemStack);
            dataOut.close();

            return Base64Coder.encodeLines(out.toByteArray());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    /**
     * Read a Base64 string and retrieve the ItemStack object from it.
     * @param base64
     * @return itemstack or null
     */
    public static ItemStack fromBase64(String base64) {
        try {
            ByteArrayInputStream stream = new ByteArrayInputStream(Base64Coder.decodeLines(base64));
            BukkitObjectInputStream dataIn = new BukkitObjectInputStream(stream);

            ItemStack itemStack = (ItemStack) dataIn.readObject();
            dataIn.close();

            return itemStack;
        } catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
        return null;
    }

}

package com.example.colorformat;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = ColorFormatMod.MODID, name = ColorFormatMod.NAME, version = ColorFormatMod.VERSION)
public class ColorFormatMod {
    public static final String MODID = "colorformat";
    public static final String NAME = "Color Format Mod";
    public static final String VERSION = "1.0";

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        logger.info("ColorFormatMod preInit");
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        logger.info("ColorFormatMod init");
        MinecraftForge.EVENT_BUS.register(this);
    }

    private String formatText(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        
        char colorChar = '&';
        char[] b = text.toCharArray();
        
        for (int i = 0; i < b.length - 1; i++) {
            if (b[i] == colorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1) {
                b[i] = '\u00A7';
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }
        
        return new String(b);
    }

    @SubscribeEvent
    public void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();
        
        if (!left.isEmpty() && !event.getName().isEmpty()) {
            ItemStack output = left.copy();
            output.setStackDisplayName(formatText(event.getName()));
            event.setOutput(output);
            event.setCost(1); 
        }
    }
    
    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.hasDisplayName()) {
            String displayName = stack.getDisplayName();
            if (displayName.contains("&")) {
                stack.setStackDisplayName(formatText(displayName));
            }
        }
    }
    
    @SubscribeEvent
    public void onServerChat(ServerChatEvent event) {
        String message = event.getMessage();
        if (message.contains("&") && (event.getPlayer().isCreative() || event.getPlayer().canUseCommand(2, "colorformat"))) {
            event.setComponent(TextComponentUtils.fromFormattedString(formatText(message)));
        }
    }
}

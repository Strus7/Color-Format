package com.example.colorformat;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class TextComponentUtils {
    private static TextFormatting getFormatting(char c) {
        for (TextFormatting format : TextFormatting.values()) {
            if (format.toString().charAt(1) == Character.toLowerCase(c)) {
                return format;
            }
        }
        return null;
    }
    
    public static ITextComponent fromFormattedString(String formatted) {
        TextComponentString result = new TextComponentString("");
        StringBuilder builder = new StringBuilder();
        TextComponentString current = null;
        TextFormatting format = null;
        
        for (int i = 0; i < formatted.length(); i++) {
            char c = formatted.charAt(i);
            
            if (c == '\u00A7' && i + 1 < formatted.length()) {
                char formatChar = formatted.charAt(i + 1);
                format = getFormatting(formatChar);
                
                if (format != null) {
                    if (!builder.toString().isEmpty()) {
                        if (current == null) {
                            current = new TextComponentString(builder.toString());
                            result.appendSibling(current);
                        } else {
                            current.appendText(builder.toString());
                        }
                        builder = new StringBuilder();
                    }
                    
                    current = new TextComponentString("");
                    current.getStyle().setColor(format);
                    result.appendSibling(current);
                    i++;
                    continue;
                }
            }
            
            builder.append(c);
        }
        
        if (!builder.toString().isEmpty()) {
            if (current == null) {
                current = new TextComponentString(builder.toString());
                result.appendSibling(current);
            } else {
                current.appendText(builder.toString());
            }
        }
        
        return result;
    }
}
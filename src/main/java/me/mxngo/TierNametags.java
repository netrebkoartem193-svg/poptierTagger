package net.mxngo.tiernametags;

import net.fabricmc.api.ClientModInitializer;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class TierNametags implements ClientModInitializer {
    https://rentry.co/poptier123/raw
    public static final String RENTRY_URL = "https://rentry.co/poptier123/raw";
    public static final Map<String, String> PLAYER_TIERS = new HashMap<>();

    @Override
    public void onInitializeClient() {
        loadTiers();
    }

    public static void loadTiers() {
        new Thread(() -> {
            try {
                URL url = new URL(https://rentry.co/poptier123/raw);
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                String line;
                PLAYER_TIERS.clear();
                while ((line = reader.readLine()) != null) {
                    if (line.contains(":")) {
                        String[] parts = line.split(":", 2);
                        PLAYER_TIERS.put(parts[0].trim().toLowerCase(), parts[1].trim());
                    }
                }
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static String getPlayerTier(String username) {
        return PLAYER_TIERS.getOrDefault(username.toLowerCase(), "");
    }
}

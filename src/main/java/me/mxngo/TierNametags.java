package me.mxngo;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TierNametags implements ClientModInitializer {
    public static final String RENTRY_URL = "https://rentry.co/poptier123/raw";
    public static final Map<String, String> PLAYER_TIERS = new HashMap<>();
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Override
    public void onInitializeClient() {
        loadTiers();
        scheduler.scheduleAtFixedRate(TierNametags::loadTiers, 5, 5, TimeUnit.MINUTES);

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            loadTiers();
        });
    }

    public static void loadTiers() {
        new Thread(() -> {
            try {
                URL url = new URL(RENTRY_URL);
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                String line;
                Map<String, String> newTiers = new HashMap<>();

                while ((line = reader.readLine()) != null) {
                    if (line.contains(":")) {
                        String[] parts = line.split(":", 2);
                        newTiers.put(parts[0].trim().toLowerCase(), parts[1].trim());
                    }
                }
                reader.close();

                synchronized (PLAYER_TIERS) {
                    PLAYER_TIERS.clear();
                    PLAYER_TIERS.putAll(newTiers);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static String getPlayerTier(String username) {
        synchronized (PLAYER_TIERS) {
            return PLAYER_TIERS.getOrDefault(username.toLowerCase(), "");
        }
    }
}

package me.mastercapexd.auth.hooks.nanolimbo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import ua.nanit.limbo.server.data.InfoForwarding;
import ua.nanit.limbo.server.data.InfoForwarding.Type;

// Class for hiding InfoForwarding dirty way initialization
public class InfoForwardingFactory {

    public InfoForwarding none() {
        Map<String, Object> map = Collections.singletonMap("type", Type.NONE);
        return createForwarding(map);
    }

    public InfoForwarding legacy() {
        Map<String, Object> map = Collections.singletonMap("type", Type.LEGACY);
        return createForwarding(map);
    }

    public InfoForwarding modern(byte[] secretKey) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", Type.MODERN);
        map.put("secretKey", secretKey);
        return createForwarding(map);
    }

    public InfoForwarding bungeeGuard(Collection<String> tokens) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", Type.BUNGEE_GUARD);
        map.put("tokens", new ArrayList<>(tokens));
        return createForwarding(map);
    }

    private InfoForwarding createForwarding(Map<String, Object> map) {
        InfoForwarding forwarding = new InfoForwarding();
        for (Entry<String, Object> entry : map.entrySet()) {
            Class<?> clazz = forwarding.getClass();
            try {
                Field field = clazz.getDeclaredField(entry.getKey());
                field.setAccessible(true);
                field.set(forwarding, entry.getValue());
            } catch (NoSuchFieldException | IllegalAccessException ignored) {
            }
        }
        return forwarding;
    }

}
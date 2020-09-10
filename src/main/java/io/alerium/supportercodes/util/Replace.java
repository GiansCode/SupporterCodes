package io.alerium.supportercodes.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Replace {

    public static List<String> replaceList(final List<String> input, String... values) {
        if (values == null) {
            return input;
        }

        final Map<String, String> replacements = getReplacements(values);

        final List<String> replacedLore = new ArrayList<>();
        for (String line : input) {
            for (String key : replacements.keySet()) {
                line = line.replace(key, replacements.get(key));
            }
            replacedLore.add(line);
        }
        return replacedLore;
    }

    public static String replaceString(final String input, final String... values) {
        if (values == null) {
            return input;
        }

        final Map<String, String> replacements = getReplacements(values);
        String replacedString = input;
        for (String key : replacements.keySet()) {
            replacedString = replacedString.replace(key, replacements.get(key));
        }
        return replacedString;
    }

    private static Map<String, String> getReplacements(final String... values) {
        final Map<String, String> replacements = new HashMap<>();
        for (int i = 0; i < values.length - 1; i += 2) {
            final String key = values[i];
            final String value = values[i + 1];
            replacements.put(key, value);
        }

        return replacements;
    }

}

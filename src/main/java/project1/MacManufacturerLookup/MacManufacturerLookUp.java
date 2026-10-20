package project1.MacManufacturerLookup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;

public class MacManufacturerLookUp {

    private final HashMap<String,String> manufacturers = new HashMap<>();

    public void loadRegistry()throws IOException {
        manufacturers.clear();
        InputStream input = getClass()
                .getResourceAsStream("/oui36.csv");

        if (input == null) {
            throw new IOException("Could not find oui36.csv");
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(input, StandardCharsets.UTF_8))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] columns = line.split(
                        ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1
                );

                if (columns.length < 3) {
                    continue;
                }

                String registry = cleanCsvValue(columns[0]);
                String assignment = cleanCsvValue(columns[1])
                        .toUpperCase(Locale.ROOT);
                String organisation = cleanCsvValue(columns[2]);

                int expectedLength = switch (registry) {
                    case "MA-L" -> 6;
                    case "MA-M" -> 7;
                    case "MA-S" -> 9;
                    default -> 0;
                };

                // Matches compared to all MA-L MA-M and MA-S
                if (assignment.matches("[0-9A-F]+")
                        && assignment.length() == expectedLength) {
                    manufacturers.put(assignment, organisation);
                }
            }
        }
    }
    private String cleanCsvValue(String value) {
        value = value.trim();

        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
        }

        return value.replace("\"\"", "\"");
    }

    public String lookupManufacturer(String macAddress) {
        String normalisedMac = macAddress
                .replace(":", "")
                .replace("-", "")
                .replace(".", "")
                .toUpperCase(Locale.ROOT);

        if (!normalisedMac.matches("[0-9A-F]{12}")) {
            throw new IllegalArgumentException(
                    "Invalid MAC address: " + macAddress
            );
        }

        for (int length : new int[]{9, 7, 6}) {
            String prefix = normalisedMac.substring(0, length);

            String manufacturer = manufacturers.get(prefix);

            if (manufacturer != null) {
                return manufacturer;
            }
        }return "Unknown manufacturer";
    }
}



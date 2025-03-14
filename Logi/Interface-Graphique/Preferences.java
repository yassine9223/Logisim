import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Preferences {

    private static final String PREFERENCES_FILE = "preferences.properties";
    private static final int MIN_ZOOM = 50;   // Limite inférieure du zoom
    private static final int MAX_ZOOM = 200;  // Limite supérieure du zoom

    // Créer le fichier si nécessaire
    static {
        File file = new File(PREFERENCES_FILE);
        if (!file.exists()) {
            try {
                file.createNewFile();  // Créer le fichier s'il n'existe pas
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void save(String theme, int zoom) {
        // Limiter le zoom entre MIN_ZOOM et MAX_ZOOM
        zoom = Math.max(MIN_ZOOM, Math.min(MAX_ZOOM, zoom));
        
        Properties properties = new Properties();
    
        // Ajouter les préférences dans les propriétés
        properties.setProperty("theme", theme);
        properties.setProperty("zoom", String.valueOf(zoom));
    
        try (FileOutputStream outputStream = new FileOutputStream(PREFERENCES_FILE)) {
            // Sauvegarder dans le fichier
            properties.store(outputStream, "Préférences de l'application");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getTheme() {
        return getPreference("theme", "Clair");  // Valeur par défaut "Clair"
    }

    public static int getZoom() {
        String zoom = getPreference("zoom", String.valueOf(MIN_ZOOM));  // Valeur par défaut "50"
        return Integer.parseInt(zoom);
    }

    private static String getPreference(String key, String defaultValue) {
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream(PREFERENCES_FILE)) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties.getProperty(key, defaultValue);
    }
}

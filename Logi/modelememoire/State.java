package modelememoire;

public enum State {
    LOW,       // Signal bas (0) → Représente un état OFF
    HIGH,      // Signal haut (1) → Représente un état ON
    UNDEFINED, // Signal indéterminé → L'entrée n'est pas encore définie
    ERROR      // Erreur dans le circuit → Mauvaise connexion ou état invalide
}

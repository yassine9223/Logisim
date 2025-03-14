#!/bin/bash
echo "📢 Compilation en cours..."

# Définir le chemin vers le SDK JavaFX
JAVAFX_PATH=~/javafx-sdk-21.0.6/lib

# Créer le dossier 'bin' s'il n'existe pas
mkdir -p bin

# Compilation des fichiers Java
javac --module-path "$JAVAFX_PATH" --add-modules javafx.controls,javafx.graphics,javafx.fxml -d bin *.java Composants/*.java Interface-Graphique/*.java

# Vérification des erreurs de compilation
if [ $? -ne 0 ]; then
    echo "❌ Erreur de compilation."
    exit 1
fi

echo "✅ Compilation réussie !"
echo "🚀 Exécution du programme..."

# Lancer l'application JavaFX
java --module-path "$JAVAFX_PATH" --add-modules javafx.controls,javafx.graphics,javafx.fxml -cp bin Main

# Pause pour éviter la fermeture immédiate
read -p "Appuyez sur Entrée pour quitter..."


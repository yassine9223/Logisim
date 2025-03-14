@echo off
echo Compilation en cours...

rem Définir le chemin complet vers JavaFX SDK
set JAVAFX_PATH=C:\Users\adem\Documents\LogiL2\javafx-sdk23\lib\

rem Créer le dossier bin s'il n'existe pas
if not exist bin mkdir bin

rem Compilation de tous les fichiers .java dans les sous-dossiers
javac --module-path %JAVAFX_PATH% --add-modules javafx.controls,javafx.graphics,javafx.fxml -d bin Main.java Interface-Graphique\*.java Composants\*.java ../modelememoire\*.java

rem Vérification d'erreur de compilation
if %errorlevel% neq 0 (
    echo Erreur de compilation.
    exit /b %errorlevel%
)
echo Compilation réussie !
echo.

echo Exécution du programme...
cd bin
java --module-path %JAVAFX_PATH% --add-modules javafx.controls,javafx.graphics,javafx.fxml Main
cd ..
pause

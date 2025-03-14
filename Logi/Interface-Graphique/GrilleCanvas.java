import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;

public class GrilleCanvas extends Canvas {
    private static final int TAILLE_GRILLE = 30;
    private double offsetX = 0;
    private double offsetY = 0;
    private final Scale scale;
    private double currentWidth;
    private double currentHeight;

    public GrilleCanvas(int largeur, int hauteur, Scale scale) {
        super(largeur, hauteur);
        this.getStyleClass().add("grille-canvas");
        this.scale = scale;
        this.currentWidth = largeur;
        this.currentHeight = hauteur;
        dessinerGrille();
    }

    public void updateWidth(double width) {
        this.currentWidth = width;
        super.setWidth(width);
    }

    public void updateHeight(double height) {
        this.currentHeight = height;
        super.setHeight(height);
    }

    public void redessinerGrille() {
        dessinerGrille();
    }

    private void dessinerGrille() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.setFill(Color.GRAY);

        double taillePoint = 2 * scale.getX();
        
        int nbPointsX = (int) (currentWidth / (TAILLE_GRILLE * scale.getX())) + 2;
        int nbPointsY = (int) (currentHeight / (TAILLE_GRILLE * scale.getY())) + 2;

        double startX = (offsetX % (TAILLE_GRILLE * scale.getX()));
        double startY = (offsetY % (TAILLE_GRILLE * scale.getY()));

        for (int i = -1; i < nbPointsX; i++) {
            for (int j = -1; j < nbPointsY; j++) {
                double x = startX + i * TAILLE_GRILLE * scale.getX();
                double y = startY + j * TAILLE_GRILLE * scale.getY();
                
                if (x >= -taillePoint && x <= currentWidth + taillePoint &&
                    y >= -taillePoint && y <= currentHeight + taillePoint) {
                    gc.fillOval(x - taillePoint / 2, y - taillePoint / 2, taillePoint, taillePoint);
                }
            }
        }
    }

    public void deplacer(double deltaX, double deltaY) {
        offsetX += deltaX;
        offsetY += deltaY;
        dessinerGrille();
    }

    public void setScale(double scaleX, double scaleY) {
        scale.setX(scaleX);
        scale.setY(scaleY);
        dessinerGrille();
    }
}
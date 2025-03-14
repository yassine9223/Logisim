import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import modelememoire.PorteNot;
import modelememoire.State;

public class PorteNotView extends ComposantView implements CircuitComponent, HasLogique {

    private PorteNot modeleLogique;  // L'objet logique associé à la porte NOT

    public PorteNotView(double x, double y) {
        super(x, y, 1, 1);
        this.modeleLogique = new PorteNot(x, y);
        dessinerComposant();
        dessinerPorts();
    }

    @Override
    protected void dessinerComposant() {
        // Affichage de débogage
        System.out.println("Dessin de la porte NOT à (" + getLayoutX() + ", " + getLayoutY() + ")");
        
        // Création du triangle pour représenter la porte NOT
        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(
            90.0, 40.0,  // Pointe droite (sortie)
            30.0, 15.0,  // Coin supérieur gauche
            30.0, 65.0   // Coin inférieur gauche
        );
        triangle.setFill(Color.TRANSPARENT);
        triangle.setStroke(Color.BLACK);
        triangle.setStrokeWidth(4);

        // Création du cercle d'inversion (sortie)
        Circle cercle = new Circle(100, 40, 6);
        cercle.setFill(Color.WHITE);
        cercle.setStroke(Color.BLACK);
        cercle.setStrokeWidth(4);

        // Ligne d'entrée
        Line ligneEntree = new Line(10, 40, 30, 40);
        ligneEntree.setStroke(Color.BLACK);
        ligneEntree.setStrokeWidth(4);

        // Ligne de sortie
        Line ligneSortie = new Line(100, 40, 110, 40);
        ligneSortie.setStroke(Color.BLACK);
        ligneSortie.setStrokeWidth(4);

        getChildren().addAll(ligneEntree, triangle, cercle, ligneSortie);
    }

    @Override
    protected void dessinerPorts() {
        entrees.clear();
        sorties.clear();

        // Port d'entrée
        Circle entree = new Circle(10, 40, 6, Color.BLUE);
        entrees.add(entree);
        getChildren().add(entree);

        // Port de sortie
        Circle sortie = new Circle(110, 40, 6, Color.RED);
        sorties.add(sortie);
        getChildren().add(sortie);
    }

    @Override
    public CircuitComponent clone() throws CloneNotSupportedException {
        return new PorteNotView(getPosition().getX(), getPosition().getY());
    }

    @Override
    public void reset() {
        modeleLogique.reset();
        if (!sorties.isEmpty()) {
            sorties.get(0).setFill(Color.GRAY);
        }
    }

    @Override
    public void evaluate() {
        State s = modeleLogique.evaluerSignal();
        if (!sorties.isEmpty()) {
            Circle c = sorties.get(0);
            if (s == State.HIGH) {
                c.setFill(Color.RED);
            } else if (s == State.LOW) {
                c.setFill(Color.BLUE);
            } else {
                c.setFill(Color.GRAY);
            }
        }
    }

    @Override
    public void initialize() {
        modeleLogique.reset();
    }

    // La méthode rotate() n'est pas redéfinie ici, ce qui signifie qu'elle utilisera l'implémentation par défaut de ComposantView.
    // Si une rotation spécifique est souhaitée pour une porte NOT, vous pouvez la surcharger ici.

    @Override
    public modelememoire.Composant getLogique() {
        return modeleLogique;
    }

    @Override
    public String getName() {
        return modeleLogique.getUID();
    }
}

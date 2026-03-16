import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;
 
/**
 * Canvas is a class to allow for simple graphical drawing on a canvas.
 * This is a modification of the general purpose Canvas, specially made for
 * the BlueJ "shapes" example.
 * Extension: se agrego drawText() para poder dibujar texto (numeros de la Grid).
 *
 * @author: Bruce Quig
 * @author: Michael Kolling (mik)
 * @author: Bryan Pinzon y Valentina Prieto (extension drawText)
 * @version: 1.6 (shapes) + drawText
 */
public class Canvas {
 
    private static Canvas canvasSingleton;
 
    public static Canvas getCanvas() {
        if (canvasSingleton == null) {
            canvasSingleton = new Canvas("BlueJ Shapes Demo", 1000, 1000, Color.white);
        }
        canvasSingleton.setVisible(true);
        return canvasSingleton;
    }
 
    private JFrame frame;
    private CanvasPane canvas;
    private Graphics2D graphic;
    private Color backgroundColour;
    private Image canvasImage;
    private List<Object> objects;
    private HashMap<Object, ShapeDescription> shapes;
 
    private Canvas(String title, int width, int height, Color bgColour) {
        frame = new JFrame();
        canvas = new CanvasPane();
        frame.setContentPane(canvas);
        frame.setTitle(title);
        canvas.setPreferredSize(new Dimension(width, height));
        backgroundColour = bgColour;
        frame.pack();
        objects = new ArrayList<>();
        shapes  = new HashMap<>();
    }
 
    public void setVisible(boolean visible) {
        if (graphic == null) {
            Dimension size = canvas.getSize();
            canvasImage = canvas.createImage(size.width, size.height);
            graphic = (Graphics2D) canvasImage.getGraphics();
            graphic.setColor(backgroundColour);
            graphic.fillRect(0, 0, size.width, size.height);
            graphic.setColor(Color.black);
        }
        frame.setVisible(visible);
    }
 
    public void draw(Object referenceObject, String color, Shape shape) {
        objects.remove(referenceObject);
        objects.add(referenceObject);
        shapes.put(referenceObject, new ShapeDescription(shape, color));
        redraw();
    }
 
    /**
     * Dibuja un String en el canvas en la posicion (x, y).
     * Unico metodo agregado al Canvas original, necesario para
     * mostrar los numeros de centimetros en la Grid.
     * @param referenceObject objeto de referencia para identidad
     * @param texto           texto a mostrar
     * @param x               posicion x en pixels
     * @param y               posicion y en pixels
     */
    public void drawText(Object referenceObject, String texto, int x, int y) {
        objects.remove(referenceObject);
        objects.add(referenceObject);
        shapes.put(referenceObject, new TextDescription(texto, x, y));
        redraw();
    }
 
    public void erase(Object referenceObject) {
        objects.remove(referenceObject);
        shapes.remove(referenceObject);
        redraw();
    }
 
    public void setForegroundColor(String colorString) {
        if      (colorString.equals("red"))     graphic.setColor(Color.red);
        else if (colorString.equals("black"))   graphic.setColor(Color.black);
        else if (colorString.equals("blue"))    graphic.setColor(Color.blue);
        else if (colorString.equals("yellow"))  graphic.setColor(Color.yellow);
        else if (colorString.equals("green"))   graphic.setColor(Color.green);
        else if (colorString.equals("magenta")) graphic.setColor(Color.magenta);
        else if (colorString.equals("white"))   graphic.setColor(Color.white);
        else                                    graphic.setColor(Color.black);
    }
 
    public void wait(int milliseconds) {
        try { Thread.sleep(milliseconds); } catch (Exception e) {}
    }
 
    private void redraw() {
        erase();
        for (Iterator i = objects.iterator(); i.hasNext();) {
            shapes.get(i.next()).draw(graphic);
        }
        canvas.repaint();
    }
 
    private void erase() {
        Color original = graphic.getColor();
        graphic.setColor(backgroundColour);
        Dimension size = canvas.getSize();
        graphic.fill(new java.awt.Rectangle(0, 0, size.width, size.height));
        graphic.setColor(original);
    }
 
    private class CanvasPane extends JPanel {
        public void paint(Graphics g) {
            g.drawImage(canvasImage, 0, 0, null);
        }
    }
 
    private class ShapeDescription {
        private Shape shape;
        private String colorString;
 
        public ShapeDescription(Shape shape, String color) {
            this.shape = shape;
            colorString = color;
        }
 
        public void draw(Graphics2D graphic) {
            setForegroundColor(colorString);
            graphic.draw(shape);
            graphic.fill(shape);
        }
    }
 
    /**
     * Descripcion de texto: dibuja un String directamente en el canvas.
     * Usa graphic.drawString() en lugar de graphic.fill().
     * Siempre en color negro, fuente Arial Bold 10pt.
     */
    private class TextDescription extends ShapeDescription {
        private String texto;
        private int tx;
        private int ty;
 
        public TextDescription(String texto, int x, int y) {
            super(new java.awt.Rectangle(x, y - 10, texto.length() * 7, 12), "black");
            this.texto = texto;
            this.tx    = x;
            this.ty    = y;
        }
 
        @Override
        public void draw(Graphics2D graphic) {
            graphic.setColor(Color.black);
            graphic.setFont(new Font("Arial", Font.BOLD, 10));
            graphic.drawString(texto, tx, ty);
        }
    }
}

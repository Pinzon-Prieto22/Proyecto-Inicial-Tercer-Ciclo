/**
 * Clase abstracta que representa cualquier elemento apilable: Cup o Lid.
 *
 * Cosas en comun de los cups y las lids:
 *   - numero, color, COLORES[], PX_POR_CM
 *   - buscaNumItem(), getColor()
 *
 * @author Bryan Pinzon y Valentina Prieto
 * @version CICLO 3
 */
public abstract class StackItem {
    protected static final String[] COLORES = {"red", "blue", "green", "yellow", "magenta"};
    public static final int PX_POR_CM = 10; // para hacer las conversiones de cm a px
    // Posicion donde se debe poner en el tablero por defecto
    protected static final int DEFAULT_X = 70;
    protected static final int DEFAULT_Y = 15;
 
    protected int numero;
    protected String color;
 
    /**
     * Constructor
     */
    public StackItem(int numero) {
        this.numero = numero;
        this.color  = COLORES[(numero - 1) % COLORES.length];// numero-1 es para que sea acorde al arreglo y el modulo lo usamos para poder ir repitiendo colores
    }
 
    /** 
     * Nos da el numero identificador del item 
     */
    public int buscaNumItem() { 
        return numero; 
    }
 
    /** 
     * Nos da el color del item 
     */
    public String getColor() { 
        return color; 
    }
 
    //Metodos abstractos
 
    /** 
     * Nos da la altura del item en cm 
     */
    public abstract int altoCm();
 
    /** 
     * Nos da la altura del item en pixeles
     */
    public abstract int altoPx();
 
    /**
     * Nos da el ancho del item en pixeles
     */
    public abstract int anchoPx();
 
    /**
     * Mueve el item a la posición centroX, y y lo hace visible
     * Donde: 
     * - centroX es el centro de la torre horizontalmente y el centro es calculado por cada subclase 
     *   segun el ancho de la torre en pixeles y el ancho del item respectivamente
     * - y es la posicion superior en pixeles
     */
    public abstract void setPosition(int centroX, int y);
 
    /**
     * Hace invisible el item en el Canvas
     */
    public abstract void erase();
    
    /**
     * Nos dice el tipo del item y su numero
     */
    public abstract String toString();
}
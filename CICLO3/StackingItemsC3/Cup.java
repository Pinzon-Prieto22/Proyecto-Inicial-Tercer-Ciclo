/**
 * Clase que representa un vaso
 * Extiende StackItem
 *
 * @author Bryan Pinzon y Valentina Prieto
 * @version CICLO 3
 */
public class Cup extends StackItem {
 
    public static final int GROSOR_PX = 5; //grosor de las paredes del cup
 
    private int altoCm;
    private int anchoPx;
    private boolean covered; //covered nos dice si tiene tapa o no (true=tiene tapa, false=notiene tapa)
 
    private Rectangle paredIzq;
    private Rectangle paredDer;
    private Rectangle base;
 
    // [osicion actual de cada parte (nos guiamos de la esquina superior-izquierda de cada rectangulo)
    private int izqX;  
    private int izqY; // posicion pared izquierda
    private int derX;
    private int derY; //posicion pared derechs
    private int baseRX;
    private int baseRY; // posicin de la base
 
    /**
     * constructor
     * crea el vaso n
     */
    public Cup(int numero) {
        super(numero); // llama al constructor de su clase padre StackItem
        this.altoCm  = 2 * numero - 1; // hacemos el alto de las cups impares
        this.anchoPx = numero * 20 + 20; // el ancho de los vasos es en cm dos veces el numero mas 2 
        this.covered = false; // solo estamos creando el cup por el momento no tiene vaso
 
        int altoPx = altoCm * PX_POR_CM; // hacemos conversion de cm a px del alto
 
        // los rectangles inician en la posicion por defecto del tablero (DEFAULT_X, DEFAULT_Y)
        izqX  = DEFAULT_X; 
        izqY  = DEFAULT_Y;
        derX  = DEFAULT_X; 
        derY  = DEFAULT_Y;
        baseRX = DEFAULT_X; 
        baseRY = DEFAULT_Y;
        
        //Creamos la pared izquierda del cup
        paredIzq = new Rectangle();
        paredIzq.changeSize(altoPx, GROSOR_PX);
        paredIzq.changeColor(color);
        //Creamos la pared derrcha del cup
        paredDer = new Rectangle();
        paredDer.changeSize(altoPx, GROSOR_PX);
        paredDer.changeColor(color);
        //Creamos la base del cup
        base = new Rectangle();
        base.changeSize(GROSOR_PX, anchoPx);
        base.changeColor(color);
    }
    /**
     * Sobreestribimos el metodo que nos da el alto en cm del iem de StackItem
     */
    @Override 
    public int altoCm(){ 
        return altoCm; 
    }
    /**
     * Sobreescribimos el metodo que nos da el alto del item en pixeles de StackItem
     */
    @Override 
    public int altoPx(){ 
        return altoCm * PX_POR_CM; 
    }
    /**
     * Sobreescribimos el metodo que nos da el ancho del item de StackItenm
     */
    @Override 
    public int anchoPx(){ 
        return anchoPx; 
    }
 
    /**
     * Nos dice si el cup esta tapado o no
     */
    public boolean isCovered(){ 
        return covered;
    }
 
    /**
     * Hace que el vaso quede como tapado o destapado 
     * si el parametro covered = true entonces lo tapa
     * si el parametro covered = false entonces lo destapa
     * true = negro, false = color original
     */
    public void setCovered(boolean covered) {
        this.covered = covered;
    }
 
    /**
     * Sobreescribimos el metodo que pone el item en el centro 
     * Pone el vaso centrado en centroX, con la esquina superior en y
     * Entonces:
     * x = centroX - anchoPx/2 (borde izquierdo del vaso)
     * paredIzq -> (x,y)
     * paredDer -> (x + anchoPx - GROSOR, y)
     * base     -> (x,y + altoPx - GROSOR)
     */
    @Override
    public void setPosition(int centroX, int y) {
        int altoPx = altoCm * PX_POR_CM;
        int x = centroX - anchoPx / 2;   // fijamos donde quedaria el borde izquierdo para que quede centrado
 
        int cambioIzqX  = x; //calculamos la ubicacion de la pared izquierda en x
        int cambioIzqY  = y; //calculamos la ubicacion de la pared izquierda en y
        int cambioDerX  = x + anchoPx - GROSOR_PX; //calculamos la ubicacion de la pared derecha en x considerando el grosor de la pared
        int cambioDerY  = y; //calculamos la ubicacion de la pared derecha en y
        int cambioBaseX = x; //calculamos la ubicacion de la base en x
        int cambioBaseY = y + altoPx - GROSOR_PX; //calculamos la ubicacion de la base del cup considerando el grosor de la base
        //Unicamos la pared izquierda
        paredIzq.makeInvisible(); 
        paredIzq.moveHorizontal(cambioIzqX - izqX);
        paredIzq.moveVertical(cambioIzqY - izqY);
        izqX = cambioIzqX; 
        izqY = cambioIzqY;
        //Ubicamos la pared derecgha
        paredDer.makeInvisible();
        paredDer.moveHorizontal(cambioDerX - derX);
        paredDer.moveVertical(cambioDerY - derY);
        derX = cambioDerX; 
        derY = cambioDerY;
        //Ubicamos la base
        base.makeInvisible();
        base.moveHorizontal(cambioBaseX - baseRX);
        base.moveVertical(cambioBaseY - baseRY);
        baseRX = cambioBaseX; 
        baseRY = cambioBaseY;
        //Hacemos visible (el cup) las paredes y la base 
        paredIzq.makeVisible();
        paredDer.makeVisible();
        base.makeVisible();
    }
    /**
     * Sobreescrinimos el metodo que borra el item en StackIten
     * (debemos hacer invisibles tanto las paredes como la base)
     */
    @Override
    public void erase() {
        paredIzq.makeInvisible();
        paredDer.makeInvisible();
        base.makeInvisible();
    }
    /**
     * Sobreescribimos el metodo que nos dice el tipo de item y su numero
     */
    @Override
    public String toString(){ 
        return "Cup," + numero; 
    }
}
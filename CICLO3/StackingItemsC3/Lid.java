/**
 * Clase que representa una tapa
 * Extiende StackItem
 *
 * @author Bryan Pinzon y Valentina Prieto
 * @version CICLO 3
 */
public class Lid extends StackItem {
 
    public static final int ALTO_TAPA_CM = 1; //El alto de la tapa siempre es de 1cm
 
    private int anchoPx;
    private Rectangle body;
 
    private int currX; // posicion actual del Rectangle
    private int currY; // posicion actual del Rectangle
 
    /**
     * Constructor
     * crea la tapa n
     */
    public Lid(int numero){
        super(numero); // llama al constructor de su clase padre StackItem
        this.anchoPx = numero * 20 + 20; //Le damos el mismo ancho que su cup respectivamente
        //las posiociones en x y y son las que tenemos por defecto
        this.currX   = DEFAULT_X;
        this.currY   = DEFAULT_Y;
 
        body = new Rectangle();
        body.changeSize(ALTO_TAPA_CM * PX_POR_CM, anchoPx);//Creamos la tapa de alto 1cm(10px) y el cncho dde su cup
        body.changeColor(color);//Le ponemos el color de su cup
    }
    /**
     * Sobreestribimos el metodo que nos da el alto en cm del item de StackItem
     */
    @Override 
    public int altoCm(){ 
        return ALTO_TAPA_CM; 
    }
    /**
     * Sobreescribimos el metodo que nos da el alto del item en pixeles de StackItem
     */
    @Override 
    public int altoPx(){ 
        return ALTO_TAPA_CM * PX_POR_CM; 
    }
    /**
     * Sobreescribimos el metodo que nos da el ancho del item de StackItenm
     */
    @Override 
    public int anchoPx() { 
        return anchoPx; 
    }
    /**
     * Pone la tapa centrada en centroX, con la esquina superior en y
     * x = centroX - anchoPx/2
     */
    @Override
    public void setPosition(int centroX, int y) {
        int x = centroX - anchoPx / 2;
        body.makeInvisible();
        //ubicamos la tapa centrada
        body.moveHorizontal(x - currX);
        body.moveVertical(y - currY);
        currX = x;
        currY = y;
        body.changeColor(color);
        //Hacemos visible la tapa
        body.makeVisible();
    }
    /**
     * Sobreescrinimos el metodo que borra el item en StackIten
     */
    @Override
    public void erase() { 
        body.makeInvisible(); 
    }
    /**
     * Sobreescribimos el metodo que nos dice el tipo de item y su numero
     */
    @Override
    public String toString() { 
        return "Tapa," + numero; 
    }
}
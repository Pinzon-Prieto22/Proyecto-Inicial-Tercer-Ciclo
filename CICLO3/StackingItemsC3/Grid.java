/**
 * Cuadricula visual que marca los centimetros de altura de la torre.
 *
 * Dibuja:
 *   - Lineas horizontales cada 1 cm (cada 10 px) marca cada 5 cm con lineas mas largas
 *   - Un eje vertical negro en el borde izquierdo.
 *   - Numeros al lado izquierdo del eje indicando los cm
 *   
 * @author Bryan Pinzon y Valentina Prieto
 * @version CICLO 3
 */
public class Grid {
 
    public static final int PX_POR_CM = 10; // 1cm = 10px
 
    private static final int ANCHO_LINEA_LARGA  = 300; // Linea cada 5cm
    private static final int ANCHO_LINEA_CORTA  = 150; // lineas 
    private static final int ANCHO_EJE          = 2;
 
    private int maxHeightCm;
    private int xEje;   // posicion x del eje vertical
    private int baseY;  // posicion y de la base de la torre
    private boolean visible;
 
    private Rectangle   eje;
    private Rectangle[] lineas;
    private NumLabel[]  etiquetas;
 
    /**
     * Constructor de la cuadricula
     * recibe los parametros:
     * maxHeightCm altura maxima en cm 
     * xEje posicion x del eje vertical
     * baseY posicion y de la base de la torre en pixeles
     */
    public Grid(int maxHeightCm, int xEje, int baseY) {
        this.maxHeightCm = maxHeightCm;
        this.xEje = xEje;
        this.baseY = baseY;
        this.visible = false;
 
        //Hacemos y ubicamos el eje vertical 
        eje = new Rectangle();
        eje.changeSize(maxHeightCm * PX_POR_CM, ANCHO_EJE);
        eje.changeColor("black");
        moverA(eje, xEje, baseY - maxHeightCm * PX_POR_CM);
 
        //Hacemos las lineas y etiquetas por cada cm
        lineas    = new Rectangle[maxHeightCm + 1];
        etiquetas = new NumLabel[maxHeightCm + 1];
 
        for (int cm = 0; cm <= maxHeightCm; cm++) {
            // comenzamos desde la base de la torre y vamos subiendo para poner las lineas
            int yLinea = baseY - cm * PX_POR_CM;
            //creamos la linea
            Rectangle linea = new Rectangle();
            //si esta a 5cm es una linea larga
            if (cm % 5 == 0) {
                linea.changeSize(1, ANCHO_LINEA_LARGA);
                linea.changeColor("black");
            // si no es una linea corta
            } else {
                linea.changeSize(1, ANCHO_LINEA_CORTA);
                linea.changeColor("black");
            }
            // Mueve la linea a la poscion xEje y yLinea
            moverA(linea, xEje, yLinea);
            // guardamos la linea en lineas
            lineas[cm] = linea;
 
            boolean mostrarNumero = (cm % 5 == 0);//Mostramos el cm solo cada 5cm
            etiquetas[cm] = new NumLabel(mostrarNumero ? String.valueOf(cm) : "", xEje - 22, yLinea + 4 ); //-22 a la izquierda del eje | +4 alineado con la linea
        }
    }
 
    /**
     * Hace visible la cuadricula con todos sus elementos.
     */
    public void makeVisible() {
        visible = true;
        eje.makeVisible();
        for (Rectangle l : lineas){
            l.makeVisible();
        }
        for (NumLabel  e : etiquetas){
            e.mostrar();
        }
    }
 
    /**
     * Hace invisible la cuadricula.
     */
    public void makeInvisible() {
        visible = false;
        eje.makeInvisible();
        for (Rectangle l : lineas){
            l.makeInvisible();
        }
        for (NumLabel  e : etiquetas){
            e.ocultar();
        }
    }
    /**
     * Mueve el rectangulo r a la posicion x,y
     */
    private void moverA(Rectangle r, int x, int y) {
        r.moveHorizontal(x - 70); //Considerando la posicion por defecto
        r.moveVertical(y - 15); //Considerando la posicion por defecto
    }
 
    /**
     * NumLabel dibuja un numero como texto en el Canvas
     * Si el texto esta vacio no dibuja nada.
     */
    private class NumLabel {
        private String texto;
        private int x;
        private int y;
        /**
         * Constructor
         * texto a mostrar, posicion x y posicion y donde va el texto
         */
        public NumLabel(String texto, int x, int y) {
            this.texto = texto;
            this.x     = x;
            this.y     = y;
        }
        /**
         * Metodo que muesyta en el canvas el texto
         */
        public void mostrar() {
            if (texto.isEmpty()){
                return;
            }
            Canvas canvas = Canvas.getCanvas();
            canvas.drawText(this, texto, x, y);
        }
        /**
         * Metodo que oculta el texto en el canvas
         */
        public void ocultar() {
            if (texto.isEmpty()) return;
            Canvas canvas = Canvas.getCanvas();
            canvas.erase(this);
        }
    }
}
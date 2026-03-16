import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
 
/**
 * Tower - simulador visual del problema Stacking Cups.
 *
 * CICLO 1: Tower(width,maxHeight), 
 *          pushCup, 
 *          popCup, 
 *          removeCup,
 *          pushLid, 
 *          popLid, 
 *          removeLid, 
 *          orderTower, 
 *          reverseTower,
 *          height, 
 *          lidedCups, 
 *          stackingItems, 
 *          makeVisible, 
 *          makeInvisible, 
 *          exit, 
 *          ok.
 *          
 * CICLO 2: Tower(cups), 
 *          swap, 
 *          cover, 
 *          swapToReduce.
 *          TowerC2Test
 *          
 * CICLO 3: TowerContest
 *          TowerContestTest
 *
 * @author Bryan Pinzon y Valentina Prieto
 * @version CICLO 3
 */
public class Tower {
 
    private static final int FINAL_CANVAS     = 900; // Borde del canvas
    private static final int MARGEN_IZQUIERDA = 80; // Margen izquierda del canvas
    private static final int GRID_X           = 65; // Posicion de la cuadricula en x
    private static final int MIN_GRID_CM      = 20; // Altura minima de la cuadricula
 
    private Stack<StackItem> stack; //Stack que lleva el registro de los items
    private int maxHeightCm; // Altura maxima de la torre
    private boolean isVisible;
    private boolean ok; // Si se puede hacer un movimiento o no
    private Grid grid; // La cuadricula
    private int gridHeightCm; // Altura de la cuadricula

    /**
     * CICLO 1 - Crea una torre vacia
     * Entran los parametros:
     * width = ancho en pixeles
     * maxHeight = alto maximo de la torre en cm
     */
    public Tower(int width, int maxHeight) {
        this.maxHeightCm  = maxHeight;
        this.isVisible    = false;
        this.ok           = true;
        this.gridHeightCm = Math.max(maxHeight, MIN_GRID_CM); // definimos el alto de la cuadricula
        stack = new Stack<>(); //cramos el stack donde vamos a guardar nuestros items
        grid  = new Grid(gridHeightCm, GRID_X, FINAL_CANVAS); // creamos la cuadricula en laposicion x y al final del canvas en y
    }
 
    /**
     * CICLO 2 - Crea una torre con n vasos sin tapas
     * Entra el parametro:
     * cup = cantidad de vasos
     */
    public Tower(int cups) {
        this(cups * 30, cups * cups + 10);//llamamos al constructor anterior con width = cups*30 y maxHeight = cups*cups+10
        // Lo rellenamos de la cantidad de cups 
        for (int i = 1; i <= cups; i++){
            stack.push(new Cup(i));
        }
        ok = true;
    }
 
    /**
     * CICLO 1 - Agrega el vaso numero i a la torre
     * Considerando lo siguiente:
     * so el cup en la cima de la torre tiene un anchoPx mayor, el cup i va dentro de el
     * si no entonces el cup i va en la cima de la torre
     */
    public void pushCup(int i) {
        //Revisamos si el vaso ya esta creafo
        if (buscaVasoNum(i) != null) {
            ok = false;
            daMensaje("El cup " + i + " ya existe");
            return;
        }
        //Creamos el vaso
        Cup newCup = new Cup(i);
        //Definimos la posici'on segun el ancho delcup
        int pos = encontrarPosInsercion(newCup.anchoPx());
        // Lo agregamos al stack
        stack.add(pos, newCup);
        ok = true;
        redraw();
    }
 
    /**
     * CICLO 1 - Quita cups desde la cima de la torre
     */
    public void popCup() {
        for (int i = stack.size() - 1; i >= 0; i--) {
            if (stack.get(i) instanceof Cup) {
                eliminarConContenido(i);
                ok = true;
                redraw();
                return;
            }
        }
        ok = false;
        daMensaje("No hay vasos en la torre para quitar");
    }
    
    /**
     * CICLO 1 - Quita el cup numero i de la torre
     * Entra el parametro:
     * i = numero del vaso que se quiere quitar
     */
    public void removeCup(int i) {
        Cup cup = buscaVasoNum(i);
        // Si no encontramos el cup
        if (cup == null) {
            ok = false;
            daMensaje("El cup " + i + " no existe");
            return;
        }
        // Segun su indice eliminamos el vaso y lo que este dentro de el
        int idx = stack.indexOf(cup);
        eliminarConContenido(idx);
        ok = true;
        redraw();
    }
 
    /**
     * CICLO 1 - Agrega la tapa numero i a la torre
     * Considerando lo siguiente:
     * si el cup en la cima de la torre tiene un anchoPx mayor, entonces la tapa queda dentro de el
     * Si no la tapa queda en la cima de la torre
     * entra el parametro:
     * i = numero de la tapa
     */
    public void pushLid(int i) {
        // Verificamos si ya existe la tapa
        if (buscaTapaNum(i) != null) {
            ok = false;
            daMensaje("La lid " + i + " ya existe");
            return;
        }
        // Creamos la tapa
        Lid newLid = new Lid(i);
        // Definimos la posicion segun el ancho del lid
        int pos = encontrarPosInsercion(newLid.anchoPx());
        // La agregamos al stack
        stack.add(pos, newLid);
        ok = true;
        redraw();
    }
 
    /**
     * CICLO 1 - Quita lids desde la cima de la torre
     */
    public void popLid() {
        for (int i = stack.size() - 1; i >= 0; i--) {
            if (stack.get(i) instanceof Lid) {
                Lid lid = (Lid) stack.get(i);
                Cup cup = buscaVasoNum(lid.buscaNumItem());
                // si esta tapando un cup entonces ahora decimos que el cup esta destapado
                if (cup != null){
                    cup.setCovered(false);
                }
                lid.erase();
                stack.remove(i);
                ok = true;
                redraw();
                return;
            }
        }
        ok = false;
        daMensaje("No hay lids para quitar");
    }
 
    /**
     * CICLO 1 - Quita la Lid numero i de la torre 
     * entra el parametro:
     * i = numero del lid que se quiere quitar
     */
    public void removeLid(int i) {
        Lid lid = buscaTapaNum(i);
        // Si no encontramos la Lid
        if (lid == null) {
            ok = false;
            daMensaje("La tapa " + i + " no existe.");
            return;
        }
        Cup cup = buscaVasoNum(i);
        // Si lid esta tapando un vaso, ahora decimos que no esta tapado ese vaso
        if (cup != null){
            cup.setCovered(false);
        }
        lid.erase();
        stack.remove(lid);
        ok = true;
        redraw();
    }
 
        /**
     * CICLO 1 - Ordena la torre de mayor (muy ancho) en la base a menor (muy angosto) en la cima
     * Considerando que:
     * Si la tapa de un vaso existe en la torre, siempre queda
     * inmediatamente encima de su vaso correspondiente al ordenar la torre
     */
    public void orderTower() {
        eraseAll();
        // construimos pares de cup y lid respectivamente
        ArrayList<ArrayList<StackItem>> pares = buildPares();
        // ordenamos los cups de mayor a menor segun su numero
        pares.sort((a, b) -> b.get(0).buscaNumItem() - a.get(0).buscaNumItem());
        //mete los pares ordenados en una lista sencilla
        ArrayList<StackItem> lista = flattenPares(pares);
        //Revisa los que caben en el alto de la torre
        lista = cabeEnAlto(lista);
 
        // volvemos a construir el stack ordenado
        stack.clear();
        for (StackItem item : lista){
            stack.push(item);
        }
        ok = true;
        redraw();
    }
 
    /**
     * CICLO 1 - Invierte el orden actual de la torre
     * Si la tapa de un vaso existe en la torre, siempre queda
     * inmediatamente encima de su vaso correspondiente tras la inversion
     */
    public void reverseTower() {
        eraseAll();
        //construimos pares de cup y lid respectivamente
        ArrayList<ArrayList<StackItem>> pares = buildPares();
 
        // ordenamos de menor a mayor los pares
        Collections.reverse(pares);
 
        // mete los pares ordenados en una lista sencilla
        ArrayList<StackItem> lista = flattenPares(pares);
        //Revisa los que caben en el alto de la torre
        lista = cabeEnAlto(lista);
 
        // reconstruimos el stac ordenado al reves
        stack.clear();
        for (StackItem item : lista){
            stack.push(item);
        }
        ok = true;
        redraw();
    }
 
    /**
     * Recorre el stack y agrupa cada cup con su lid respectivamente
     * Si hay tapas que no tienen vaso las incluimos como un par de un solo elemento
     */
    private ArrayList<ArrayList<StackItem>> buildPares() {
        ArrayList<ArrayList<StackItem>> pares = new ArrayList<>();
        // creamos un contenedor para meter los lids con su numero 
        java.util.HashMap<Integer, Lid> lidsDisponibles = new java.util.HashMap<>();
        //Metemos los lids con su numero en un contenedor
        for (StackItem item : stack) {
            if (item instanceof Lid) {
                lidsDisponibles.put(item.buscaNumItem(), (Lid) item);
            }
        }
 
        // guardamos los liids ya asignados
        java.util.HashSet<Integer> lidsAsignados = new java.util.HashSet<>(); //usamos HashSet para no tener duplicados
 
        for (StackItem item : stack) {
            if (item instanceof Cup) {
                // creamos un arraylist para guardar los pares
                ArrayList<StackItem> par = new ArrayList<>();
                // Si el item es cup lo agregamos al arraylist
                par.add(item);
                // Buscamos el numero del cup
                int num = item.buscaNumItem();
                //Si el numero del cup es igual al numero del lid en el HashMap
                if (lidsDisponibles.containsKey(num)) {
                    // agregamos el lid al arraylits
                    par.add(lidsDisponibles.get(num));
                    // guardamos el numero del lid asignado en el HashSet
                    lidsAsignados.add(num);
                }
                // Al ArrayList que devuelve le agregamos el par de cup,lid
                pares.add(par);
            }
        }
 
        // Tapas sin vaso las agregamos como pares de un solo elemento.
        for (StackItem item : stack) {
            if (item instanceof Lid && !lidsAsignados.contains(item.buscaNumItem())) {
                ArrayList<StackItem> par = new ArrayList<>(); //Creamos el arrayList que guardaria los pares
                par.add(item); // Agregamos unicamente el Lid
                pares.add(par);// Lo agregamos al ArrayList que devuelve
            }
        }
        return pares;
    }
 
    /**
     * Hace que el ArrayList anterior que es ArrayList<ArrayList<StackItem>>
     * Sea un Arraylist<StackItem>
     */
    private ArrayList<StackItem> flattenPares(ArrayList<ArrayList<StackItem>> pares) {
        ArrayList<StackItem> lista = new ArrayList<>();
        for (ArrayList<StackItem> par : pares) {
            lista.addAll(par);
        }
        return lista;
    }
 
    /**
     * CICLO 2 - Intercambia dos items en la torre.
     * Entran los parametros:
     * o1 = {"cup"/"lid", "numero"}
     * o2 = {"cup"/"lid", "numero"}
     */
    public void swap(String[] o1, String[] o2) {
        StackItem item1 = buscaElItem(o1[0], Integer.parseInt(o1[1]));
        StackItem item2 = buscaElItem(o2[0], Integer.parseInt(o2[1]));
        if (item1 == null || item2 == null) {
            ok = false;
            daMensaje("Alguno de los dos items no se encontro");
            return;
        }
        int idx1 = stack.indexOf(item1);
        int idx2 = stack.indexOf(item2);
        stack.set(idx1, item2);
        stack.set(idx2, item1);
        ok = true;
        redraw();
    }
 
    /**
     * CICLO 2 - Pone la tapa de cada vaso directamente encima de el, si la tapa ya existe en la torre
     */
    public void cover() {
        //variable de control
        boolean any = false;
        for (int i = 0; i < stack.size(); i++) {
            // Si no es un cup
            if (!(stack.get(i) instanceof Cup)){
                continue;
            }
            Cup cup = (Cup) stack.get(i);
            // si cup esta tapada
            if (cup.isCovered()){
                continue;
            }
            Lid lid = buscaTapaNum(cup.buscaNumItem());
            // si la tapa no existe
            if (lid == null){
                continue;
            }
            //Quitamos la lid
            stack.remove(lid);
            // Considerando el indice de la cup
            int cupIdx = stack.indexOf(cup);
            // Agregamso al stack la Lid una posicion adelante del cup
            stack.add(cupIdx + 1, lid);
            // Ahora el cup esta tapado
            cup.setCovered(true);
            any = true;
        }
        // Si no se pudo entonces:
        ok = any;
        if (!any) daMensaje("No hay vasos que se puedan tapar");
        redraw();
    }
 
    /**
     * CICLO 1 - Retorna la altura total en cm 
     */
    public int height() {
        ArrayList<int[]> exteriores = new ArrayList<>(); // [anchoPx, intTopCm]
        int globalTop = 0; // Altura maxima de la pila
        int maxCima   = 0; // Guarda la mayor altura alcanzada por cualquier elemento
 
        for (StackItem item : stack) {
            // obtenemos las dimensiones del objeto
            int anchoPx = item.anchoPx();
            int altoCm  = item.altoCm();
            // si el objeto esta metido dentro de otro 
            boolean metido = false;
            //Verifica si el item cabe en otro
            for (int e = exteriores.size() - 1; e >= 0; e--) {
                if (anchoPx < exteriores.get(e)[0]) {
                    int base = exteriores.get(e)[1];
                    int cima = base + altoCm;
                    exteriores.get(e)[1] = cima;
                    maxCima = Math.max(maxCima, cima);
                    exteriores.add(new int[]{anchoPx, base + 1});
                    metido = true;
                    break;
                }
            }
            //Si el item no cabe en otro
            if (!metido) {
                int cima = globalTop + altoCm;
                globalTop = cima;
                maxCima   = Math.max(maxCima, cima);
                exteriores.clear();
                if (item instanceof Cup && !((Cup) item).isCovered()) {
                    exteriores.add(new int[]{anchoPx, globalTop - altoCm + 1});
                }
            }
        }
        return maxCima;
    }
 
    /**
     * CICLO 1 - Retorna los numeros de los vasos tapados, ordenados ascendentemente
     */
    public int[] lidedCups() {
        // creamos un arrayList para guardar los numeros de los cups que estan tapados
        ArrayList<Integer> result = new ArrayList<>();
        // Le agregamos los numeros de los cups tapados
        for (StackItem item : stack) {
            if (item instanceof Cup && ((Cup) item).isCovered()) {
                result.add(item.buscaNumItem());
            }
        }
        // ordenamos el ArrayList
        Collections.sort(result);
        // Creamos el arreglo qeu devuelve
        int[] arr = new int[result.size()];
        // rellenamos el arreglo
        for (int i = 0; i < result.size(); i++){
            arr[i] = result.get(i);
        }
        return arr;
    }
 
    /**
     * CICLO 1 - Retorna el stack como String[][] {tipo, numero}
     */
    public String[][] stackingItems() {
        // Creamos el arreglo que retorna
        String[][] result = new String[stack.size()][2];
        // Rellenamos el arreglo
        for (int i = 0; i < stack.size(); i++) {
            StackItem item = stack.get(i);
            result[i][0] = (item instanceof Cup) ? "cup" : "lid";
            result[i][1] = String.valueOf(item.buscaNumItem());
        }
        return result;
    }
 
    /**
     * CICLO 2 - Retorna el par de items cuyo intercambio reduciria la altura
     */
    public String[][] swapToReduce() {
        int alturaActual = height();
        int mejorAltura  = alturaActual;
        int mejorI = -1;
        int mejorJ = -1;
 
        // Creamos un ArrayList para guardar los indices donde estan los cups
        ArrayList<Integer> indicesCups = new ArrayList<>();
        // guardamos los indices de los cups
        for (int k = 0; k < stack.size(); k++) {
            if (stack.get(k) instanceof Cup) {
                indicesCups.add(k);
            }
        }
        // Probamos todos los pares de Cups
        for (int a = 0; a < indicesCups.size(); a++) {
            for (int b = a + 1; b < indicesCups.size(); b++) {
                int i = indicesCups.get(a);
                int j = indicesCups.get(b);
                // Simulamos el swap y medir la altura resultante
                int alturaConSwap = heightSimulado(i, j);
 
                if (alturaConSwap < mejorAltura) {
                    mejorAltura = alturaConSwap;
                    mejorI = i;
                    mejorJ = j;
                }
            }
        }
 
        if (mejorI == -1) {
            return new String[0][0]; // ningun swap reduce la altura
        }
 
        return new String[][]{
            {"cup", String.valueOf(stack.get(mejorI).buscaNumItem())},
            {"cup", String.valueOf(stack.get(mejorJ).buscaNumItem())}
        };
    }
 
    /**
     * Calcula la altura del stack como quedaria si se intercambiaran los
     * elementos en los indices idx1 e idx2, sin modificar el stack real
     * Entran los parametros:
     * idx1 indice del primer elemento a intercambiar
     * idx2 indice del segundo elemento a intercambiar
     * devuelve: altura resultante en cm
     */
    private int heightSimulado(int idx1, int idx2) {
        // Construimos una copia del stack con el swap aplicado
        ArrayList<StackItem> copia = new ArrayList<>(stack);
        StackItem tmp = copia.get(idx1);
        copia.set(idx1, copia.get(idx2));
        copia.set(idx2, tmp);
        // Aplicamos la misma logica de height a la copia
        ArrayList<int[]> exteriores = new ArrayList<>();
        int globalTop = 0;
        int maxCima   = 0;
        for (StackItem item : copia) {
            int anchoPx = item.anchoPx();
            int altoCm  = item.altoCm();
            boolean metido = false;
            for (int e = exteriores.size() - 1; e >= 0; e--) {
                if (anchoPx < exteriores.get(e)[0]) {
                    int base = exteriores.get(e)[1];
                    int cima = base + altoCm;
                    exteriores.get(e)[1] = cima;
                    maxCima = Math.max(maxCima, cima);
                    exteriores.add(new int[]{anchoPx, base + 1});
                    metido = true;
                    break;
                }
            }
            if (!metido) {
                int cima = globalTop + altoCm;
                globalTop = cima;
                maxCima   = Math.max(maxCima, cima);
                exteriores.clear();
                if (item instanceof Cup && !((Cup) item).isCovered()) {
                    exteriores.add(new int[]{anchoPx, globalTop - altoCm + 1});
                }
            }
        }
        return maxCima;
    }
 
    /**
     * CICLO 1 - Hace visible la torre y la cuadricula
     */
    public void makeVisible() {
        isVisible = true;
        grid.makeVisible();
        redraw();
    }
 
    /**
     * CICLO 1 - Hace invisible la torre y la cuadricula
     */
    public void makeInvisible() {
        isVisible = false;
        grid.makeInvisible();
        eraseAll();
    }
 
    /**
     * CICLO 1 - Sale del simulador
     */
    public void exit() { 
        makeInvisible(); 
    }
 
    /**
     * CICLO 1 - Nos dice true si la ultima operacion fue exitosa
     */
    public boolean ok() { 
        return ok; 
    }
 
    /**
     * Determina la posicion de insercion para un nuevo elemento.
     * Considerando si se puede meter o no en el ultimo elemento
     * Entra el parametro:
     * nuevoAnchoPx = ancho en pixeles del nuevo elemento
     * retorna el indice donde deberia insertarse
     */
    private int encontrarPosInsercion(int nuevoAnchoPx) {
        // Buscamos el cup mas reciente de la cima y que no este tapado
        int topCupIdx = -1;
        for (int i = stack.size() - 1; i >= 0; i--) {
            StackItem item = stack.get(i);
            if (item instanceof Cup && !((Cup) item).isCovered()) {
                topCupIdx = i;
                break;
            }
        }
        // Si el cup de la cima puede contener al nuevo elemento entoncews lo insetamos dentro
        if (topCupIdx >= 0 && stack.get(topCupIdx).anchoPx() > nuevoAnchoPx) {
            int exteriorAncho = stack.get(topCupIdx).anchoPx();
            int fin = topCupIdx + 1;
            while (fin < stack.size() && stack.get(fin).anchoPx() <= exteriorAncho) {
                fin++;
            }
            return fin;
        }
        // Si no cabe en el cup de la cima  o no hay cup entonces va al final del stack
        return stack.size();
    }
 
    /**
     * Elimina el Cup en idx y todo lo que tenga dentro y su tapa
     * Entra el parametro:
     * idx = indice del Cup a eliminar
     */
    private void eliminarConContenido(int idx) {
        int exteriorAncho = stack.get(idx).anchoPx();
        //Creamos un arraylist de lo que vamos aeliminar
        ArrayList<Integer> aEliminar = new ArrayList<>();
        aEliminar.add(idx);
        //Variable de control
        int j = idx + 1;
        //Rellenamos el arrayklist a eliminar
        while (j < stack.size() && stack.get(j).anchoPx() <= exteriorAncho) {
            aEliminar.add(j);
            j++;
        }
        //Eliminamos
        for (int k = aEliminar.size() - 1; k >= 0; k--) {
            int pos = aEliminar.get(k);
            stack.get(pos).erase();
            stack.remove(pos);
        }
    }
 
    /**
     * Busca un Cup por numero
     */
    private Cup buscaVasoNum(int n) {
        for (StackItem item : stack){
            if (item instanceof Cup && item.buscaNumItem() == n){
                 return (Cup) item;
            }
        }
        return null;
    }
 
    /**
     * Busca un Lid por numero
     */
    private Lid buscaTapaNum(int n) {
        for (StackItem item : stack){
            if (item instanceof Lid && item.buscaNumItem() == n){
                return (Lid) item;
            }
        }
        return null;
    }
 
    /**
     * Busca un StackItem por tipo y numero
     */
    private StackItem buscaElItem(String type, int n) {
        if (type.equalsIgnoreCase("cup")){
            return buscaVasoNum(n);
        }
        if (type.equalsIgnoreCase("lid")){
            return buscaTapaNum(n);
        }
        return null;
    }
 
    /**
     * Filtra una lista dejando solo los elementos que caben en maxHeightCm
     */
    private ArrayList<StackItem> cabeEnAlto(ArrayList<StackItem> items) {
        ArrayList<StackItem> result = new ArrayList<>();
        int total = 0;
        for (StackItem item : items) {
            int h = item.altoCm();
            if (total + h <= maxHeightCm) { 
                result.add(item); total += h; 
            }
        }
        return result;
    }
 
    /**
     * Hace invisible todos los elementos
     */
    private void eraseAll() {
        for (StackItem item : stack){
            item.erase();
        }
    }
 
    /**
     * Redibuja todos los elementos 
     */
    private void redraw() {
        // Si no es visible
        if (!isVisible){
            return;
        }
        // Si es visible
        int alturaActual = height();
        // Si la altura es mayor a la altura de la cuadricula
        if (alturaActual > gridHeightCm) {
            grid.makeInvisible();
            //Hacemos una cuadricula más grande
            gridHeightCm = alturaActual + MIN_GRID_CM;
            grid = new Grid(gridHeightCm, GRID_X, FINAL_CANVAS);
            grid.makeVisible();
        }
        // Borramos todo
        eraseAll();
        // tomamos el centro y el final del canvas
        final int centroX = MARGEN_IZQUIERDA + 150;
        int yPx = FINAL_CANVAS;
        
        // Creamos un ArrayList de los anchos de los cups
        ArrayList<Integer> extAncho = new ArrayList<>();
        // Creamos un ArrayList de la posicion de la bse disponible del cup
        ArrayList<Integer> extBaseY = new ArrayList<>();
        // Recorremos todo el stack
        for (StackItem item : stack) {
            // Obteneoms las dimensiones
            int anchoPx = item.anchoPx();
            int altoPx  = item.altoPx();
            //Si se meete o no
            boolean metido = false;
 
            // recorremos todos los anchos
            for (int e = extAncho.size() - 1; e >= 0; e--) {
                // si el ancho del item es menor que el ancho
                if (anchoPx < extAncho.get(e)) {
                    int posY = extBaseY.get(e) - altoPx;
                    //metemos el item
                    item.setPosition(centroX, posY);
                    extBaseY.set(e, posY); 
                    //Eliminamos los anchos que no nos sirven
                    while (extAncho.size() > e + 1) {
                        extAncho.remove(extAncho.size() - 1);
                        extBaseY.remove(extBaseY.size() - 1);
                    }
                    // Si el cup esta abierto
                    if (item instanceof Cup && !((Cup) item).isCovered()) {
                        // lo agregamos a los anchos que se les puede meter algo
                        extAncho.add(anchoPx);
                        // agregamos su base a que se le poner algo encima 
                        extBaseY.add(posY + altoPx - Cup.GROSOR_PX);
                    }
                    // Si metimos el objeto
                    metido = true;
                    break;
                }
            }
            // Si no esta metido/no se puede meter 
            if (!metido) {
                // Va en la cima
                yPx -= altoPx;
                item.setPosition(centroX, yPx);
                extAncho.clear();
                extBaseY.clear();
                if (item instanceof Cup && !((Cup) item).isCovered()) {
                    extAncho.add(anchoPx);
                    extBaseY.add(yPx + altoPx - Cup.GROSOR_PX);
                }
            }
        }
    }
 
    /**
     * Muestra un mensaje
     */
    private void daMensaje(String msg) {
        if (isVisible)
            JOptionPane.showMessageDialog(null, msg, "Tower", JOptionPane.WARNING_MESSAGE);
    }
}
 
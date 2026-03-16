import java.util.ArrayList;
 
/**
 * TowerContest resuelve el Problema "Stacking Cups"
 *
 * @author Bryan Pinzon y Valentina Prieto
 * @version CICLO 3
 */
public class TowerContest {
    /**
     * Resuelve el problema de la maraton
     * Entran los parametros:
     * n = numero de vasos (1 <= n <= 200000)
     * h = altura deseada (1 <= h <= 4*10^10) el numero favorito
     */
    public static String resolver(int n, long h) {
        if (n == 1) {
            return (h == 1) ? "1" : "impossible";
        }
 
        long alturaN   = 2 * n - 1; // altura del vaso n
        long alturaMin = alturaN;    // altura minima alcanzable con n vasos
        long alturaMax = (long) n * n; // altura maxima alcanzable con n vasos
 
        if (h < alturaMin || h > alturaMax) {
            return "impossible";
        }
 
        int vasosDisponibles = n - 1;
 
        long sumaObjetivoPlataforma = h - alturaN;
        // Buscamos un grupo de vasos que cumpla: suma sus alturas = h - alturaN
        // Si se cumple entonces los apilamos de esa forma
        boolean[] seleccionPlataforma = buscarSubconjuntoConSuma(sumaObjetivoPlataforma, vasosDisponibles);
        if (seleccionPlataforma != null) {
            // Construye la secuencia de apilamiento
            return construirSecuenciaConPlataforma(n, seleccionPlataforma);
        }
 
        long sumaObjetivoDentro = h - 1;
        // Buscamos un grupo de vasos que cumplan: suma sus alturas = h - 1
        // Si se cumple entoonces los apilamos de esa forma
        boolean[] seleccionDentro = buscarSubconjuntoConSuma(sumaObjetivoDentro, vasosDisponibles);
        if (seleccionDentro != null) {
            //Construye la secuencia de apilamiento
            return construirSecuenciaVasoGrandePrimero(n, seleccionDentro);
        }
 
        return "impossible";
    }
 
    /**
     * Busca un subconjunto de vasos cuya suma de alturas sea exactamente el objetivo dado
     * Entran parametros:
     * objetivo = suma de alturas deseada
     * m = cantidad de vasos disponibles: {1..m}
     * Retorna:
     * boolean[m] donde:
     * [i-1]=true si el vaso i fue seleccionado
     * o
     * null si es imposible alcanzar el objetivo
     */
    private static boolean[] buscarSubconjuntoConSuma(long objetivo, int m) {
        if (objetivo < 0) {
            return null;
        }
        if (objetivo == 0) {
            return new boolean[m];// crea un arreglo de tamaño m y por defecto todos sus valores son false
        }
 
        // Intento 1: prueba de mayor a menor 
        boolean[] resultado = prueba(objetivo, m, true);
 
        // Intento 2: prueba de menor a mayor
        return (resultado != null) ? resultado : prueba(objetivo, m, false);
    }
 
    /**
     * Probamos encontrar los vasos que cumplen
     * Considerando:
     * Si tenemos un faltante de 2 saltamos el vaso porque no podriamos cubrir ese restante
     * 
     * Entran parametros:
     * objetivo = suma de alturas que necesitamos
     * m = vasos disponibles: {1..m}
     * 
     * como donde:
     * true = itera de m hacia 1 
     * o
     * false = de 1 hacia m
     *  
     * Retorna:
     * boolean[m] con la seleccion
     * o 
     * null si no se pudo cubrir el objetivo
     * 
     */
    private static boolean[] prueba(long objetivo, int m, boolean como) {
        boolean[] seleccion = new boolean[m]; // creamos un arreglo de tamaño m que por defecto es false
        long restante = objetivo; // Lo que nos falta para llegar a la suma que queremos 
        //recorremos los vasos disponibles
        for (int paso = 0; paso < m; paso++) {
            // Si como = true recorre del mayor al menor
            // Si como = false recorre del menor al mayor
            int i = como ? (m - paso) : (paso + 1); 
            //Calcula la altura del vaso
            long alturaVaso = 2 * i - 1;
            //Si cabe en lo que nos falta
            if (alturaVaso <= restante) {
                // Volvemos a calcular el restante
                long nuevoRestante = restante - alturaVaso;
                // si ese vaso nos deja un faltante de 2, lo saltamos
                if (nuevoRestante == 2){
                    continue;
                }
                // La posicion de la seleccion de vasos la hacemos true
                seleccion[i - 1] = true;
                restante = nuevoRestante;
                if (restante == 0){
                    return seleccion;
                }
            }
        }
        return (restante == 0) ? seleccion : null;
    }
 
    /**
     * construye el orden de inserción de los vasos
     * 
     * Entran los parametros:
     * n = numero del vaso principal
     * seleccion = boolean[n-1]: true si el vaso i cumple
     * Retorna
     * String con las alturas de insercion separadas por un espacio
     */
    private static String construirSecuenciaConPlataforma(int n, boolean[] seleccion) {
        //Creamos la lista para los que ordenamos ascendentemente
        ArrayList<Integer> plataformaAscendente = new ArrayList<>();
        //Creamos la lista para los que ordenamos descendentemente (items metidos)
        ArrayList<Integer> restantesDescendentes = new ArrayList<>();
        
        for (int i = 1; i <= n - 1; i++) {
            // rellenamos los que van ascendentemente
            if (seleccion[i - 1]){
                plataformaAscendente.add(i);
            }
            // rellenamos los que van descendentemente
            else{
                restantesDescendentes.add(i);
            }
        }
        // los que van descendentemente los ordenamos descendentemente
        java.util.Collections.sort(restantesDescendentes, java.util.Collections.reverseOrder());
        // Nos apoyamos de un stringbuildes para mostrar ya el resultado
        StringBuilder sb = new StringBuilder();
        // agregamos los vasos que van ascendentemente
        for (int v : plataformaAscendente) {
            agregarAltura(sb, v);
        }
        // agregamos el vaso n
        agregarAltura(sb, n);
        // agregamos los vasos que van dentro del vaso n
        for (int v : restantesDescendentes){
            agregarAltura(sb, v);
        }
        return sb.toString().trim();
    }
 
    /**
     * construye el orden de inserción de los vasos vaso grande primero, apilar dentro
     * Entran los parametros:
     * n = numero del vaso principal
     * seleccion  = boolean[n-1]: true si el vaso i cumple
     * Retorna
     * String con las alturas de insercion separadas por espacio
     */
    private static String construirSecuenciaVasoGrandePrimero(int n, boolean[] seleccion) {
        //Creamos la lista para los que ordenamos ascendentemente
        ArrayList<Integer> subconjuntoAscendente = new ArrayList<>();
        //Creamos la lista para los que ordenamos descendentemente (items metidos)
        ArrayList<Integer> restantesDescendentes = new ArrayList<>();
 
        for (int i = 1; i <= n - 1; i++) {
            if (seleccion[i - 1]){
                subconjuntoAscendente.add(i);
            }
            else{
                restantesDescendentes.add(i);
            }
        }
        java.util.Collections.sort(restantesDescendentes, java.util.Collections.reverseOrder());
 
        StringBuilder sb = new StringBuilder();
        agregarAltura(sb, n);
        for (int v : subconjuntoAscendente)  agregarAltura(sb, v);
        for (int v : restantesDescendentes)  agregarAltura(sb, v);
        return sb.toString().trim();
    }
 
    /**
     * Agrega la altura del vaso al StringBuilder con un espacio
     * Entran los parametros:
     * sb = StringBuilder donde se acumula la secuencia
     * vaso = numero del vaso cuya altura se agrega
     */
    private static void agregarAltura(StringBuilder sb, int vaso) {
        if (sb.length() > 0){
            sb.append(' ');
        }
        sb.append(2 * vaso - 1);
    }
 
    private static Tower torreActual = null; // lo usamos para que limpie el simulador cada vez que haga una torre
    /**
     * Simula visualmente la solucion en una Tower.
     * Si ya habia una simulacion activa, la elimina antes de crear la nueva.
     * Entran los parametros:
     * n = numero de vasos
     * h = altura deseada
     */
    public static void simular(int n, int h) {
        String solucion = resolver(n, h);
        if (solucion.equals("impossible")) {
            System.out.println("No es posible construir una torre de altura " + h + " con " + n + " vasos.");
            return;
        }
        // borramos la torre que ya estaba
        if (torreActual != null) {
            torreActual.makeInvisible();
            torreActual = null;
        }
        String[] partes = solucion.split(" ");
        int[] ordenVasos = new int[partes.length];
        for (int i = 0; i < partes.length; i++) {
            int altura = Integer.parseInt(partes[i]);
            ordenVasos[i]= (altura + 1) / 2;
        }
        torreActual = new Tower(n * 30, h + 5);
        for (int num : ordenVasos) {
            torreActual.pushCup(num);
        }
        //Muestra el resultado
        torreActual.makeVisible();
        System.out.println("Solucion: " + solucion);
        System.out.println("Altura lograda: " + torreActual.height() + " cm");
    }
}
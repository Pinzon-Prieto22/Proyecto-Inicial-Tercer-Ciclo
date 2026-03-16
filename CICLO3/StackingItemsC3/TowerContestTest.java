import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
 
/**
 * Pruebas de unidad para TowerContest - Ciclo 3.
 *
 * @author Bryan Pinzon y Valentina Prieto
 * @version CICLO 3
 */
public class TowerContestTest {
    /**
     * Debe resolver bien el enunciado dado
     * No debe decir que es imposible
     */
    @Test
    public void segunElEnunciadoDebeSerPosibleAltura9Con4Cups() {
        // n=4, h=9 -> debe ser posible (alturaMin=7, alturaMax=16)
        String result = TowerContest.resolver(4, 9);
        assertNotEquals("impossible", result);
        assertEquals(9, calcularAltura(result));
    }
    /**
     * Debe decir que es imposible segun el input2 del enunciado
     */
    @Test
    public void segunElEnunciadoDebeSerImposible() {
        // n=4, h=100 -> imposible (alturaMax = 4^2 = 16)
        assertEquals("impossible", TowerContest.resolver(4, 100));
    }
    /**
     * Debe resolver con la altura minima a la que se puede llegar con 3 cups
     */
    @Test
    public void haceTestCon3CupsYSuAlturaMinimaPosible() {
        // n=3: alturaMin = 2*3-1 = 5
        String result = TowerContest.resolver(3, 5);
        assertNotEquals("impossible", result);
        assertEquals(5, calcularAltura(result));
    }
    /**
     * Debe resolver con la altura maxima a la que se puede llegar con 3 cups
     */ 
    @Test
    public void haceTestCon3CupsYSuAlturaMaximaPosible() {
        // n=3: alturaMax = 3^2 = 9
        String result = TowerContest.resolver(3, 9);
        assertNotEquals("impossible", result);
        assertEquals(9, calcularAltura(result));
    }
    /**
     * Debe decir que es imposible hacer una altura de 4 con 3 cups (alturaminima = 5)
     */ 
    @Test
    public void debeDecirQueEsImposibleLograrAltura4Con3Cups() {
        // n=3: alturaMin=5, pedir 4 -> imposible
        assertEquals("impossible", TowerContest.resolver(3, 4));
    }
    /**
     * Debe decir que es imposible lograr una altura de 10 con 3 cups (alturamaxima = 9)
     */ 
    @Test
    public void debeDecirQueEsImposibleLograrAltura10Con3Cups() {
        // n=3: alturaMax=9, pedir 10 -> imposible
        assertEquals("impossible", TowerContest.resolver(3, 10));
    }
    /**
     * Debe decir que es imposible lograr una altura de 7 con 3 cups (no se puede)
     */ 
    @Test
    public void debeDecirQueEsImposibleLograrAltura7Con3Cups() {
        // n=3: alturas alcanzables = {5, 6, 8, 9}; h=7 no es alcanzable
        assertEquals("impossible", TowerContest.resolver(3, 7));
    }
    /**
     * Debe resolver bien altura 1 con 1 cup
     */
    @Test
    public void debeResolverBienAltura1ConCup1() {
        assertEquals("1", TowerContest.resolver(1, 1));
    }
    /**
     * Debe decir que es imposible lograr una altura 2 con 1 cup
     */ 
    @Test
    public void debeDecirQueEsImposibleLograrAltura2Con1Cup() {
        assertEquals("impossible", TowerContest.resolver(1, 2));
    }
    /**
     * Calcula la altura real de la torre dada la secuencia solucion
     * Entra el parametro:
     * solucion = string de alturas separadas por espacios
     * Retorna:
     * altura total calculada en cm
     */
    private long calcularAltura(String solucion) {
        String[] partes = solucion.split(" ");
        long[] alturas  = new long[partes.length];
        for (int i = 0; i < partes.length; i++) {
            alturas[i] = Long.parseLong(partes[i]);
        }
        ArrayList<Long> extH = new ArrayList<>();
        ArrayList<Long> extIntTop = new ArrayList<>();
        long globalTop = 0;
        long maxCima   = 0;
        for (long h : alturas) {
            boolean metido = false;
            for (int e = extH.size() - 1; e >= 0; e--) {
                if (h < extH.get(e)) {
                    long base = extIntTop.get(e);
                    long cima = base + h;
                    extIntTop.set(e, cima);
                    maxCima = Math.max(maxCima, cima);
                    while (extH.size() > e + 1) {
                        extH.remove(extH.size() - 1);
                        extIntTop.remove(extIntTop.size() - 1);
                    }
                    extH.add(h);
                    extIntTop.add(base + 1);
                    metido = true;
                    break;
                }
            }
            if (!metido) {
                long cima = globalTop + h;
                globalTop = cima;
                maxCima   = Math.max(maxCima, cima);
                extH.clear();
                extIntTop.clear();
                extH.add(h);
                extIntTop.add(globalTop - h + 1);
            }
        }
        return maxCima;
    }
}
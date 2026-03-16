import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
/**
 * @author Bryan Pinzón y Valentina Prieto
 * @version CICLO 2
 */
public class TowerC2Test {
    private Tower tower;
    @BeforeEach
    public void setUp() {
        tower = new Tower(300, 100);
    }
    /**
     * Debe agregar un vaso correctamente.
     * Verifica que la operación sea exitosa y que la torre tenga 1 elemento.
     */
    @Test
    public void debeAgregarVasoCorrectamente() {
        tower.pushCup(3);
        assertTrue(tower.ok());
        assertEquals(1, tower.stackingItems().length);
    }
    
    /**
     * No debe permitir agregar un vaso duplicado.
     * Verifica que la operación falle y no aumente el tamaño de la torre.
     */
    @Test
    public void noDebePermitirVasoDuplicado() {
        tower.pushCup(3);
        tower.pushCup(3);
        assertFalse(tower.ok());
        assertEquals(1, tower.stackingItems().length);
    }

    /**
     * Debe permitir agregar múltiples vasos diferentes.
     */
    @Test
    public void debeAgregarMultiplesVasosDiferentes() {
        tower.pushCup(1);
        tower.pushCup(2);
        tower.pushCup(3);
        assertTrue(tower.ok());
        assertEquals(3, tower.stackingItems().length);
    }

    /**
     * Debe eliminar el vaso superior.
     */
    @Test
    public void debeEliminarVasoSuperior() {
        tower.pushCup(2);
        tower.pushCup(4);
        tower.popCup();
        assertTrue(tower.ok());
        assertEquals(1, tower.stackingItems().length);
        assertEquals("2", tower.stackingItems()[0][1]);
    }

    /**
     * No debe eliminar vaso si la torre está vacía.
     */
    @Test
    public void noDebeEliminarVasoEnTorreVacia() {
        tower.popCup();
        assertFalse(tower.ok());
    }

    /**
     * Eliminar un vaso debe eliminar también su tapa si está cubierto.
     */
    @Test
    public void eliminarVasoDebeEliminarTapaAsociada() {
        tower.pushCup(2);
        tower.pushLid(2);
        tower.cover();
        tower.popCup();
        assertTrue(tower.ok());
        assertEquals(0, tower.stackingItems().length);
    }

    /**
     * Debe eliminar un vaso específico por número.
     */
    @Test
    public void debeEliminarVasoPorNumero() {
        tower.pushCup(1);
        tower.pushCup(2);
        tower.removeCup(1);
        assertTrue(tower.ok());
        assertEquals(1, tower.stackingItems().length);
        assertEquals("2", tower.stackingItems()[0][1]);
    }

    /**
     * No debe eliminar un vaso inexistente.
     */
    @Test
    public void noDebeEliminarVasoInexistente() {
        tower.pushCup(1);
        tower.removeCup(5);
        assertFalse(tower.ok());
        assertEquals(1, tower.stackingItems().length);
    }

    /**
     * Debe agregar una tapa correctamente.
     */
    @Test
    public void debeAgregarTapaCorrectamente() {
        tower.pushLid(2);
        assertTrue(tower.ok());
        assertEquals("lid", tower.stackingItems()[0][0]);
    }

    /**
     * No debe permitir tapas duplicadas.
     */
    @Test
    public void noDebePermitirTapaDuplicada() {
        tower.pushLid(2);
        tower.pushLid(2);
        assertFalse(tower.ok());
    }

    /**
     * Debe sumar correctamente la altura total.
     */
    @Test
    public void debeCalcularAlturaTotalCorrectamente() {
        tower.pushCup(1); // 1 cm
        tower.pushCup(2); // 3 cm
        tower.pushLid(1); // NO SUMA PORQUE QUEDA DENTRO
        assertEquals(4, tower.height());
    }

    /**
     * Altura debe ser 0 si la torre está vacía.
     */
    @Test
    public void alturaDebeSerCeroEnTorreVacia() {
        assertEquals(0, tower.height());
    }

    /**
     * Debe ordenar la torre de mayor a menor.
     */
    @Test
    public void debeOrdenarTorreDeMayorAMenor() {
        tower.pushCup(1);
        tower.pushCup(3);
        tower.pushCup(2);
        tower.orderTower();
        String[][] items = tower.stackingItems();
        assertEquals("3", items[0][1]);
        assertEquals("1", items[items.length - 1][1]);
    }

    /**
     * Debe invertir el orden de la torre.
     */
    @Test
    public void debeInvertirOrdenDeTorre() {
        tower.pushCup(1);
        tower.pushCup(2);
        tower.pushCup(3);
        tower.reverseTower();
        String[][] items = tower.stackingItems();
        assertEquals("3", items[0][1]);
        assertEquals("1", items[items.length - 1][1]);
    }

    /**
     * Debe tapar vasos cuando exista tapa correspondiente.
     */
    @Test
    public void debeTaparVasoConTapaCorrespondiente() {
        tower.pushCup(3);
        tower.pushLid(3);
        tower.cover();
        int[] lided = tower.lidedCups();
        assertEquals(1, lided.length);
        assertEquals(3, lided[0]);
    }

    /**
     * Debe fallar cover cuando no exista tapa correspondiente.
     */
    @Test
    public void noDebeTaparSiNoExisteTapaCorrespondiente() {
        tower.pushCup(2);
        tower.pushLid(4);
        tower.cover();
        assertFalse(tower.ok());
    }

    /**
     * Debe intercambiar dos vasos correctamente.
     */
    @Test
    public void debeIntercambiarDosVasos() {
        tower.pushCup(1);
        tower.pushCup(2);
        String[] o1 = {"cup", "1"};
        String[] o2 = {"cup", "2"};
        tower.swap(o1, o2);
        assertTrue(tower.ok());
    }

    /**
     * No debe intercambiar elementos inexistentes.
     */
    @Test
    public void noDebeIntercambiarElementoInexistente() {
        tower.pushCup(1);
        String[] o1 = {"cup", "1"};
        String[] o2 = {"cup", "9"};
        tower.swap(o1, o2);
        assertFalse(tower.ok());
    }

    /**
     * ok() debe ser true después de una operación exitosa.
     */
    @Test
    public void okDebeSerTrueTrasOperacionExitosa() {
        tower.pushCup(1);
        assertTrue(tower.ok());
    }

    /**
     * ok() debe ser false después de una operación fallida.
     */
    @Test
    public void okDebeSerFalseTrasOperacionFallida() {
        tower.popCup();
        assertFalse(tower.ok());
    }

    /**
     * ok() debe volver a true tras una operación exitosa posterior a un fallo.
     */
    @Test
    public void okDebeReiniciarseTrasOperacionExitosa() {
        tower.popCup();
        assertFalse(tower.ok());
        tower.pushCup(1);
        assertTrue(tower.ok());
    }
}

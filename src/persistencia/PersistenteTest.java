package persistencia;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import modelo.Entidade;

// ---

class MockEntidade extends Entidade {
  public MockEntidade(int id) {
    super(id);
  }
}

// ---

public class PersistenteTest {

  private Persistente<MockEntidade> persistente;
  private MockEntidade entidade1;
  private MockEntidade entidade2;

  @BeforeEach
  void setUp() {
    persistente = new Persistente<>();
    entidade1 = new MockEntidade(1);
    entidade2 = new MockEntidade(2);
  }

  // um caso de teste que insere uma entidade com id ainda não existente
  @Test
  void testeInserirIdNaoExistente() throws InexistentIdException {
    assertEquals(0, persistente.getSize());
    boolean saida = persistente.insert(entidade1);
    assertEquals(1, persistente.getSize());
    assertEquals(entidade1, persistente.findById(1));
    assertTrue(saida);
  }

  // um caso de teste que insere uma entidade com id já existente
  @Test
  void testeInserirIdJaExistente() throws InexistentIdException {
    persistente.insert(entidade1);
    assertEquals(1, persistente.getSize());

    // Nova entidade com o mesmo ID
    MockEntidade entidadeDuplicada = new MockEntidade(1);
    boolean saida = persistente.insert(entidadeDuplicada);

    assertEquals(1, persistente.getSize());
    assertEquals(entidade1, persistente.findById(1));
    assertFalse(saida);
  }

  // um caso de teste que altera uma entidade com id não existente
  @Test
  void testeAlterarIdNaoExistente() throws InexistentIdException {
    persistente.insert(entidade1);
    MockEntidade entidadeParaAlterar = new MockEntidade(99);

    assertThrows(InexistentIdException.class, () -> {
      persistente.update(99, entidadeParaAlterar);
    });

    assertEquals(1, persistente.getSize());
    assertEquals(entidade1, persistente.findById(1));
  }

  // um caso de teste que altera uma entidade com id existente
  @Test
  void testeAlterarIdExistente() throws InexistentIdException {
    persistente.insert(entidade1);
    MockEntidade entidadeAlterada = new MockEntidade(1);

    Boolean resultado = persistente.update(1, entidadeAlterada);

    assertTrue(resultado);
    assertEquals(entidadeAlterada, persistente.findById(1));
  }

  // um caso de teste que remove uma entidade com id não existente
  @Test
  void testeApagarIdNaoExistente() {
    persistente.insert(entidade1);
    assertEquals(1, persistente.getSize());

    assertThrows(InexistentIdException.class, () -> {
      persistente.remove(99);
    });

    assertEquals(1, persistente.getSize());
  }

  // um caso de teste que remove uma entidade com id já existente
  @Test
  void testeApagarIdExistente() throws InexistentIdException {
    persistente.insert(entidade1);
    assertEquals(1, persistente.getSize());

    Boolean resultado = persistente.remove(1);

    assertTrue(resultado);
    assertEquals(0, persistente.getSize());
  }

  // um caso de teste que busca uma entidade com id não existente
  @Test
  void testeBuscarIdNaoExistente() {
    persistente.insert(entidade1);
    assertThrows(InexistentIdException.class, () -> {
      persistente.findById(99);
    });
  }

  // um caso de teste que busca uma entidade com id existente
  @Test
  void testeBuscarIdExistente() throws InexistentIdException {
    persistente.insert(entidade1);
    persistente.insert(entidade2);

    MockEntidade entidadeEncontrada = persistente.findById(2);

    assertNotNull(entidadeEncontrada);
    assertEquals(entidade2, entidadeEncontrada);
  }

}
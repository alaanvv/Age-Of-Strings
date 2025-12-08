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
    persistente.insert(entidade1);
    assertEquals(1, persistente.getSize());
    assertEquals(entidade1, persistente.findById(1));
  }

  // um caso de teste que insere uma entidade com id já existente
  @Test
  void testeInserirIdJaExistente() throws InexistentIdException {
    persistente.insert(entidade1);
    assertEquals(1, persistente.getSize());

    // Nova entidade com o mesmo ID
    MockEntidade entidadeDuplicada = new MockEntidade(1);
    persistente.insert(entidadeDuplicada);

    assertEquals(1, persistente.getSize());
    assertEquals(entidadeDuplicada, persistente.findById(1));
  }

  // um caso de teste que altera uma entidade com id não existente
  @Test
  void testeAlterarIdNaoExistente() {
    persistente.insert(entidade1);
    MockEntidade entidadeParaAlterar = new MockEntidade(99);

    Boolean resultado = persistente.update(99, entidadeParaAlterar);

    assertFalse(resultado);
    assertEquals(1, persistente.getSize());
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

  // region Testes de Apagar
  @Test
  void testeApagarIdNaoExistente() {
    persistente.insert(entidade1);
    assertEquals(1, persistente.getSize());

    Boolean resultado = persistente.remove(99);

    assertFalse(resultado);
    assertEquals(1, persistente.getSize());
  }

  @Test
  void testeApagarIdExistente() {
    persistente.insert(entidade1);
    assertEquals(1, persistente.getSize());

    Boolean resultado = persistente.remove(1);

    assertTrue(resultado);
    assertEquals(0, persistente.getSize());
  }
  // endregion

  // region Testes de Buscar
  @Test
  void testeBuscarIdNaoExistente() {
    persistente.insert(entidade1);
    assertThrows(InexistentIdException.class, () -> {
      persistente.findById(99);
    });
  }

  @Test
  void testeBuscarIdExistente() throws InexistentIdException {
    persistente.insert(entidade1);
    persistente.insert(entidade2);

    MockEntidade entidadeEncontrada = persistente.findById(2);

    assertNotNull(entidadeEncontrada);
    assertEquals(entidade2, entidadeEncontrada);
  }
  // endregion

  // region Testes de Exists
  @Test
  void testeExistsQuandoIdExiste() {
    persistente.insert(entidade1); // Adiciona uma entidade com ID 1
    assertTrue(persistente.exists(1), "Deve retornar true para um ID existente.");
  }

  @Test
  void testeExistsQuandoIdNaoExiste() {
    persistente.insert(entidade1); // Adiciona ID 1
    assertFalse(persistente.exists(99), "Deve retornar false para um ID que não existe.");
  }

  @Test
  void testeExistsEmColecaoVazia() {
    assertFalse(persistente.exists(1), "Deve retornar false para qualquer ID em uma coleção vazia.");
  }
  // endregion
}

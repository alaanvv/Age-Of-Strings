package persistencia;

import modelo.Entidade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Uma classe mock simples que estende Entidade para usar nos testes.
class MockEntidade extends Entidade {
    private String nome;

    public MockEntidade(int id, String nome) {
        super(id);
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MockEntidade that = (MockEntidade) obj;
        return getId().equals(that.getId()) && nome.equals(that.nome);
    }
}

public class PersistenteTest {

    private Persistente<MockEntidade> persistente;
    private MockEntidade entidade1;
    private MockEntidade entidade2;

    @BeforeEach
    void setUp() {
        persistente = new Persistente<>();
        entidade1 = new MockEntidade(1, "Entidade Um");
        entidade2 = new MockEntidade(2, "Entidade Dois");
    }

    //region Testes de Inserir
    @Test
    void testInserir_IdNaoExistente() throws InexistentIdException {
        assertEquals(0, persistente.getSize());
        persistente.insert(entidade1);
        assertEquals(1, persistente.getSize());
        assertEquals(entidade1, persistente.findById(1));
    }

    @Test
    void testInserir_IdJaExistente() throws InexistentIdException {
        persistente.insert(entidade1);
        assertEquals(1, persistente.getSize());

        // Inserindo uma nova entidade com o mesmo ID
        MockEntidade entidadeDuplicada = new MockEntidade(1, "Entidade Duplicada");
        persistente.insert(entidadeDuplicada);

        // O tamanho deve continuar 1, e o valor antigo deve ser substituído
        assertEquals(1, persistente.getSize());
        assertEquals(entidadeDuplicada, persistente.findById(1));
    }
    //endregion

    //region Testes de Alterar
    @Test
    void testAlterar_IdNaoExistente() {
        persistente.insert(entidade1);
        MockEntidade entidadeParaAlterar = new MockEntidade(99, "Entidade Nao Existente");

        Boolean resultado = persistente.update(99, entidadeParaAlterar);

        assertFalse(resultado);
        assertEquals(1, persistente.getSize());
    }

    @Test
    void testAlterar_IdExistente() throws InexistentIdException {
        persistente.insert(entidade1);
        MockEntidade entidadeAlterada = new MockEntidade(1, "Entidade Um Alterada");

        Boolean resultado = persistente.update(1, entidadeAlterada);

        assertTrue(resultado);
        assertEquals("Entidade Um Alterada", persistente.findById(1).getNome());
    }
    //endregion

    //region Testes de Apagar
    @Test
    void testApagar_IdNaoExistente() {
        persistente.insert(entidade1);
        assertEquals(1, persistente.getSize());

        Boolean resultado = persistente.remove(99);

        assertFalse(resultado);
        assertEquals(1, persistente.getSize());
    }

    @Test
    void testApagar_IdExistente() {
        persistente.insert(entidade1);
        assertEquals(1, persistente.getSize());

        Boolean resultado = persistente.remove(1);

        assertTrue(resultado);
        assertEquals(0, persistente.getSize());
    }
    //endregion

    //region Testes de Buscar
    @Test
    void testBuscar_IdNaoExistente() {
        persistente.insert(entidade1);
        assertThrows(InexistentIdException.class, () -> {
            persistente.findById(99);
        });
    }

    @Test
    void testBuscar_IdExistente() throws InexistentIdException {
        persistente.insert(entidade1);
        persistente.insert(entidade2);

        MockEntidade entidadeEncontrada = persistente.findById(2);

        assertNotNull(entidadeEncontrada);
        assertEquals(entidade2, entidadeEncontrada);
    }
    //endregion

    //region Testes de Exists
    @Test
    void testExists_QuandoIdExiste() {
        persistente.insert(entidade1); // Adiciona uma entidade com ID 1
        assertTrue(persistente.exists(1), "Deve retornar true para um ID existente.");
    }

    @Test
    void testExists_QuandoIdNaoExiste() {
        persistente.insert(entidade1); // Adiciona ID 1
        assertFalse(persistente.exists(99), "Deve retornar false para um ID que não existe.");
    }

    @Test
    void testExists_EmColecaoVazia() {
        assertFalse(persistente.exists(1), "Deve retornar false para qualquer ID em uma coleção vazia.");
    }
    //endregion
}

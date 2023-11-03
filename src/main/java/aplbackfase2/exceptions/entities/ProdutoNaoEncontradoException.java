package aplbackfase2.exceptions.entities;

public class ProdutoNaoEncontradoException  extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ProdutoNaoEncontradoException() {
        super("Produto não encontrado");
    }

    public ProdutoNaoEncontradoException(String msg) {
        super(msg);
    }
}

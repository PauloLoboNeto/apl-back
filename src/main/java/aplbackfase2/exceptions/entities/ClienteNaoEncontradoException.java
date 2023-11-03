package aplbackfase2.exceptions.entities;

public class ClienteNaoEncontradoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ClienteNaoEncontradoException() {
        super("Cliente não encontrado");
    }

    public ClienteNaoEncontradoException(String msg) {
        super(msg);
    }
}

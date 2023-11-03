package aplbackfase2.entities;

import lombok.*;

import java.util.UUID;

@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PedidoFila {

    private Pedido pedido;
    private Long numeroNaFila;

    public PedidoFila (Pedido pedido) {
        this.pedido = pedido;
    }

}

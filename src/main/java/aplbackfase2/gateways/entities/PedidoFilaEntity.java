package aplbackfase2.gateways.entities;

import aplbackfase2.entities.PedidoFila;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "fila",
        uniqueConstraints = @UniqueConstraint(columnNames = {"numero_na_fila", "id_pedido"})
)
public class PedidoFilaEntity {

    @Id
    @GeneratedValue
    @Column(name = "numero_na_fila")
    private Long numeroNaFila;

    @ManyToOne
    @JoinColumn(name = "id_pedido")
    private PedidoEntity pedido;

    public PedidoFilaEntity(PedidoFila pedidoFila) {
        this.pedido = new PedidoEntity().from(pedidoFila.getPedido(), false);
        this.numeroNaFila = pedidoFila.getNumeroNaFila();
    }

    public PedidoFila toPedidoFila() {
        var pedidoFila = new PedidoFila();
        pedidoFila.setNumeroNaFila(this.numeroNaFila);
        pedidoFila.setPedido(this.pedido.to());
        return pedidoFila;
    }
}

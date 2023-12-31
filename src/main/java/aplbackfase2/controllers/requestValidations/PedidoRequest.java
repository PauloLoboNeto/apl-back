package aplbackfase2.controllers.requestValidations;

import aplbackfase2.entities.Cliente;
import aplbackfase2.entities.Pedido;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRequest {

    @NotNull(message = "id cliente não pode estar vazio")
    private UUID idCliente;

    public Pedido from(PedidoRequest request, Cliente cliente) {

        return Pedido.builder()
                .cliente(cliente)
                .build();
    }
}

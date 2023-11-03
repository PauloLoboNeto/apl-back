package aplbackfase2.gateways.entities;

import aplbackfase2.entities.PedidoProduto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "pedido_produtos")
public class PedidoProdutoEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "id_pedido")
    private PedidoEntity pedido;

    @ManyToOne
    @JoinColumn(name = "id_produto")
    private ProdutoEntity produto;

    @Column(name = "v_produto")
    private BigDecimal valorProduto;

    @Column(name = "txt_obs_produto")
    private String observacaoProduto;

    public static PedidoProduto to(PedidoProdutoEntity pedidoProdutoEntity) {
        return PedidoProduto.builder()
                .id(pedidoProdutoEntity.getId())
                .pedidoId(pedidoProdutoEntity.getPedido().getIdPedido())
                .produtoId(pedidoProdutoEntity.getProduto().getIdProduto())
                .produtoDescricao(pedidoProdutoEntity.getProduto().getDescricaoProduto())
                .valorProduto(pedidoProdutoEntity.getValorProduto())
                .observacaoProduto(pedidoProdutoEntity.getObservacaoProduto())
                .build();
    }

    public static PedidoProdutoEntity from(PedidoProduto pedidoProduto, PedidoEntity pedidoEntity, ProdutoEntity produtoEntity) {
        return PedidoProdutoEntity.builder()
                .id(pedidoProduto.getId())
                .pedido(pedidoEntity)
                .produto(produtoEntity)
                .valorProduto(pedidoProduto.getValorProduto())
                .observacaoProduto(pedidoProduto.getObservacaoProduto())
                .build();
    }
}

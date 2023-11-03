package aplbackfase2.gateways.entities;

import aplbackfase2.utils.enums.StatusPagamento;
import aplbackfase2.utils.enums.StatusPedido;
import aplbackfase2.entities.Pedido;
import aplbackfase2.entities.PedidoProduto;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.Calendar;
import java.util.stream.Collectors;

@Entity
@Data
@AllArgsConstructor
@Builder
@Table(name = "pedidos")
public class PedidoEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID idPedido;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    @NotNull
    private ClienteEntity cliente;

    @Enumerated(EnumType.STRING)
    @Column(name = "id_status")
    @NotNull
    private StatusPedido statusPedido;

    @Enumerated(EnumType.STRING)
    @Column(name = "id_status_pagamento")
    @NotNull
    private StatusPagamento statusPagamento;

    @Column(name = "v_pedido")
    private BigDecimal valorPedido;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dt_h_inclusao")
    private Date dataInclusao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dt_h_atualizacao")
    private Date dataAtualizacao;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PedidoProdutoEntity> produtos;

    public PedidoEntity() {}

    public Pedido to() {
        List<PedidoProduto> pedidoProdutos = null;
        if (this.produtos != null) {
            pedidoProdutos = this.produtos.stream()
                    .map(PedidoProdutoEntity::to)
                    .collect(Collectors.toList());
        }
        return Pedido.builder()
                .idPedido(this.idPedido)
                .cliente(new ClienteEntity().to(this.cliente))
                .statusPedido(this.statusPedido)
                .statusPagamento(this.statusPagamento)
                .valorPedido(this.valorPedido)
                .produtos(pedidoProdutos)
                .dataInclusao(this.dataInclusao)
                .dataAtualizacao(this.dataAtualizacao)
                .build();
    }

    public PedidoEntity from(Pedido pedido, boolean isCreated) {
        PedidoEntityBuilder pedidoEntityBuilder = PedidoEntity.builder()
                .idPedido(pedido.getIdPedido())
                .cliente(new ClienteEntity().from(pedido.getCliente()))
                .valorPedido(pedido.getValorPedido())
                .dataInclusao(pedido.getDataInclusao())
                .dataAtualizacao(pedido.getDataAtualizacao());

        if(isCreated) {
            pedidoEntityBuilder.dataInclusao(this.obterDataHoraAtual());
            pedidoEntityBuilder.statusPedido(StatusPedido.A);
            pedidoEntityBuilder.statusPagamento(StatusPagamento.PENDENTE);
        } else {
            pedidoEntityBuilder.statusPedido(pedido.getStatusPedido());
            pedidoEntityBuilder.statusPagamento(pedido.getStatusPagamento());
        }

        return pedidoEntityBuilder.build();
    }

    private Date obterDataHoraAtual(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE),
                calendar.get(Calendar.HOUR),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND));
        return new Date(calendar.getTime().getTime());
    }
}

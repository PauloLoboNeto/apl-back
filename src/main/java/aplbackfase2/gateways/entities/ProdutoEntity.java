package aplbackfase2.gateways.entities;

import aplbackfase2.entities.Produto;
import aplbackfase2.utils.enums.TipoProduto;
import aplbackfase2.entities.DescricaoProduto;
import aplbackfase2.entities.NomeProduto;
import aplbackfase2.entities.ValorProduto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "produtos")
public class ProdutoEntity {
    @Id
    @GeneratedValue
    private UUID idProduto;

    @Column(name = "txt_nome_produto")
    @Embedded
    private NomeProduto nomeProduto;

    @Column(name = "txt_descricao_produto")
    @Embedded
    private DescricaoProduto descricaoProduto;

    private String tipoProduto;

    @Column(name = "v_produto")
    @Embedded
    private ValorProduto valorProduto;


    @Column(name = "dt_h_atualizacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAtualizacao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dt_h_inclusao")
    private Date dataCriacao;

    private Boolean ativo;

    public Produto to(ProdutoEntity produtoEntity) {
        return Produto.builder()
                .idProduto(produtoEntity.getIdProduto())
                .nomeProduto(produtoEntity.getNomeProduto())
                .descricaoProduto(produtoEntity.getDescricaoProduto())
                .tipoProduto(TipoProduto.fromCodigo(produtoEntity.getTipoProduto()))
                .valorProduto(produtoEntity.getValorProduto())
                .ativo(produtoEntity.getAtivo())
                .build();
    }

    public ProdutoEntity from(Produto produto, boolean isCreated) {
        ProdutoEntityBuilder produtoEntityBuilder = ProdutoEntity.builder()
                .nomeProduto(produto.getNomeProduto())
                .descricaoProduto(produto.getDescricaoProduto())
                .tipoProduto(produto.getTipoProduto().getCodigo())
                .valorProduto(produto.getValorProduto())
                .ativo(produto.getAtivo());

        if(isCreated) {
            produtoEntityBuilder.dataCriacao(this.obterDataHoraAtual());
            produtoEntityBuilder.ativo(true);
        }

            produtoEntityBuilder.dataAtualizacao(this.obterDataHoraAtual());

        return produtoEntityBuilder.build();
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

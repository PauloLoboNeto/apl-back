package aplbackfase2.gateways;

import aplbackfase2.entities.PedidoFila;
import aplbackfase2.exceptions.entities.PedidoNaoEncontradoException;
import aplbackfase2.exceptions.entities.PedidoOperacaoNaoSuportadaException;
import aplbackfase2.gateways.entities.PedidoEntity;
import aplbackfase2.gateways.entities.PedidoFilaEntity;
import aplbackfase2.gateways.entities.PedidoProdutoEntity;
import aplbackfase2.interfaces.gateways.IPedidoRepositoryPort;
import aplbackfase2.interfaces.repositories.FilaRepository;
import aplbackfase2.interfaces.repositories.PedidoProdutoRepository;
import aplbackfase2.interfaces.repositories.PedidoRepository;
import aplbackfase2.entities.Pedido;
import aplbackfase2.utils.enums.StatusPedido;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoRepositoryAdapter implements IPedidoRepositoryPort {

    private final PedidoRepository pedidoRepository;
    private final PedidoProdutoRepository pedidoProdutoRepository;
    private final FilaRepository filaRepository;


    @Override
    @Transactional
    public Pedido cadastrar(Pedido pedido) {
        PedidoEntity pedidoEntity = new PedidoEntity().from(pedido, true);
        return this.pedidoRepository.save(pedidoEntity).to();
    }

    @Override
    @Transactional
    public Pedido atualizar(Pedido pedido) {
        PedidoEntity existingPedidoEntity = this.pedidoRepository.findById(pedido.getIdPedido())
                .orElseThrow(() -> new PedidoNaoEncontradoException("Pedido não encontrado, id: " + pedido.getIdPedido()));
        existingPedidoEntity = existingPedidoEntity.from(pedido, false);
        return this.pedidoRepository.save(existingPedidoEntity).to();
    }

    @Override
    @Transactional
    public Pedido atualizarStatus(StatusPedido status, UUID idPedido) throws PedidoNaoEncontradoException {
        Pedido pedido = buscarPorId(idPedido)
                .orElseThrow(() -> new PedidoNaoEncontradoException());
        pedido.setStatusPedido(status);
        return atualizar(pedido);
    }

    @Override
    @Transactional
    public void remover(UUID idPedido) {
        PedidoEntity pedidoEntity = this.pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new PedidoNaoEncontradoException("Pedido not found, id: " + idPedido));

        pedidoEntity.getProdutos().forEach(pedidoProdutoEntity -> {
            this.pedidoProdutoRepository.deleteById(pedidoProdutoEntity.getId());
        });

        Optional<PedidoFilaEntity> fila = this.filaRepository.findByIdPedido(idPedido);
        if (fila.isPresent()) {
            this.filaRepository.deleteById(fila.get().getNumeroNaFila());
        }


        this.pedidoRepository.delete(pedidoEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> buscarTodos(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return this.pedidoRepository.listagemOrdenadaPorStatusExcluindoFinalizados(pageable).stream()
                .map(PedidoEntity::to)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Pedido> buscarPorId(UUID idPedido) {
        return this.pedidoRepository.findByIdPedido(idPedido)
                .map(PedidoEntity::to);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> buscarPedidosPorCliente(UUID idCliente) {
        return this.pedidoRepository.findByIdCliente(idCliente).stream()
                .map(PedidoEntity::to)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> buscarPedidosPorStatus(StatusPedido statusPedido) {
        return this.pedidoRepository.findByStatusPedido(statusPedido).stream()
                .map(PedidoEntity::to)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> buscarPedidosPorClienteEStatus(UUID idCliente, StatusPedido statusPedido) {
        return this.pedidoRepository.findByIdClienteAndStatusPedido(idCliente, statusPedido.toString()).stream()
                .map(PedidoEntity::to)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Pedido checkout(UUID idPedido) {
        PedidoEntity pedidoEntity = this.pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new PedidoNaoEncontradoException("Pedido não encontrado, id: " + idPedido));

        if (!pedidoEntity.getStatusPedido().equals(StatusPedido.A)) {
            throw new PedidoOperacaoNaoSuportadaException("Pedido precisa estar abertoA");
        }

        Pedido pedido = pedidoEntity.to();
        List<PedidoProdutoEntity> pedidoProdutos = pedidoProdutoRepository.findByPedido(pedido);
        BigDecimal totalValorPedido = pedidoProdutos.stream()
                .map(PedidoProdutoEntity::getValorProduto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        pedidoEntity.setValorPedido(totalValorPedido);
        pedidoEntity.setStatusPedido(StatusPedido.R);

        return this.pedidoRepository.save(pedidoEntity).to();
    }
}

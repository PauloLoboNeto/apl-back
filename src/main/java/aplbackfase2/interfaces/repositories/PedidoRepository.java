package aplbackfase2.interfaces.repositories;

import aplbackfase2.utils.enums.StatusPedido;
import aplbackfase2.gateways.entities.PedidoEntity;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoEntity, UUID> {
    Optional<PedidoEntity> findByIdPedido(UUID idPedido);

    @Query("SELECT p FROM PedidoEntity p WHERE id_cliente = ?1")
    List<PedidoEntity> findByIdCliente(UUID idCliente);

    List<PedidoEntity> findByStatusPedido(StatusPedido statusPedido);

    @Query("SELECT p FROM PedidoEntity p WHERE id_cliente = ?1 AND id_status LIKE ?2")
    List<PedidoEntity> findByIdClienteAndStatusPedido(UUID idCliente, String statusPedido);

    List<PedidoEntity> listagemOrdenadaPorStatusExcluindoFinalizados(Pageable pageable);
}

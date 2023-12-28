package kr.smartisoft.demo.ServerInfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import kr.smartisoft.demo.ServerInfo.entity.Servers;

@Repository
public interface ServersRepository extends JpaRepository<Servers, Long> {
    @Query(value = "SELECT " +
            "  ts.server_idx" +
            ", ts.server_name" +
            ", ts.ip_address" +
            ", ts.port" +
            ", ts.total_disk" +
            ", ts.total_memory" +
            ", ts.gpu1_name" +
            ", ts.gpu1_total_memory" +
            ", ts.gpu1_power_limit" +
            ", ts.gpu2_name" +
            ", ts.gpu2_total_memory" +
            ", ts.gpu2_power_limit" +
            ", ts.gpu3_name" +
            ", ts.gpu3_total_memory" +
            ", ts.gpu3_power_limit" +
            ", ts.gpu4_name" +
            ", ts.gpu4_total_memory" +
            ", ts.gpu4_power_limit" +
            ", ts.created_at" +
            " FROM tbl_servers ts " +
            " WHERE ts.port = :reqPort", nativeQuery = true)
    Servers findByPort(@Param(value = "reqPort") String reqPort);
}

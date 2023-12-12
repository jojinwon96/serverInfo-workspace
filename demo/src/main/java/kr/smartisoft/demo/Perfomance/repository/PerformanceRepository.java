package kr.smartisoft.demo.Perfomance.repository;

import kr.smartisoft.demo.Perfomance.entity.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {

    @Query(value = "SELECT " +
            "  p.idx" +
            ", p.server_name" +
            ", p.used_cpu" +
            ", p.cpu_time" +
            ", p.total_memory" +
            ", p.used_memory" +
            ", p.free_memory" +
            ", p.total_disk" +
            ", p.used_disk" +
            ", p.free_disk" +
            ", p.gpu_name" +
            ", p.gpu_bus_id" +
            ", p.gpu_temp" +
            ", p.gpu_utili" +
            ", p.gpu_memory_utili" +
            ", p.gpu_total_memory" +
            ", p.gpu_used_memory" +
            ", p.gpu_free_memory" +
            ", p.gpu_power_draw" +
            ", p.gpu_power_limit" +
            ", p.gpu_fan_speed" +
            " FROM tbl_performance p " +
            " WHERE p.server_name = :serverName" +
            " ORDER BY p.cpu_time DESC LIMIT 1", nativeQuery = true)
    Performance findByServerName(@Param(value = "serverName") int serverName);
}

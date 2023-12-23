package kr.smartisoft.demo.ServerInfo.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@ToString
@Table(name = "tbl_servers_spec")
@EntityListeners(AuditingEntityListener.class)
public class ServersSpec {

    @Id
    @Column(columnDefinition = "INTEGER")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx; // 기본키

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "server_idx")
    private Servers servers;

    @CreationTimestamp
    @Column(name = "cpu_time")
    private LocalDateTime cpuDate; // cpu 현재 시간

    @Column(name = "used_cpu")
    private double usageCPU; // CPU 이용률

    @Column(name = "total_memory")
    private double totalMemorySize; // 총 메모리 사이즈

    @Column(name = "used_memory")
    private double useMemorySize; // 사용중인 메모리

    @Column(name = "free_memory")
    private double freeMemorySize; // 사용 가능한 메모리

    @Column(name = "total_disk")
    private double totalDiskSize; // DISK 총량

    @Column(name = "used_disk")
    private double useDiskSize; // DISK 사용 용량

    @Column(name = "free_disk")
    private double freeDiskSize; // DISK 남은 용량

    @Column(name = "gpu_name")
    private String gpuName; // GPU 이름

    @Column(name = "gpu_temp")
    private int temperatureGPU; // gpu 온도

    @Column(name = "gpu_utili")
    private int utilizationGPU; // gpu 가용

    @Column(name = "gpu_memory_utili")
    private int utilizationMemory; // 메모리 가용

    @Column(name = "gpu_total_memory")
    private int totalMemory; // 총 GPU 메모리

    @Column(name = "gpu_used_memory")
    private int usedMemory; // 사용중인 GPU 메모리

    @Column(name = "gpu_free_memory")
    private int freeMemory; // 사용 가능한 GPU 메모리

    @Column(name = "gpu_power_draw")
    private double powerDraw; // 사용전력

    @Column(name = "gpu_power_limit")
    private double powerLimit; // 최대 전력

    @Column(name = "gpu_fan_speed")
    private int fanSpeed; // 팬 속도

}

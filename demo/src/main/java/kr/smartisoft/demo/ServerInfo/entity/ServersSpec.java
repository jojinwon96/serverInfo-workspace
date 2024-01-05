package kr.smartisoft.demo.ServerInfo.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@ToString
@Component
@Table(name = "tbl_server_spec")
@EntityListeners(AuditingEntityListener.class)
public class ServersSpec {

    @Id
    @Column(columnDefinition = "INTEGER")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serverSpecIdx; // 기본키

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "server_idx")
    private Servers servers;

    @CreationTimestamp
    @Column(name = "cpu_time")
    private LocalDateTime cpuDate; // cpu 현재 시간

    @Column(name = "used_cpu")
    private double usedCPU; // CPU 이용률

    @Column(name = "free_disk")
    private String freeDiskSize; // DISK 남은 용량

    @Column(name = "used_disk")
    private String useDiskSize; // DISK 사용 용량

    @Column(name = "free_memory")
    private String freeMemorySize; // 사용 가능한 메모리

    @Column(name = "used_memory")
    private String useMemorySize; // 사용중인 메모리

    @Column(name = "avaliable_memory")
    private String avaliableSize;

    @Column(name = "gpu1_free_memory")
    private int gpu1FreeMemory; // 사용 가능한 GPU 메모리

    @Column(name = "gpu1_used_memory")
    private int gpu1UsedMemory; // 사용중인 GPU 메모리

    @Column(name = "gpu1_power_draw")
    private double gpu1PowerDraw; // 사용전력

    @Column(name = "gpu1_temp")
    private int temperatureGPU1; // gpu 온도

    @Column(name = "gpu1_fan_speed")
    private int gpu1FanSpeed; // 팬 속도

    @Column(name = "gpu1_utili")
    private int utilizationGPU1;

    @Column(name = "gpu1_memory_utili")
    private int utilizationMemory1;

    @Column(name = "gpu2_free_memory")
    private int gpu2FreeMemory; // 사용 가능한 GPU 메모리

    @Column(name = "gpu2_used_memory")
    private int gpu2UsedMemory; // 사용중인 GPU 메모리

    @Column(name = "gpu2_power_draw")
    private double gpu2PowerDraw; // 사용전력

    @Column(name = "gpu2_temp")
    private int temperatureGPU2; // gpu 온도

    @Column(name = "gpu2_fan_speed")
    private int gpu2FanSpeed; // 팬 속도

    @Column(name = "gpu2_utili")
    private int utilizationGPU2;

    @Column(name = "gpu2_memory_utili")
    private int utilizationMemory2;

    @Column(name = "gpu3_free_memory")
    private int gpu3FreeMemory; // 사용 가능한 GPU 메모리

    @Column(name = "gpu3_used_memory")
    private int gpu3UsedMemory; // 사용중인 GPU 메모리

    @Column(name = "gpu3_power_draw")
    private double gpu3PowerDraw; // 사용전력

    @Column(name = "gpu3_temp")
    private int temperatureGPU3; // gpu 온도

    @Column(name = "gpu3_fan_speed")
    private int gpu3FanSpeed; // 팬 속도

    @Column(name = "gpu3_utili")
    private int utilizationGPU3;

    @Column(name = "gpu3_memory_utili")
    private int utilizationMemory3;

    @Column(name = "gpu4_free_memory")
    private int gpu4FreeMemory; // 사용 가능한 GPU 메모리

    @Column(name = "gpu4_used_memory")
    private int gpu4UsedMemory; // 사용중인 GPU 메모리

    @Column(name = "gpu4_power_draw")
    private double gpu4PowerDraw; // 사용전력

    @Column(name = "gpu4_temp")
    private int temperatureGPU4; // gpu 온도

    @Column(name = "gpu4_fan_speed")
    private int gpu4FanSpeed; // 팬 속도

    @Column(name = "gpu4_utili")
    private int utilizationGPU4;

    @Column(name = "gpu4_memory_utili")
    private int utilizationMemory4;
}

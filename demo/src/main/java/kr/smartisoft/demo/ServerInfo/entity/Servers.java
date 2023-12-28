package kr.smartisoft.demo.ServerInfo.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@ToString
@Component
@Table(name = "tbl_servers")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Servers {

    @Id
    @Column(columnDefinition = "INTEGER")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serverIdx;

    @Column(name = "server_name")
    private String serverName;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "port")
    private String port;

    @Column(name = "total_disk")
    private String totalDisk;

    @Column(name = "total_memory")
    private String totalMemory;

    @Column(name = "gpu1_name")
    private String gpu1Name;

    @Column(name = "gpu1_total_memory")
    private int gpu1TotalMemory;

    @Column(name = "gpu1_power_limit")
    private double gpu1PowerLimit;

    @Column(name = "gpu2_name")
    private String gpu2Name;

    @Column(name = "gpu2_total_memory")
    private int gpu2TotalMemory;

    @Column(name = "gpu2_power_limit")
    private double gpu2PowerLimit;

    @Column(name = "gpu3_name")
    private String gpu3Name;

    @Column(name = "gpu3_total_memory")
    private int gpu3TotalMemory;

    @Column(name = "gpu3_power_limit")
    private double gpu3PowerLimit;

    @Column(name = "gpu4_name")
    private String gpu4Name;

    @Column(name = "gpu4_total_memory")
    private int gpu4TotalMemory;

    @Column(name = "gpu4_power_limit")
    private double gpu4PowerLimit;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createAt;


}

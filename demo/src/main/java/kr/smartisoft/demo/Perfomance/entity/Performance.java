package kr.smartisoft.demo.MonitorInfo.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Getter
@Setter
@ToString
public class Performance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long serverIdx;
    private int serverName; // 서버 이름 (3, 4, 5)
    private Double usageCPU; // CPU 이용률
    private long cpuTime; // 현재 CPU 시간
    private double freeMemorySize; // 사용 가능한 메모리
    private double totalMemorySize; // 총 메모리 사이즈
    private Double totalDiskSize; // DISK 총량
    private Double useDiskSize; // DISK 사용 용량
    private Double freeDiskSize; // DISK 남은 용량
    private String gpuName; // GPU 이름
    private String gpuBusId; // PCI bus id
    private int temperatureGPU;
    private int utilizationGPU; // gpu 가용
    private int utilizationMemory;
    private int totalMemory;
    private int usedMemory;
    private double powerDraw; // 사용전력
    private double powerLimit; // 최대 전력
    private int fanSpeed; // 팬 속도

}

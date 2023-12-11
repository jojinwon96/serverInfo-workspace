package kr.smartisoft.demo.MonitorInfo.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CPUItem {

    private Double usageCPU; // CPU 이용률
    private long cpuTime; // 현재 CPU 시간
}

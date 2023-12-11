package kr.smartisoft.demo.MonitorInfo.entity;

import lombok.*;

@Getter
@Setter
@ToString
public class GPUItem {

    private String gpuName; // GPU 이름
    private String gpuBusId; // PCI bus id
    private int temperatureGPU;
    private int utilizationGPU; // gpu 가용
    private int utilizationMemory;
    private int memboryTotal;
    private int memoryUsed;
    private double powerDraw; // 사용전력
    private double powerLimit; // 최대 전력
    private int fanSpeed;

}

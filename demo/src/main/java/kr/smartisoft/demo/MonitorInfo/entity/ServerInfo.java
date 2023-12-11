package kr.smartisoft.demo.MonitorInfo.entity;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ServerInfo {
    private long serverIdx; // 서버 고유 아이디
    private String serverName; // 서버 이름

    private double freeMemorySize; // 사용 가능한 메모리
    private double totalMemorySize; // 총 메모리 사이즈
    private Double totalDiskSize; // DISK 총량
    private Double useDiskSize; // DISK 사용 용량
    private Double freeDiskSize; // DISK 남은 용량


}

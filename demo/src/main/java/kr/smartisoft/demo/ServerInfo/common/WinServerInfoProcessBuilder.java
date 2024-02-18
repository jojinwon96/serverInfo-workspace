package kr.smartisoft.demo.ServerInfo.common;

import com.sun.management.OperatingSystemMXBean;
import kr.smartisoft.demo.ServerInfo.entity.Servers;
import kr.smartisoft.demo.ServerInfo.entity.ServersSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.List;

@Service
public class WinServerInfoProcessBuilder {

    private static final List<String> gpuInfoCommand = Arrays.asList("nvidia-smi", "--query-gpu=gpu_name,memory.total,power.limit", "--format=csv,noheader,nounits");
    private static final List<String> gpuSpecCommand = Arrays.asList("nvidia-smi", "--query-gpu=temperature.gpu,memory.total,memory.used,power.draw,fan.speed,utilization.gpu,utilization.memory", "--format=csv,noheader,nounits");

    @Autowired
    Servers servers;

    @Autowired
    ServersSpec serversSpec;

    public Servers getServersInfo() {

        servers = cpuAndMemoryInfo();

        return servers;
    }

    public ServersSpec getServerSpec() {

        serversSpec = cpuAndMemorySpec();

        return serversSpec;
    }

    private Servers cpuAndMemoryInfo() {
        Servers cpuAndMemoryInfo = new Servers();

        // CPU & Memory 정보 가져오기
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

        long totalPhysicalMemorySizeBytes = osBean.getTotalPhysicalMemorySize();
        double totalPhysicalMemorySizeGB = totalPhysicalMemorySizeBytes / (1024.0 * 1024.0 * 1024.0);
        cpuAndMemoryInfo.setTotalMemory(String.format("%.2f", totalPhysicalMemorySizeGB));

        // Disk 정보 가져오기
        double totalSize;
        File varDirectory = new File("/var");
        // 총 용량 (바이트 단위)
        totalSize = varDirectory.getTotalSpace();
        cpuAndMemoryInfo.setTotalDisk(String.format("%.2f", totalSize / (1024.0 * 1024.0 * 1024.0)));

        return cpuAndMemoryInfo;
    }

    private ServersSpec cpuAndMemorySpec() {
        ServersSpec cpuAndMemorySpec = new ServersSpec();

        // CPU & Memory 정보 가져오기
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

        long freePhysicalMemorySizeBytes = osBean.getFreePhysicalMemorySize();
        long totalPhysicalMemorySizeBytes = osBean.getTotalPhysicalMemorySize();

        double freePhysicalMemorySizeGB = freePhysicalMemorySizeBytes / (1024.0 * 1024.0 * 1024.0);
        double totalPhysicalMemorySizeGB = totalPhysicalMemorySizeBytes / (1024.0 * 1024.0 * 1024.0);
        double usedPhysicalMemorySizeGB = totalPhysicalMemorySizeGB - freePhysicalMemorySizeGB;

        cpuAndMemorySpec.setUsedCPU(Double.parseDouble(String.format("%.2f", osBean.getSystemCpuLoad() * 100)));
        cpuAndMemorySpec.setFreeMemorySize(String.format("%.2f", freePhysicalMemorySizeGB));
        cpuAndMemorySpec.setUseMemorySize(String.format("%.2f", usedPhysicalMemorySizeGB));

        // Disk 정보 가져오기
        double totalSize, freeSize, useSize;
        File varDirectory = new File("/var");

        // 총 용량 (바이트 단위)
        totalSize = varDirectory.getTotalSpace();
        // 사용 중인 용량 (바이트 단위)
        useSize = totalSize - varDirectory.getFreeSpace();
        // 사용 가능한 용량 (바이트 단위)
        freeSize = varDirectory.getUsableSpace();

        cpuAndMemorySpec.setUseDiskSize(String.format("%.2f", useSize / (1024.0 * 1024.0 * 1024.0)));
        cpuAndMemorySpec.setFreeDiskSize(String.format("%.2f", freeSize / (1024.0 * 1024.0 * 1024.0)));

        return cpuAndMemorySpec;
    }
}

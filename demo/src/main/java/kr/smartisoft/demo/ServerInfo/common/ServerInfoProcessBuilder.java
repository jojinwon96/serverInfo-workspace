package kr.smartisoft.demo.ServerInfo.common;

import com.sun.management.OperatingSystemMXBean;
import kr.smartisoft.demo.ServerInfo.entity.Servers;
import kr.smartisoft.demo.ServerInfo.entity.ServersSpec;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ServerInfoProcessBuilder {

    private static final List<String> gpuInfoCommand = Arrays.asList("nvidia-smi", "--query-gpu=gpu_name,memory.total,power.limit", "--format=csv,noheader,nounits");
    private static final List<String> gpuSpecCommand = Arrays.asList("nvidia-smi", "--query-gpu=temperature.gpu,memory.total,memory.used,power.draw,fan.speed,utilization.gpu,utilization.memory", "--format=csv,noheader,nounits");

    @Autowired
    Servers servers;

    @Autowired
    ServersSpec serversSpec;

    public Servers getServersInfo(){

        // GPU 정보 가져오기
        ProcessBuilder processBuilder = new ProcessBuilder(gpuInfoCommand);

        Process process = null;

        try {
            // processBuilder 참조
            process = processBuilder.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {

            String line;
            int count = 0;

            servers = cpuAndMemoryInfo();

            while ((line = reader.readLine()) != null) {

                String[] tokens = line.split(", ");

                if (count == 0){
                    servers.setGpu1Name(tokens[0]);
                    servers.setGpu1TotalMemory(Integer.parseInt(tokens[1]));
                    servers.setGpu1PowerLimit(Double.parseDouble(tokens[2]));
                } else if (count == 1){
                    servers.setGpu2Name(tokens[0]);
                    servers.setGpu2TotalMemory(Integer.parseInt(tokens[1]));
                    servers.setGpu2PowerLimit(Double.parseDouble(tokens[2]));
                } else if (count == 2){
                    servers.setGpu3Name(tokens[0]);
                    servers.setGpu3TotalMemory(Integer.parseInt(tokens[1]));
                    servers.setGpu3PowerLimit(Double.parseDouble(tokens[2]));
                } else if (count == 3){
                    servers.setGpu4Name(tokens[0]);
                    servers.setGpu4TotalMemory(Integer.parseInt(tokens[1]));
                    servers.setGpu4PowerLimit(Double.parseDouble(tokens[2]));
                }

                count++;

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            // 자식 프로세스가 종료 될때까지 대기
            process.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return servers;
    }

    public ServersSpec getServerSpec() {

        // GPU 정보 가져오기
        ProcessBuilder processBuilder = new ProcessBuilder(gpuSpecCommand);

        Process process = null;

        try {
            // processBuilder 참조
            process = processBuilder.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {

            String line;
            int count = 0;

            serversSpec = cpuAndMemorySpec();

            while ((line = reader.readLine()) != null) {

                String[] tokens = line.split(", ");

                if (count == 0){
                    serversSpec.setTemperatureGPU1(Integer.parseInt(tokens[0]));
                    serversSpec.setGpu1FreeMemory(Integer.parseInt(tokens[1]) - Integer.parseInt(tokens[2]));
                    serversSpec.setGpu1UsedMemory(Integer.parseInt(tokens[2]));
                    serversSpec.setGpu1PowerDraw(Double.parseDouble(tokens[3]));
                    serversSpec.setGpu1FanSpeed(Integer.parseInt(tokens[4]));
                    serversSpec.setUtilizationGPU1(Integer.parseInt(tokens[5]));
                    serversSpec.setUtilizationMemory1(Integer.parseInt(tokens[6]));
                } else if (count == 1){
                    serversSpec.setTemperatureGPU2(Integer.parseInt(tokens[0]));
                    serversSpec.setGpu2FreeMemory(Integer.parseInt(tokens[1]) - Integer.parseInt(tokens[2]));
                    serversSpec.setGpu2UsedMemory(Integer.parseInt(tokens[2]));
                    serversSpec.setGpu2PowerDraw(Double.parseDouble(tokens[3]));
                    serversSpec.setGpu2FanSpeed(Integer.parseInt(tokens[4]));
                    serversSpec.setUtilizationGPU2(Integer.parseInt(tokens[5]));
                    serversSpec.setUtilizationMemory2(Integer.parseInt(tokens[6]));
                } else if (count == 2){
                    serversSpec.setTemperatureGPU3(Integer.parseInt(tokens[0]));
                    serversSpec.setGpu3FreeMemory(Integer.parseInt(tokens[1]) - Integer.parseInt(tokens[2]));
                    serversSpec.setGpu3UsedMemory(Integer.parseInt(tokens[2]));
                    serversSpec.setGpu3PowerDraw(Double.parseDouble(tokens[3]));
                    serversSpec.setGpu3FanSpeed(Integer.parseInt(tokens[4]));
                    serversSpec.setUtilizationGPU3(Integer.parseInt(tokens[5]));
                    serversSpec.setUtilizationMemory3(Integer.parseInt(tokens[6]));
                } else if (count == 3){
                    serversSpec.setTemperatureGPU4(Integer.parseInt(tokens[0]));
                    serversSpec.setGpu4FreeMemory(Integer.parseInt(tokens[1]) - Integer.parseInt(tokens[2]));
                    serversSpec.setGpu4UsedMemory(Integer.parseInt(tokens[2]));
                    serversSpec.setGpu4PowerDraw(Double.parseDouble(tokens[3]));
                    serversSpec.setGpu4FanSpeed(Integer.parseInt(tokens[4]));
                    serversSpec.setUtilizationGPU4(Integer.parseInt(tokens[5]));
                    serversSpec.setUtilizationMemory4(Integer.parseInt(tokens[6]));
                }

                count++;

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            // 자식 프로세스가 종료 될때까지 대기
            process.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return serversSpec;
    }

    private Servers cpuAndMemoryInfo(){
        Servers cpuAndMemoryInfo = new Servers();

        // CPU & Memory 정보 가져오기
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

        long totalPhysicalMemorySizeBytes = osBean.getTotalPhysicalMemorySize();
        double totalPhysicalMemorySizeGB =  totalPhysicalMemorySizeBytes / (1024.0 * 1024.0 * 1024.0);
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
        double totalPhysicalMemorySizeGB =  totalPhysicalMemorySizeBytes / (1024.0 * 1024.0 * 1024.0);
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

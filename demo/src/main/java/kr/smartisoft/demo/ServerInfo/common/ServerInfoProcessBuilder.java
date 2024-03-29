package kr.smartisoft.demo.ServerInfo.common;

import com.sun.management.OperatingSystemMXBean;
import kr.smartisoft.demo.ServerInfo.entity.Servers;
import kr.smartisoft.demo.ServerInfo.entity.ServersSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.List;

@Service
public class ServerInfoProcessBuilder {

    private static final List<String> gpuInfoCommand = Arrays.asList("nvidia-smi", "--query-gpu=gpu_name,memory.total,power.limit", "--format=csv,noheader,nounits");
    private static final List<String> gpuSpecCommand = Arrays.asList("nvidia-smi", "--query-gpu=temperature.gpu,memory.total,memory.used,power.draw,fan.speed,utilization.gpu,utilization.memory", "--format=csv,noheader,nounits");

    @Autowired
    Servers servers;

    @Autowired
    ServersSpec serversSpec;

    public Servers getServersInfo() {

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

                if (count == 0) {
                    servers.setGpu1Name(tokens[0]);
                    servers.setGpu1TotalMemory(Integer.parseInt(tokens[1]));
                    servers.setGpu1PowerLimit(Double.parseDouble(tokens[2]));
                } else if (count == 1) {
                    servers.setGpu2Name(tokens[0]);
                    servers.setGpu2TotalMemory(Integer.parseInt(tokens[1]));
                    servers.setGpu2PowerLimit(Double.parseDouble(tokens[2]));
                } else if (count == 2) {
                    servers.setGpu3Name(tokens[0]);
                    servers.setGpu3TotalMemory(Integer.parseInt(tokens[1]));
                    servers.setGpu3PowerLimit(Double.parseDouble(tokens[2]));
                } else if (count == 3) {
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

                if (count == 0) {
                    serversSpec.setTemperatureGPU1(Integer.parseInt(tokens[0]));
                    serversSpec.setGpu1FreeMemory(Integer.parseInt(tokens[1]) - Integer.parseInt(tokens[2]));
                    serversSpec.setGpu1UsedMemory(Integer.parseInt(tokens[2]));
                    serversSpec.setGpu1PowerDraw(Double.parseDouble(tokens[3]));
                    serversSpec.setGpu1FanSpeed(Integer.parseInt(tokens[4]));
                    serversSpec.setUtilizationGPU1(Integer.parseInt(tokens[5]));
                    serversSpec.setUtilizationMemory1(Integer.parseInt(tokens[6]));
                } else if (count == 1) {
                    serversSpec.setTemperatureGPU2(Integer.parseInt(tokens[0]));
                    serversSpec.setGpu2FreeMemory(Integer.parseInt(tokens[1]) - Integer.parseInt(tokens[2]));
                    serversSpec.setGpu2UsedMemory(Integer.parseInt(tokens[2]));
                    serversSpec.setGpu2PowerDraw(Double.parseDouble(tokens[3]));
                    serversSpec.setGpu2FanSpeed(Integer.parseInt(tokens[4]));
                    serversSpec.setUtilizationGPU2(Integer.parseInt(tokens[5]));
                    serversSpec.setUtilizationMemory2(Integer.parseInt(tokens[6]));
                } else if (count == 2) {
                    serversSpec.setTemperatureGPU3(Integer.parseInt(tokens[0]));
                    serversSpec.setGpu3FreeMemory(Integer.parseInt(tokens[1]) - Integer.parseInt(tokens[2]));
                    serversSpec.setGpu3UsedMemory(Integer.parseInt(tokens[2]));
                    serversSpec.setGpu3PowerDraw(Double.parseDouble(tokens[3]));
                    serversSpec.setGpu3FanSpeed(Integer.parseInt(tokens[4]));
                    serversSpec.setUtilizationGPU3(Integer.parseInt(tokens[5]));
                    serversSpec.setUtilizationMemory3(Integer.parseInt(tokens[6]));
                } else if (count == 3) {
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

    private Servers cpuAndMemoryInfo() {

        Servers cpuAndMemoryInfo = new Servers();

        try {
            // "/proc/meminfo" 파일 읽기
            BufferedReader reader = new BufferedReader(new FileReader("/proc/meminfo"));

            // 파일에서 각 줄을 읽어오기
            String line;
            while ((line = reader.readLine()) != null) {
                // 원하는 정보 추출 (예: 전체 메모리 및 사용 중인 메모리)
                if (line.startsWith("MemTotal:")) {
                    cpuAndMemoryInfo.setTotalMemory(String.format("%.2f",parseMemoryValue(line)));
                }
            }
            // 파일 닫기
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

        cpuAndMemorySpec.setUsedCPU(Double.parseDouble(String.format("%.2f", osBean.getSystemCpuLoad() * 100)));

        try {
            // "/proc/meminfo" 파일 읽기
            BufferedReader reader = new BufferedReader(new FileReader("/proc/meminfo"));

            // 파일에서 각 줄을 읽어오기
            String line;

            while ((line = reader.readLine()) != null) {
                // 원하는 정보 추출 (예: 전체 메모리 및 사용 중인 메모리)
                if (line.startsWith("MemFree:")) {
                    cpuAndMemorySpec.setFreeMemorySize(String.format("%.2f",parseMemoryValue(line)));
                } else if (line.startsWith("MemAvailable:")) {
                    cpuAndMemorySpec.setAvaliableSize(String.format("%.2f",parseMemoryValue(line)));
                }
            }
            // 파일 닫기
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    // "/proc/meminfo"에서 메모리 값을 추출하는 함수
    private Double parseMemoryValue(String line) {

        String[] tokens = line.trim().split("\\s+");

        // 공백으로 나누어 첫 번째 열의 값을 가져옴
        // 두 번째 열의 값을 가져와서 KB를 MB로 변환
        return (double) Integer.parseInt(tokens[1]) / (1024 * 1024);
    }
}

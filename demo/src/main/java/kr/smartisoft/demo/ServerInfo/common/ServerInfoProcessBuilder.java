package kr.smartisoft.demo.ServerInfo.common;

import com.sun.management.OperatingSystemMXBean;
import kr.smartisoft.demo.ServerInfo.entity.Servers;
import kr.smartisoft.demo.ServerInfo.entity.ServersSpec;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class ServerInfoProcessBuilder {

    private static final String serverSpecCommand = "\"nvidia-smi\", \"--query-gpu=gpu_name,temperature.gpu,utilization.gpu,utilization.memory,memory.total,memory.used,power.draw,power.limit,fan.speed\", \"--format=csv,noheader,nounits\"";

    public synchronized List<ServersSpec> getServerSpecList() {

        List<ServersSpec> serversSpecList = new ArrayList<>();

        // GPU 정보 가져오기
        ProcessBuilder processBuilder = new ProcessBuilder(serverSpecCommand);

        Process process = null;

        try {
            process = processBuilder.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;

            while ((line = reader.readLine()) != null) {
                ServersSpec serverSpec = CPUInfo();

                // 테스트
                serverSpec.setServers(new Servers(1L, 3));

                String[] tokens = line.split(", ");

                serverSpec.setGpuName(tokens[0]);

                serverSpec.setTemperatureGPU(Integer.parseInt(tokens[1]));

                serverSpec.setUtilizationGPU(Integer.parseInt(tokens[2]));

                if (tokens[3].equals("[Not Supported]")) {
                    serverSpec.setUtilizationMemory(-1);
                } else {
                    serverSpec.setUtilizationMemory(Integer.parseInt(tokens[3]));
                }

                if (tokens[4].equals("[Not Supported]")) {
                    serverSpec.setTotalMemory(-1);
                } else {
                    serverSpec.setTotalMemory(Integer.parseInt(tokens[4]));
                }

                if (tokens[5].equals("[Not Supported]")) {
                    serverSpec.setUsedMemory(-1);
                } else {
                    serverSpec.setUsedMemory(Integer.parseInt(tokens[5]));
                }

                serverSpec.setFreeMemory(serverSpec.getTotalMemory() - serverSpec.getUsedMemory());

                if (tokens[6].equals("[Not Supported]")) {
                    serverSpec.setPowerDraw(-1);
                } else {
                    serverSpec.setPowerDraw(Double.parseDouble(tokens[6]));
                }

                if (tokens[7].equals("[Not Supported]")) {
                    serverSpec.setPowerLimit(-1);
                } else {
                    serverSpec.setPowerLimit(Double.parseDouble(tokens[7]));
                }

                if (tokens[8].equals("[Not Supported]")) {
                    serverSpec.setFanSpeed(-1);
                } else {
                    serverSpec.setFanSpeed(Integer.parseInt(tokens[8]));
                }

                serversSpecList.add(serverSpec);
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

        for (ServersSpec serversSpec : serversSpecList){
            System.out.println(serversSpec);
        }


        return serversSpecList;
    }

    private ServersSpec CPUInfo(){
        ServersSpec serverSpec = new ServersSpec();

        // CPU & Memory 정보 가져오기
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

        // 이전 CPU 시간
        long prevCpuTime = osBean.getProcessCpuTime();

        // 현재 시간 가져오기 (한국 시간으로 변환)
        LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        // 현재 CPU 시간
        long currCpuTime = osBean.getProcessCpuTime() - prevCpuTime;

        // CPU 사용량 및 한국 시간 출력
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = currentTime.format(formatter);
        System.out.println("시간 : " + formattedTime);

        // 이전 CPU 시간 업데이트
        prevCpuTime = currCpuTime;

        serverSpec.setUsageCPU(Double.parseDouble(String.format("%.2f", osBean.getSystemCpuLoad() * 100)));
        serverSpec.setFreeMemorySize(Double.parseDouble(String.format("%.2f", (double) osBean.getFreePhysicalMemorySize() / 1024 / 1024 / 1024)));
        serverSpec.setTotalMemorySize(Double.parseDouble(String.format("%.2f", (double) osBean.getTotalPhysicalMemorySize() / 1024 / 1024 / 1024)));
        serverSpec.setUseMemorySize(Double.parseDouble(String.format("%.2f", ((double) osBean.getTotalPhysicalMemorySize() / 1024 / 1024 / 1024) - (double) osBean.getFreePhysicalMemorySize() / 1024 / 1024 / 1024)));

        // Disk 정보 가져오기
        double totalSize, freeSize, useSize;
        File root = new File("C:\\");

        totalSize = root.getTotalSpace() / Math.pow(1024, 3);
        useSize = root.getUsableSpace() / Math.pow(1024, 3);
        freeSize = totalSize - useSize;

        serverSpec.setTotalDiskSize(totalSize);
        serverSpec.setUseDiskSize(useSize);
        serverSpec.setFreeDiskSize(freeSize);

        return serverSpec;
    }
}

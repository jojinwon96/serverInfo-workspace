package kr.smartisoft.demo.MonitorInfo.controller;

import com.sun.management.OperatingSystemMXBean;
import kr.smartisoft.demo.MonitorInfo.entity.Performance;
import kr.smartisoft.demo.MonitorInfo.service.PerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping
public class PerformanceController {

    private Performance performance = new Performance();

    @Autowired
    PerformanceService performanceService;

    @GetMapping("/info")
    public void CPUInformation(){

        getUsingCPUAndMemmory();
        getUsingDisk();
        getUsingGPU();

        System.out.println(performance);
    }

    public void getUsingGPU(){

        ProcessBuilder processBuilder = new ProcessBuilder("\"nvidia-smi\", \"--query-gpu=gpu_name,gpu_bus_id,temperature.gpu,utilization.gpu,utilization.memory,memory.total,memory.used,power.draw,power.limit,fan.speed\", \"--format=csv,noheader,nounits\"");
        Process process = null;

        try {
            process = processBuilder.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(", ");

                performance.setGpuName(tokens[0]);

                performance.setGpuBusId(tokens[1]);

                performance.setTemperatureGPU(Integer.parseInt(tokens[2]));

                performance.setUtilizationGPU(Integer.parseInt(tokens[3]));

                if (tokens[4].equals("[Not Supported]")) {
                    performance.setUtilizationMemory(-1);
                } else {
                    performance.setUtilizationMemory(Integer.parseInt(tokens[4]));
                }

                if (tokens[5].equals("[Not Supported]")) {
                    performance.setTotalMemory(-1);
                } else {
                    performance.setTotalMemory(Integer.parseInt(tokens[5]));
                }

                if (tokens[6].equals("[Not Supported]")) {
                    performance.setUsedMemory(-1);
                } else {
                    performance.setUsedMemory(Integer.parseInt(tokens[6]));
                }

                if (tokens[7].equals("[Not Supported]")) {
                    performance.setPowerDraw(-1);
                } else {
                    performance.setPowerDraw(Double.parseDouble(tokens[7]));
                }

                if (tokens[8].equals("[Not Supported]")) {
                    performance.setPowerLimit(-1);
                } else {
                    performance.setPowerLimit(Double.parseDouble(tokens[8]));
                }

                if (tokens[9].equals("[Not Supported]")) {
                    performance.setFanSpeed(-1);
                } else {
                    performance.setFanSpeed(Integer.parseInt(tokens[9]));
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            process.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private void getUsingDisk() {
        double totalSize, freeSize, useSize;
        File root = new File("C:\\");

        totalSize = root.getTotalSpace() / Math.pow(1024, 3);
        useSize = root.getUsableSpace() / Math.pow(1024, 3);
        freeSize = totalSize - useSize;

        System.out.println("전체 디스크 용량 : " + Math.ceil(totalSize) + " GB");
        System.out.println("남은 사용 용량 : " + Math.ceil(freeSize) + " GB");
        System.out.println("사용 용량 : " + Math.ceil(useSize) + " GB");

        performance.setTotalDiskSize(totalSize);
        performance.setUseDiskSize(useSize);
        performance.setFreeDiskSize(freeSize);
    }

    private void getUsingCPUAndMemmory() {
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

        // 이전 CPU 시간
        long prevCpuTime = osBean.getProcessCpuTime();

        System.out.println("***********************************************************");
        // 전체 시스템에 대한 "최근 CPU 사용량"을 반환합니다
        System.out.println("CPU 이용률 : " + String.format("%.2f", osBean.getSystemCpuLoad() * 100));
        // 사용 가능한 메모리 총량 반환
        System.out.println("사용 가능한 메모리 : " + String.format("%.2f", (double) osBean.getFreePhysicalMemorySize() / 1024 / 1024 / 1024));
        // 메모리 총량(바이트) 반환
        System.out.println("총 메모리 : " + String.format("%.2f", (double) osBean.getTotalPhysicalMemorySize() / 1024 / 1024 / 1024));

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

        performance.setUsageCPU(Double.parseDouble(String.format("%.2f", osBean.getSystemCpuLoad() * 100)));
        performance.setFreeMemorySize(Double.parseDouble(String.format("%.2f", (double) osBean.getFreePhysicalMemorySize() / 1024 / 1024 / 1024)));
        performance.setTotalMemorySize(Double.parseDouble(String.format("%.2f", (double) osBean.getTotalPhysicalMemorySize() / 1024 / 1024 / 1024)));
    }


}

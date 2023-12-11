package kr.smartisoft.demo.MonitorInfo.controller;

import com.sun.management.OperatingSystemMXBean;
import kr.smartisoft.demo.MonitorInfo.entity.GPUItem;
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
public class CPUController {

    @GetMapping("/info")
    public void CPUInformation() throws InterruptedException {
        //getUsingDisk();
        //getUsingCPUAndMemmory();
        GPUMonitor();

    }

    private void GPUMonitor() {
        try {
            GPUItem gpuItem = getGPUInfo();
            System.out.println(gpuItem);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public GPUItem getGPUInfo() throws IOException, InterruptedException {
        GPUItem item = new GPUItem();

        ProcessBuilder processBuilder = new ProcessBuilder("\"nvidia-smi\", \"--query-gpu=gpu_name,gpu_bus_id,temperature.gpu,utilization.gpu,utilization.memory,memory.total,memory.used,power.draw,power.limit,fan.speed\", \"--format=csv,noheader,nounits\"");
        Process process = processBuilder.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            int gpuIndex = 0;

            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(", ");

                item.setGpuName(tokens[0]);

                item.setGpuBusId(tokens[1]);

                item.setTemperatureGPU(Integer.parseInt(tokens[2]));

                item.setUtilizationGPU(Integer.parseInt(tokens[3]));

                if (tokens[4].equals("[Not Supported]")) {
                    item.setUtilizationMemory(-1);
                } else {
                    item.setUtilizationMemory(Integer.parseInt(tokens[4]));
                }

                if (tokens[5].equals("[Not Supported]")) {
                    item.setMemboryTotal(-1);
                } else {
                    item.setMemboryTotal(Integer.parseInt(tokens[5]));
                }

                if (tokens[6].equals("[Not Supported]")) {
                    item.setMemoryUsed(-1);
                } else {
                    item.setMemoryUsed(Integer.parseInt(tokens[6]));
                }

                if (tokens[7].equals("[Not Supported]")) {
                    item.setPowerDraw(-1);
                } else {
                    item.setPowerDraw(Double.parseDouble(tokens[7]));
                }

                if (tokens[8].equals("[Not Supported]")) {
                    item.setPowerLimit(-1);
                } else {
                    item.setPowerLimit(Double.parseDouble(tokens[8]));
                }

                if (tokens[9].equals("[Not Supported]")) {
                    item.setFanSpeed(-1);
                } else {
                    item.setFanSpeed(Integer.parseInt(tokens[9]));
                }

                gpuIndex++;
            }
        }

        process.waitFor();

        return item;
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


    }


}

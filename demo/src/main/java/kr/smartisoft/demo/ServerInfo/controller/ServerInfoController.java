package kr.smartisoft.demo.ServerInfo.controller;

import kr.smartisoft.demo.ServerInfo.common.DynamicChangeScheduler;
import kr.smartisoft.demo.ServerInfo.common.ServerInfoProcessBuilder;
import kr.smartisoft.demo.ServerInfo.common.SeverFileManager;
import kr.smartisoft.demo.ServerInfo.entity.Servers;
import kr.smartisoft.demo.ServerInfo.service.ServerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/server-info")
public class ServerInfoController {

    @Autowired
    DynamicChangeScheduler dynamicChangeScheduler;

    @Autowired
    private ServerInfoProcessBuilder serverInfoProcessBuilder;

    @Autowired
    private ServerInfoService serverInfoService;

    @Autowired
    private SeverFileManager severFileManager;

    private Boolean isRun = false;

    @PostMapping("/save")
    private Servers save(@RequestBody Servers serverInfo) {

        System.out.println("넘어온 값 : " + serverInfo);

        Servers servers = serverInfoProcessBuilder.getServersInfo();
        servers.setServerName(serverInfo.getServerName());
        servers.setIpAddress(serverInfo.getIpAddress());
        servers.setPort(serverInfo.getPort());

        // Servers 정보를 파일에 저장
        Servers savedServerInfo = severFileManager.saveToFile(servers);

        // 파일로 저장된 결과 DB에도 적용
        if (savedServerInfo != null) {
            serverInfoService.saveServerInfo(savedServerInfo);
        }

        System.out.println("저장된 값 : " + serverInfo);

        // 저장된 결과
        return savedServerInfo;
    }

    @PostMapping("/change-cron")
    private ResponseEntity changeCron(@RequestBody int cron) throws InterruptedException {
        dynamicChangeScheduler.stopScheduler();
        Thread.sleep(1000);
        dynamicChangeScheduler.startScheduler();
        return new ResponseEntity("주기 설정 완료", HttpStatus.OK);
    }

}

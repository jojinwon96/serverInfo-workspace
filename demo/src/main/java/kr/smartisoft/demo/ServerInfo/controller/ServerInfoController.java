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

    @PostMapping("/save")
    private String save(@RequestBody Servers serverInfo) {

        System.out.println("req server info : " + serverInfo);

        Servers getServer = serverInfoService.getServer(serverInfo.getPort());

        if (getServer != null){
            return "already saved server info";
        } else {
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

            System.out.println("saved server info : " + serverInfo);

            // 저장된 결과
            return "saved server";
        }

    }

    @PostMapping("/change-cron")
    private ResponseEntity changeCron(@RequestBody int cron) throws InterruptedException {
        System.out.println("cron : " + cron);
        dynamicChangeScheduler.stopScheduler();
        Thread.sleep(1000);
        dynamicChangeScheduler.startScheduler();
        return new ResponseEntity("success change cron", HttpStatus.OK);
    }

}

package kr.smartisoft.demo.ServerInfo.controller;

import kr.smartisoft.demo.ServerInfo.common.ServerInfoProcessBuilder;
import kr.smartisoft.demo.ServerInfo.common.SeverFileManager;
import kr.smartisoft.demo.ServerInfo.entity.Servers;
import kr.smartisoft.demo.ServerInfo.entity.ServersSpec;
import kr.smartisoft.demo.ServerInfo.service.ServerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;


@RestController
@RequestMapping("/api/server-info")
public class ServerInfoController {

    @Autowired
    private ServerInfoProcessBuilder serverInfoProcessBuilder;

    @Autowired
    private ServerInfoService serverInfoService;

    @Autowired
    private Servers servers;

    @Autowired
    private ServersSpec serverSpec;

    @Autowired
    private SeverFileManager severFileManager;

    private static Boolean isRun = false;

    /**
     * 서버 시작 또는 재시작시
     */
    @PostConstruct
    @Transactional
    private void init() {
        // 서버 정보(서버이름, 포트, ip) 가져오기
        servers = severFileManager.readJsonFromFile();

        // 서버 정보(서버이름, 포트, ip)가 있을때만 로직 실행
        if (servers != null) {
            Servers dbServerInfo = serverInfoService.getServer(servers.getPort());

            // 같은 정보가 DB에도 있다면
            if (dbServerInfo != null) {
                servers = dbServerInfo;
                isRun = true;
            } else {
                saveServerInfo(servers);
            }
        }

    }

    @PostMapping("/save")
    private Servers save(@RequestBody Servers serverInfo) {

        servers = serverInfoProcessBuilder.getServersInfo();
        servers.setServerName(serverInfo.getServerName());
        servers.setIpAddress(serverInfo.getIpAddress());
        servers.setPort(serverInfo.getPort());

        // Servers 정보를 파일에 저장
        Servers savedServerInfo = severFileManager.saveToFile(servers);

        // 파일로 저장된 결과 DB에도 적용
        if (savedServerInfo != null) {
            saveServerInfo(savedServerInfo);
        }

        // 저장된 결과
        return savedServerInfo;
    }

    /**
     * 서버 정보 가져오기
     */
    @Scheduled(fixedRate = 3000)
    @Transactional
    private void saveServerSpec() {
        if (isRun) {
            saveServerSpec(servers);
        }
    }

    @Transactional
    private void saveServerInfo(Servers servers) {
        serverInfoService.saveServerInfo(servers);
        saveServerSpec(servers);

        isRun = true;
    }

    @Transactional
    private void saveServerSpec(Servers servers) {
        serverSpec = serverInfoProcessBuilder.getServerSpec();

        // 서버의 gpu 갯수만큼 db에 저장
        serverSpec.setServers(servers);

        serverInfoService.saveServersSpec(serverSpec);

    }


}

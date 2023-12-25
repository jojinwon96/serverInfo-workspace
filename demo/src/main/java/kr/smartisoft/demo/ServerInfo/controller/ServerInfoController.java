package kr.smartisoft.demo.ServerInfo.controller;

import com.sun.management.OperatingSystemMXBean;
import kr.smartisoft.demo.ServerInfo.common.ServerInfoProcessBuilder;
import kr.smartisoft.demo.ServerInfo.entity.Servers;
import kr.smartisoft.demo.ServerInfo.entity.ServersSpec;
import kr.smartisoft.demo.ServerInfo.service.ServerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class ServerInfoController {

    @Autowired
    ServerInfoProcessBuilder serverInfoProcessBuilder;

    @Autowired
    ServerInfoService serverInfoService;


    /**
     * 초기 서버 정보 등록
     */
//    @PostConstruct
//    private void setServerInfo(){
//
//        Servers servers = new Servers();
//        servers.setServerName(4);
//
//        serverInfoService.saveServerInfo(servers);
//    }

    /**
     * 서버 정보 가져오기
     */
    @Scheduled(fixedRate = 3000)
    private void saveServerSpec() {

        List<ServersSpec> serversSpecList = serverInfoProcessBuilder.getServerSpecList();

        // 서버의 gpu 갯수만큼 db에 저장
        for (ServersSpec serversSpec : serversSpecList){
            serverInfoService.saveServersSpec(serversSpec);
        }
    }



}

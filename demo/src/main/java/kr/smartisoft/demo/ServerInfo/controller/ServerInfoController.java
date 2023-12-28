package kr.smartisoft.demo.ServerInfo.controller;

import kr.smartisoft.demo.ServerInfo.common.ServerInfoProcessBuilder;
import kr.smartisoft.demo.ServerInfo.entity.Servers;
import kr.smartisoft.demo.ServerInfo.entity.ServersSpec;
import kr.smartisoft.demo.ServerInfo.service.ServerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
public class ServerInfoController {

    @Autowired
    private Environment environment;

    @Autowired
    private ServerInfoProcessBuilder serverInfoProcessBuilder;

    @Autowired
    private ServerInfoService serverInfoService;

    @Autowired
    private Servers servers;

    @Autowired
    private ServersSpec serverSpec;

    /**
     * 초기 서버 정보 등록
     */
    @PostConstruct
    @Transactional
    private void init() {

        Map<String, String> portAndAddress = getServerInfo();

        Servers getServer = serverInfoService.getServer(portAndAddress.get("port"));

        if (getServer != null){
            servers = getServer;
        } else {
            saveServerInfo(portAndAddress.get("port"), portAndAddress.get("ipAddress"));
        }

    }

    /**
     * 서버 정보 가져오기
     */
    @Scheduled(fixedRate = 3000)
    @Transactional
    private void saveServerSpec() {
        saveServerSpecs(servers);
    }

    private Map<String, String> getServerInfo() {
        InetAddress localHost = null;

        try {
            localHost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        String port = environment.getProperty("server.port");
        String ipAddress = localHost.getHostAddress();

        Map<String, String> map = new HashMap<>();

        map.put("port", port);
        map.put("ipAddress", ipAddress);

        return map;
    }



    @Transactional
    private void saveServerInfo(String port, String ipAddress) {

        servers = serverInfoProcessBuilder.getServersInfo();

        String serverName = "";

        if(port.equals("9030")){
            serverName = "3번 서버";
        } else if(port.equals("9040")){
            serverName = "4번 서버";
        } else if(port.equals("9050")){
            serverName = "5번 서버";
        } else if(port.equals("9060")){
            serverName = "6번 서버";
        }

        servers.setServerName(serverName);

        servers.setPort(port);
        servers.setIpAddress(ipAddress);

        serverInfoService.saveServerInfo(servers);
        saveServerSpecs(servers);
    }

    @Transactional
    private void saveServerSpecs(Servers servers) {
        serverSpec = serverInfoProcessBuilder.getServerSpec();

        // 서버의 gpu 갯수만큼 db에 저장
        serverSpec.setServers(servers);

        serverInfoService.saveServersSpec(serverSpec);

    }


}

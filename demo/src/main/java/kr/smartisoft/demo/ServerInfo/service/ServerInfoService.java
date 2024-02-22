package kr.smartisoft.demo.ServerInfo.service;

import kr.smartisoft.demo.ServerInfo.common.ServerInfoProcessBuilder;
import kr.smartisoft.demo.ServerInfo.common.SeverFileManager;
import kr.smartisoft.demo.ServerInfo.entity.Servers;
import kr.smartisoft.demo.ServerInfo.entity.ServersSpec;
import kr.smartisoft.demo.ServerInfo.repository.ServersRepository;
import kr.smartisoft.demo.ServerInfo.repository.ServersSpecRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServerInfoService {

    @Autowired
    private ServersSpecRepository serversSpecRepository;

    @Autowired
    private ServersRepository serversRepository;

    @Autowired
    private ServerInfoProcessBuilder serverInfoProcessBuilder;

    @Autowired
    private Servers servers;

    @Autowired
    private ServersSpec serverSpec;

    private Boolean isRun = false;

    public void init() {
        System.out.println("start schedule");
        SeverFileManager severFileManager = new SeverFileManager();

        // 서버 정보(서버이름, 포트, ip) 가져오기
        Servers readServer = severFileManager.readJsonFromFile();

        // 서버 정보(서버이름, 포트, ip)가 있을때만 로직 실행
        if (readServer != null) {
            Servers dbServerInfo = getServer(readServer.getPort());

            // 같은 정보가 DB에도 있다면
            if (dbServerInfo != null) {
                servers = dbServerInfo;
                isRun = true;
            } else {
                servers = readServer;
                saveServerInfo(servers);
            }
        }
    }

    public void saveServerInfo(Servers reqServers) {
        servers = reqServers;
        serversRepository.save(servers);
        saveServerSpec(servers);

        isRun = true;
    }

    public void saveServerSpec() {
        if (isRun) {
            saveServerSpec(servers);
        }
    }

    public void saveServerSpec(Servers servers) {
        serverSpec = serverInfoProcessBuilder.getServerSpec();

        // 서버의 gpu 갯수만큼 db에 저장
        serverSpec.setServers(servers);

        saveServersSpec(serverSpec);
    }

    public Servers getServer(String reqPort) {
        return serversRepository.findByPort(reqPort);
    }

    public void saveServersSpec(ServersSpec performance) {
        serversSpecRepository.save(performance);
    }

}

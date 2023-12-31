package kr.smartisoft.demo.ServerInfo.service;

import kr.smartisoft.demo.ServerInfo.entity.Servers;
import kr.smartisoft.demo.ServerInfo.entity.ServersSpec;
import kr.smartisoft.demo.ServerInfo.repository.ServersRepository;
import kr.smartisoft.demo.ServerInfo.repository.ServersSpecRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServerInfoService {

    @Autowired
    ServersSpecRepository serversSpecRepository;

    @Autowired
    ServersRepository serversRepository;

    public Servers getServer(String reqPort) {
        return serversRepository.findByPort(reqPort);
    }

    public ServersSpec getServersSpec(int serverName) {
        return serversSpecRepository.findByServerName(serverName);
    }

    @Transactional
    public void saveServerInfo(Servers servers) {
        serversRepository.save(servers);
    }

    @Transactional
    public void saveServersSpec(ServersSpec performance) {
        serversSpecRepository.save(performance);
    }


}

package kr.smartisoft.demo.ServerInfo.service;

import kr.smartisoft.demo.ServerInfo.entity.Servers;
import kr.smartisoft.demo.ServerInfo.entity.ServersSpec;

public interface ServerInfoServiceImpl {
    void init();
    void saveServerInfo(Servers reqServers);
    void saveServerSpec();
    void saveServerSpec(Servers servers);
    Servers getServer(String reqPort);
    void saveServersSpec(ServersSpec performance);
}

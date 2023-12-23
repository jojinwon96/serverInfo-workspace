package kr.smartisoft.demo.ServerInfo.service;

import kr.smartisoft.demo.ServerInfo.entity.ServersSpec;
import kr.smartisoft.demo.ServerInfo.repository.PerformanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PerformanceService {

    @Autowired
    PerformanceRepository performanceRepository;

    public void savePerformance(ServersSpec performance){
        performanceRepository.save(performance);
    }

    public ServersSpec getPerformance(int serverName){
        return performanceRepository.findByServerName(serverName);
    }
}

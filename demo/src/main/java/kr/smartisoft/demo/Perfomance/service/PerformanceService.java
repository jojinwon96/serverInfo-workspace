package kr.smartisoft.demo.Perfomance.service;

import kr.smartisoft.demo.Perfomance.entity.Performance;
import kr.smartisoft.demo.Perfomance.repository.PerformanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PerformanceService {

    @Autowired
    PerformanceRepository performanceRepository;

    public void savePerformance(Performance performance){
        performanceRepository.save(performance);
    }

    public Performance getPerformance(int serverName){
        return performanceRepository.findByServerName(serverName);
    }
}

package kr.smartisoft.demo.ServerInfo.repository;

import kr.smartisoft.demo.ServerInfo.entity.Servers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServersRepository extends JpaRepository<Servers, Long> {

}

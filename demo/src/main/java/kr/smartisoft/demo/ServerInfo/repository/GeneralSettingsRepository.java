package kr.smartisoft.demo.ServerInfo.repository;

import kr.smartisoft.demo.ServerInfo.entity.GeneralSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneralSettingsRepository extends JpaRepository<GeneralSettings, Long> {

}

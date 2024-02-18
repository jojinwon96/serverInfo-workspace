package kr.smartisoft.demo.ServerInfo.service;

import kr.smartisoft.demo.ServerInfo.entity.GeneralSettings;
import kr.smartisoft.demo.ServerInfo.repository.GeneralSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeneralSettingsService {

    @Autowired
    private GeneralSettingsRepository generalSettingsRepository;

    public int getIntervalTime() {

        GeneralSettings generalSettings = generalSettingsRepository.findById(1L).orElse(null);
        if (generalSettings != null) {
            return generalSettings.getIntervalTime();
        } else {
            return 0; // or throw an exception, or use a default value, etc.
        }
    }


}

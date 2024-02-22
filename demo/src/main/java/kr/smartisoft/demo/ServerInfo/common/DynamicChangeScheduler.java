package kr.smartisoft.demo.ServerInfo.common;

import kr.smartisoft.demo.ServerInfo.service.GeneralSettingsService;
import kr.smartisoft.demo.ServerInfo.service.ServerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class DynamicChangeScheduler {
    private ThreadPoolTaskScheduler scheduler;

    private String cron = "*/5 * * * * *";

    @Autowired
    private GeneralSettingsService generalSettingsService;

    @Autowired
    ServerInfoService serverInfoService;

    @PostConstruct
    public void init() {
        startScheduler();
    }

    @PreDestroy
    public void destroy() {
        stopScheduler();
    }

    public void startScheduler() {
        int interval = generalSettingsService.getIntervalTime();

        changeCronSet(convertCron(interval));

        serverInfoService.init();

        scheduler = new ThreadPoolTaskScheduler();
        scheduler.initialize();
        // scheduler setting
        scheduler.schedule(getRunnable(), getTrigger());
    }

    public void changeCronSet(String cron) {
        this.cron = cron;
    }

    public void stopScheduler() {
        scheduler.shutdown();
    }

    private Runnable getRunnable() {
        // do something
        return () -> {
            serverInfoService.saveServerSpec();
        };
    }

    private Trigger getTrigger() {
        // cronSetting
        return new CronTrigger(cron);
    }

    private String convertCron(int interval){
        String setCron = "";

        // 59초까지는 초 단위로 실행
        if (interval < 60) {
            setCron = "*/" + interval + " * * * * *";
        }
        else {
            int minutes = interval / 60;
            int remainingSeconds = interval % 60; // 초의 나머지

            setCron = remainingSeconds + " */" + minutes + " * * * *";
        }

        return setCron;
    }

}

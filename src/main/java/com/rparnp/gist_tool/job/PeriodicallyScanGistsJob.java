package com.rparnp.gist_tool.job;

import com.rparnp.gist_tool.service.GistService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class PeriodicallyScanGistsJob {

    @Resource
    private GistService gistService;

    @Scheduled(cron = "0 0 */3 * * *")
    public void run() {
        gistService.getScannedUsers()
                .forEach(user -> gistService.uploadGists(user));
    }
}
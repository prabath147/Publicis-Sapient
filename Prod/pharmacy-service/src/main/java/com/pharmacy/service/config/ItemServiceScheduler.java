package com.pharmacy.service.config;

import com.pharmacy.service.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.ParseException;

@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "scheduler.enabled", matchIfMissing = true)
public class ItemServiceScheduler {

    @Autowired
    ItemService itemService;

    @Scheduled(cron = "0 0 13 * * *", zone = "GMT+5:30")
    public void deleteExpired() throws ParseException {
        itemService.deleteExpired();
    }

    @Scheduled(cron = "0 0 13 * * *", zone = "GMT+5:30")
    public void notifyNearlyExpired() throws ParseException {
        itemService.getNearlyExpired();
    }

    @Scheduled(cron = "0 0 13 * * *", zone = "GMT+5:30")
    public void deleteNullInventory() {
        itemService.getNullInventory();
    }

}

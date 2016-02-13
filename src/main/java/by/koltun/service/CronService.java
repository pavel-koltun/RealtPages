package by.koltun.service;

import by.koltun.service.apartment.ApartmentsCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Сервис, запускающий задачи по расписанию
 */
@Service
public class CronService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CronService.class);

    @Autowired private ApartmentsCommonService apartmentsCommonService;


    @Scheduled(cron = "0 */1 * * * *")
    public void receiveLastRentApartments() {

        try {

            apartmentsCommonService.getLastCreatedRentApartments();
        } catch (InterruptedException e) {

            LOGGER.error("Exception in scheduled task is:", e);
        }
    }
}

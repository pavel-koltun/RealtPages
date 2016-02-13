package by.koltun.service;

import by.koltun.service.apartment.ApartmentsCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Сервис, запускающий задачи по расписанию
 */
@Service
public class CronService {

    @Autowired private ApartmentsCommonService apartmentsCommonService;


    @Scheduled(cron = "*/20 * * * * *")
    public void receiveLastRentApartments() {

        apartmentsCommonService.getLastCreatedRentApartments();
    }
}

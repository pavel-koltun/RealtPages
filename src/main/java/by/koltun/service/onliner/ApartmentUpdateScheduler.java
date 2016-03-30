package by.koltun.service.onliner;

import by.koltun.service.onliner.rent.ApartmentRentUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Scheduling service to launch apartment update processes
 */
@Service
public class ApartmentUpdateScheduler {

    @Autowired ApartmentRentUpdateService apartmentRentUpdateService;

    @Scheduled(cron = "0 */3 * * * *")
    public void startApartmentRentUpdate() {

        apartmentRentUpdateService.getRentApartments();
    }
}

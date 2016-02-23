package by.koltun.service;

import by.koltun.domain.ApartmentRent;
import by.koltun.repository.ApartmentRentRepository;
import by.koltun.service.apartment.ApartmentsCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Сервис, запускающий задачи по расписанию
 */
@Service
public class CronService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CronService.class);

    @Autowired private ApartmentsCommonService apartmentsCommonService;


    @Scheduled(cron = "0 */5 * * * *")
    public void receiveLastRentApartments() {

        try {

            apartmentsCommonService.getLastUpdatedRentApartments();
        } catch (InterruptedException e) {

            LOGGER.error("Exception in scheduled task is:", e);
        }
    }


    @Inject private ApartmentRentRepository apartmentRentRepository;

    //@Scheduled(fixedDelay = 30000)
    public void findLastUpdatedRentApartment() {

        Optional<ApartmentRent> apartmentRent = apartmentRentRepository.findTopByOrderByUpdatedDesc();

        LOGGER.info("{}", apartmentRent);
    }
}

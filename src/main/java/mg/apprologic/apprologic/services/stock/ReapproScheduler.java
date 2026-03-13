package mg.apprologic.apprologic.services.stock;

import mg.apprologic.apprologic.model.stock.ReapproNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReapproScheduler {

    @Autowired
    private ReapproService reapproService;

    @Autowired
    private NotificationService notificationService;



    // Exécution chaque jour à 9h


    @Scheduled(cron = "0 0 9 * * *")
    public void checkDailyReappro() {
        List<ReapproNotification> alerts = reapproService.verifierStock();
        if (!alerts.isEmpty()) {
            notificationService.saveNotifications(alerts);
        }
    }




   /* @Scheduled(cron = "0 * * * * *") // Exécute toutes les minutes
    public void checkDailyReappro() {
        List<ReapproNotification> alerts = reapproService.verifierStock();
        if (!alerts.isEmpty()) {
            notificationService.saveNotifications(alerts);
        }
    }*/
}

package mg.apprologic.apprologic.services.stock;

import mg.apprologic.apprologic.model.stock.ReapproNotification;
import mg.apprologic.apprologic.repository.stock.ReapproNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class NotificationService {

    @Autowired
    ReapproNotificationRepository reapproNotificationRepository;


    public void saveNotifications(List<ReapproNotification> notifications) {
        reapproNotificationRepository.saveAll(notifications);
    }



    public void markAllAsRead(List<ReapproNotification> unreadNotifications) {
        unreadNotifications.forEach(notification -> {
            notification.setLue(true);
        });
        reapproNotificationRepository.saveAll(unreadNotifications);
    }

    public Integer countUnreadNotifications() {
        return reapproNotificationRepository.countByLueFalse();
    }

    public List<ReapproNotification> getUnreadNotifications() {
        return reapproNotificationRepository.findByLueFalseOrderByDateCreationDesc();
    }
}

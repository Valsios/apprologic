package mg.apprologic.apprologic.controller.notification;

import mg.apprologic.apprologic.model.stock.ReapproNotification;
import mg.apprologic.apprologic.services.stock.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;
    @PostMapping("/mark-as-read")
    public String markAsRead(Model model) {
        List<ReapproNotification> reapproNotificationList = notificationService.getUnreadNotifications();
        StringBuilder stringBuilder = new StringBuilder();
        ReapproNotification.setStringBuilder(stringBuilder,reapproNotificationList);
        String fileName = "prevision_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm")) + ".csv";
        String csvContent = stringBuilder.toString();

        model.addAttribute("csvContent",csvContent);
        model.addAttribute("csvFileName",fileName);
        model.addAttribute("liste_alerte",reapproNotificationList);
        model.addAttribute("showDownload", true);
        notificationService.markAllAsRead(reapproNotificationList);
        return "article/MessageReapprovisionnement";
    }
    @GetMapping("/count")
    @ResponseBody
    public Map<String, Integer> getNotificationCount() {
        return Collections.singletonMap("count", notificationService.countUnreadNotifications());
    }
}
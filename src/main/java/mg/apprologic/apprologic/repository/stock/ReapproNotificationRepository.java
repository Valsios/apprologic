package mg.apprologic.apprologic.repository.stock;

import mg.apprologic.apprologic.model.stock.ReapproNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReapproNotificationRepository extends JpaRepository<ReapproNotification,Integer> {

    public Integer countByLueFalse();
    public List<ReapproNotification> findByLueFalseOrderByDateCreationDesc();
}

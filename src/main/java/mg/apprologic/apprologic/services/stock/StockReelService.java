package mg.apprologic.apprologic.services.stock;

import mg.apprologic.apprologic.model.stock.StockReel;
import mg.apprologic.apprologic.repository.stock.StockReelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockReelService {

    @Autowired
    StockReelRepository stockReelRepository;

    public void save (StockReel stockReel)
    {
        stockReelRepository.save(stockReel);
    }
}

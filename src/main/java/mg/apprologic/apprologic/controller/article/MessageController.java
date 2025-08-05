package mg.apprologic.apprologic.controller.article;

import mg.apprologic.apprologic.model.stock.MessageBox;
import mg.apprologic.apprologic.model.stock.StockFille;
import mg.apprologic.apprologic.services.stock.StockFilleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/message")
public class MessageController {

    @Autowired
    StockFilleService stockFilleService;

    @GetMapping("/all")
    public String getMessages(Model model)
    {
        model.addAttribute("messageBoxList",allMessages());
        return "/article/MessageBox";
    }

    public List<MessageBox> allMessages()
    {
        List<StockFille> stockFilleList = stockFilleService.stock_date(null,null);
        List<MessageBox> messageBoxList = new ArrayList<>();
        for (StockFille stockFille : stockFilleList)
        {
            MessageBox messageBox = new MessageBox();
            messageBox.setArticle(stockFille.getArticle());
            messageBox.setDernierMouvement(stockFille.getLast_date());
            messageBox.setEnStock(stockFille.getStock_date());
            if (messageBox.getType_message() != null && !messageBox.getType_message().isEmpty())
            {
                messageBoxList.add(messageBox);
            }
        }
        return messageBoxList;
    }


}

package mg.apprologic.apprologic.model.stock;

import jakarta.persistence.*;
import mg.apprologic.apprologic.model.article.Article;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
public class ReapproNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Article article;

    private Double quantiteRecommandee;

    private LocalDateTime dateCreation;

    private boolean lue = false;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Double getQuantiteRecommandee() {
        return quantiteRecommandee;
    }

    public void setQuantiteRecommandee(Double quantiteRecommandee) {
        this.quantiteRecommandee = quantiteRecommandee;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public boolean isLue() {
        return lue;
    }

    public void setLue(boolean lue) {
        this.lue = lue;
    }

    public static String getHeader()
    {
        String toReturn = "";
        toReturn += "Article;Quantité;UDM;Date Alerte;Delai de demande\n";
        return toReturn;
    }
    public String stringValue()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String toReturn = "";
        toReturn += this.getArticle().getCodeArticle()+"-"+this.getArticle().getDesignation()+";"+this.getQuantiteRecommandee().intValue()+";"+this.getArticle().getUdm().getDescription()+";"+this.getDateCreation().format(formatter)+";"+Article.getDefaultDelayDemand()+"\n";
        return toReturn;
    }

    public static void setStringBuilder(StringBuilder stringBuilder, List<ReapproNotification> reapproNotificationList)
    {
        stringBuilder.append(getHeader());
        for (ReapproNotification reapproNotification : reapproNotificationList)
        {
            stringBuilder.append(reapproNotification.stringValue());
        }
    }
}
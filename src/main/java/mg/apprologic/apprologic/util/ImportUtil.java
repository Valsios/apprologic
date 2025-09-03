package mg.apprologic.apprologic.util;

public class ImportUtil {


    public ImportUtil(String[] line) {
        this.line = line;
    }

    private String[] line;

    public String[] getLine() {
        return line;
    }

    public void setLine(String[] line) {
        this.line = line;
    }

    public String getPGI()
    {
        return this.getLine()[0].trim();
    }

    public String getDesignation()
    {
        return this.getLine()[1].trim();
    }

    public String getLocal()

    {
        return this.getLine()[2].trim();
    }

    public String getTrave()
    {
        return this.getLine()[3].trim();
    }

    public String getAlveole()
    {
        return this.getLine()[4].trim();
    }

    public String getEtagere()
    {
        return this.getLine()[5].trim();
    }

    public String getBac()
    {
        return this.getLine()[6].trim();
    }

    public String getUdm()
    {
        return this.getLine()[7].trim();
    }

    public String keepOnlyAlphanumeric(String input) {
        if (input == null) {
            return "";
        }
        return input.replaceAll("[^a-zA-Z0-9]", "");
    }
    public String getStockInitiale()
    {
        if (this.getLine()[8].isEmpty())
        {
            return "0";
        }
        return keepOnlyAlphanumeric(this.getLine()[8].trim());
    }

    public String getEntree()
    {
        return  keepOnlyAlphanumeric(this.getLine()[9].trim());
    }

    public String getSortie()
    {
        return keepOnlyAlphanumeric(this.getLine()[10].trim());
    }
}

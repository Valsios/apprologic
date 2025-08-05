package mg.apprologic.apprologic.services.bons;


import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;

public class HtmlToPdfConverter {

    public static void convertHtmlToPdf(String htmlFilePath, String pdfFilePath) throws Exception {
        // Read HTML file
        String htmlContent = readFile(htmlFilePath);

        // Configure renderer
        ITextRenderer renderer = new ITextRenderer();

        // Set document with base URL for resources (like images)
        renderer.setDocumentFromString(htmlContent, "file:///");

        // Layout and render
        renderer.layout();

        // Output to PDF
        try (OutputStream os = new FileOutputStream(pdfFilePath)) {
            renderer.createPDF(os);
        }
    }

    private static String readFile(String filePath) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }
        }
        return contentBuilder.toString();
    }

}
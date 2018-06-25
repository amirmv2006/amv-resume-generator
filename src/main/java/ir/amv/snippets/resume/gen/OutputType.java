package ir.amv.snippets.resume.gen;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @author Amir
 */
public enum OutputType {
    PDF(new JRPdfExporter(DefaultJasperReportsContext.getInstance())),
    DOCX(new JRDocxExporter(DefaultJasperReportsContext.getInstance())),
    HTML(new HtmlExporter(DefaultJasperReportsContext.getInstance()));

    private Exporter exporter;

    OutputType(final Exporter exporter) {
        this.exporter = exporter;
    }

    public String fileExtension() {
        return this.name().toLowerCase();
    }

    public void export(final JasperPrint jasperPrint, final String destFileName) throws JRException, IOException {
        String destFileNameAndExt = destFileName + "." + fileExtension();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        if (this.equals(HTML)) {
            exporter.setExporterOutput(new SimpleHtmlExporterOutput(destFileNameAndExt));
        } else {
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(destFileNameAndExt));
        }
        exporter.exportReport();
        Desktop.getDesktop().open(new File(destFileNameAndExt));
    }
}

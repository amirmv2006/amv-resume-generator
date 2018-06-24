package ir.amv.snippets.resume.gen;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.amv.snippets.resume.gen.model.ResumePart;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

/**
 * @author Amir
 */
public class ResumeGeneratorApp {

    public static void main(String[] args) throws JRException, IOException {
        InputStream inputStream = ResumeGeneratorApp.class.getResourceAsStream("/MyResume.jrxml");
        JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        ObjectMapper objectMapper = new ObjectMapper();
        List<ResumePart> parts = objectMapper.readValue(ResumeGeneratorApp.class.getResource("/resume.json"), new
                TypeReference<List<ResumePart>>() {});

        JRDataSource dataSource = new JRBeanCollectionDataSource(parts);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<String, Object>(), dataSource);
        String destFileName = "simple.docx";

        JRDocxExporter exporter = new JRDocxExporter(DefaultJasperReportsContext.getInstance());
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(destFileName));
        exporter.exportReport();

        Desktop.getDesktop().open(new File(destFileName));
    }
}

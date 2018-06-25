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
        JasperDesign jasperDesign = loadReport("MyResume.jrxml");
        JasperDesign subPart = loadReport("SubPart.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperReport subPartResume = JasperCompileManager.compileReport(subPart);

        ObjectMapper objectMapper = new ObjectMapper();
        List<ResumePart> parts = objectMapper.readValue(ResumeGeneratorApp.class.getResource("/resume.json"), new
                TypeReference<List<ResumePart>>() {});

        JRDataSource dataSource = new JRBeanCollectionDataSource(parts);
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("subpartResume", subPartResume);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        String destFileName = "simple";
//        OutputType.PDF.export(jasperPrint, destFileName);
        OutputType.DOCX.export(jasperPrint, destFileName);
//        OutputType.HTML.export(jasperPrint, destFileName);
    }

    private static JasperDesign loadReport(final String jrxmlFileName) throws JRException {
        InputStream inputStream = ResumeGeneratorApp.class.getResourceAsStream("/" + jrxmlFileName);
        return JRXmlLoader.load(inputStream);
    }

    public static JasperReport getItemSubReportDesign() throws JRException {
        JasperDesign subPartItem = loadReport("SubPartItem.jrxml");
        JasperReport subPartResume = JasperCompileManager.compileReport(subPartItem);
        return subPartResume;
    }
}

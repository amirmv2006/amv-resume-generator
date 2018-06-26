package ir.amv.snippets.resume.gen;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.amv.snippets.resume.gen.model.ResumePart;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

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

        System.out.println("Welcome to AMV Resume Generator");
        System.out.println("My resume have some parts with a number associated to them, in order to customize the " +
                "generated resume, you can enter a number which is sum of the parts you want on the generated resume");
        System.out.println("For example, if you want to only see Summary(1) and Experience(2) you can enter 1+2=3");
        System.out.println("These are the resume parts and the number associated to them:");
        int counter = 1;
        for (ResumePart part : parts) {
            System.out.println(part.getTitle() + " = " + counter);
            counter = counter << 1;
        }
        System.out.println("Enter the number now");
        Scanner scanner = new Scanner(System.in);
        int input = scanner.nextInt();

        List<ResumePart> filtered = new ArrayList<>();
        for (int i = 0; i < parts.size(); i++) {
            int flag = 1 << i;
            if ((input & flag) != 0) {
                filtered.add(parts.get(i));
            }
        }
        System.out.println("Now choose output type:");
        OutputType[] outputTypes = OutputType.values();
        for (int i = 0; i < outputTypes.length; i++) {
            System.out.println(i + " = " + outputTypes[i]);
        }
        int outputIndex = scanner.nextInt();
        JRDataSource dataSource = new JRBeanCollectionDataSource(filtered);
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("subpartResume", subPartResume);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        String destFileName = "resume";
        outputTypes[outputIndex].export(jasperPrint, destFileName);
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

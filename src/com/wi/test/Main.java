package com.wi.test;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDStructureElement;
import org.apache.xmpbox.schema.XmpSchemaException;

import javax.xml.transform.TransformerException;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

public class Main {

    public static void main(String[] args) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss:SSS");
        System.out.println(dateFormat.format(new Date()));
        try {
            PDFormBuilder formBuilder = new PDFormBuilder(1, "UA EXAMPLE");
            formBuilder.addRoot();
            PDStructureElement part1 = formBuilder.addPart();
            PDStructureElement sec1 = formBuilder.addSection(part1);
            formBuilder.addForm(sec1);
            formBuilder.setFieldBGColor(PDConstants.WHITE);
            formBuilder.setFieldBorderColor(PDConstants.BLACK);
            DataTable table = new DataTable();
            table.addRow(new Row(Arrays.asList(
                    new Cell("Header 1", Color.lightGray, 5, 25, PDConstants.CENTER_ALIGN),
                    new Cell("Header 2", Color.lightGray, 5, 200, PDConstants.CENTER_ALIGN),
                    new Cell("Header 3", Color.lightGray, 5, 100, PDConstants.CENTER_ALIGN),
                    new Cell("Header 4", Color.lightGray, 5, 60, PDConstants.CENTER_ALIGN),
                    new Cell("Header 5", Color.lightGray, 5, 100, PDConstants.CENTER_ALIGN)),
                    30));
            table.addRow(new Row(Arrays.asList(
                    new Cell("", Color.lightGray, 5, 25, PDConstants.CENTER_ALIGN),
                    new Cell("", Color.lightGray, 5, 200, PDConstants.CENTER_ALIGN),
                    new Cell("", Color.lightGray, 5, 100, PDConstants.CENTER_ALIGN),
                    new Cell("Yes", Color.lightGray, 5, 20, PDConstants.CENTER_ALIGN),
                    new Cell("No", Color.lightGray, 5, 20, PDConstants.CENTER_ALIGN),
                    new Cell("N/A", Color.lightGray, 5, 20, PDConstants.CENTER_ALIGN),
                    new Cell("", Color.lightGray, 5, 100, PDConstants.CENTER_ALIGN)),
                    20));
            table.addRow(new Row(Collections.singletonList(
                    new Cell("SECTION HEADER 1", 6, 485, PDConstants.LEFT_ALIGN)),
                    20));
            table.addRow(new Row(Arrays.asList(
                    new Cell("  Row \n Header", 5, 25, PDConstants.TOP_ALIGN),
                    new Cell("Mshon the wata for aort-terall convis ew m acute inpll shage datieduct a desk rent " +
                            "\nproetalptem hcment ssypiosps in teports includintive paysital cost rg hoshe " +
                            "\nSte undctrol, fer aarl State cost conryate of Mah and, whior cost ",
                            5, 200, PDConstants.TOP_ALIGN),
                    new Cell("System Verification: N/A.", 5, 100, PDConstants.TOP_ALIGN),
                    new Cell("", 5, 20, PDConstants.TOP_ALIGN, "Yes", ""),
                    new Cell("", 5, 20, PDConstants.TOP_ALIGN, "No", ""),
                    new Cell("", 5, 20, PDConstants.TOP_ALIGN, "N/A", ""),
                    new Cell("", 5, 100, PDConstants.TOP_ALIGN, "", "Text Box")),
                    135));
            formBuilder.drawDataTable(table, 50, 250, 0, Arrays.asList("Yes", "No", "N/A"), "Header 4");
            formBuilder.addParentTree();
            formBuilder.saveAndClose("UAEXAMPLE.PDF");
            //formBuilder.checkTreeStructure(formBuilder.getPdf().getDocumentCatalog().getStructureTreeRoot().getKids());
            //formBuilder.checkMarkedContent();
            //formBuilder.saveAndClose("UAEXAMPLE.PDF");
        } catch (IOException | TransformerException | XmpSchemaException ex) {
            ex.printStackTrace();
        }
        System.out.println(dateFormat.format(new Date()));
    }
}
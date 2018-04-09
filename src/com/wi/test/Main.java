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
import java.util.Date;

public class Main {

    public static void main(String[] args) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:ss:SSS");
        System.out.println(dateFormat.format(new Date()));
        try {
            PDFormBuilder formBuilder = new PDFormBuilder(1, "UA EXAMPLE");
            formBuilder.addRoot();
            PDStructureElement part1 = formBuilder.addPart();
            PDStructureElement sec1 = formBuilder.addSection(part1);
            formBuilder.addForm(sec1);
            DataTable table = new DataTable();
            table.addRow(new Row(Arrays.asList(
                    new Cell(0, "Header 1", Color.lightGray, 5, 25, PDConstants.CENTER_ALIGN),
                    new Cell(1, "Header 2", Color.lightGray, 5, 200, PDConstants.CENTER_ALIGN),
                    new Cell(2, "Header 3", Color.lightGray, 5, 100, PDConstants.CENTER_ALIGN),
                    new Cell(3, "Header 4", Color.lightGray, 5, 60, PDConstants.CENTER_ALIGN),
                    new Cell(4, "Header 5", Color.lightGray, 5, 100, PDConstants.CENTER_ALIGN)),
                    30));
            table.addRow(new Row(Arrays.asList(
                    new Cell(0, "", Color.lightGray, 5, 25, PDConstants.CENTER_ALIGN),
                    new Cell(1, "", Color.lightGray, 5, 200, PDConstants.CENTER_ALIGN),
                    new Cell(2, "", Color.lightGray, 5, 100, PDConstants.CENTER_ALIGN),
                    new Cell(3, "Yes", Color.lightGray, 5, 20, PDConstants.CENTER_ALIGN),
                    new Cell(4, "No", Color.lightGray, 5, 20, PDConstants.CENTER_ALIGN),
                    new Cell(5, "N/A", Color.lightGray, 5, 20, PDConstants.CENTER_ALIGN),
                    new Cell(6, "", Color.lightGray, 5, 100, PDConstants.CENTER_ALIGN)),
                    20));
            table.addRow(new Row(Arrays.asList(
                    new Cell(0, "SECTION HEADER 1", 6, 485, PDConstants.LEFT_ALIGN)),
                    20));
            table.addRow(new Row(Arrays.asList(
                    new Cell(0, "  Row \n Header", 5, 25, PDConstants.TOP_ALIGN),
                    new Cell(1,
                            "Mshon the wata for aort-terall convis ew m acute inpll shage datieduct a desk rent " +
                            "\nproetalptem hcment ssypiosps in teports includintive paysital cost rg hoshe " +
                            "\nSte undctrol, fer aarl State cost conryate of Mah and, whior cost ",
                            5, 200, PDConstants.TOP_ALIGN),
                    new Cell(2, "System Verification: N/A.", 5, 100, PDConstants.TOP_ALIGN),
                    new Cell(3, "", 5, 20, PDConstants.TOP_ALIGN),
                    new Cell(4, "", 5, 20, PDConstants.TOP_ALIGN),
                    new Cell(5, "", 5, 20, PDConstants.TOP_ALIGN),
                    new Cell(6, "", 5, 100, PDConstants.TOP_ALIGN)),
                    135));
            formBuilder.drawDataTable(table, 50, 250, 0);
            formBuilder.setFieldBGColor(PDConstants.WHITE);
            formBuilder.setFieldBorderColor(PDConstants.BLACK);
            formBuilder.addRadioField(
                    375, 325, 60, 10, 5, "Choices", Arrays.asList("Yes", "No", "N/A"), 0, false);
            formBuilder.addTextField(
                    435, 320, 100, 135, 5, "AnotherField", "", 0);
            formBuilder.addTaggingOperators();
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
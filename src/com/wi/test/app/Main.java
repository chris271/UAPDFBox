package com.wi.test.app;

import com.wi.test.constants.PDConstants;
import com.wi.test.pojo.TableCellMarkup;
import com.wi.test.util.PDFormBuilder;
import com.wi.test.pojo.Cell;
import com.wi.test.pojo.DataTable;
import com.wi.test.pojo.Row;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDStructureElement;
import org.apache.pdfbox.pdmodel.documentinterchange.taggedpdf.StandardStructureTypes;
import org.apache.xmpbox.schema.XmpSchemaException;

import javax.xml.transform.TransformerException;
import java.awt.Color;
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

            //Hard coded table1
            PDFormBuilder formBuilder = new PDFormBuilder(1, "UA EXAMPLE");
            PDStructureElement sec1 = formBuilder.addRoot(0);
            formBuilder.drawElement(
                    new Cell("Wage Index Desk Review", new Color(229, 229, 229),
                            Color.BLUE.darker().darker(), 12, formBuilder.PAGE_WIDTH - 100, PDConstants.LEFT_ALIGN),
                    50, 25, 50, sec1, StandardStructureTypes.H1, 0);
            DataTable table1 = new DataTable("Selected provider review details.", "Wage Index 1");
            table1.addRow(new Row(Arrays.asList(
                    new Cell("Provider:", 5, 100, PDConstants.LEFT_ALIGN,  "", new TableCellMarkup("Row", "Provider")),
                    new Cell("26-2686", 5, 400, PDConstants.LEFT_ALIGN, "", new TableCellMarkup(new String[]{"Provider"}))),
                    15));
            table1.addRow(new Row(Arrays.asList(
                    new Cell("Provider Name:", 5, 100, PDConstants.LEFT_ALIGN, "", new TableCellMarkup("Row", "ProviderName")),
                    new Cell("Some name", 5, 400, PDConstants.LEFT_ALIGN, "", new TableCellMarkup(new String[]{"ProviderName"}))),
                    15));
            table1.addRow(new Row(Arrays.asList(
                    new Cell("FYE:", 5, 100, PDConstants.LEFT_ALIGN, "", new TableCellMarkup("Row", "FYE")),
                    new Cell("12/31/2016", 5, 400, PDConstants.LEFT_ALIGN, "", new TableCellMarkup(new String[]{"FYE"}))),
                    15));
            table1.addRow(new Row(Arrays.asList(
                    new Cell("Puerto Rico Provider:", 5, 100, PDConstants.LEFT_ALIGN, "", new TableCellMarkup("Row", "PuertoRicoProvider")),
                    new Cell("No", 5, 400, PDConstants.LEFT_ALIGN, "", new TableCellMarkup(new String[]{"PuertoRicoProvider"}))),
                    15));
            table1.addRow(new Row(Arrays.asList(
                    new Cell("CBSA Code:", 5, 100, PDConstants.LEFT_ALIGN, "", new TableCellMarkup("Row", "CBSACode")),
                    new Cell("99948", 5, 400, PDConstants.LEFT_ALIGN, "", new TableCellMarkup(new String[]{"CBSACode"}))),
                    15));
            table1.addRow(new Row(Arrays.asList(
                    new Cell("Current FYE MCR File:", 5, 100, PDConstants.LEFT_ALIGN, "", new TableCellMarkup("Row", "CurrentFYEMCRFile")),
                    new Cell("As Filed, Received Date 5/15/2016, Not Adjusted, Cost Report Id: 1234567", 5, 400, PDConstants.LEFT_ALIGN, "", new TableCellMarkup(new String[]{"CurrentFYEMCRFile"}))),
                    15));
            table1.addRow(new Row(Arrays.asList(
                    new Cell("Previous FYE Used in Comparisons:", 5, 100, PDConstants.LEFT_ALIGN, "", new TableCellMarkup("Row", "PreviousFYEUsedinComparisons")),
                    new Cell("12/31/2015", 5, 400, PDConstants.LEFT_ALIGN, "", new TableCellMarkup(new String[]{"PreviousFYEUsedinComparisons"}))),
                    15));
            table1.addRow(new Row(Arrays.asList(
                    new Cell("Previous FYE MCR File:", 5, 100, PDConstants.LEFT_ALIGN, "", new TableCellMarkup("Row", "PreviousFYEMCRFile")),
                    new Cell("Amended #2, Received Date 5/12/2016, MAC Adjusted for Wage Index, Cost Report Id: 1234567", 5, 400, PDConstants.LEFT_ALIGN, "", new TableCellMarkup(new String[]{"PreviousFYEMCRFile"}))),
                    15));
            table1.addRow(new Row(Arrays.asList(
                    new Cell("Reviewer(s):", 5, 100, PDConstants.LEFT_ALIGN, "", new TableCellMarkup("Row", "Reviewers")),
                    new Cell("", 5, 300, PDConstants.LEFT_ALIGN, "Reviewers", new TableCellMarkup(new String[]{"Reviewers"}))),
                    15));
            table1.addRow(new Row(Arrays.asList(
                    new Cell("Review Start Date:", 5, 100, PDConstants.LEFT_ALIGN, "", new TableCellMarkup("Row", "ReviewStartDate")),
                    new Cell("", 5, 300, PDConstants.LEFT_ALIGN, "Review Start Date", new TableCellMarkup(new String[]{"ReviewStartDate"}))),
                    15));
            table1.addRow(new Row(Arrays.asList(
                    new Cell("Review End Date:", 5, 100, PDConstants.LEFT_ALIGN, "", new TableCellMarkup("Row", "ReviewEndDate")),
                    new Cell("", 5, 300, PDConstants.LEFT_ALIGN, "Review End Date", new TableCellMarkup(new String[]{"ReviewEndDate"}))),
                    15));
            table1.addRow(new Row(Arrays.asList(
                    new Cell("Supervisor(s):", 5, 100, PDConstants.LEFT_ALIGN, "", new TableCellMarkup("Row", "Supervisors")),
                    new Cell("", 5, 300, PDConstants.LEFT_ALIGN, "Supervisors", new TableCellMarkup(new String[]{"Supervisors"}))),
                    15));
            table1.addRow(new Row(Arrays.asList(
                    new Cell("Supervisor Review Date:", 5, 100, PDConstants.LEFT_ALIGN, "", new TableCellMarkup("Row", "SupervisorReviewDate")),
                    new Cell("", 5, 300, PDConstants.LEFT_ALIGN, "Supervisor Review Date", new TableCellMarkup(new String[]{"SupervisorReviewDate"}))),
                    15));
            formBuilder.drawDataTable(table1, 50, 100, 0, sec1);

            //Hard coded table2
            DataTable table2 = new DataTable("Table for Wage Index Desk Review Audit Procedures for Provider", "Wage Index 2");
            table2.addRow(new Row(Arrays.asList(
                    new Cell("Section/ \n  Step", Color.lightGray, 5, 25, PDConstants.LEFT_ALIGN, new TableCellMarkup(1, "Column", "SectionStep")),
                    new Cell("Description", Color.lightGray, 5, 225, PDConstants.CENTER_ALIGN,  new TableCellMarkup(1, "Column", "Description")),
                    new Cell("  System Verification Output", Color.lightGray, 5, 75, PDConstants.CENTER_ALIGN, new TableCellMarkup(1, "Column", "SystemVerificationOutput")),
                    new Cell("Review", Color.lightGray, 5, 60, PDConstants.CENTER_ALIGN, new TableCellMarkup(3, "Column", "Review")),
                    new Cell("Notes/Disposition", Color.lightGray, 5, 100, PDConstants.CENTER_ALIGN, new TableCellMarkup(1, "Column", "NotesDisposition"))),
                    Collections.emptyList(), "", 30));
            table2.addRow(new Row(Arrays.asList(
                    new Cell("", Color.lightGray, 5, 25, PDConstants.CENTER_ALIGN, new TableCellMarkup()),
                    new Cell("", Color.lightGray, 5, 225, PDConstants.CENTER_ALIGN, new TableCellMarkup()),
                    new Cell("", Color.lightGray, 5, 75, PDConstants.CENTER_ALIGN, new TableCellMarkup()),
                    new Cell("Yes", Color.lightGray, 5, 20, PDConstants.CENTER_ALIGN, new TableCellMarkup(new String[]{"Review"}, "Yes")),
                    new Cell("No", Color.lightGray, 5, 20, PDConstants.CENTER_ALIGN, new TableCellMarkup(new String[]{"Review"}, "No")),
                    new Cell("N/A", Color.lightGray, 5, 20, PDConstants.CENTER_ALIGN, new TableCellMarkup(new String[]{"Review"}, "N/A")),
                    new Cell("", Color.lightGray, 5, 100, PDConstants.CENTER_ALIGN, new TableCellMarkup())),
                    Collections.emptyList(), "", 20));
            table2.addRow(new Row(Collections.singletonList(
                    new Cell("SECTION 2: WAGE INDEX DESK REVIEW PROCEDURES (FORM CMS-2552-10, WORKSHEET S-3, " +
                            "PARTS II AND III, and S-3, Part IV WAGE DATA)", 6, 485, PDConstants.LEFT_ALIGN, new TableCellMarkup(7, "Column", "SECTION2"))),
                    Collections.emptyList(), "", 20));
            table2.addRow(new Row(Arrays.asList(
                    new Cell("    2\nGeneral", 5, 25, PDConstants.TOP_ALIGN, new TableCellMarkup("Row", new String[]{"SectionStep"}, "2General")),
                    new Cell("MACs shall conduct a desk review on the wage data for all short-term acute inpatient \n" +
                            "prospective payment system (IPPS) hospital cost reports (including hospitals in\n" +
                            "the State of Maryland, which are operating under a State cost control system),\n" +
                            "for cost reporting periods beginning on or after October 1, 2014, through\n" +
                            "September 30, 2015, (that is, FY 2015 wage data).",
                            5, 225, PDConstants.TOP_ALIGN, new TableCellMarkup(new String[]{"Description", "2General"})),
                    new Cell("System Verification: N/A.", 5, 75, PDConstants.TOP_ALIGN, new TableCellMarkup(new String[]{"SystemVerificationOutput", "2General"})),
                    new Cell("", 5, 20, PDConstants.TOP_ALIGN, "Yes", "",  new TableCellMarkup(new String[]{"Review", "2General", "Yes"})),
                    new Cell("", 5, 20, PDConstants.TOP_ALIGN, "No", "",  new TableCellMarkup(new String[]{"Review", "2General", "No"})),
                    new Cell("", 5, 20, PDConstants.TOP_ALIGN, "N/A", "", new TableCellMarkup(new String[]{"Review", "2General", "N/A"})),
                    new Cell("", 5, 100, PDConstants.TOP_ALIGN, "", "Notes/Disposition", new TableCellMarkup(new String[]{"NotesDisposition", "2General"}))),
                    Arrays.asList("Yes", "No", "N/A"), "Review", 50));
            table2.addRow(new Row(Arrays.asList(
                    new Cell("    2\nGeneral", 5, 25, PDConstants.TOP_ALIGN, new TableCellMarkup("Row", new String[]{"SectionStep"}, "2General1")),
                    new Cell("MACs shall conduct the desk review on only one of the reporting periods for \n" +
                            "hospitals that have more than one cost reporting period beginning during FY \n" +
                            "2015. Select the longest period; if there is more than one period of that length, \n" +
                            "select the latest period.",
                            5, 225, PDConstants.TOP_ALIGN, new TableCellMarkup(new String[]{"Description", "2General1"})),
                    new Cell("System Verification: N/A.", 5, 75, PDConstants.TOP_ALIGN, new TableCellMarkup(new String[]{"SystemVerificationOutput", "2General1"})),
                    new Cell("", 5, 20, PDConstants.TOP_ALIGN, "Yes", "", new TableCellMarkup(new String[]{"Review", "2General1", "Yes"})),
                    new Cell("", 5, 20, PDConstants.TOP_ALIGN, "No", "", new TableCellMarkup(new String[]{"Review", "2General1", "No"})),
                    new Cell("", 5, 20, PDConstants.TOP_ALIGN, "N/A", "", new TableCellMarkup(new String[]{"Review", "2General1", "N/A"})),
                    new Cell("", 5, 100, PDConstants.TOP_ALIGN, "", "Notes/Disposition", new TableCellMarkup(new String[]{"NotesDisposition", "2General1"}))),
                    Arrays.asList("Yes", "No", "N/A"), "Review", 40));
            table2.addRow(new Row(Arrays.asList(
                    new Cell("    2", 5, 25, PDConstants.TOP_ALIGN, new TableCellMarkup("Row", "TWOHOSPITALDATA")),
                    new Cell("MULTICAMPUS HOSPITAL DATA",
                            5, 460, PDConstants.TOP_ALIGN, new TableCellMarkup(6, "Column", "MULTICAMPUSHOSPITALDATA"))),
                    Collections.emptyList(), "", 10));
            table2.addRow(new Row(Arrays.asList(
                    new Cell("    2", 5, 25, PDConstants.TOP_ALIGN, new TableCellMarkup("Row", new String[]{"SectionStep"}, "Two")),
                    new Cell("MACs shall verify the information on Worksheet S-2, Part I, Lines 165 and 166 of form\n" +
                            "CMS- 2552-10 to determine if a hospital is a multicampus provider with facilities \n" +
                            "in different CBSAs. If a hospital answered Y for Yes to line 165, verify that the \n" +
                            "main campus and the off-site campus(es) are each classified as section 1886 subsection \n" +
                            "(d) hospitals, or they are located in Puerto Rico, i.e., the campuses are paid \n" +
                            "under the IPPS. If a campus(es) is not paid under the IPPS, then change the response to \n" +
                            "line 165 to N, if appropriate, and delete the non-IPPS campus’s information from line 166.   \n" +
                            "Note: At the time the hospital filled out these lines in its cost report beginning on \n" +
                            "or after 10/1/14 and before 9/30/15, it is possible that the multi-campus provider had \n" +
                            "campuses located only in a single CBSA but after FY 2015, has since created campuses \n" +
                            "located in different CBSAs. For the purpose of properly apportioning the FY 2015 cost \n" +
                            "report data for the FY 2019 wage index, MACs shall ensure that lines 165 and 166 are \n" +
                            "properly completed reflecting the campuses in existence during the FY 2015 cost \n" +
                            "reporting period, and NOT reflecting the status for a subsequent cost reporting period.",
                            5, 225, PDConstants.TOP_ALIGN, new TableCellMarkup(new String[]{"Description", "Two"})),
                    new Cell("Not verified by system, \n" +
                            "displaying relevant data:\n" +
                            "S-2 Part I Ln 165: Y", 5, 75, PDConstants.TOP_ALIGN, new TableCellMarkup(new String[]{"SystemVerificationOutput", "Two"})),
                    new Cell("", 5, 20, PDConstants.TOP_ALIGN, "Yes", "", new TableCellMarkup(new String[]{"Review", "Two", "Yes"})),
                    new Cell("", 5, 20, PDConstants.TOP_ALIGN, "No", "", new TableCellMarkup(new String[]{"Review", "Two", "No"})),
                    new Cell("", 5, 20, PDConstants.TOP_ALIGN, "N/A", "", new TableCellMarkup(new String[]{"Review", "Two", "N/A"})),
                    new Cell("", 5, 100, PDConstants.TOP_ALIGN, "", "Notes/Disposition", new TableCellMarkup(new String[]{"NotesDisposition", "Two"}))),
                    Arrays.asList("Yes", "No", "N/A"), "Review", 140));
            formBuilder.drawDataTable(table2, 50, 310, 0, sec1);
            PDStructureElement textDiv = formBuilder.drawElement(
                    new Cell("GENERAL NOTES/DISPOSITION:", Color.WHITE, Color.BLACK, 6, 200, PDConstants.LEFT_ALIGN),
                    50, 620, 15, sec1, StandardStructureTypes.P, 0);
            formBuilder.addTextArea(textDiv, 40, 645, formBuilder.PAGE_WIDTH - 80,
                    150, "GENERAL NOTES/DISPOSITION", 0);
            formBuilder.saveAndClose("UAEXAMPLE.PDF");

        } catch (IOException | TransformerException | XmpSchemaException ex) {
            ex.printStackTrace();
        }
        System.out.println(dateFormat.format(new Date()));
    }
}
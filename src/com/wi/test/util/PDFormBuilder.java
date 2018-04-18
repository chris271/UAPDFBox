package com.wi.test.util;

import com.wi.test.constants.PDConstants;
import com.wi.test.pojo.DataTable;
import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.*;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDMarkInfo;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDObjectReference;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDStructureElement;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDStructureTreeRoot;
import org.apache.pdfbox.pdmodel.documentinterchange.markedcontent.PDMarkedContent;
import org.apache.pdfbox.pdmodel.documentinterchange.markedcontent.PDPropertyList;
import org.apache.pdfbox.pdmodel.documentinterchange.taggedpdf.PDArtifactMarkedContent;
import org.apache.pdfbox.pdmodel.documentinterchange.taggedpdf.StandardStructureTypes;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceCharacteristicsDictionary;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceDictionary;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceEntry;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDRadioButton;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;
import org.apache.pdfbox.pdmodel.interactive.viewerpreferences.PDViewerPreferences;
import org.apache.xmpbox.XMPMetadata;
import org.apache.xmpbox.schema.XMPSchema;
import org.apache.xmpbox.schema.XmpSchemaException;
import org.apache.xmpbox.xml.XmpSerializer;

import javax.xml.transform.TransformerException;
import java.awt.Color;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.util.*;

public class PDFormBuilder {

    private PDDocument pdf = null;
    private PDAcroForm acroForm = null;
    private ArrayList<PDPage> pages = new ArrayList<>();
    private ArrayList<COSDictionary> annotDicts = new ArrayList<>();
    private ArrayList<PDObjectReference> annotationRefs = new ArrayList<>();
    private ArrayList<PDField> fields = new ArrayList<>();
    private ArrayList<PDAnnotationWidget> widgets = new ArrayList<>();
    private PDColor fieldBGColor = null;
    private PDColor fieldBorderColor = null;
    private PDFont defaultFont = null;
    private PDStructureElement rootElem = null;
    private PDStructureElement currentElem = null;
    private PDStructureElement currentForm = null;
    private COSDictionary currentMarkedContentDictionary;
    private COSArray nums = new COSArray();
    private COSArray numDictionaries = new COSArray();
    private int currentMCID = 0;
    private int currentStructParent = 1;
    private final float PAGE_HEIGHT = PDRectangle.A4.getHeight();
    private final float PAGE_WIDTH = PDRectangle.A4.getWidth();
    private final String DEFAULT_APPEARANCE = "/Helv 10 Tf 0 g";
    private final String FIELD_APPEARANCE = "/Helv 12 Tf 0 g";

    public PDFormBuilder(int initPages, String title) throws IOException, TransformerException, XmpSchemaException {
        //Setup new document
        pdf = new PDDocument();
        acroForm = new PDAcroForm(pdf);
        pdf.getDocumentInformation().setTitle(title);

        //Set AcroForm Appearance Characteristics
        PDResources resources = new PDResources();
        defaultFont = PDType0Font.load(pdf,
                new PDTrueTypeFont(PDType1Font.HELVETICA.getCOSObject()).getTrueTypeFont(), true);
        resources.put(COSName.getPDFName("Helv"), defaultFont);
        acroForm.setNeedAppearances(true);
        acroForm.setXFA(null);
        acroForm.setDefaultResources(resources);
        acroForm.setDefaultAppearance(DEFAULT_APPEARANCE);
        fieldBorderColor = new PDColor(new float[]{1,0,0}, PDDeviceRGB.INSTANCE);
        fieldBGColor = new PDColor(new float[]{0,1,0}, PDDeviceRGB.INSTANCE);

        //Add UA XMP metadata based on specs at https://taggedpdf.com/508-pdf-help-center/pdfua-identifier-missing/
        XMPMetadata xmp = XMPMetadata.createXMPMetadata();
        xmp.createAndAddDublinCoreSchema();
        xmp.getDublinCoreSchema().setTitle(title);
        xmp.getDublinCoreSchema().setDescription(title);
        xmp.createAndAddPDFAExtensionSchemaWithDefaultNS();
        xmp.getPDFExtensionSchema().addNamespace("http://www.aiim.org/pdfa/ns/schema#", "pdfaSchema");
        xmp.getPDFExtensionSchema().addNamespace("http://www.aiim.org/pdfa/ns/property#", "pdfaProperty");
        xmp.getPDFExtensionSchema().addNamespace("http://www.aiim.org/pdfua/ns/id/", "pdfuaid");
        XMPSchema uaSchema = new XMPSchema(XMPMetadata.createXMPMetadata(),
                "pdfaSchema", "pdfaSchema", "pdfaSchema");
        uaSchema.setTextPropertyValue("schema", "PDF/UA Universal Accessibility Schema");
        uaSchema.setTextPropertyValue("namespaceURI", "http://www.aiim.org/pdfua/ns/id/");
        uaSchema.setTextPropertyValue("prefix", "pdfuaid");
        XMPSchema uaProp = new XMPSchema(XMPMetadata.createXMPMetadata(),
                "pdfaProperty", "pdfaProperty", "pdfaProperty");
        uaProp.setTextPropertyValue("name", "part");
        uaProp.setTextPropertyValue("valueType", "Integer");
        uaProp.setTextPropertyValue("category", "internal");
        uaProp.setTextPropertyValue("description", "Indicates, which part of ISO 14289 standard is followed");
        uaSchema.addUnqualifiedSequenceValue("property", uaProp);
        xmp.getPDFExtensionSchema().addBagValue("schemas", uaSchema);
        xmp.getPDFExtensionSchema().setPrefix("pdfuaid");
        xmp.getPDFExtensionSchema().setTextPropertyValue("part", "1");
        XmpSerializer serializer = new XmpSerializer();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        serializer.serialize(xmp, baos, true);
        PDMetadata metadata = new PDMetadata(pdf);
        metadata.importXMPMetadata(baos.toByteArray());
        pdf.getDocumentCatalog().setMetadata(metadata);

        //Adjust other document metadata
        PDDocumentCatalog documentCatalog = pdf.getDocumentCatalog();
        documentCatalog.setLanguage("English");
        documentCatalog.setViewerPreferences(new PDViewerPreferences(new COSDictionary()));
        documentCatalog.getViewerPreferences().setDisplayDocTitle(true);
        documentCatalog.setAcroForm(acroForm);
        PDStructureTreeRoot structureTreeRoot = new PDStructureTreeRoot();
        HashMap<String, String> roleMap = new HashMap<>();
        roleMap.put("Annotation", "Span");
        roleMap.put("Artifact", "P");
        roleMap.put("Bibliography", "BibEntry");
        roleMap.put("Chart", "Figure");
        roleMap.put("Diagram", "Figure");
        roleMap.put("DropCap", "Figure");
        roleMap.put("EndNote", "Note");
        roleMap.put("FootNote", "Note");
        roleMap.put("InlineShape", "Figure");
        roleMap.put("Outline", "Span");
        roleMap.put("Strikeout", "Span");
        roleMap.put("Subscript", "Span");
        roleMap.put("Superscript", "Span");
        roleMap.put("Underline", "Span");
        structureTreeRoot.setRoleMap(roleMap);
        documentCatalog.setStructureTreeRoot(structureTreeRoot);
        PDMarkInfo markInfo = new PDMarkInfo();
        markInfo.setMarked(true);
        documentCatalog.setMarkInfo(markInfo);

        //Create document initial pages
        COSArray cosArray = new COSArray();
        cosArray.add(COSName.getPDFName("PDF"));
        cosArray.add(COSName.getPDFName("Text"));
        COSArray boxArray = new COSArray();
        boxArray.add(new COSFloat(0.0f));
        boxArray.add(new COSFloat(0.0f));
        boxArray.add(new COSFloat(612.0f));
        boxArray.add(new COSFloat(792.0f));
        for (int i = 0; i < initPages; i++) {
            PDPage page = new PDPage(PDRectangle.LETTER);
            page.getCOSObject().setItem(COSName.getPDFName("Tabs"), COSName.S);
            page.setResources(resources);
            page.getResources().getCOSObject().setItem(COSName.PROC_SET, cosArray);
            page.getCOSObject().setItem(COSName.CROP_BOX, boxArray);
            page.getCOSObject().setItem(COSName.ROTATE, COSInteger.get(0));
            page.getCOSObject().setItem(COSName.STRUCT_PARENTS, COSInteger.get(0));
            pages.add(page);
            pdf.addPage(pages.get(pages.size() - 1));
        }
        nums.add(COSInteger.get(0));

    }

    //Add a text box at a given location starting from the top-left corner.
    private PDAnnotationWidget addTextField(float x, float y, float width, float height, String name, int pageIndex) throws IOException {

        PDRectangle rect = new PDRectangle(x, PAGE_HEIGHT - height - y, width, height);

        //Create the form field and add it to the acroForm object with a given name.
        fields.add(new PDTextField(acroForm));
        fields.get(fields.size() - 1).setPartialName(name);
        fields.get(fields.size() - 1).setAlternateFieldName(name);
        fields.get(fields.size() - 1).setValue("");
        ((PDTextField)fields.get(fields.size() - 1)).setDefaultAppearance(FIELD_APPEARANCE);
        ((PDTextField)fields.get(fields.size() - 1)).setMultiline(true);
        acroForm.getFields().add(fields.get(fields.size() - 1));
        System.out.println(name);

        // Specify a widget associated with the field.
        PDAnnotationWidget widget = new PDAnnotationWidget();
        widget.setRectangle(rect);
        PDAppearanceCharacteristicsDictionary fieldAppearance
                = new PDAppearanceCharacteristicsDictionary(new COSDictionary());
        fieldAppearance.setBorderColour(fieldBorderColor);
        fieldAppearance.setBackground(fieldBGColor);
        widget.setAppearanceCharacteristics(fieldAppearance);
        widget.setPage(pages.get(pageIndex));
        widget.setPrinted(true);

        //Add object reference to widget for tagging purposes.
        PDObjectReference objectReference = new PDObjectReference();
        objectReference.setReferencedObject(widget);
        annotationRefs.add(objectReference);
        widget.getCOSObject().setInt(COSName.STRUCT_PARENT, currentStructParent);
        currentStructParent++;

        //Add the widget to the page.
        widgets.add(widget);
        pages.get(pageIndex).getAnnotations().add(widgets.get(widgets.size() - 1));
        ((PDTextField)fields.get(fields.size() - 1)).setWidgets(Collections.singletonList(widgets.get(widgets.size() - 1)));

        return widgets.get(widgets.size() - 1);
    }

    //Add radio buttons at a given location starting from the top-left corner with or without text labels.
    private PDAnnotationWidget addRadioButton(float x, float y, float width, float height, List<String> values,
                                      int pageIndex, int valueIndex) throws IOException {

        //Bounding box for the widget.
        PDRectangle rect = new PDRectangle(
                x + valueIndex * (width / values.size()), PAGE_HEIGHT - height - y, width / values.size(), height);
        // Specify a widget associated with the field
        PDAnnotationWidget widget = new PDAnnotationWidget();
        widget.setRectangle(rect);
        widget.setPage(pages.get(pageIndex));

        //Appearance will always default to black border with white background.
        PDAppearanceCharacteristicsDictionary fieldAppearance
                = new PDAppearanceCharacteristicsDictionary(new COSDictionary());
        fieldAppearance.setBorderColour(PDConstants.BLACK);
        fieldAppearance.setBackground(PDConstants.WHITE);
        widget.setAppearanceCharacteristics(fieldAppearance);

        //Tie data values to the PDF document.
        PDAppearanceDictionary appearance = new PDAppearanceDictionary();
        COSDictionary dict = new COSDictionary();
        dict.setItem(COSName.getPDFName("Off"), new COSDictionary());
        dict.setItem(COSName.getPDFName(values.get(valueIndex)), new COSDictionary());
        PDAppearanceEntry appearanceEntry = new PDAppearanceEntry(dict);
        appearance.setNormalAppearance(appearanceEntry);
        widget.setPrinted(true);

        //Add object reference to widget for tagging purposes.
        PDObjectReference objectReference = new PDObjectReference();
        objectReference.setReferencedObject(widget);
        annotationRefs.add(objectReference);
        widget.getCOSObject().setInt(COSName.STRUCT_PARENT, currentStructParent);
        currentStructParent++;

        //Add the widget to the page.
        widgets.add(widget);
        pages.get(pageIndex).getAnnotations().add(widgets.get(widgets.size() - 1));

        return widgets.get(widgets.size() - 1);
    }

    public void drawSectionHeader(float x, float y, float height, String text, Color bgColor, Color textColor, int fontSize,
                               int pageIndex, PDStructureElement parent) throws IOException {

        //Set up the next marked content element with an MCID and create the containing H1 structure element.
        PDPageContentStream contents = new PDPageContentStream(
                pdf, pages.get(pageIndex), PDPageContentStream.AppendMode.APPEND, false);
        currentElem = addContentToParent(null, StandardStructureTypes.H1, pages.get(pageIndex), parent);

        //Make the actual cell rectangle and set as artifact to avoid detection.
        setNextMarkedContentDictionary(COSName.ARTIFACT.getName());
        contents.beginMarkedContent(COSName.ARTIFACT, PDPropertyList.create(currentMarkedContentDictionary));

        //Draws the cell itself with the given colors and location.
        drawDataCell(bgColor, bgColor, x, y + height, PAGE_WIDTH - x * 2, height, contents);
        contents.endMarkedContent();
        addContentToParent(COSName.ARTIFACT, null, pages.get(pageIndex), currentElem);
        contents.close();

        //Draw the cell's text as
        contents = new PDPageContentStream(
                pdf, pages.get(pageIndex), PDPageContentStream.AppendMode.APPEND, false);
        setNextMarkedContentDictionary(COSName.P.getName());
        contents.beginMarkedContent(COSName.P, PDPropertyList.create(currentMarkedContentDictionary));

        //Draws the given text centered within the current table cell.
        drawText(x, y + height + fontSize, fontSize, text, textColor, contents);

        //End the marked content and append it's P structure element to the containing TD structure element.
        contents.endMarkedContent();
        addContentToParent(COSName.P, null, pages.get(pageIndex), currentElem);
        contents.close();
    }

    //Given a DataTable (Even an irregular table) will draw each cell and any given text.
    public void drawDataTable(DataTable table, float x, float y, int pageIndex, List<String> radioValues,
                       String radioName, PDStructureElement parent) throws IOException {

        //Create a stream for drawing table's contents and append table structure element to the current form's structure element.
        PDStructureElement currentTable = addContentToParent(null, StandardStructureTypes.TABLE, pages.get(pageIndex), parent);

        //Go through each row and add a TR structure element to the table structure element.
        for (int i = 0; i < table.getRows().size(); i++) {

            //Go through each column and draw the cell and any cell's text with given alignment.
            PDStructureElement currentRow = addContentToParent(null, StandardStructureTypes.TR, pages.get(pageIndex), currentTable);
            ArrayList<PDAnnotationWidget> radioWidgets = new ArrayList<>();

            for(int j = 0; j < table.getRows().get(i).getCells().size(); j++) {

                //Set up the next marked content element with an MCID and create the containing TD structure element.
                PDPageContentStream contents = new PDPageContentStream(
                        pdf, pages.get(pageIndex), PDPageContentStream.AppendMode.APPEND, false);
                currentElem = addContentToParent(null, StandardStructureTypes.TD, pages.get(pageIndex), currentRow);

                //Make the actual cell rectangle and set as artifact to avoid detection.
                setNextMarkedContentDictionary(COSName.ARTIFACT.getName());
                contents.beginMarkedContent(COSName.ARTIFACT, PDPropertyList.create(currentMarkedContentDictionary));

                //Draws the cell itself with the given colors and location.
                drawDataCell(table.getCell(i, j).getCellColor(), table.getCell(i, j).getBorderColor(),
                        x + table.getRows().get(i).getCellPosition(j),
                        y + table.getRowPosition(i),
                        table.getCell(i, j).getWidth(), table.getRows().get(i).getHeight(), contents);
                contents.endMarkedContent();
                currentElem = addContentToParent(COSName.ARTIFACT, StandardStructureTypes.P, pages.get(pageIndex), currentElem);
                contents.close();

                //Draw the cell's text as
                contents = new PDPageContentStream(
                        pdf, pages.get(pageIndex), PDPageContentStream.AppendMode.APPEND, false);
                setNextMarkedContentDictionary(COSName.P.getName());
                contents.beginMarkedContent(COSName.P, PDPropertyList.create(currentMarkedContentDictionary));

                //Choose alignment...
                if (table.getCell(i, j).getAlign().equalsIgnoreCase(PDConstants.CENTER_ALIGN)) {

                    //Draws the given text centered within the current table cell.
                    drawText(x + table.getRows().get(i).getCellPosition(j) + table.getCell(i, j).getWidth() / 2 -
                                    table.getCell(i, j).getFontSize() / 3.75f * table.getCell(i, j).getText().length(),
                            y + table.getRowPosition(i) +
                                    table.getRows().get(i).getHeight() / 2 + table.getCell(i, j).getFontSize() / 4,
                            table.getCell(i, j).getFontSize(), table.getCell(i, j).getText(),
                            table.getCell(i, j).getTextColor(), contents);

                } else if (table.getCell(i, j).getAlign().equalsIgnoreCase(PDConstants.TOP_ALIGN)) {

                    //Draws the given text in the current cell left aligned with 5 pixels of padding.
                    drawText(x + table.getRows().get(i).getCellPosition(j) + 5,
                            y + table.getRowPosition(i) + table.getCell(i, j).getFontSize() / 4 + 5,
                            table.getCell(i, j).getFontSize(), table.getCell(i, j).getText(),
                            table.getCell(i, j).getTextColor(), contents);

                } else if (table.getCell(i, j).getAlign().equalsIgnoreCase(PDConstants.LEFT_ALIGN)) {

                    //Draws the given text in the current cell left aligned with 5 pixels of padding.
                    drawText(x + table.getRows().get(i).getCellPosition(j) + 5,
                            y + table.getRowPosition(i) +
                                    table.getRows().get(i).getHeight() / 2 + table.getCell(i, j).getFontSize() / 4,
                            table.getCell(i, j).getFontSize(), table.getCell(i, j).getText(),
                            table.getCell(i, j).getTextColor(), contents);

                }

                //End the marked content and append it's P structure element to the containing TD structure element.
                contents.endMarkedContent();
                addContentToParent(COSName.P, null, pages.get(pageIndex), currentElem);
                contents.close();

                //Add a radio button widget.
                if (!table.getCell(i, j).getRbVal().isEmpty()) {
                    PDStructureElement fieldElem = new PDStructureElement(StandardStructureTypes.FORM, currentElem);
                    radioWidgets.add(addRadioButton(
                            x + table.getRows().get(i).getCellPosition(j) -
                                    radioWidgets.size() * 10 + table.getCell(i, j).getWidth() / 4,
                            y + table.getRowPosition(i),
                            table.getCell(i, j).getWidth() * 1.5f, 20,
                            radioValues, pageIndex, radioWidgets.size()));
                    fieldElem.setPage(pages.get(pageIndex));
                    COSArray kArray = new COSArray();
                    kArray.add(COSInteger.get(currentMCID));
                    fieldElem.getCOSObject().setItem(COSName.K, kArray);
                    addWidgetContent(annotationRefs.get(annotationRefs.size() - 1), fieldElem, StandardStructureTypes.FORM, pageIndex);
                }

                if (radioValues.size() == radioWidgets.size()) {
                    //Create the form field and add it to the acroForm object with a given name.
                    fields.add(new PDRadioButton(acroForm));
                    fields.get(fields.size() - 1).setPartialName(radioName);
                    fields.get(fields.size() - 1).setAlternateFieldName(
                            radioName + " Row " + (i + 1) + " Column " + (j - radioValues.size() - 1));
                    ((PDRadioButton)fields.get(fields.size() - 1)).setExportValues(radioValues);
                    fields.get(fields.size() - 1).getCOSObject().setName(COSName.DV, radioValues.get(0));
                    acroForm.getFields().add(fields.get(fields.size() - 1));
                    ((PDRadioButton) fields.get(fields.size() - 1)).setWidgets(radioWidgets);
                    ((PDRadioButton) fields.get(fields.size() - 1)).setDefaultValue(radioValues.get(0));
                }

                //Add a text field in the current cell.
                if (!table.getCell(i, j).getTextVal().isEmpty()) {
                    PDStructureElement fieldElem = new PDStructureElement(StandardStructureTypes.FORM, currentElem);
                    addTextField(x + table.getRows().get(i).getCellPosition(j),
                            y + table.getRowPosition(i),
                            table.getCell(i, j).getWidth(), table.getRows().get(i).getHeight(),
                            table.getCell(i, j).getTextVal(), pageIndex);
                    fieldElem.setPage(pages.get(pageIndex));
                    COSArray kArray = new COSArray();
                    kArray.add(COSInteger.get(currentMCID));
                    fieldElem.getCOSObject().setItem(COSName.K, kArray);
                    addWidgetContent(annotationRefs.get(annotationRefs.size() - 1), fieldElem, StandardStructureTypes.FORM, pageIndex);
                }

            }
        }
    }

    private void addWidgetContent(PDObjectReference objectReference, PDStructureElement fieldElem, String type, int pageIndex) {
        COSDictionary annotDict = new COSDictionary();
        COSArray annotArray = new COSArray();
        annotArray.add(COSInteger.get(currentMCID));
        annotArray.add(objectReference);
        annotDict.setItem(COSName.K, annotArray);
        annotDict.setString(COSName.LANG, "EN-US");
        annotDict.setItem(COSName.P, currentElem.getCOSObject());
        annotDict.setItem(COSName.PG, pages.get(0).getCOSObject());
        annotDict.setName(COSName.S, type);
        annotDicts.add(annotDict);

        setNextMarkedContentDictionary(type);
        numDictionaries.add(annotDict);
        fieldElem.appendKid(objectReference);
        currentElem.appendKid(fieldElem);
    }

    //Add a rectangle at a given location starting from the top-left corner.
    private void drawDataCell(Color cellColor, Color borderColor, float x, float y, float width, float height,
                               PDPageContentStream contents) throws IOException{
        //Open up a stream to draw a bordered rectangle.
        contents.setNonStrokingColor(cellColor);
        contents.setStrokingColor(borderColor);
        contents.addRect(x, PAGE_HEIGHT - height - y, width, height);
        contents.fillAndStroke();
    }

    //Add text at a given location starting from the top-left corner.
    private void drawText(float x, float y, int fontSize, String text, Color textColor,
                  PDPageContentStream contents) throws IOException {
        //Open up a stream to draw text at a given location.
        contents.beginText();
        contents.setFont(defaultFont, fontSize);
        contents.newLineAtOffset(x, PAGE_HEIGHT - y);
        contents.setNonStrokingColor(textColor);
        String[] lines = text.split("\n");
        for (String s: lines) {
            contents.showText(s);
            contents.newLineAtOffset(0, -(fontSize * 2));
        }
        contents.endText();
    }

    //Add a structure element to a parent structure element with optional marked content given a non-null name param.
    private PDStructureElement addContentToParent(COSName name, String type, PDPage currentPage, PDStructureElement parent) {
        //Create a structure element and add it to the current section.
        PDStructureElement structureElement = null;
        if (type != null) {
            structureElement = new PDStructureElement(type, parent);
            structureElement.setPage(currentPage);
        }
        //If COSName is not null then there is marked content.
        if (name != null) {
            //numDict for parent tree
            COSDictionary numDict = new COSDictionary();
            numDict.setInt(COSName.K, currentMCID - 1);
            numDict.setString(COSName.LANG, "EN-US");
            numDict.setItem(COSName.PG, currentPage.getCOSObject());
            if (structureElement != null) {
                if (!COSName.ARTIFACT.equals(name)) {
                    structureElement.appendKid(new PDMarkedContent(name, currentMarkedContentDictionary));
                } else {
                    structureElement.appendKid(new PDArtifactMarkedContent(currentMarkedContentDictionary));
                }
                numDict.setItem(COSName.P, structureElement.getCOSObject());
            } else {
                if (!COSName.ARTIFACT.equals(name)) {
                    parent.appendKid(new PDMarkedContent(name, currentMarkedContentDictionary));
                } else {
                    parent.appendKid(new PDArtifactMarkedContent(currentMarkedContentDictionary));
                }
                numDict.setItem(COSName.P, parent.getCOSObject());
            }
            numDict.setName(COSName.S, name.getName());
            numDictionaries.add(numDict);
        }
        if (structureElement != null) {
            parent.appendKid(structureElement);
        }
        return structureElement;
    }

    //Assign an id for the next marked content element.
    private void setNextMarkedContentDictionary(String tag) {
        currentMarkedContentDictionary = new COSDictionary();
        currentMarkedContentDictionary.setName("Tag", tag);
        currentMarkedContentDictionary.setInt(COSName.MCID, currentMCID);
        currentMCID++;
    }

    //Adds the parent tree to root struct element to identify tagged content
    public void addParentTree() {
        COSDictionary dict = new COSDictionary();
        nums.add(numDictionaries);
        for (int i = 1; i < currentStructParent; i++) {
            nums.add(COSInteger.get(i));
            nums.add(annotDicts.get(i - 1));
        }
        dict.setItem(COSName.NUMS, nums);
        PDNumberTreeNode numberTreeNode = new PDNumberTreeNode(dict, dict.getClass());
        pdf.getDocumentCatalog().getStructureTreeRoot().setParentTreeNextKey(currentStructParent);
        pdf.getDocumentCatalog().getStructureTreeRoot().setParentTree(numberTreeNode);
    }

    //Add a blank page to the document.
    public void addPage() {
        PDPage page = new PDPage(PDRectangle.LETTER);
        page.getCOSObject().setItem(COSName.getPDFName("Tabs"), COSName.S);
        pages.add(page);
        pdf.addPage(pages.get(pages.size() - 1));
    }

    //Adds a DOCUMENT structure element as the structure tree root.
    public void addRoot() {
        PDStructureElement root = new PDStructureElement(StandardStructureTypes.DOCUMENT, null);
        root.setAlternateDescription("The document's root structure element.");
        root.setTitle("PDF Document");
        pdf.getDocumentCatalog().getStructureTreeRoot().appendKid(root);
        currentElem = root;
        rootElem = root;
    }

    //Adds a PART structure element to the structure tree root.
    public PDStructureElement addPart() {
        PDStructureElement part = new PDStructureElement(StandardStructureTypes.PART,
                pdf.getDocumentCatalog().getStructureTreeRoot());
        part.setAlternateDescription("The current pages main content part.");
        part.setTitle("Main Content");
        rootElem.appendKid(part);
        currentElem = part;
        return part;
    }

    //Adds a SECT structure element to the given parent.
    public PDStructureElement addSection(PDStructureElement parent) {
        PDStructureElement sect = new PDStructureElement(StandardStructureTypes.SECT, parent);
        parent.appendKid(sect);
        currentElem = sect;
        return sect;
    }

    public void saveAndClose(String filePath) throws IOException {
        pdf.save(filePath);
        pdf.close();
    }

    public PDDocument getPdf() {
        return pdf;
    }

    public ArrayList<PDPage> getPages() {
        return pages;
    }

    public ArrayList<PDField> getFields() {
        return fields;
    }

    public PDColor getFieldBGColor() {
        return fieldBGColor;
    }

    public void setFieldBGColor(PDColor fieldBGColor) {
        this.fieldBGColor = fieldBGColor;
    }

    public PDColor getFieldBorderColor() {
        return fieldBorderColor;
    }

    public void setFieldBorderColor(PDColor fieldBorderColor) {
        this.fieldBorderColor = fieldBorderColor;
    }

}

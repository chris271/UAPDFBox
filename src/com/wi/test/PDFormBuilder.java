package com.wi.test;

import org.apache.pdfbox.contentstream.PDContentStream;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdfwriter.ContentStreamWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.COSObjectable;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDMarkInfo;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDStructureElement;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDStructureTreeRoot;
import org.apache.pdfbox.pdmodel.documentinterchange.markedcontent.PDMarkedContent;
import org.apache.pdfbox.pdmodel.documentinterchange.markedcontent.PDPropertyList;
import org.apache.pdfbox.pdmodel.documentinterchange.taggedpdf.StandardStructureTypes;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDTilingPattern;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceCharacteristicsDictionary;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceDictionary;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceEntry;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDRadioButton;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;
import org.apache.pdfbox.pdmodel.interactive.viewerpreferences.PDViewerPreferences;
import org.apache.pdfbox.text.PDFMarkedContentExtractor;
import org.apache.pdfbox.text.TextPosition;
import org.apache.pdfbox.util.Matrix;
import org.apache.xmpbox.XMPMetadata;
import org.apache.xmpbox.schema.XMPSchema;
import org.apache.xmpbox.schema.XmpSchemaException;
import org.apache.xmpbox.xml.XmpSerializer;

import javax.xml.transform.TransformerException;
import java.awt.Color;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class PDFormBuilder {

    private PDDocument pdf = null;
    private PDAcroForm acroForm = null;
    private ArrayList<PDPage> pages = new ArrayList<>();
    private ArrayList<PDField> fields = new ArrayList<>();
    private PDColor fieldBGColor = null;
    private PDColor fieldBorderColor = null;
    private PDFont defaultFont = null;
    private PDStructureElement rootElem = null;
    private PDStructureElement currentElem = null;
    private PDStructureElement currentForm = null;
    private COSDictionary currentMarkedContentDictionary;
    private int currentMCID = 1;
    private final float PAGE_HEIGHT = PDRectangle.A4.getHeight();
    private final float PAGE_WIDTH = PDRectangle.A4.getWidth();
    private final String DEFAULT_APPEARANCE = "/Helv 10 Tf 0 g";
    private final String FIELD_APPEARANCE = "/Helv 12 Tf 0 g";

    PDFormBuilder(int initPages, String title) throws IOException, TransformerException, XmpSchemaException {
        //Setup new document
        pdf = new PDDocument();
        acroForm = new PDAcroForm(pdf);
        pdf.getDocumentInformation().setTitle(title);

        //Set AcroForm Appearance Characteristics
        PDResources resources = new PDResources();
        defaultFont = PDType0Font.load(pdf,
                new PDTrueTypeFont(PDType1Font.HELVETICA.getCOSObject()).getTrueTypeFont(), true);
        resources.put(COSName.getPDFName("Helv"), defaultFont);
        /*PDExtendedGraphicsState extendedGraphicsState = new PDExtendedGraphicsState();
        extendedGraphicsState.getCOSObject().setBoolean(COSName.AIS, false);
        extendedGraphicsState.getCOSObject().setName(COSName.BM, "Normal");
        extendedGraphicsState.getCOSObject().setFloat(COSName.CA, 1.0f);
        extendedGraphicsState.getCOSObject().setBoolean(COSName.OP, false);
        extendedGraphicsState.getCOSObject().setInt(COSName.OPM, 1);
        extendedGraphicsState.getCOSObject().setBoolean(COSName.SA, false);
        extendedGraphicsState.getCOSObject().setName(COSName.TYPE, "ExtGState");
        extendedGraphicsState.getCOSObject().setName(COSName.SMASK, "None");
        extendedGraphicsState.getCOSObject().setFloat(COSName.getPDFName("ca"), 1.0f);
        extendedGraphicsState.getCOSObject().setBoolean(COSName.getPDFName("op"), false);
        resources.put(COSName.EXT_G_STATE, extendedGraphicsState);*/
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
        roleMap.put("TextBox", "Art");
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

    }

    //Add a text box at a given location starting from the top-left corner.
    void addTextField(float x, float y, float width, float height, int fontSize, String name, String value,
                      int pageIndex) throws IOException {
        PDRectangle rect = new PDRectangle(x, PAGE_HEIGHT - height - y, width, height);

        //Create the form field and add it to the acroForm object with a given name.
        fields.add(new PDTextField(acroForm));
        fields.get(fields.size() - 1).setPartialName(name);
        fields.get(fields.size() - 1).setAlternateFieldName(name);
        ((PDTextField)fields.get(fields.size() - 1)).setDefaultAppearance(FIELD_APPEARANCE);
        ((PDTextField)fields.get(fields.size() - 1)).setMultiline(true);
        acroForm.getFields().add(fields.get(fields.size() - 1));

        List<PDAnnotationWidget> widgets = new ArrayList<>();
        // Specify a widget associated with the field.
        PDAnnotationWidget widget = new PDAnnotationWidget();
        widget.setRectangle(rect);
        widget.setPage(pages.get(pageIndex));
        PDAppearanceCharacteristicsDictionary fieldAppearance
                = new PDAppearanceCharacteristicsDictionary(new COSDictionary());
        fieldAppearance.setBorderColour(fieldBorderColor);
        fieldAppearance.setBackground(fieldBGColor);
        widget.setAppearanceCharacteristics(fieldAppearance);
        widget.setParent(((PDTextField)fields.get(fields.size() - 1)));

        //Set annotation to visible on print
        widget.setPrinted(true);
        widgets.add(widget);
        pages.get(pageIndex).getAnnotations().add(widgets.get(0));
        ((PDTextField)fields.get(fields.size() - 1)).setWidgets(widgets);
        fields.get(fields.size() - 1).setValue(value);
    }

    //Add radio buttons at a given location starting from the top-left corner with or without text labels.
    void addRadioField(float x, float y, float width, float height, int fontSize, String name, List<String> values,
                       int pageIndex, boolean text) throws IOException {
        //Create the form field and add it to the acroForm object with a given name.
        fields.add(new PDRadioButton(acroForm));
        fields.get(fields.size() - 1).setPartialName(name);
        fields.get(fields.size() - 1).setAlternateFieldName(name);
        ((PDRadioButton)fields.get(fields.size() - 1)).setExportValues(values);
        fields.get(fields.size() - 1).getCOSObject().setName(COSName.DV, values.get(0));
        acroForm.getFields().add(fields.get(fields.size() - 1));

        List<PDAnnotationWidget> widgets = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            PDRectangle rect = new PDRectangle(
                    x + i * (width / values.size()), PAGE_HEIGHT - height - y, width / values.size(), height);

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
            dict.setItem(COSName.getPDFName(values.get(i)), new COSDictionary());
            PDAppearanceEntry appearanceEntry = new PDAppearanceEntry(dict);
            appearance.setNormalAppearance(appearanceEntry);
            //widget.setAppearance(appearance);

            //Set annotation to visible when printing.
            widget.setPrinted(true);
            widget.getCOSObject().setInt(COSName.STRUCT_PARENT, 1);
            widget.getCOSObject().setItem(COSName.PARENT, currentForm.getCOSObject());
            //widget.setParent(((PDRadioButton)fields.get(fields.size() - 1)));
            widgets.add(widget);
            pages.get(pageIndex).getAnnotations().add(widgets.get(widgets.size() - 1));
            if (text) {
                PDPageContentStream contents = new PDPageContentStream(
                        pdf, pages.get(pageIndex), PDPageContentStream.AppendMode.APPEND, false);
                setNextMarkedContentDictionary();
                contents.beginMarkedContent(COSName.P, PDPropertyList.create(currentMarkedContentDictionary));
                drawText(rect.getLowerLeftX() + rect.getWidth() / 10,
                        PAGE_HEIGHT - rect.getLowerLeftY() - rect.getHeight() / 2,
                        fontSize, values.get(i), Color.black, contents);
                contents.close();
                addContentToParent(COSName.S, StandardStructureTypes.P, pages.get(pageIndex), currentElem);
            }
        }

        ((PDRadioButton) fields.get(fields.size() - 1)).setWidgets(widgets);
        ((PDRadioButton) fields.get(fields.size() - 1)).setDefaultValue(values.get(0));
        fields.get(fields.size() - 1).getCOSObject().setItem(COSName.PARENT, currentForm.getCOSObject());
    }

    //Given a DataTable (Even an irregular table) will draw each cell and any given text.
    void drawDataTable(DataTable table, float x, float y, int pageIndex) throws IOException{
        //Create a stream for drawing table's contents and append table structure element to the current form's structure element.
        PDPageContentStream contents = new PDPageContentStream(
                pdf, pages.get(pageIndex), PDPageContentStream.AppendMode.APPEND, false);
        PDStructureElement currentTable = addContentToParent(null, StandardStructureTypes.TABLE, pages.get(pageIndex), currentForm);
        //Go through each row and add a TR structure element to the table structure element.
        for (int i = 0; i < table.getRows().size(); i++) {
            PDStructureElement currentRow = addContentToParent(null, StandardStructureTypes.TR, pages.get(pageIndex), currentTable);
            //Go through each column and draw the cell and any cell's text with given alignment.
            for(int j = 0; j < table.getRows().get(i).getCells().size(); j++) {
                //Set up the next marked content element with an MCID and create the containing TD structure element.
                setNextMarkedContentDictionary();
                contents.beginMarkedContent(COSName.OC, PDPropertyList.create(currentMarkedContentDictionary));
                currentElem = addContentToParent(null, StandardStructureTypes.TD, pages.get(pageIndex), currentRow);
                //Draws the cell itself with the given colors and location.
                /*drawDataCell(table.getCell(i, j).getCellColor(), table.getCell(i, j).getBorderColor(),
                        x + table.getRows().get(i).getCellPosition(j),
                        y + table.getRowPosition(i),
                        table.getCell(i, j).getWidth(), table.getRows().get(i).getHeight(), contents);*/
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
                addContentToParent(COSName.P, StandardStructureTypes.P, pages.get(pageIndex), currentElem);
            }
        }
        contents.close();
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
        PDStructureElement structureElement = new PDStructureElement(type, parent);
        structureElement.setPage(currentPage);
        //If COSName is not null then there is marked content.
        if (name != null) {
            PDMarkedContent markedContent = new PDMarkedContent(name, currentMarkedContentDictionary);
            structureElement.appendKid(markedContent);
        }
        parent.appendKid(structureElement);
        return structureElement;
    }

    //Assign an id for the next marked content element.
    private void setNextMarkedContentDictionary() {
        currentMarkedContentDictionary = new COSDictionary();
        currentMarkedContentDictionary.setName("Tag", "P");
        currentMarkedContentDictionary.setName("Role", "S");
        currentMarkedContentDictionary.setString(COSName.ACTUAL_TEXT, "TEXT");
        currentMarkedContentDictionary.setInt(COSName.MCID, currentMCID);
        currentMCID++;

    }

    //List all alternate descriptions given root struct elem.
    void checkTreeStructure(List<Object> kids) {
        for (Object o : kids) {
            if (o instanceof PDStructureElement) {
                PDStructureElement structElement = (PDStructureElement)o;
                checkTreeStructure(structElement.getKids());
                System.out.println(structElement.getAlternateDescription());
            }
        }
    }

    //Not working still experimental
    void checkMarkedContent() throws IOException {
        PDFMarkedContentExtractor markedContentExtractor = new PDFMarkedContentExtractor("UTF-8");
        markedContentExtractor.processPage(pdf.getPage(0));
        List<PDMarkedContent>  markedContents = markedContentExtractor.getMarkedContents();
        for (Object o: markedContents) {
            if (o instanceof PDMarkedContent) {
                PDMarkedContent pdMarkedContent = (PDMarkedContent) o;
                System.out.println("mcid=" + pdMarkedContent.getMCID() + ", " + pdMarkedContent.toString());
            } else {
                System.out.println(o.toString());
            }
        }
    }

    void addTaggingOperators() throws IOException {
        for (PDPage page : pdf.getPages()) {
            List<Object> newTokens = createTokensWithoutText(page);
            PDStream newContents = new PDStream(pdf);
            writeTokensToStream(newContents, newTokens);
            page.setContents(newContents);
            page.getResources().getCOSObject().removeItem(COSName.PROPERTIES);
        }
        acroForm.getDefaultResources().getCOSObject().removeItem(COSName.PROPERTIES);
    }

    private static void writeTokensToStream(PDStream newContents, List<Object> newTokens) throws IOException {
        try (OutputStream out = newContents.createOutputStream(COSName.FLATE_DECODE)) {
            ContentStreamWriter writer = new ContentStreamWriter(out);
            writer.writeTokens(newTokens);
        }
    }

    private static List<Object> createTokensWithoutText(PDContentStream contentStream) throws IOException {
        PDFStreamParser parser = new PDFStreamParser(contentStream);
        Object token = parser.parseNextToken();
        List<Object> newTokens = new ArrayList<>();
        Integer currentMCID = 1;
        while (token != null) {
            if (token instanceof Operator) {
                Operator op = (Operator) token;
                System.out.println("Operator: " + op.getName());
                if (op.getName().equals("BT")) {
                    token = parser.parseNextToken();
                    continue;
                } else if (op.getName().equals("ET")) {
                    token = parser.parseNextToken();
                    continue;
                } else if (op.getName().equals("EMC")) {
                    newTokens.add(token);
                    newTokens.add(Operator.getOperator("ET"));
                    token = parser.parseNextToken();
                    continue;
                } else if (op.getName().equals("Tf")) {
                    newTokens.add(token);
                    newTokens.add(COSInteger.get(0));
                    newTokens.add(Operator.getOperator("Tc"));
                    newTokens.add(COSInteger.get(0));
                    newTokens.add(Operator.getOperator("Tw"));
                    token = parser.parseNextToken();
                    continue;
                }
            } else  if (token instanceof COSInteger) {
                COSInteger integer = (COSInteger)token;
                System.out.println(token.getClass().getName().split("\\.")[4] + ": " + (integer.intValue()));
            } else  if (token instanceof COSString) {
                COSString string = (COSString)token;
                System.out.println(token.getClass().getName().split("\\.")[4] + ": " + (string.getString()));
            } else  if (token instanceof COSName) {
                COSName name = (COSName)token;
                if (name.getName().contains("OC")) {
                    newTokens.add(Operator.getOperator("BT"));
                    COSDictionary mcidDict = new COSDictionary();
                    mcidDict.setInt(COSName.MCID, currentMCID);
                    newTokens.add(COSName.P);
                    newTokens.add(mcidDict);
                    System.out.println("New COSDict: " + (mcidDict.toString()));
                    currentMCID++;
                    token = parser.parseNextToken();
                    continue;
                } else if (name.getName().contains("Prop")) {
                    token = parser.parseNextToken();
                    continue;
                }
                System.out.println(token.getClass().getName().split("\\.")[4] + ": " + (name.getName()));
            } else  if (token instanceof COSFloat) {
                COSFloat floatP = (COSFloat)token;
                System.out.println(token.getClass().getName().split("\\.")[4] + ": " + (floatP.floatValue()));
            } else {
                System.out.println("OTHER");
            }
            newTokens.add(token);
            token = parser.parseNextToken();
        }
        return newTokens;
    }

    //Add a blank page to the document.
    void addPage() {
        PDPage page = new PDPage(PDRectangle.LETTER);
        page.getCOSObject().setItem(COSName.getPDFName("Tabs"), COSName.S);
        pages.add(page);
        pdf.addPage(pages.get(pages.size() - 1));
    }

    //Adds a DOCUMENT structure element as the structure tree root.
    void addRoot() {
        PDStructureElement root = new PDStructureElement(StandardStructureTypes.DOCUMENT, null);
        root.setAlternateDescription("The document's root structure element.");
        root.setTitle("PDF Document");
        pdf.getDocumentCatalog().getStructureTreeRoot().appendKid(root);
        currentElem = root;
        rootElem = root;
    }

    //Adds a PART structure element to the structure tree root.
    PDStructureElement addPart() {
        PDStructureElement part = new PDStructureElement(StandardStructureTypes.PART,
                pdf.getDocumentCatalog().getStructureTreeRoot());
        part.setAlternateDescription("The current pages main content part.");
        part.setTitle("Main Content");
        rootElem.appendKid(part);
        currentElem = part;
        return part;
    }

    //Adds a SECT structure element to the given parent.
    PDStructureElement addSection(PDStructureElement parent) {
        PDStructureElement sect = new PDStructureElement(StandardStructureTypes.SECT, parent);
        parent.appendKid(sect);
        currentElem = sect;
        return sect;
    }

    //Adds a FORM structure element to the given parent.
    void addForm(PDStructureElement parent) {
        PDStructureElement form = new PDStructureElement(StandardStructureTypes.FORM, parent);
        parent.appendKid(form);
        currentElem = form;
        currentForm = form;
    }

    void saveAndClose(String filePath) throws IOException {
        pdf.save(filePath);
        pdf.close();
    }

    PDDocument getPdf() {
        return pdf;
    }

    ArrayList<PDPage> getPages() {
        return pages;
    }

    ArrayList<PDField> getFields() {
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

/* RANDOM STUFF
PDMarkedContent markedContent = new PDMarkedContent(COSName.FT, widget.getCOSObject());
COSDictionary dictionary = new COSDictionary();
dictionary.setName(COSName.TYPE, "OBJR");
dictionary.setItem(COSName.OBJ, widget);
PDObjectReference reference = new PDObjectReference(dictionary);
reference.setReferencedObject(widget);
System.out.println(reference.getReferencedObject().getCOSObject());
currentForm.appendKid(reference);
COSDictionary dictionary = new COSDictionary();
dictionary.setName("Role", PDPrintFieldAttributeObject.ROLE_RB);
dictionary.setName("checked", "off");
dictionary.setString("Desc", "Radio Button");
dictionary.setName(COSName.O, "PrintField");
currentMarkedContentDictionary.setItem(COSName.PROPERTIES, prop);
PDPrintFieldAttributeObject fieldAttributeObject = new PDPrintFieldAttributeObject(dictionary);
PDStructureElement structureElement = new PDStructureElement(StandardStructureTypes.RB, currentForm);
structureElement.setPage(pages.get(pageIndex));
structureElement.addAttribute(fieldAttributeObject);
currentForm.appendKid(structureElement);
addContentToParent(COSName.WIDGET, StandardStructureTypes.P, pages.get(pageIndex), currentForm, false);
System.out.println(markedContent.getMCID());
System.out.println(markedContent.getActualText());
 */

}

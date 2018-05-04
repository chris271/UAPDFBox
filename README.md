# UAPDFBox
High level APIs for making user accessible PDFs

To compile Apache PDFBox must be included in external libraries.
An example output PDF has been provided named as 'UAEXAMPLE.PDF'

Work still to be completed:

While in the screen reader software JAWS there seems to be an issue when pressing e to go to the next text box in the PDF.
After pressing e it goes to the last text box in the page and pressing e again will loop back to the same element.

Also, When you are on the last text box and press TAB then it will wrap back to the first text box.
However, JAWS will then read the name of the last text box while focusing any other text box.
Pressing TAB after loading the PDF will read the form elements correctly until reaching the last text box.

While in the screen reader software JAWS there seems to be an issue when pressing a to go to the next radio button in the PDF.
After pressing a it says something like 'No radio buttons in the document'.

PAC 2 tool as recommended by section508.gov: http://www.access-for-all.ch/en/pdf-lab/pdf-accessibility-checker-pac.html

CREATED BY - chris271
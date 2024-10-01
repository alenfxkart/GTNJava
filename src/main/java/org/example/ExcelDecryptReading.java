package org.example;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

public class ExcelDecryptReading {
    public static void main(String args[]) throws IOException {
        String privateKey = "30820276020100300d06092a864886f70d0101010500048202603082025c020100028181009732d3dd224ac8964032b570fdc4e20ea85966a2d12bf96824b554feb9c8fb37905621e45050e0405661063af417995b4ac10b73803bca4eb42955899f263a52e874406686b0ecb0e55150000da8eb5cf98632949dec4db8bbe419a0e4a89bf38ae6767e7e27a35dddf1f5d3eea6e34783fde76e504986b6470bf87de9490a370203010001028180129bdcdb8096f7ad1665a7532ac88a92074249918c70ff17443d5522acb701c44aed6eea6cc0277983a8195e34209b6d52fc036ec9bf921cb1abccad4d7b24fe52d3cc79fb77321df9e29b0257757425198508bb242184f1f1c8f8a4e0d84cdf8bdc9010e3ed68793b5b77be5e2114ccaab3ad2da34f5fa3f41776fe1833ad31024100c60c58b03fa1caa1cb9eb33878b5e58174915b87629ab84feaac7b70276324bdd38c48f9a8b97724800f4688dd442c22b37882f3368192648e124dce39a717ad024100c371011c082e1f6b54d6d43a5508ff8e4781d020908be25c194c1b6f16dccd5a20d8b03be64e3b3f6700fdd1940990af662ff509c7ac90df8983707e12b9f5f30241009e5960d00b10a1a39bdad8115ef290fe60e597f937addd908862408ffbee19826d92cd065111796185c2b0f2dd7794926a4d00e6d5bde3fee259603c737c5641024062888987a9741f710c6fd6c5a5f3886c220f0770338fe2ca8e1279ce5317750c987cc6b51c529aba47630f9466ef3f9bbe883520fe23395309d05399ac354713024019889dd555f982cede7a943147794e7829a3b5bc10bd8aeba9b8fddfee97dd0391af208e4d8d3148025960612b213781e767f22875cea37adefb7c4379b574ca";

        String publicKey = "30819f300d06092a864886f70d010101050003818d00308189028181008ab72c81849829d0b4940becced7d38021f0592972486b4c8e4933b9c31f6087dba5204c916a8b497666aed0929fd036b0ae239b93e4f507aaaeb86ec3d3e76fa8c7faffe0fa8c2b3c8bd404648e6be7de25d29c1e6057fce56853b9d59d6aae4b97052ec80951482fb566182cc423c92f1ce2a35beffc7bf724c1458e2050710203010001";
        int rowcnt = 0;
        int i = 1;

        try {
            File file = new File("D:\\Projects\\Java\\GTNWebCall\\DecryptNYSE.xlsx");   //creating a new file instance
            FileInputStream fis = new FileInputStream(file);   //obtaining bytes from the file
//creating Workbook instance that refers to .xlsx file
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0);     //creating a Sheet object to retrieve object
            Iterator<Row> itr = sheet.iterator();    //iterating over excel file
            while (itr.hasNext()) {
                Row row = itr.next();
                Iterator<Cell> cellIterator = row.cellIterator();   //iterating over each column
                i = 1;
                String xchange = "";
                String ticker = "";
                String isin = "";
                String decrypval = "";

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_STRING:    //field that represents string cell type

                            if (i == 1) {
                                xchange = cell.getStringCellValue();
                                System.out.print(xchange + "\t\t\t");
                            }
                            if (i == 2) {
                                ticker = cell.getStringCellValue();
                                System.out.print(ticker + "\t\t\t");
                            }
                            if (i == 3) {
                                isin = cell.getStringCellValue();
                                System.out.print(isin + "\t\t\t");

                            }
                            if (i == 5) {

                                decrypval = cell.getStringCellValue();

                                if (rowcnt > 0) {
                                    try {

                                        DecryptionHandler sso2 = new DecryptionHandler(privateKey, publicKey);
                                        String result2 = sso2.validate(decrypval);
                                        System.out.println(result2 + "\t\t\t");

                                    } catch (Exception e) {
                                        System.out.println(e);
                                    }
                                }
                            }

                            i++;
                            break;
                        case Cell.CELL_TYPE_NUMERIC:    //field that represents number cell type
                            System.out.print(cell.getNumericCellValue() + "\t\t\t");

                            break;
                        default:
                    }
                }
                rowcnt++;
                System.out.println("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package org.example;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ExcelReadingNew {

    public static void main(String args[]) throws IOException {
        String privateKey ="30820277020100300d06092a864886f70d0101010500048202613082025d02010002818100e269b327d3fc24db7d3f7c0c0ce17a3f8cca92f1a8aa4e61c904f9d7db50a1781921bafc5cbe938dc4a0aca31bba07b6808c2a20a81625ac2a7bd2e06e234770b018e4b14e93d6df6b0df4115a74f0d46e1570cc7e082c529db372543678c079173f7b86d43386721654fbee5b059e2819710d5445739c3b71b55c1109727867020301000102818100c5bb500641811c633fbff8c2c1610d95808ccdc614ea1f281119b264cc995f5af67fd9c059f2ea044eab8ff9a6d651b41611d31cd00eff799b64a666f0690912810530c765b8d05b4fd52d82119c9eb45cde38ab6daa1ffc21a28aa3cc02b1b51aadbe2ae451100bb0be6f940d43dd961a05d8bcfd975502efaa215031531a11024100fcbcc3d196cda3bb2591a3f33c4635037dc48c293f0f27d5cedf46f16cacf950715ded0ce7a47553ffa7f5d35c12ac8285db82bf57b6b24ec5e50cfa98b3ac69024100e555f060970e096fd57787afbbc29ffdb44d4ed99d9e70d01580134b3fe264c592f1a8c5febc891d5567f9da30bc686bfb6efdcf78b4bd420ee0701ab585a44f024100935486e25b02ad7b9f3ef3500bc6f188837c5c287f361a86acfdc3a16134c4717c64c0975141b193b1e8fb88a697cc309a4f883c7aa969f039b478886d13f061024029a627d782d75bc361a3299332a82fe225627cd56c989fa84a9f4de1b6a268e6b0bf0e8596b3aabb170da1048d214a5d81f630460387df6f25c080db78e7e26502400373525ed324d8873086fa34477818642b797a912115e9f24cad0882e7e3684b520e6eb35ee493094fa2d848541a66663db6d66532cf1dc23030b87a45118efc";

        String publicKey ="30819f300d06092a864886f70d010101050003818d0030818902818100c3bc987e31379f097ac27b3c8a8fb426ae2c2d3ac18b7f642eb6b766cdbcc01273e625a22527850e6677865a2e3c4b79c88a905e3768339fcb6723692ec84c55569de43f8627d237be9cdd70483e34d6a8dddb11315a0d8cf64cf8e314315f68323ad4c28be3bfaf8038fcb186734ad2fa91f794cd80a4a6c4c4fdacd6e3abd30203010001";
        String toEncrypt = "";
        String encyptedPayload ="";
        int rowcnt = 0;
        int i = 1;

        Map<String, Object[]> GTNData
                = new TreeMap<String, Object[]>();

        GTNData.put(
                "1",
                new Object[] { "Id", "Exchange + ISIN", "Javaop" });


        try {
            File file = new File("F:\\Projects\\Java\\GTNWebCall\\GTN_Compliance_List_New_Sep30.xlsx");   //creating a new file instance
            FileInputStream fis = new FileInputStream(file);   //obtaining bytes from the file
//creating Workbook instance that refers to .xlsx file
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0);     //creating a Sheet object to retrieve object
            Iterator<Row> itr = sheet.iterator();    //iterating over excel file
            while (itr.hasNext()) {
                Row row = itr.next();
                Iterator<Cell> cellIterator = row.cellIterator();   //iterating over each column
                i =1;
                Integer Id = 0;
                String exhIsin = "";
                String isin = "";
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_STRING:    //field that represents string cell type

//                            if (i == 1) {
//                                Id = cell.getStringCellValue();
//                                //System.out.print(xchange + "\t\t\t");
//                            }
//                            if (i == 2) {
//                                exhIsin = cell.getStringCellValue();
//                                //System.out.print(ticker + "\t\t\t");
//                            }
                            if (i == 2) {

                                //System.out.print(isin + "\t\t\t");

                                if(rowcnt > 0)
                                {
                                    exhIsin = cell.getStringCellValue();

                                    try {

                                        //toEncrypt = xchange + "|" +  ticker+"|" ;  // "NSDQ|GOOGL|";
                                        toEncrypt = cell.getStringCellValue();
                                        // System.out.println("Payload: " + toEncrypt);
                                        Encoder encoder = new Encoder(privateKey, publicKey);
                                        encyptedPayload = encoder.getDualEncrypted(toEncrypt);
                                       // System.out.println(encyptedPayload + "\t\t\t");

                                    } catch (Exception e) {
                                        System.out.println(e);
                                    }
                                    GTNData.put(
                                            Integer.toString(rowcnt + 1),
                                            new Object[] { Id, exhIsin, encyptedPayload });

                                }

                            }
//                            if(i==3)
//                            {
//                                cell.setCellValue(encyptedPayload);
//                            }
                            i++;
                            break;
                        case Cell.CELL_TYPE_NUMERIC:    //field that represents number cell type
                            //System.out.print(cell.getNumericCellValue() + "\t\t\t");
                            if (i == 1) {
                                //Id = String.valueOf((int) cell.getNumericCellValue());
                                Id =  (int) cell.getNumericCellValue();
                                //System.out.print(xchange + "\t\t\t");
                            }

                            i++;

                            break;

                        default:
                    }
                }
                rowcnt++;
                //System.out.println("");
            }
        //Write the workbook in file system

                    XSSFWorkbook workbook = new XSSFWorkbook();

        //Create a blank sheet

                    XSSFSheet sheet1 = workbook.createSheet("Employee Data");

            Set<String> keyset = GTNData.keySet();
            int rownum = 0;
            for (String key : keyset) {

                Row row = sheet1.createRow(rownum++);
                Object [] objArr = GTNData.get(key);
                int cellnum = 0;
                for (Object obj : objArr)
                {
                    Cell cell = row.createCell(cellnum++);
                    if(obj instanceof String)
                        cell.setCellValue((String)obj);
                    else if(obj instanceof Integer)
                        cell.setCellValue((Integer)obj);
                }
            }

            try {
                FileOutputStream out = new FileOutputStream(new File("GTN_Compliance_List_New3_Out.xlsx"));
                workbook.write(out);
                out.close();
                System.out.println("GTN_Compliance_List_New3_Out.xlsx written successfully on disk.");
            }
            catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

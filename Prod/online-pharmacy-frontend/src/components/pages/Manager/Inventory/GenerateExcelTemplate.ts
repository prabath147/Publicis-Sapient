import { showNotification } from "@mantine/notifications";
import { getStoreInventory } from "./InventoryAPI";

const { Workbook } = require('exceljs');


const header = ["Medicine ID", "Proprietary Name", "Medicine Name", "Medicine Category", "Product Type", "Medicine Description", "Dosage From", "Medicine Image", "Manufacture Date(dd-mm-yyyy)", "Expiry Datee(dd-mm-yyyy)", "Quantity", "Price"];
const headerUpdate = ["Item ID", "Medicine ID", "Proprietary Name", "Medicine Name", "Medicine Category", "Product Type", "Medicine Description", "Dosage From", "Medicine Image", "Manufacture Date(dd-mm-yyyy)", "Expiry Datee(dd-mm-yyyy)", "Quantity", "Price"];
// const Workbook = new ExcelJS.Workbook();
function generateExcelTemplate() {

    //Create workbook and worksheet
    let workbook = new Workbook();
    let worksheet = workbook.addWorksheet('sheet');

    //Add Header Row
    let headerRow = worksheet.addRow(header);

    // Cell Style : Fill and Border
    headerRow.eachCell((cell, number) => {
        cell.fill = {
            type: 'pattern',
            pattern: 'solid',
            fgColor: { argb: 'FFFFFF00' },
            bgColor: { argb: 'FF0000FF' }
        }
        cell.border = { top: { style: 'thin' }, left: { style: 'thin' }, bottom: { style: 'thin' }, right: { style: 'thin' } }
    })
    worksheet.getColumn(1).width = 11;
    worksheet.getColumn(2).width = 15.5;
    worksheet.getColumn(3).width = 15.5;
    worksheet.getColumn(4).width = 16.4;
    worksheet.getColumn(5).width = 11.2;
    worksheet.getColumn(6).width = 30;
    worksheet.getColumn(7).width = 11.5;
    worksheet.getColumn(8).width = 14;
    worksheet.getColumn(9).width = 16;
    worksheet.getColumn(10).width = 16;
    worksheet.getColumn(11).width = 10;
    worksheet.getColumn(12).width = 10;


    // list.forEach((element,index) =>{
    //   worksheet.getColumn('E1:').dataValidation = {
    //     type: 'list',
    //     allowBlank: true,
    //     formulae: ['"Selected,Rejected,On-hold"']
    // };

    for (let rowNumber = 2; rowNumber <= 100; rowNumber++) {
        worksheet.getCell('E' + (rowNumber)).dataValidation = {
            type: 'list',
            allowBlank: true,
            formulae: ['"Generic,Non-Generic"']
        };
    }



    //Generate Excel File with given name
    workbook.xlsx.writeBuffer().then((data) => {
        let blob = new Blob([data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
        const fileURL = window.URL.createObjectURL(blob);
        const alink = document.createElement('a');
        alink.href = fileURL;
        alink.download = 'inventory.xlsx';
        alink.click();
    })
    //   await workbook.xlsx.writeFile('candidate.xlsx');


}
async function generateInventorySheet(storeId) {

    try {
        //Create workbook and worksheet
        let workbook = new Workbook();
        let worksheet = workbook.addWorksheet('sheet');

        //Add Header Row
        let headerRow = worksheet.addRow(headerUpdate);

        // Cell Style : Fill and Border
        headerRow.eachCell((cell, number) => {
            cell.fill = {
                type: 'pattern',
                pattern: 'solid',
                fgColor: { argb: 'FFFFFF00' },
                bgColor: { argb: 'FF0000FF' }
            }
            cell.border = { top: { style: 'thin' }, left: { style: 'thin' }, bottom: { style: 'thin' }, right: { style: 'thin' } }
        })

        worksheet.getColumn(1).width = 11;
        worksheet.getColumn(2).width = 11;
        worksheet.getColumn(3).width = 15.5;
        worksheet.getColumn(4).width = 15.5;
        worksheet.getColumn(5).width = 16.4;
        worksheet.getColumn(6).width = 11.2;
        worksheet.getColumn(7).width = 30;
        worksheet.getColumn(8).width = 11.5;
        worksheet.getColumn(8).width = 14;
        worksheet.getColumn(10).width = 16;
        worksheet.getColumn(11).width = 16;
        worksheet.getColumn(12).width = 10;
        worksheet.getColumn(13).width = 10;

        // list.forEach((element,index) =>{
        //   worksheet.getColumn('E1:').dataValidation = {
        //     type: 'list',
        //     allowBlank: true,
        //     formulae: ['"Selected,Rejected,On-hold"']
        // };


        const { data } = await getStoreInventory(storeId);

        data.forEach(item => {
            let row: any[] = [];
            // Object.keys(item)
            //     .forEach(key => {
            //         const value: string = item[key];
            //         row.push(value);
            //     });
            row.push(item.itemId);
            row.push(item.product.productId);
            row.push(item.product.proprietaryName);
            row.push(item.product.productName);

            let catSet = "";


            item.product.categorySet.forEach((cat, index) => {
                catSet += cat.categoryName;
                if (index + 1 < item.product.categorySet.length) {
                    catSet += ",";
                }
            })

            row.push(catSet);
            row.push(item.product.productType ? "Generic" : "Non-Generic");
            row.push(item.product.description);
            row.push(item.product.dosageForm);
            row.push(item.product.imageUrl);
            row.push(new Date(item.manufacturedDate));
            row.push(new Date(item.expiryDate));
            // row.push(item.expiryDate);
            row.push(item.itemQuantity);
            row.push(item.price);
            worksheet.addRow(row);
        });


        // for (let rowNumber = 2; rowNumber <= 100; rowNumber++) {
        //     worksheet.getCell('E' + (rowNumber)).dataValidation = {
        //         type: 'list',
        //         allowBlank: true,
        //         formulae: ['"Generic,Non-Generic"']
        //     };
        // }



        //Generate Excel File with given name
        workbook.xlsx.writeBuffer().then((data) => {
            let blob = new Blob([data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
            const fileURL = window.URL.createObjectURL(blob);
            const alink = document.createElement('a');
            alink.href = fileURL;
            alink.download = 'inventory.xlsx';
            alink.click();
        })
        //   await workbook.xlsx.writeFile('candidate.xlsx');
    } catch (error) {
        showNotification({
            message: "Something went wrong!!",
            color: "red"
        });
    }

}




export { generateExcelTemplate, generateInventorySheet };


package com.pharmacy.service.utils;

import com.pharmacy.service.dto.CategoryDto;
import com.pharmacy.service.dto.ItemDto;
import com.pharmacy.service.dto.ProductDto;
import com.pharmacy.service.dto.StoreDto;
import com.pharmacy.service.exception.ResourceException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class StoreItemExporter {
    private final StoreDto storeDto;
    private final List<ItemDto> itemDtoList;
    private final XSSFWorkbook workbook;
    private XSSFSheet sheet;

    private final boolean isUpdate;

    public StoreItemExporter(StoreDto storeDto, List<ItemDto> itemDtoList, boolean isUpdate) {
        this.storeDto = storeDto;
        this.itemDtoList = itemDtoList;
        this.workbook = new XSSFWorkbook();
        this.isUpdate = isUpdate;
    }

    private void getAddInventoryHeader(boolean isUpdate) {
        this.sheet = this.workbook.createSheet("Inventory-" + storeDto.getStoreName());

        Row row = this.sheet.createRow(0);

        CellStyle style = this.workbook.createCellStyle();
        XSSFFont font = this.workbook.createFont();
        font.setBold(true);
        font.setFontHeight(13);
        style.setFont(font);
        style.setLocked(true);

        if (isUpdate) this.createCell(row, 0, "Item ID", style);
        this.createCell(row, 0, "Medicine ID", style);
        this.createCell(row, 1, "Proprietary Name", style);
        this.createCell(row, 2, "Medicine Name", style);
        this.createCell(row, 3, "Category", style);
        this.createCell(row, 4, "Product Type", style);
        this.createCell(row, 5, "Description", style);
        this.createCell(row, 6, "Dosage Form", style);
        this.createCell(row, 7, "Image URL", style);
        this.createCell(row, 8, "Manufacture Date", style);
        this.createCell(row, 9, "Expiry Date", style);
        this.createCell(row, 10, "Quantity", style);
        this.createCell(row, 11, "Price", style);
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        this.sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Date) {
            CreationHelper createHelper = workbook.getCreationHelper();
            CellStyle newStyle = workbook.createCellStyle();
            newStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-mm-yyyy"));
            cell.setCellValue((Date) value);
            cell.setCellStyle(newStyle);
        } else {
            if (value instanceof Long) {
                cell.setCellValue((Long) value);
            } else if (value instanceof Boolean) {
                cell.setCellValue((Boolean) value);
            } else if (value instanceof Double) {
                cell.setCellValue((Double) value);
            } else {
                cell.setCellValue((String) value);
            }
            cell.setCellStyle(style);
        }
    }

    private void getAddInventorySheet(boolean isUpdate) {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        style.setLocked(true);

        for (ItemDto itemDto : this.itemDtoList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            ProductDto productDto = itemDto.getProduct();

            if (isUpdate) this.createCell(row, columnCount++, itemDto.getItemId(), style);
            this.createCell(row, columnCount++, productDto.getProductId(), style);
            this.createCell(row, columnCount++, productDto.getProprietaryName(), style);
            this.createCell(row, columnCount++, productDto.getProductName(), style);
            this.createCell(row, columnCount++, getCategory(productDto.getCategorySet()), style);
            this.createCell(row, columnCount++, productDto.getProductType(), style);
            this.createCell(row, columnCount++, productDto.getDescription(), style);
            this.createCell(row, columnCount++, productDto.getDosageForm(), style);
            this.createCell(row, columnCount++, productDto.getImageUrl(), style);
            this.createCell(row, columnCount++, itemDto.getManufacturedDate(), style);
            this.createCell(row, columnCount++, itemDto.getExpiryDate(), style);
            this.createCell(row, columnCount++, itemDto.getItemQuantity(), style);
            this.createCell(row, columnCount, itemDto.getPrice(), style);
        }
    }

    private String getCategory(Set<CategoryDto> categoryDtoSet) {
        Optional<CategoryDto> optionalCategoryDto = categoryDtoSet.stream().findAny();
        if (optionalCategoryDto.isPresent()) return optionalCategoryDto.get().getCategoryName();
        throw new ResourceException("Category", "category set empty", categoryDtoSet, ResourceException.ErrorType.FOUND);
    }

    public void exportInventorySheet(HttpServletResponse response) throws IOException {
        getAddInventoryHeader(this.isUpdate);
        getAddInventorySheet(this.isUpdate);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();

    }
}

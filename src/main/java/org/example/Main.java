package org.example;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Main {

    // === НАСТРОЙКИ (меняйте под себя) ===
    private static final float MARGIN_LEFT   = 25f;
    private static final float MARGIN_RIGHT  = 25f;
    private static final float MARGIN_TOP    = 0f;
    private static final float MARGIN_BOTTOM = 0f;
    private static final float FONT_SIZE     = 15f;
    private static final float CELL_PADDING  = 1.5f;
    private static final float COLUMN_GAP    = 0f;
    // =====================================

    public static void main(String[] args) throws Exception {

        // 1. Генерируем комбинации через ArrayList
        List<String> combos = new ArrayList<>();
        for (int i = 1; i <= 4; i++)
            for (int j = 1; j <= 4; j++)
                for (int k = 1; k <= 4; k++)
                    for (int l = 1; l <= 4; l++)
                        combos.add(i + "-" + j + "-" + k + "-" + l);

        System.out.println("Сгенерировано: " + combos.size()); // должно быть 128

        // 2. Создаём PDF (A4 альбомная)
        Document doc = new Document(PageSize.A4.rotate(),
                MARGIN_LEFT, MARGIN_RIGHT, MARGIN_TOP, MARGIN_BOTTOM);
        PdfWriter.getInstance(doc, new FileOutputStream("combinations.pdf"));
        doc.open();

        // 3. Заголовок
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
        Paragraph title = new Paragraph(
                "", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(10f);
        doc.add(title);

        // 4. Таблица 4 колонки × 32 строки
        int total   = combos.size();        // 128
        int rows    = total / 8;            // 32
        int columns = 8;

        PdfPTable table = new PdfPTable(columns);
        table.setWidthPercentage(100);
        table.setSpacingBefore(5f);
        table.setSpacingAfter(5f);
        table.setWidths(new float[]{1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f});

        Font cellFont = new Font(Font.FontFamily.HELVETICA, FONT_SIZE);

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                int idx = c * rows + r;     // индекс в списке
                PdfPCell cell = new PdfPCell(
                        new Phrase(combos.get(idx), cellFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setPadding(CELL_PADDING);
                cell.setPaddingLeft(COLUMN_GAP);
                cell.setPaddingRight(COLUMN_GAP);
                table.addCell(cell);
            }
        }
        doc.add(table);

        // 5. Итоговая надпись
        Font totalFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
        Paragraph totalP = new Paragraph("Всего: " + total, totalFont);
        totalP.setAlignment(Element.ALIGN_CENTER);
        totalP.setSpacingBefore(10f);
        doc.add(totalP);

        doc.close();
        System.out.println("PDF создан: combinations.pdf");
    }
}
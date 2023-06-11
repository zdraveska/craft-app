package mk.ukim.finki.draftcraft.util;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import mk.ukim.finki.draftcraft.dto.ProductDto;
import mk.ukim.finki.draftcraft.dto.ShopDto;

import java.io.OutputStream;
import java.util.List;

import static com.itextpdf.text.BaseColor.BLUE;
import static com.itextpdf.text.BaseColor.WHITE;


public class PdfUtil {


  private static void writeTableHeader(PdfPTable table) {
    PdfPCell cell = new PdfPCell();
    cell.setBackgroundColor(BLUE);
    cell.setPadding(5);

    Font font = FontFactory.getFont(FontFactory.HELVETICA);
    font.setColor(WHITE);

    cell.setPhrase(new Phrase("Name", font));
    table.addCell(cell);

    //TODO continue

  }

  private static void writeTableData(PdfPTable table, List<ProductDto> products) {
    products.forEach((product) -> {
      table.addCell(product.getName());
      table.addCell(product.getCategory().name());
      //TODO continue
    });
  }

  //TODO decide if needed
  public static void exportShopProducts(OutputStream outputStream, ShopDto shop)
      throws DocumentException {
    Document document = new Document(PageSize.A4);
    PdfWriter.getInstance(document, outputStream);

    document.open();
    Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
    font.setSize(40);
    font.setColor(BLUE);

    Paragraph p = new Paragraph("Shop", font);
    p.setAlignment(Paragraph.ALIGN_CENTER);

    document.add(p);

    PdfPTable table = new PdfPTable(4);
    table.setWidthPercentage(100f);
    table.setWidths(new float[]{2.5f, 2.5f, 8.0f, 3.0f});
    table.setSpacingBefore(10);
    writeTableHeader(table);
    writeTableData(table, shop.getProducts());

    document.add(table);

    document.close();
  }
}

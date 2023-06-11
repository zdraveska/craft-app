package mk.ukim.finki.draftcraft.util;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import mk.ukim.finki.draftcraft.domain.exceptions.InvalidFileException;
import mk.ukim.finki.draftcraft.domain.shop.Product;
import mk.ukim.finki.draftcraft.domain.shop.Shop;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

public class CsvUtil {

  public static List<Product> parseProductsFromCsv(MultipartFile file, Shop shop) {
    try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
      CsvToBean<Product> csvToBean = new CsvToBeanBuilder<Product>(reader)
          .withType(Product.class)
          .withIgnoreLeadingWhiteSpace(true)
          .build();
      List<Product> products = csvToBean.parse();
      for (Product product : products) {
        product.setShop(shop);
      }
      reader.close();
      return products;
    } catch (IOException e) {
      throw new InvalidFileException(file.getOriginalFilename());
    }
  }

}


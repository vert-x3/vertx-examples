package io.vertx.example.web.http2;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.impl.VertxInternal;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Image {

  final Map<Integer, Buffer> colorMap = new HashMap<>();

  private final int width;
  private final int height;
  private final int[] data;

  public Image(Vertx vertx, String name) {
    try {
      final BufferedImage raster = ImageIO.read(((VertxInternal) vertx).resolveFile(name));
      width = raster.getWidth();
      height = raster.getHeight();

      data = raster.getRGB(0, 0, width, height, null, 0, width);

      for (int pixel : data)
        if (!colorMap.containsKey(pixel)) {
          BufferedImage offlineImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

          Graphics2D g2 = offlineImage.createGraphics();
          g2.setPaint(new Color(pixel, true));
          g2.fillRect(0, 0, 1, 1);

          ByteArrayOutputStream out = new ByteArrayOutputStream();
          ImageIO.write(offlineImage, "PNG", out);

          colorMap.put(pixel, Buffer.buffer().appendBytes(out.toByteArray()));
          out.close();
          g2.dispose();
        }

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public Buffer generateHTML(int sz) {
    final Buffer buffer = Buffer.buffer();

    buffer.appendString("<!DOCTYPE html>");
    buffer.appendString("<html>");
    buffer.appendString("<head>");
    buffer.appendString("<title>H2 test</title>");
    buffer.appendString("<style>" +
      "#wrap { width:" + width * sz + "px; height:" + height * sz + "px };" +
      ".row { width:" + width * sz + "px }" +
      "img { margin:0; padding:0; border:none; float:left; width: " + sz + "px; height: " + sz + "px }" +
      "</style>");
    buffer.appendString("</head>");
    buffer.appendString("<body><div id=\"wrap\">");

    for (int i = 0; i < height; i++) {
      buffer.appendString("<div class=\"row\">");
      for (int j = 0; j < width; j++) {
        buffer.appendString("<img src=\"/img/" + j + "/" + i + "\">");
      }
      buffer.appendString("</div>");
    }

    buffer.appendString("</div></body>");
    buffer.appendString("</html>");

    return buffer;
  }

  public Buffer getPixel(int x, int y) {
    return colorMap.get(data[y * width + x]);
  }
}

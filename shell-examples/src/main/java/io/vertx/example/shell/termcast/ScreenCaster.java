package io.vertx.example.shell.termcast;

import io.vertx.core.Vertx;
import io.vertx.ext.shell.term.Term;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ScreenCaster {

  private final Robot robot;
  private final Vertx vertx;
  private final Term term;
  private boolean interrupted;

  public ScreenCaster(Vertx vertx, Robot robot, Term term) {
    this.vertx = vertx;
    this.robot = robot;
    this.term = term;
    term.stdinHandler(keys -> {
      // Ctrl-C or Ctrl-D
      if (keys.contains("\u0003") || keys.contains("\u0004")) {
        interrupted = true;
      }
    });
  }

  public void handle() {
    if (term.width() > 0 && term.height() > 0) {
      broadcast();
    } else {
      term.resizehandler(v -> {
        broadcast();
      });
    }
  }

  private void broadcast() {
    if (interrupted) {
      term.close();
      return;
    }
    BufferedImage capture = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
    int width = term.width();
    int height = term.height();
    Image temp = capture.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    BufferedImage scaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = scaled.createGraphics();
    g2d.drawImage(temp, 0, 0, null);
    g2d.dispose();
    StringBuilder sb = new StringBuilder();
    for (int y = 0; y < height; y++) {
      sb.append("\033[").append(y + 1).append(";1H");
      for (int x = 0; x < width; x++) {
        Color pixel = new Color(scaled.getRGB(x, y));
        int r = pixel.getRed();
        int g = pixel.getGreen();
        int b = pixel.getBlue();
        double grey = (r + g + b) / 3.0;
        if (grey < 51) {
          sb.append('\u2588');
        } else if (grey < 102) {
          sb.append('\u2593');
        } else if (grey < 153) {
          sb.append('\u2592');
        } else if (grey < 204) {
          sb.append('\u2591');
        } else {
          sb.append(' ');
        }
      }
    }
    term.write(sb.toString());
    vertx.setTimer(100, v -> broadcast());
  }
}

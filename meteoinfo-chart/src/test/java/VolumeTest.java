import org.meteoinfo.chart.GLChart;
import org.meteoinfo.chart.GLChartPanel;
import org.meteoinfo.chart.MouseMode;
import org.meteoinfo.chart.jogl.GLPlot;
import org.meteoinfo.chart.graphic.VolumeGraphic;
import org.meteoinfo.chart.render.jogl.RayCastingType;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.joml.Math.clamp;

public class VolumeTest {
    int width, height, depth;
    byte[] data;
    Byte min = null;
    Byte max = null;
    byte[] colors = new byte[256 * 4];

    public void readData() throws IOException {
        //File file = new File("src\\test\\resources\\sagittal.png");
        File file = new File(VolumeTest.class.getResource("/sagittal.png").getFile());
        final BufferedImage bufferedImage = ImageIO.read(file);
        width = bufferedImage.getWidth() / 2;
        height = bufferedImage.getHeight() / 88;
        depth = 176;
        data = new byte[width * height * depth];
        for (int w = 0, b = 0; w < 2; w++) {
            for (int h = 0; h < 88; h++) {
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        data[b++] = (byte) ((bufferedImage.getRGB(w * width + x, h * height + y) >> 16) & 0xff);
                        if (min == null || data[b - 1] < min) {
                            min = data[b - 1];
                        }
                        if (max == null || data[b - 1] > max) {
                            max = data[b - 1];
                        }
                    }
                }
            }
        }
    }

    public void readColorMap() throws IOException {
        //File file = new File("D:\\Temp\\image\\colors1.png");
        File file = new File(VolumeTest.class.getResource("/colors1.png").getFile());
        final BufferedImage bufferedImage = ImageIO.read(file);
        final byte[] originalColors = new byte[256 * 3];
        float[] opacityLevels = new float[]{0, 1};
        float[] opacityNodes = new float[]{0f, 1f};
        float[] colorRange = new float[]{0f, 1f};
        for (int i = 0; i < 256; i++) {
            final int color = bufferedImage.getRGB(i, 0);
            originalColors[i * 3 + 0] = (byte) ((color >> 16) & 0xff);
            originalColors[i * 3 + 1] = (byte) ((color >> 8) & 0xff);
            originalColors[i * 3 + 2] = (byte) ((color) & 0xff);
        }

        final float cRange = colorRange[1] - colorRange[0];
        final float min = opacityLevels[0] * opacityLevels[0];
        final float max = opacityLevels[1] * opacityLevels[1];
        final float opacityNodeRange = opacityNodes[1] - opacityNodes[0];
        for (int i = 0; i < 256; i++) {
            float px = ((float) i) / 255;
            float a;
            if (px <= opacityNodes[0]) {
                a = opacityNodes[0];
            } else if (px > opacityNodes[1]) {
                a = opacityNodes[1];
            } else {
                final float ratio = (px - opacityNodes[0]) / opacityNodeRange;
                a = (min * (1 - ratio) + max * ratio);
            }
            if (i < 100)
                a = 0;
            int colorI = 0;
            if (px > colorRange[1] * 255) {
                colorI = 255;
            } else if (px > colorRange[0]) {
                colorI = clamp(0, 255, Math.round(((((float) i) / 255) - colorRange[0]) * (1f / cRange) * 255f));
            }
            float r = ((float) Byte.toUnsignedInt(originalColors[colorI * 3 + 0])) / 255;
            float g = ((float) Byte.toUnsignedInt(originalColors[colorI * 3 + 1])) / 255;
            float b = ((float) Byte.toUnsignedInt(originalColors[colorI * 3 + 2])) / 255;

            r = r * r * a;
            g = g * g * a;
            b = b * b * a;

            colors[i * 4 + 0] = (byte) Math.round(r * 255);
            colors[i * 4 + 1] = (byte) Math.round(g * 255);
            colors[i * 4 + 2] = (byte) Math.round(b * 255);
            colors[i * 4 + 3] = (byte) Math.round(a * 255);
        }
    }

    public VolumeGraphic createGraphics() {
        return new VolumeGraphic(this.data, this.width, this.height, this.depth, this.colors);
    }

    public static void main(String[] args) throws IOException {
        VolumeTest test = new VolumeTest();
        test.readData();
        test.readColorMap();
        VolumeGraphic graphics = test.createGraphics();
        graphics.setRayCastingType(RayCastingType.SPECULAR);
        JFrame frame = new JFrame("Volume Test");
        GLPlot plot = new GLPlot();
        plot.setOrthographic(false);
        plot.setClipPlane(false);
        //plot.setBackground(Color.black);
        //plot.setForeground(Color.blue);
        plot.setDrawBoundingBox(true);
        plot.setDrawBase(true);
        plot.setBoxed(true);
        plot.setDisplayXY(true);
        //plot.getXAxis().setDrawTickLabel(false);
        //plot.getYAxis().setDrawTickLabel(false);
        plot.setDisplayZ(true);
        plot.getZAxis().setLabel("Z axis");
        //plot.getGridLine().setDrawXLine(false);
        //plot.getGridLine().setDrawYLine(false);
        //plot.getGridLine().setDrawZLine(false);
        plot.addGraphic(graphics);
        GLChartPanel canvas = new GLChartPanel(new GLChart());
        canvas.getChart().addPlot(plot);
        canvas.setMouseMode(MouseMode.ROTATE);
        frame.getContentPane().add(canvas);
        frame.pack();
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

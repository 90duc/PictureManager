package cn.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.IntBuffer;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritablePixelFormat;

import javax.imageio.ImageIO;

/**
 *
 * @author MK
 */
public class CompressImage {

    public static void compressImage() {
        File imageFile = new File("G:\\动物\\00.jpg");
        File imageFile1 = new File("G:\\动物\\00a.jpg");
        try {
            int width = 100;
            int height = 80;
            Image image = ImageIO.read(imageFile).getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            bi.getGraphics().drawImage(image, 0, 0, null);
            ImageIO.write(bi, "jpg", imageFile1);
        } catch (IOException ex) {
            // Logger.getLogger(Swing.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static javafx.scene.image.Image compressImage(URL url) {

        javafx.scene.image.Image imagea = null;
        try {
            int width = 100;
            int height = 80;
            //  .getPixelReader().getPixels();

            Image image = ImageIO.read(url).getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);
            BufferedImage bi = new BufferedImage(width, height, BufferedImage.SCALE_SMOOTH);
            bi.getGraphics().drawImage(image, 0, 0, null);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bi, "bmp", bos);
            //ImageInputStream inputStream=ImageIO.createImageInputStream(image);
            //ImageIO.getr

            imagea = new javafx.scene.image.Image(new ByteArrayInputStream(bos.toByteArray()));
        } catch (IOException ex) {
          //  imagea = ImageFile.getIMAGE();
            // ex.printStackTrace();
        }
        return imagea;
    }

    public static javafx.scene.image.Image compressImage(javafx.scene.image.Image image) {

        javafx.scene.image.Image imagea = null;

        int width = 100;
        int height = 80;
        int[] buff = new int[height * width];
        WritablePixelFormat<IntBuffer> pixelFormat
                = PixelFormat.getIntArgbInstance();
        image.getPixelReader().getPixels(0, 0, width, height, pixelFormat, buff, 0, width);
        Canvas canvas = new Canvas(width + 2, height + 2);    //新建javafx的画布
        //获取像素的写入器
        PixelWriter pixelWriter = canvas.getGraphicsContext2D().getPixelWriter();
        //根据写像素的格式模版把int型数组写到画布
        pixelWriter.setPixels(1, 1, width, height,
                pixelFormat, buff, 0, width);

        imagea = canvas.snapshot(null, null);

        return imagea;
    }

    public static Canvas compressImage(File file) {

        Canvas canvas;
        try {
            int width = 100;
            int height = 80;
            BufferedImage bi = new BufferedImage(width,
                    height, BufferedImage.SCALE_FAST);
            bi.getGraphics().drawImage(ImageIO.read(file), 0, 0, width,
                    height, null);
            //把图片画到图片缓冲区
            bi.getGraphics().dispose();
            //将图片缓冲区的数据转换成int型数组
            int[] data = ((DataBufferInt) bi.getData().getDataBuffer()).getData();
            //获得写像素的格式模版
            WritablePixelFormat<IntBuffer> pixelFormat
                    = PixelFormat.getIntArgbInstance();
            canvas = new Canvas(width + 2, height + 2);    //新建javafx的画布
            //获取像素的写入器
            PixelWriter pixelWriter = canvas.getGraphicsContext2D().getPixelWriter();
            //根据写像素的格式模版把int型数组写到画布
            pixelWriter.setPixels(1, 1, width, height,
                    pixelFormat, data, 0, width);
            //设置树节点的图标
        } catch (IOException ex) {
            canvas = new Canvas();
        }

        return canvas;

    }
}

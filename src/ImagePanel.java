import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

public class ImagePanel extends JPanel {
    private BufferedImage image;
    private BufferedImage originalImage;
    private double threshold;
    public ImagePanel() {
        try{
            this.image = ImageIO.read(new File("imgs/photo.png"));
            this.originalImage = image;
        } catch(IOException e) {
            System.err.println(e.getMessage());
        }

        this.threshold = 0.5;
        processImage();
    }

    public void updateThreshold(double threshold) {
        this.threshold = threshold;
        processImage();
    }

    public double getThreshold(){
        return threshold;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.image, 0, 0, this);
    }

    protected void processImage() {
        int[][] imageArray = imageTo2dArray(originalImage);

        final int width = this.image.getWidth();
        final int height = this.image.getHeight();

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                if(x == width - 1 || y == height - 1 || x == 0 || y == 0) {
                    imageArray[x][y] = convertToARGB(255, 0, 0, 0);
                } else {
                    int[] pixel = convertFromARGB(imageArray[x][y]);
                    pixel = rgbToGreyscale(pixel);

                    int oldPixel = pixel[1];

                    int grey;

                    if (pixel[1] / 255.0 > this.threshold) {
                        grey = 255;
                    } else {
                        grey = 0;
                    }

                    int error = oldPixel - grey;
                    imageArray[x + 1][y] = applyError(imageArray[x + 1][y], error * 7. / 16.);
                    imageArray[x - 1][y + 1] = applyError(imageArray[x - 1][y + 1], error * 3. / 16.);
                    imageArray[x][y + 1] = applyError(imageArray[x][y + 1], error * 5. / 16.);
                    imageArray[x + 1][y + 1] = applyError(imageArray[x + 1][y + 1], error * 1. / 16.);


                    imageArray[x][y] = convertToARGB(255, grey, grey, grey);
//                    imageArray[x][y] = grey * 0x22010101;
                }

            }
        }


        this.image = arrayToImage(imageArray);
    }

    protected int[][] imageTo2dArray(BufferedImage image) {
        final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        final int width = image.getWidth();
        final int height = image.getHeight();
        final boolean hasAlphaChannel = image.getAlphaRaster() != null;

        int[][] result = new int[width][height];
        if (hasAlphaChannel) {
            final int pixelLength = 4;
            for (int pixel = 0, row = 0, col = 0; pixel + 3 < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
                argb += ((int) pixels[pixel + 1] & 0xff); // blue
                argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
                result[col][row] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        } else {
            final int pixelLength = 3;
            for (int pixel = 0, row = 0, col = 0; pixel + 2 < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb = argb - 16777216; // 255 alpha
                argb += ((int) pixels[pixel] & 0xff); // blue
                argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
                result[col][row] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        }



        return result;
    }

    protected BufferedImage arrayToImage(int[][] imageArray) {
        int xLength = imageArray.length;
        int yLength = imageArray[0].length;
        BufferedImage b = new BufferedImage(xLength, yLength, 3);

        for(int x = 0; x < xLength; x++) {
            for(int y = 0; y < yLength; y++) {
                b.setRGB(x, y, imageArray[x][y]);
            }
        }

        return b;
    }

    protected int[] convertFromARGB(int pixelARGB) {
        int[] pixel = new int[4];

        pixel[0] = (pixelARGB >> 24) & 0xFF; // alpha
        pixel[1] = (pixelARGB >> 16) & 0xFF; // red
        pixel[2] = (pixelARGB >> 8 ) & 0xFF; // green
        pixel[3] = (pixelARGB      ) & 0xFF; // blue

        return pixel;
    }

    protected int convertToARGB(int alpha, int red, int green, int blue) {
        int pixel = 0;

        pixel += (( alpha & 0xff) << 24); // alpha
        pixel += (( blue & 0xff));       // blue
        pixel += (( green & 0xff) << 8);  // green
        pixel += (( red & 0xff) << 16); // red

        return pixel;
    }

    protected int[] rgbToGreyscale(int[] pixel) {
        int[] output = new int[4];

        output[0] = 255;
        int grey = (int)(0.3 * pixel[1] + 0.59 * pixel[2] + 0.11 * pixel[3]);
        output[1] = grey;
        output[2] = grey;
        output[3] = grey;
        return output;
    }

    protected int applyError(int pixel, double error) {
        int[] pixelArray = convertFromARGB(pixel);

        return convertToARGB(
                    pixelArray[0],
                    (int)(pixelArray[1] + error),
                    (int)(pixelArray[2] + error),
                    (int)(pixelArray[3] + error)
                );
    }



}

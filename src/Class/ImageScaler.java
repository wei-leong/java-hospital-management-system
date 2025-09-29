/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;

import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author Wlhoe
 */
public class ImageScaler {
    
    private int defaultWidth = 1;
    private int defaultHeight = 1;
    
    // Default Constructor
    public ImageScaler() {
    }

    // Square Default
    public ImageScaler(int size) {
        this(size, size);
    }

    // Customized Width and Height
    public ImageScaler(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("width/height must be > 0");
        }
        this.defaultWidth = width;
        this.defaultHeight = height;
    }
    
    // Return a Scaled ImageIcon using Specified Width / Height
    public ImageIcon returnScaledImageIcon (String imagePath, int width, int height){
        ImageIcon originalImage = new ImageIcon(getClass().getResource(imagePath));
        Image img = originalImage.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
    
    // Return a Scaled ImageIcon using DefaultWidth / Height
    public ImageIcon returnScaledImageIcon(String imagePath){
        ImageIcon originalImage = new ImageIcon(getClass().getResource(imagePath));
        Image img = originalImage.getImage().getScaledInstance(defaultWidth, defaultHeight, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
    
    // Return a ScaledImage using Specified Width / Height
    public Image returnScaledImage (String imagePath, int width, int height){
        ImageIcon originalImage = new ImageIcon(getClass().getResource(imagePath));
        Image img = originalImage.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return img;
    }
    
    // Return a ScaledImage using DefaultWidth / Height
    public Image returnScaledImage (String imagePath){
        ImageIcon originalImage = new ImageIcon(getClass().getResource(imagePath));
        Image img = originalImage.getImage().getScaledInstance(defaultWidth, defaultHeight, Image.SCALE_SMOOTH);
        return img;
    }
}

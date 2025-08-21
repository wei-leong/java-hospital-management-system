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
    public ImageScaler(){};
    
    public ImageIcon returnScaledImageIcon (String imagePath, int width, int height){
        ImageIcon originalImage = new ImageIcon(getClass().getResource(imagePath));
        Image img = originalImage.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
    
    public Image returnScaledImage (String imagePath, int width, int height){
        ImageIcon originalImage = new ImageIcon(getClass().getResource(imagePath));
        Image img = originalImage.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return img;
    }
}

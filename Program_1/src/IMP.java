/*
 *Hunter Lloyd
 * Copyrite.......I wrote, ask permission if you want to use it outside of class.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.awt.image.PixelGrabber;
import java.awt.image.MemoryImageSource;
import java.util.prefs.Preferences;


class IMP implements MouseListener {
    JFrame frame;
    JPanel mp;
    JButton start;
    JScrollPane scroll;
    JMenuItem openItem, exitItem, resetItem;
    Toolkit toolkit;
    File pic;
    ImageIcon img;
    int colorX, colorY;
    int[] pixels;
    int[] results;
    int originalWidth;
    int originalHeight;
    //Instance Fields you will be using below

    //This will be your height and width of your 2d array
    int height = 0, width = 0;

    //your 2D array of pixels
    int picture[][];

    /*
     * In the Constructor I set up the GUI, the frame the menus. The open pulldown
     * menu is how you will open an image to manipulate.
     */
    IMP() {
        toolkit = Toolkit.getDefaultToolkit();
        frame = new JFrame("Image Processing Software by Hunter");
        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu functions = getFunctions();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent ev) {
                quit();
            }
        });
        openItem = new JMenuItem("Open");
        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                handleOpen();
            }
        });
        resetItem = new JMenuItem("Reset");
        resetItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                reset();
            }
        });
        exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                quit();
            }
        });
        file.add(openItem);
        file.add(resetItem);
        file.add(exitItem);
        bar.add(file);
        bar.add(functions);
        frame.setSize(600, 600);
        mp = new JPanel();
        mp.setBackground(new Color(0, 0, 0));
        scroll = new JScrollPane(mp);
        frame.getContentPane().add(scroll, BorderLayout.CENTER);
        JPanel butPanel = new JPanel();
        butPanel.setBackground(Color.black);
        start = new JButton("start");
        start.setEnabled(false);
        butPanel.add(start);
        frame.getContentPane().add(butPanel, BorderLayout.SOUTH);
        frame.setJMenuBar(bar);
        frame.setVisible(true);
    }

    /*
     * This method creates the pulldown menu and sets up listeners to selection of the menu choices. If the listeners are activated they call the methods
     * for handling the choice, removeRed, fun2, fun3, fun4, etc. etc.
     */

    private JMenu getFunctions() {
        JMenu fun = new JMenu("Functions");

        JMenuItem firstItem = new JMenuItem("MyExample - removeRed method");
        JMenuItem rotate = new JMenuItem("Rotate Image"); // Dropdown menu item
        JMenuItem greyScale = new JMenuItem("Grey Scale"); // Dropdown menu item
        JMenuItem blur = new JMenuItem("Blur Image"); // Dropdown menu item
        JMenuItem edgeDetection = new JMenuItem("Edge Detection"); // Dropdown menu item
        JMenuItem histogram = new JMenuItem("Histogram"); // Dropdown menu item
        JMenuItem normalizeHistogram = new JMenuItem("Normalized Histogram"); // Dropdown menu item

        JMenuItem colorDetection = new JMenuItem("Color Detection"); // Dropdown menu item


        firstItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                removeRed();
            }
        });

        rotate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                rotateImage();  // Calls method to rotate the image.
            }
        });

        greyScale.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                greyScale();  // Calls method to rotate the image.
            }
        });

        blur.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                blur();  // Calls method to rotate the image.
            }
        });
        edgeDetection.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                edgeDetection();  // Calls method to rotate the image.
            }
        });
        histogram.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                histogram();  // Calls method to rotate the image.
            }
        });
        normalizeHistogram.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                normalizeHistogram();  // Calls method to rotate the image.
            }
        });
        colorDetection.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                colorDetection();  // Calls method to rotate the image.
            }
        });

        fun.add(firstItem);
        fun.add(rotate);
        fun.add(greyScale);
        fun.add(blur);
        fun.add(edgeDetection);
        fun.add(histogram);
        fun.add(normalizeHistogram);
        fun.add(colorDetection);
        return fun;

    }

    /*
     * This method handles opening an image file, breaking down the picture to a one-dimensional array and then drawing the image on the frame.
     * You don't need to worry about this method.
     */
    private void handleOpen() {

        img = new ImageIcon();
        JFileChooser chooser = new JFileChooser();
        Preferences pref = Preferences.userNodeForPackage(IMP.class);
        String path = pref.get("DEFAULT_PATH", "");

        chooser.setCurrentDirectory(new File(path));
        int option = chooser.showOpenDialog(frame);

        if (option == JFileChooser.APPROVE_OPTION) {
            pic = chooser.getSelectedFile();
            pref.put("DEFAULT_PATH", pic.getAbsolutePath());
            img = new ImageIcon(pic.getPath());
        }
        width = img.getIconWidth();
        height = img.getIconHeight();
        originalHeight = height;
        originalWidth = width;

        JLabel label = new JLabel(img);
        label.addMouseListener(this);
        pixels = new int[width * height];

        results = new int[width * height];


        Image image = img.getImage();

        PixelGrabber pg = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            System.err.println("Interrupted waiting for pixels");
            return;
        }
        for (int i = 0; i < width * height; i++)
            results[i] = pixels[i];
        turnTwoDimensional();
        mp.removeAll();
        mp.add(label);
        mp.revalidate();
        reset();
    }

    /*
     * The libraries in Java give a one dimensional array of RGB values for an image, I thought a 2-Dimensional array would be more usefull to you
     * So this method changes the one dimensional array to a two-dimensional.
     */
    private void turnTwoDimensional() {
        picture = new int[height][width];
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                picture[i][j] = pixels[i * width + j];


    }

    /*
     *  This method takes the picture back to the original picture
     */
    private void reset() {
        width = originalWidth;
        height = originalHeight;
        img = null;
        for (int i = 0; i < width * height; i++)
            pixels[i] = results[i];
        Image img2 = toolkit.createImage(new MemoryImageSource(width, height, pixels, 0, width));
        JLabel label2 = new JLabel(new ImageIcon(img2));
        turnTwoDimensional();
        mp.removeAll();
        mp.add(label2);
        mp.revalidate();
        mp.repaint();

    }

    /*
     * This method is called to redraw the screen with the new image.
     */
    private void resetPicture() {
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                pixels[i * width + j] = picture[i][j];
        Image img2 = toolkit.createImage(new MemoryImageSource(width, height, pixels, 0, width));
        JLabel label2 = new JLabel(new ImageIcon(img2));
        mp.removeAll();
        mp.add(label2);
        mp.revalidate();
    }

    /*
     * This method takes a single integer value and breaks it down doing bit manipulation to 4 individual int values for A, R, G, and B values
     */
    private int[] getPixelArray(int pixel) {
        int temp[] = new int[4];
        temp[0] = (pixel >> 24) & 0xff;
        temp[1] = (pixel >> 16) & 0xff;
        temp[2] = (pixel >> 8) & 0xff;
        temp[3] = (pixel) & 0xff;
        return temp;

    }

    /*
     * This method takes an array of size 4 and combines the first 8 bits of each to create one integer.
     */
    private int getPixels(int rgb[]) {
        int alpha = 0;
        int rgba = (rgb[0] << 24) | (rgb[1] << 16) | (rgb[2] << 8) | rgb[3];
        return rgba;
    }

    public void getValue() {
        int pix = picture[colorY][colorX];
        int temp[] = getPixelArray(pix);
        System.out.println("Color value " + temp[0] + " " + temp[1] + " " + temp[2] + " " + temp[3]);
    }

    /**************************************************************************************************
     * This is where you will put your methods. Every method below is called when the corresponding pulldown menu is
     * used. As long as you have a picture open first the when your removeRed, fun2, fun....etc method is called you will
     * have a 2D array called picture that is holding each pixel from your picture.
     *************************************************************************************************/
    /*
     * Example function that just removes all red values from the picture.
     * Each pixel value in picture[i][j] holds an integer value. You need to send that pixel to getPixelArray the method which will return a 4 element array
     * that holds A,R,G,B values. Ignore [0], that's the Alpha channel which is transparency, we won't be using that, but you can on your own.
     * getPixelArray will breaks down your single int to 4 ints so you can manipulate the values for each level of R, G, B.
     * After you make changes and do your calculations to your pixel values the getPixels method will put the 4 values in your ARGB array back into a single
     * integer value so you can give it back to the program and display the new picture.
     */
    private void removeRed() {

        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                int rgbArray[] = new int[4];
                //get three ints for R, G and B
                rgbArray = getPixelArray(picture[i][j]);
                rgbArray[1] = 0;
                //take three ints for R, G, B and put them back into a single int
                picture[i][j] = getPixels(rgbArray);
            }
        resetPicture();
    }


    private void rotateImage() {    // Method to rotate the image 90 degrees.
        int temp[] = new int[width * height];
        int counter = 0;
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                temp[counter] = picture[i][j];  // Single array that stores all of the pixels.
                counter++;
            }
        int temp_picture[][] = new int[width][height];  // temp array to not affect the original picture.
        counter = 0;
        for (int i = 0; i < height; i++) {
            for (int j = width - 1; j >= 0; j--) {
                temp_picture[j][i] = temp[counter]; // Loops through, rotating the picture into the temp array holder.
                counter++;
            }
        }
        mp.repaint(); // repaints image to black background
        picture = temp_picture; // sets picture to the temp variable.
        switchHeightWidth();
        resetPicture();
    }


    private void greyScale() { // Method to turn picture to grey scale.
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                int rgbArray[] = new int[4]; //get three ints for R, G and B
                rgbArray = getPixelArray(picture[i][j]);
                int lumos_value = checkValues((rgbArray[1] * 0.21) + (rgbArray[2] * 0.72) + (rgbArray[3] * 0.07));  // Luminosity formula for current pixel.
                for (int count = 1; count < rgbArray.length; count++) {
                    rgbArray[count] = lumos_value;  //Sets r,g,b to luminosity value
                }
                picture[i][j] = getPixels(rgbArray);    // sets  pixel to luminosity value.
            }
        resetPicture();
    }

    private void blur() {
        int[][] temp = picture;
        int boundryAdjust[] = {-1, 0, 1}; // array to loop through for surrounding values
        greyScale(); // Sets picture to grey scale
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                int[] rgbArray = new int[4];    // array to add up rgb values to use for average in a later function.
                int counter = 0;    // Counter to divide by for average.
                for (int h = 0; h < boundryAdjust.length; h++)  // loops through the boundryAdjust array.  this grabs the surrounding locations in the Array.
                    for (int w = 0; w < boundryAdjust.length; w++) {
                        if (i + boundryAdjust[h] >= 0 && i + boundryAdjust[h] < height && j + boundryAdjust[w] >= 0 && j + boundryAdjust[w] < width) {  //Long if statement to prevent array out of bounds.
                            counter++;
                            int[] pixelValues = getPixelArray(picture[i + boundryAdjust[h]][j + boundryAdjust[w]]); //gets the pixel array of the current surrounding pixel location.
                            for (int count = 0; count < pixelValues.length; count++) {
                                rgbArray[count] += pixelValues[count];  // adds value to temp rgb array
                            }
                        }

                    }
                for (int count = 0; count < rgbArray.length; count++) {
                    rgbArray[count] = checkValues(rgbArray[count] / counter); // gets the average of the argb value.
                }
                temp[i][j] = getPixels(rgbArray);   // Adds the pixel of the blurred image in a temp array.
            }
        picture = temp;
        resetPicture();
    }

    private void edgeDetection() {
        int[][] temp = picture;
        int boundryAdjust[] = {-1, 0, 1}; // array to loop through for surrounding values
        greyScale(); // Sets picture to grey scale
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                int[] rgbArray = getPixelArray(picture[i][j]);// array to add up rgb values to use for average in a later function.
                int rgbVal = picture[i][j] * 8; // Multiplies current pixel by surrounding value.
                for (int h = 0; h < boundryAdjust.length; h++)  // loops through the boundryAdjust array.  this grabs the surrounding locations in the Array.
                    for (int w = 0; w < boundryAdjust.length; w++) {
                        if (i + boundryAdjust[h] >= 0 && i + boundryAdjust[h] < height && j + boundryAdjust[w] >= 0 && j + boundryAdjust[w] < width) {  //Long if statement to prevent array out of bounds.
                            int pixelValues = picture[i + boundryAdjust[h]][j + boundryAdjust[w]]; //gets the pixel array of the current surrounding pixel location.
                            rgbVal += pixelValues * -1; //Adds surrounding pixel value to current pixel location.
                        }
                    }
                // If statement to set values to black or white.
                if (rgbVal > 255) {
                    rgbArray[0] = 255;
                    rgbArray[1] = 255;
                    rgbArray[2] = 255;
                    rgbArray[3] = 255;

                } else {
                    rgbArray[0] = 0;
                    rgbArray[1] = 0;
                    rgbArray[2] = 0;
                    rgbArray[3] = 0;
                }
                temp[i][j] = getPixels(rgbArray);   // Adds the pixel of the blurred image in a temp array.
            }
        picture = temp;
        resetPicture();
    }

/*TODO        Use the values in the histogram to equalize the image:
        Use the mapping function to normalize the distribution evenly
        https://en.wikipedia.org/wiki/Histogram_equalization */
    private void histogram() {
        int[] red = new int[256];
        int[] green = new int[256];
        int[] blue = new int[256];
        for (int i = 0; i < 256; i++) {  // Initializes array values
            red[i] = 0;
            green[i] = 0;
            blue[i] = 0;
        }
        for (int x = 0; x < height; x++) { // Gets pixel values and adds 1 to each count.
            for (int y = 0; y < width; y++) {
                int[] rgbArray = getPixelArray(picture[x][y]);
                red[rgbArray[1]] += 1;
                green[rgbArray[2]] += 1;
                blue[rgbArray[3]] += 1;

            }
        }
        setUpHistoJframe(red, green, blue); // runs method for jframe to draw histogram
    }

    private void normalizeHistogram() {
        int cdfRed = 0;
        int cdfgreen = 0;
        int cdfblue = 0;
        int[] red = new int[256];
        int[] green = new int[256];
        int[] blue = new int[256];
        int[] redNorm = new int[256];
        int[] greenNorm = new int[256];
        int[] blueNorm = new int[256];
        for (int i = 0; i < 256; i++) {  // Initializes the arrays.
            red[i] = 0;
            green[i] = 0;
            blue[i] = 0;
            redNorm[i] = 0;
            greenNorm[i] = 0;
            blueNorm[i] = 0;
        }
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                int[] rgbArray = getPixelArray(picture[x][y]);
                red[normalize(cdfRed)] += 1;
                cdfRed = (cdfRed + rgbArray[1]);
                green[normalize(cdfgreen)] += 1;
                cdfgreen= (cdfgreen + rgbArray[2]);
                blue[normalize(cdfblue)] += 1;
                cdfblue = (cdfblue + rgbArray[3]);
            }
        }

        int sum = 0;
        for (int i = 0; i < blue.length; i++){
        System.out.println("i: "+ i +"blue: " + blue[i] + "red " + red[i]);

        }
        System.out.println("Initial blue :" + sum);

//        for (int i = 0; i < red.length; i++) { // sets cdf
//            red[i] = (cdfRed + red[i]);
//            cdfRed = red[i];
//            redNorm[normalize(red[i])] += 1;
//            green[i] = (cdfgreen + green[i]);
//            cdfgreen = green[i];
//            greenNorm[normalize(green[i])] += 1;
//            blue[i] = (cdfblue + blue[i]);
//            cdfblue = blue[i];
//            blueNorm[normalize(blue[i])] += 1;
//        }
//        for (int i = 0; i < height; i++) { // sets cdf
//            System.out.println(i + "value : " + blueNorm[i]);
//
//        }
        sum = 0;
        for (int i = 0; i < blue.length; i++){
            sum += blueNorm[i];
        }
        System.out.println("last blue :" + sum);

        setUpHistoJframe(red, green, blue);
    }


    private int normalize(int cdf) {
        double cdfMin = 1.0;
        double l = 256.0;
//        double test = ((cdf-cdfMin)/((height*width)-1)) * (l -1);
        return checkValues(Math.round(((cdf-cdfMin)/((height*width)-cdfMin)) * (l -1)));

        }

    private void setUpHistoJframe(int[] red, int[] green, int[] blue) {
        // Provided by Hunter.
        int histoHeight = 600;
        JFrame redFrame = new JFrame("Red");
        redFrame.setSize(305, histoHeight);
        redFrame.setLocation(800, 0);
        JFrame greenFrame = new JFrame("Green");
        greenFrame.setSize(305, histoHeight);
        greenFrame.setLocation(1106, 0);
        JFrame blueFrame = new JFrame("blue");
        blueFrame.setSize(305, histoHeight);
        blueFrame.setLocation(1412, 0);
        MyPanel redPanel = new MyPanel(red, "red");
        MyPanel greenPanel = new MyPanel(green, "green");
        MyPanel bluePanel = new MyPanel(blue, "blue");
        redFrame.getContentPane().add(redPanel, BorderLayout.CENTER);
        redFrame.setVisible(true);
        greenFrame.getContentPane().add(greenPanel, BorderLayout.CENTER);
        greenFrame.setVisible(true);
        blueFrame.getContentPane().add(bluePanel, BorderLayout.CENTER);
        blueFrame.setVisible(true);
        start.setEnabled(true);
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {  // Draws histograms on click
                redPanel.drawing(histoHeight);
                greenPanel.drawing(histoHeight);
                bluePanel.drawing(histoHeight);
                start.setEnabled(false);    // Disables button after histogram is drawn.
            }
        });
    }


    private void colorDetection() {
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                int rgbArray[] = getPixelArray(picture[i][j]);
                int color;
                //get three ints for R, G and B
                if (rgbArray[1] > 208 && rgbArray[2] < 140 && rgbArray[2] > 50 && rgbArray[3] < 100) { // Orange has r =255 and b = 0
                    color = 255;

                } else {
                    color = 0;
                    //take three ints for R, G, B and put them back into a single int
                }
                for (int count = 1; count < rgbArray.length; count++) {
                    rgbArray[count] = color;   // Sets value to white.
                }
                picture[i][j] = getPixels(rgbArray);
            }
        resetPicture();
    }


    private int checkValues(double rgb_value) { //Corrects value if greater than 255 or less than 0
        if (rgb_value > 255.0) {
            return 255;
        } else if (rgb_value < 0.0) {
            return 0;
        } else {
            return (int) rgb_value;
        }
    }

    private void switchHeightWidth() {
        int heightTemp = height;
        height = width; // Changes the height of the picture variable.
        width = heightTemp; // Changes the width of the picture variable.
    }


    private void quit() {
        System.exit(0);
    }

    @Override
    public void mouseEntered(MouseEvent m) {
    }

    @Override
    public void mouseExited(MouseEvent m) {
    }

    @Override
    public void mouseClicked(MouseEvent m) {
        colorX = m.getX();
        colorY = m.getY();
        System.out.println(colorX + "  " + colorY);
        getValue();
        start.setEnabled(true);
    }

    @Override
    public void mousePressed(MouseEvent m) {
    }

    @Override
    public void mouseReleased(MouseEvent m) {
    }

    public static void main(String[] args) {
        IMP imp = new IMP();
    }

}
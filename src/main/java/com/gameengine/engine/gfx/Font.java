package com.gameengine.engine.gfx;

import java.io.IOException;

// Render to BufferedImage using Graphics2d and set Background to pink, then get pixel data from the BufferImage and use setPixel.
public class Font {
    public static Font STANDARD = null;

    static {
        try {
            STANDARD = new Font("/GUI/sans.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Image fontImage;
    private int[] offsets;
    private int[] widths;

    public Font(String path) throws IOException {
        int scale = 3;
        fontImage = new Image(path, scale);

        offsets = new int[256];
        widths = new int[256];

        int unicode = 0;

        for(int i = 0; i < fontImage.getWidth(); i+=scale){
            if(fontImage.getPixels()[i] == 0xff0000ff){
                offsets[unicode] = i;
            }

            if(fontImage.getPixels()[i] == 0xffffff00){
                widths[unicode] = i - offsets[unicode];
                unicode++;
            }
        }
    }

    public Image getFontImage() {
        return fontImage;
    }

    public void setFontImage(Image fontImage) {
        this.fontImage = fontImage;
    }

    public int[] getOffsets() {
        return offsets;
    }

    public void setOffsets(int[] offsets) {
        this.offsets = offsets;
    }

    public int[] getWidths() {
        return widths;
    }

    public void setWidths(int[] widths) {
        this.widths = widths;
    }
}

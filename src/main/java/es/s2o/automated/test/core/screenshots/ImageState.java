package es.s2o.automated.test.core.screenshots;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * @author s2o
 */
public class ImageState {

	private final int[][] map;
	private final int width;
	private final int height;

	public ImageState(BufferedImage img, int resX, int resY) {
		// setup brightness map
		width = img.getWidth() / resX;
		height = img.getHeight() / resY;
		map = new int[height][width];

		// build map and stats
		int ta = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				ta = (int) (100 * getBrightnessAtPoint(img, x * resX, y * resY));
				// int ta1 = (int)(100*Util.getBrightnessAtPoint(img, x * resX, y * resY));
				// int ta2 = (int)(100*Util.getBrightnessAtPoint(img, x * resX +1, y * resY +1));
				// int ta3 = (int)(100*Util.getBrightnessAtPoint(img, x * resX +2, y * resY +2));
				// ta = (int)((ta1 + ta2 + ta3) / 3);
				map[y][x] = ta;
			}
		}
	}

	public int[][] getMap() {
		return map;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public float getBrightnessAtPoint(BufferedImage img, int x, int y) {
		return getColorFactor(new Color(img.getRGB(x, y)));
	}

	public float getColorFactor(Color c) {
		float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
		return (float) (hsb[2] * 0.5 + ((hsb[0] / 360) * 50/* 1/2 of 100 */));
	}

}

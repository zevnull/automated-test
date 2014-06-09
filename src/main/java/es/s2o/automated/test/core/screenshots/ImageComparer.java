package es.s2o.automated.test.core.screenshots;

import java.awt.image.BufferedImage;

/**
 * compare two images
 * 
 * @author s2o
 */
public class ImageComparer {

	private int comparex = 93;
	private int comparey = 69;
	private int leniency = 10;

	/**
	 * @param comparex
	 * @param comparey
	 * @param leniency
	 */
	public ImageComparer() {

	}

	/**
	 * @param comparex
	 * @param comparey
	 * @param leniency
	 */
	public ImageComparer(int comparex, int comparey, int leniency) {
		this.comparex = comparex;
		this.comparey = comparey;
		this.leniency = leniency;
	}

	/**
	 * @param s1
	 * @param s2
	 * @return
	 */
	public ComparisonResult compare(BufferedImage img1, BufferedImage img2) {
		// convert the images into state maps
		ImageState s1 = new ImageState(img1, 1, 1);
		ImageState s2 = new ImageState(img2, 1, 1);
		return compare(s1, s2);
	}

	//
	/**
	 * @param s1
	 * @param s2
	 * @return
	 */
	public ComparisonResult compare(ImageState s1, ImageState s2) {
		int cx = comparex;
		if (cx > s1.getWidth())
			cx = s1.getWidth();
		int cy = comparey;
		if (cy > s1.getHeight())
			cy = s1.getHeight();

		// how many points per section
		int bx = (int) (Math.floor(s1.getWidth() / cx));
		if (bx <= 0)
			bx = 1;
		int by = (int) (Math.floor(s1.getHeight() / cy));
		if (by <= 0)
			by = 1;
		int[][] variance = new int[cy][cx];

		// set to a match by default, if a change is found then flag non-match
		boolean match = true;
		// loop through whole image and compare individual blocks of images
		int ty = 0;
		for (int y = 0; y < cy; y++) {
			ty = y * by;
			for (int x = 0; x < cx; x++) {
				int b1 = aggregateMapArea(s1.getMap(), x * bx, ty, bx, by);
				int b2 = aggregateMapArea(s2.getMap(), x * bx, ty, bx, by);
				int diff = Math.abs(b1 - b2);
				variance[y][x] = diff;
				if (diff > leniency) { // the difference in a certain region has passed the threshold value
					match = false;
				}

			}

		}
		return new ComparisonResult(leniency, variance, match);
	}

	private int aggregateMapArea(int[][] map, int ox, int oy, int w, int h) {
		int t = 0;
		for (int i = 0; i < h; i++) {
			int ty = oy + i;
			for (int j = 0; j < w; j++)
				t += map[ty][ox + j];
		}
		return t / (w * h);
	}

}

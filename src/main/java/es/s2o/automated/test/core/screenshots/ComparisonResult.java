package es.s2o.automated.test.core.screenshots;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * @author s2o
 */
public class ComparisonResult {

	private final int[][] variance;
	private final int width;
	private final int height;
	private final boolean match;
	private final int leniency;

	/**
	 * @param s1
	 * @param s2
	 * @param variance
	 * @param match
	 */
	public ComparisonResult(int leniency, int[][] variance, boolean match) {
		this.leniency = leniency;
		this.variance = variance;
		this.match = match;
		height = variance.length;
		width = variance[0].length;
	}

	/**
	 * Return the image that indicates the regions where changes where detected. there is something pitifully wrong with
	 * the images this method creates :( dunno what it is to fix it. correction blocks get rendered in a different scale
	 * than the original image.
	 * 
	 * @param cx
	 * @param comparer
	 * @return
	 */
	public BufferedImage getChangeIndicator(BufferedImage cx) {
		// setup change display image
		Graphics2D gc = cx.createGraphics();
		gc.setColor(Color.RED);

		float bx = (cx.getWidth() / width);
		float by = (cx.getHeight() / height);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (variance[y][x] > leniency)
					gc.drawRect((int) (x * bx), (int) (y * by), (int) bx, (int) by);
			}
		}
		return cx;
	}

	public boolean isMatch() {
		return match;
	}

}

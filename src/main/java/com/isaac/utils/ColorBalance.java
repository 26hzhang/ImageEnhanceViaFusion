package com.isaac.utils;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class ColorBalance {

	/**
	 * Simplest Color Balance. Performs color balancing via histogram
	 * normalization.
	 *
	 * @param img
	 *            input color or gray scale image
	 * @param percent
	 *            controls the percentage of pixels to clip to white and black.
	 *            (normally, choose 1~10)
	 * @return Balanced image in CvType.CV_32F
	 */
	public static Mat SimplestColorBalance(Mat img, int percent) {
		if (percent <= 0)
			percent = 5;
		img.convertTo(img, CvType.CV_32F);
		List<Mat> channels = new ArrayList<Mat>();
		int rows = img.rows(); // number of rows of image
		int cols = img.cols(); // number of columns of image
		int chnls = img.channels(); //  number of channels of image
		double halfPercent = percent / 200.0;
		if (chnls == 3) {
			Core.split(img, channels);
		} else {
			channels.add(img);
		}
		List<Mat> results = new ArrayList<Mat>();
		for (int i = 0; i < chnls; i++) {
			// find the low and high precentile values (based on the input percentile)
			Mat flat = new Mat();
			channels.get(i).reshape(1, 1).copyTo(flat);
			Core.sort(flat, flat, Core.SORT_ASCENDING);
			double lowVal = flat.get(0, (int) Math.floor(flat.cols() * halfPercent))[0];
			double topVal = flat.get(0, (int) Math.ceil(flat.cols() * (1.0 - halfPercent)))[0];
			// saturate below the low percentile and above the high percentile
			Mat channel = channels.get(i);
			for (int m = 0; m < rows; m++) {
				for (int n = 0; n < cols; n++) {
					if (channel.get(m, n)[0] < lowVal)
						channel.put(m, n, lowVal);
					if (channel.get(m, n)[0] > topVal)
						channel.put(m, n, topVal);
				}
			}
			Core.normalize(channel, channel, 0, 255, Core.NORM_MINMAX);
			channel.convertTo(channel, CvType.CV_32F);
			results.add(channel);
		}
		Mat outval = new Mat();
		Core.merge(results, outval);
		return outval;
	}

}

package com.isaac.enhance;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class Pyramid {

	public static Mat[] GaussianPyramid(Mat img, int level) {
		Mat[] gaussPyr = new Mat[level];
		Mat mask = filterMask(img);
		Mat tmp = new Mat();
		Imgproc.filter2D(img, tmp, -1, mask);
		gaussPyr[0] = tmp.clone();
		Mat tmpImg = img.clone();
		for (int i = 1; i < level; i++) {
			// resize image
			Imgproc.resize(tmpImg, tmpImg, new Size(), 0.5, 0.5, Imgproc.INTER_LINEAR);
			// blur image
			tmp = new Mat();
			Imgproc.filter2D(tmpImg, tmp, -1, mask);
			gaussPyr[i] = tmp.clone();
		}
		return gaussPyr;
	}

	public static Mat[] LaplacianPyramid(Mat img, int level) {
		Mat[] lapPyr = new Mat[level];
		//Mat mask = filterMask(img);
		lapPyr[0] = img.clone();
		Mat tmpImg = img.clone();
		for (int i = 1; i < level; i++) {
			// resize image
			Imgproc.resize(tmpImg, tmpImg, new Size(), 0.5, 0.5, Imgproc.INTER_LINEAR);
			lapPyr[i] = tmpImg.clone();
		}
		// calculate the DoG
		for (int i = 0; i < level - 1; i++) {
			Mat tmpPyr = new Mat();
			Imgproc.resize(lapPyr[i + 1], tmpPyr, lapPyr[i].size(), 0, 0, Imgproc.INTER_LINEAR);
			Core.subtract(lapPyr[i], tmpPyr, lapPyr[i]);
		}
		return lapPyr;
	}

	public static Mat PyramidReconstruct(Mat[] pyramid) {
		int level = pyramid.length;
		for (int i = level - 1; i > 0; i--) {
			Mat tmpPyr = new Mat();
			Imgproc.resize(pyramid[i], tmpPyr, pyramid[i - 1].size(), 0, 0, Imgproc.INTER_LINEAR);
			Core.add(pyramid[i - 1], tmpPyr, pyramid[i - 1]);
		}
		return pyramid[0];
	}

	private static Mat filterMask(Mat img) {
		double[] h = { 1.0 / 16.0, 4.0 / 16.0, 6.0 / 16.0, 4.0 / 16.0, 1.0 / 16.0 };
		Mat mask = new Mat(h.length, h.length, img.type());
		for (int i = 0; i < h.length; i++) {
			for (int j = 0; j < h.length; j++) {
				mask.put(i, j, h[i] * h[j]);
			}
		}
		return mask;
	}

}

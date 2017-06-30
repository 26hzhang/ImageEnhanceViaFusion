package com.isaac.enhance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.CLAHE;
import org.opencv.imgproc.Imgproc;

import com.isaac.utils.ColorBalance;
import com.isaac.utils.ImShow;

public class EnhanceFunc {

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static void main(String[] args) {
		String imgPath = "images/5.jpg";
		Mat image = Imgcodecs.imread(imgPath, Imgcodecs.CV_LOAD_IMAGE_COLOR);
		new ImShow("original").showImage(image);
		// color balance
		Mat img1 = ColorBalance.SimplestColorBalance(image, 5);
		img1.convertTo(img1, CvType.CV_8UC1);
		// Perform sRGB to CIE Lab color space conversion
		Mat LabIm1 = new Mat();
		Imgproc.cvtColor(img1, LabIm1, Imgproc.COLOR_BGR2Lab);
		Mat L1 = new Mat();
		Core.extractChannel(LabIm1, L1, 0);
		// apply CLAHE
		Mat[] result = applyCLAHE(LabIm1, L1);
		Mat img2 = result[0];
		Mat L2 = result[1];
		// calculate normalized weight
		Mat w1 = calWeight(img1, L1);
		Mat w2 = calWeight(img2, L2);
		Mat sumW = new Mat();
		Core.add(w1, w2, sumW);
		Core.divide(w1, sumW, w1);
		Core.divide(w2, sumW, w2);
		// construct the gaussian pyramid for weight
		int level = 5;
		Mat[] weight1 = Pyramid.GaussianPyramid(w1, level);
		Mat[] weight2 = Pyramid.GaussianPyramid(w2, level);
		// construct the laplacian pyramid for input image channel
		img1.convertTo(img1, CvType.CV_32F);
		img2.convertTo(img2, CvType.CV_32F);
		List<Mat> bgr = new ArrayList<Mat>();
		Core.split(img1, bgr);
		Mat[] bCnl1 = Pyramid.LaplacianPyramid(bgr.get(0), level);
		Mat[] gCnl1 = Pyramid.LaplacianPyramid(bgr.get(1), level);
		Mat[] rCnl1 = Pyramid.LaplacianPyramid(bgr.get(2), level);
		bgr.clear();
		Core.split(img2, bgr);
		Mat[] bCnl2 = Pyramid.LaplacianPyramid(bgr.get(0), level);
		Mat[] gCnl2 = Pyramid.LaplacianPyramid(bgr.get(1), level);
		Mat[] rCnl2 = Pyramid.LaplacianPyramid(bgr.get(2), level);
		// fusion process
		Mat[] bCnl = new Mat[level];
		Mat[] gCnl = new Mat[level];
		Mat[] rCnl = new Mat[level];
		for (int i = 0; i < level; i++) {
			Mat cn = new Mat();
			Core.add(bCnl1[i].mul(weight1[i]), bCnl2[i].mul(weight2[i]), cn);
			bCnl[i] = cn.clone();
			Core.add(gCnl1[i].mul(weight1[i]), gCnl2[i].mul(weight2[i]), cn);
			gCnl[i] = cn.clone();
			Core.add(rCnl1[i].mul(weight1[i]), rCnl2[i].mul(weight2[i]), cn);
			rCnl[i] = cn.clone();
		}
		// reconstruct & output
		Mat bChannel = Pyramid.PyramidReconstruct(bCnl);
		Mat gChannel = Pyramid.PyramidReconstruct(gCnl);
		Mat rChannel = Pyramid.PyramidReconstruct(rCnl);
		Mat fusion = new Mat();
		Core.merge(new ArrayList<Mat>(Arrays.asList(bChannel, gChannel, rChannel)), fusion);
		fusion.convertTo(fusion, CvType.CV_8UC1);
		new ImShow("fusion").showImage(fusion);
	}

	private static Mat[] applyCLAHE(Mat img, Mat L) {
		Mat[] result = new Mat[2];
		CLAHE clahe = Imgproc.createCLAHE();
		clahe.setClipLimit(2.0);
		Mat L2 = new Mat();
		clahe.apply(L, L2);
		Mat LabIm2 = new Mat();
		List<Mat> lab = new ArrayList<Mat>();
		Core.split(img, lab);
		Core.merge(new ArrayList<Mat>(Arrays.asList(L2, lab.get(1), lab.get(2))), LabIm2);
		Mat img2 = new Mat();
		Imgproc.cvtColor(LabIm2, img2, Imgproc.COLOR_Lab2BGR);
		result[0] = img2;
		result[1] = L2;
		return result;
	}

	private static Mat calWeight(Mat img, Mat L) {
		Core.divide(L, new Scalar(255.0), L);
		L.convertTo(L, CvType.CV_32F);
		// calculate laplacian contrast weight
		Mat WL = WeightCalculate.LaplacianContrast(L);
		WL.convertTo(WL, L.type());
		// calculate Local contrast weight
		Mat WC = WeightCalculate.LocalContrast(L);
		WC.convertTo(WC, L.type());
		// calculate the saliency weight
		Mat WS = WeightCalculate.Saliency(img);
		WS.convertTo(WS, L.type());
		// calculate the exposedness weight
		Mat WE = WeightCalculate.Exposedness(L);
		WE.convertTo(WE, L.type());
		// sum
		Mat weight = WL.clone();
		Core.add(weight, WC, weight);
		Core.add(weight, WS, weight);
		Core.add(weight, WE, weight);
		return weight;
	}

}

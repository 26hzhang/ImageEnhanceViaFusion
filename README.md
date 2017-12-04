## Underwater Image Enhance via Fusion

**Notice**: _All the codes are integrated in my newly created repository: [OptimizedImageEnhance](https://github.com/IsaacChanghau/OptimizedImageEnhance), this repository will be deleted later, and the compressed file of this repository is also available in the new repository: [[link]](https://github.com/IsaacChanghau/OptimizedImageEnhance/blob/master/pre_standalones/ImageEnhanceViaFusion-master.zip)._

It is a Java implementation of algorithms proposed in the [paper](http://perso.telecom-paristech.fr/~Gousseau/ProjAnim/2015/ImageSousMarine.pdf): Enhancing Underwater Images and Videos by Fusion, published by Cosmin Ancuti on [CVPR](https://en.wikipedia.org/wiki/Conference_on_Computer_Vision_and_Pattern_Recognition), 2012.

This paper describes a novel strategy to enhance underwater videos and images. Built on the fusion principles, the strategy derives the inputs and the weight measures only from the degraded version of the image. In order to overcome the limitations of the underwater medium, two inputs are defined that represent color corrected and contrast enhanced versions of the original underwater image/frame, but also four weight maps that aim to increase the visibility of the distant objects degraded due to the medium scattering and absorption. The strategy is a single image approach that does not require specialized hardware or knowledge about the underwater conditions or scene structure. The fusion framework also supports temporal coherence between adjacent frames by performing an effective edge preserving noise reduction strategy. The enhanced images and videos are characterized by reduced noise level, better exposedness of the dark regions, improved global contrast while the finest details and edges are enhanced significantly.

**NOTE**: The image showing method is obtain from the repository: [ImShow-Java-OpenCV](https://github.com/master-atul/ImShow-Java-OpenCV). And the implementation of Matlab are put in this project too.


### Requirements

* [Java 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).

* [OpenCV](http://opencv.org/). To make the OpenCV can work with JAVA IDE like IntelliJ or Eclipse, you may need to follow the guidance of [OpenCV Java Tutorials](http://opencv-java-tutorials.readthedocs.io/en/latest/01-installing-opencv-for-java.html) to set up OpenCV for Java in your favorite IDE.

### Results

##### Heavy Distorted Fish Example
<img src="README-images/org-1.jpg" width = "425" height = "350" align=center />  <img src="README-images/enh-1.png" width = "425" height = "350" align=center />

##### Enhancement Examples with Normalized Weight Maps
<img src="README-images/org-2.png" width = "425" height = "350" align=center />  <img src="README-images/enh-2.png" width = "425" height = "350" align=center />
<img src="README-images/w1-2.png" width = "425" height = "350" align=center />  <img src="README-images/w2-2.png" width = "425" height = "350" align=center />

<img src="README-images/org-3.png" width = "425" height = "350" align=center />  <img src="README-images/enh-3.png" width = "425" height = "350" align=center />
<img src="README-images/w1-3.png" width = "425" height = "350" align=center />  <img src="README-images/w2-3.png" width = "425" height = "350" align=center />

#### Other Results
<img src="README-images/org-4.png" width = "425" height = "350" align=center />  <img src="README-images/enh-4.png" width = "425" height = "350" align=center />

<img src="README-images/org-5.png" width = "425" height = "350" align=center />  <img src="README-images/enh-5.png" width = "425" height = "350" align=center />

<img src="README-images/org-6.png" width = "425" height = "350" align=center />  <img src="README-images/enh-6.png" width = "425" height = "350" align=center />

<img src="README-images/org-8.png" width = "425" height = "350" align=center />  <img src="README-images/enh-8.png" width = "425" height = "350" align=center />

<img src="README-images/org-9.png" width = "425" height = "350" align=center />  <img src="README-images/enh-9.png" width = "425" height = "350" align=center />

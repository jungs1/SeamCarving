# Abstract
This side project was inspired by Coursera's Algorithms by Princenton course. The coursera assignment was to implement seam carving. Seam carving is an algorithm used to resize a given image without distorting the image content. The algorithm was discovered in 2007, and it now widely used in graphics applications. My project is an extension of the knowledge I gained from the course. Although the final result was decent, in my opinion, the project still needs alot work in refining the seam carving algorithm.
<br/>
[Coursera Assignment](https://coursera.cs.princeton.edu/algs4/assignments/seam/specification.php) <br/>

**Original Image.** <br/>
![](/documentation/Broadway_tower.jpg) <br/>
<br />
**Scaling makes the castle look distorted.** <br/>
![](/documentation/Broadway_tower_scale.png) <br/>
<br />
**Cropping removes part of the castle.** <br/>
![](/documentation/Broadway_tower_cropped.png) <br/>
<br />
**Image after applying seam carving algorithm.** <br/>
![](/documentation/Broadway_tower_Seam_Carving.png) <br/>
<br />

# Credits
[yeori](https://github.com/yeori) <br/>

# Introduction
Seam carving is primarily used to resize an image by finding the appropriate seams. Using this idea, seam carving algorithm could produce the right seams between text lines in order to extract the text. Although the ultimate purpose of this approach is to extract the text lines, this project is only focused on refining the seam carving algorithm to detect the right seams between text lines. 

# Problem 1: Seam Identification <br/>
**Test Input File 1.**  <br/>
![](/documentation/in01-seam.png) <br/>
The red line above in the image represents the seam. As seen, the seam 
As expected, the seam represents the least energy in the image (black in this case), not being able to extract the lines. <br/>
**Test Input File 2.** <br/>
![](/documentation/in02-seam.png) <br/>
However, if the test image file contains less space between the lines, seam carving algorithm can inaccurately extract the lines <br/>

# Future Fixes - Energy Calculation <br/>
The energy of each pixel is calculated using the dual-gradient energy function. There are certainly other energy functions to experiment, but since dual-gradient energy function was used in the coursera assignment, I simply used the same energy function. Maybe there are better enery functions that can better produce seams between text lines. <br/>
![](/documentation/dual-gradient.jpg) <br/>
**Test Input File 1.**  <br/>
![](/documentation/in01-energy.png) <br/>
**Test Input File 2.** <br/>
![](/documentation/in02-energy.png) <br/>



# Seam Identification Approach 1 <br/>
**Seam carving algorithm was applied after vertically splitting the image into multiple sections. In order to accurately extract the lines, only the common seam paths were selected. (path that did not share the same energy was removed). The blue line connects the splitted seams by finding the nearest start to end point.** <br/>
**Test Input File 1.** <br/>
![](/documentation/in01-no-blur.png) <br/>
**Test Input File 2.** <br/>
![](/documentation/in02-no-blur.png) <br/>


# Seam Identification Approach 2 <br/>
**Implemented blur algorithm to see if seam carving algorithm improved. Interestingly, blur effect did not have much improvement.** <br/>
**Test Input File 1.** <br/>
![](/documentation/in01-blur.png) <br/>
**Test Input File 2.** <br/>
![](/documentation/in02-blur.png) <br/>

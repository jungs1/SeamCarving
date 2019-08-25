# SeamCarving
Seam carving is an algorithm used to resize a given image by finding the right specific seams. Each seams represents the least energy in the image which is computed by some given energy function. Removing the seams reduces the image and inserting the seams extends the image. <br/>


**Original Image.** <br/>
![](/documentation/Broadway_tower.jpg) <br/>
<br />
**Scaling makes the castle look distorted.** <br/>
![](/documentation/Broadway_tower_scale.png) <br/>
<br />
**Cropping removes part of the castle.** <br/>
![](/documentation/Broadway_tower_cropped.png) <br/>
<br />
**Seam Carving.** <br/>
![](/documentation/Broadway_tower_Seam_Carving.png) <br/>
<br />

# 1. Energy Calculation <br/>
**Energy of each pixel is calculated using the dual-gradient energy function. White pixels indicate that the energy is high. Seam carving algorithm avoids removing high energy pixels in order to save the image content after resizing.**<br/>
**Test Input File 1.**  <br/>
![](/documentation/in01-energy.png) <br/>
**Test Input File 2.** <br/>
![](/documentation/in02-energy.png) <br/>

# 2. Seam Identification <br/>
**Test Input File 1.**  <br/>
![](/documentation/in01-seam.png) <br/>
**The red line represents the seam. As expected, the seam represents the least energy in the image (black in this case), not being able to extract the lines.** <br/>
**Test Input File 2.** <br/>
![](/documentation/in02-seam.png) <br/>
**However, if the test image file contains less space between the lines, seam carving algorithm can inaccurately extract the lines** <br/>


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

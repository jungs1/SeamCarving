# Abstract
This side project was inspired by Coursera's *Algorithms by Princenton* course. The coursera assignment was to simply implement seam carving. Seam carving is an algorithm used to resize a given image without distorting the image content. The algorithm was discovered in 2007, and it now widely used in graphics applications. This project is an extension of the seam carving knowledge I gained from the course. Although the final result was decent, in my opinion, the project still needs alot work. <br/>
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
Seam carving is primarily used to resize an image by finding the appropriate seams. Using this idea, seam carving algorithm can produce the right seams between text lines in order to extract the text. Although the ultimate purpose of this approach is to extract the text lines, this project is only focused on refining the seam carving algorithm to detect the right seams between text lines. 

# Seam Identification Problem <br/>
**Image with boarders**  <br/>
![](/documentation/in01-seam.png) <br/>
The red line above in the image represents the seam. As expected, without refining the seam carving algorithm, the seam cannot identify lines between texts. <br/>
**Image with no boarders** <br/>
![](/documentation/in02-seam.png) <br/>
However, if the test image file contains less space between the lines and little to none boarders, seam carving algorithm can some-what inaccurately identify lines between the text. <br/>


# Solution for Seam Identification <br/>
**Partitioned Image** <br/>
![](/documentation/common-ancestor.png) <br/>
In order for the seam to accurately identify lines between text, seam carving algorithm was applied after partitioning the image into multiple vertical pieces. The black lines on the image indiciate the splitted image sections. The red lines are the seams. Notice how each seams look like a horizontally faced elongated tree data structure.<br/>
**Common Ancestor** <br/>
![](/documentation/common-ancestor-thickness.png) <br/>
Imagine each seams represent a tree data structure. Finding the common ancestor of each tree would give the most common path of that tree. I have highlighted the common path in red, and the rest of tree in pink. Only the common path would be used the identify the lines. <br/>
**Connecting the Red Lines** <br/>
![](/documentation/in01-no-blur.png) <br/>
After the pink seams are removed, we now have to connect the red lines, to complete the line. I used a brute force method where I simply connected one end of the red endpoint to the nearest other end of the red endpoint. The connected lines are indiciated in blue. <br/>
**Blur Image** <br/>
![](/documentation/in01-blur.png) <br/>
![](/documentation/in02-blur.png) <br/>
I implemented a blur algorithm to see if seam carving algorithm improved in any way. Interestingly, blur effect did not have much improvement.. <br/>

# Future Fixes - Energy Calculation <br/>
The energy of each pixel is calculated using the dual-gradient energy function. There are certainly other energy functions to experiment, but since dual-gradient energy function was used in the coursera assignment, I simply used the same energy function. Perhaps there are other energy functions to implement for a better result. <br/>
**Dual-gradient Energy Function** 
![](/documentation/dual-gradient.jpg) <br/>

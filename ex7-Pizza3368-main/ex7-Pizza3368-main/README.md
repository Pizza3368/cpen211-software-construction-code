# Evolution

## Background: Zygotes and Killing Cells

A zygote is the first reproductive cell for an organism. When an organism is developing, each cell splits into two new cells or does not split at all. For mature organisms, cells do not split any further.

During an organism’s development, let us assign numbers to cells as they emerge. The zygote will be cell 0, and it may split into cells 1 and 2. Cell 2 may not split any further, but Cell 1 may split into Cell 3 and Cell 4. Cell 3 and Cell 4 are, like Cell 2, terminal cells that do not divide any further. This type of organism will thus have exactly three cells (Cells 2, 3 and 4).

If, for any reason, a cell is killed during the development process then it will be absent in the mature form of the organism/species. If a cell that splits is killed then, naturally, the descendants of that cell will be missing in the mature organism.

In the example above, if Cell 3 is killed then the mature organism has Cells 2 and 4. If Cell 1 is eliminated then the mature organism only has Cell 2.
 
Essentially, this evolutionary process is described by a binary tree. Suppose you are given an array encoding of this binary tree: there is an array of integers, `int[] evolutionaryTree`, that describes the evolution of the organism. In this array, the i<sup>th</sup> element, `evolutionaryTree[i]`, represents the parent cell of *Cell i* in the evolutionary process. Predictably, the array entry for the zygote is `-1` because the zygote has no parent. Note that the zygote need not be Cell 0. *The numbering can be arbitrary.*

+ Suppose you were given the `evolutionaryTree` array and the index of a cell that dies prematurely during the evolutionary process then you have to compute the number of cells that can be found in the mature form of the organism.
+ Similar to the case above, instead of simply counting the number of surviving cells in the mature organism, return a `Set<Integer>` with the cells that survive.

## Logistics

**Grading**

| Work Accomplished | Grade |
| ----------------- | ----- |
| Implementation passes all hidden tests | A |
| Implementation passes all but one hidden test | C |
| Implementation fails more than one hidden test | F |

**Submission Instructions**

+ Submit your work to the Github classroom repository that was created for you.
+ **Do not alter the directory/folder structure. You should retain the structure as in this repository.**
+ Do not wait until the last minute to push your work to Github. It is a good idea to push your work at intermediate points as well. _We would recommend that you get your Git and Github workflow set up at the start._

**What Should You Implement / Guidelines**

+ You should implement all the methods that are indicated with `TODO`.
+ Passing the provided tests is the minimum requirement. Use the tests to identify cases that need to be handled. Passing the provided tests is *not sufficient* to infer that your implementation is complete and that you will get full credit. Additional tests will be used to evaluate your work. The provided tests are to guide you.
+ You can implement additional helper methods if you need to but you should keep these methods `private` to the appropriate classes.
+ You do not need to implement new classes.
+ You can use additional **standard** Java libraries (in `java.util`) by importing them.
+ Do not throw new exceptions unless the specification for the method permits exceptions.


## Honour Code

By submitting your work to Github you agree to the following:

+ You did not consult with any other person for the purpose of completing this activity.
+ You did not aid any other person in the class in completing their activity.
+ If you consulted any external sources, such as resources available on the World Wide Web, in completing the examination then you have cited the source. (You do not need to cite class notes or Sun/Oracle Java documentation.)
+ You are not aware of any infractions of the honour code for this examination.

## Answers to FAQs

* **Can I consult Java documentation and other Internet-based sources?**

  Yes, you can. The point of this test is not to demonstrate mastery over syntax but that you can solve a problem in a    reasonable amount of time with reasonable resources.

  *If you find useful information online outside the official Java documentation and the course material, you must cite the source. You should do so by adding comments in your source code.*

  Naturally you are expected to adhere to all of the course and UBC policies on academic integrity.

* **Why am I not guaranteed full credit if my implementation passes all the provided tests?**

  It is easy to develop an implementation that passes the provided tests and not much else. A good-faith implementation that passes all the provided tests is very likely to pass other tests too.
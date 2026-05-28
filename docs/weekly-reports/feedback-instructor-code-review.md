
# Instructor Code Review Feedback

**Contact**: Dr. Yiji Zhang (yiji.zhang@northwestern.edu)

**Purpose of This Document**:
The instructor will perform code review with respect to software design, error handling, format and style on the main branch every week starting Week 6 using the letter grade A standards.
The following chapters of the textbook are considered: Chapter 1, 2, 3, 4, 5, 6, 7, 9, and 10. The corresponding lectures are considered, too.

Please note that this feedback does not include evaluation of your progress, the proper use of linters, the quality of your test cases, or your compliance of TDD/BDD workflow.  
You can find the weekly feedback from your dedicated PM/TA for that.

## Week 7-8 Code Review
This review is for the code your team developed in Week 7 and 8.
I apologize for this delayed code review (should have been given last Friday but I got really sick...).
As compensation, I will add one extra code review in Week 10 (around Thursday).

I have read all the code in the `main` branch. Very good work overall! I found no violation of any coding standard. Especially, very good work in breaking a big function into small private functions! 

One minor comment: I noticed some methods defined as `protected` (like `Turn#createAttackPhase`). If the purpose of these are for testing, you change it to "package private" instead of it has a narrower scope. 
The Java syntax of this is simply not using any keyword, aka just
``` 
ReinforcementPhase createReinforcementPhase(Player p, int troopsToPlace) {
    return new ReinforcementPhase(p, troopsToPlace);
}
```
Good quality work!!

## Week 6 Code Review
I have read every line of production code currently in the main branch.
A couple of small things:
1. There is use of magic numbers. Example: Game class, calculateStartingArmies method, the use of 50 and 5.
2. I noticed a lot of null checking in the code. So here are a couple of things to consider:
1) indeed, it is generally a good practice to do "defense programming". Your null check is a great example.
2) However, what uncle Bob argues in the book is that instead of doing null check everywhere, especially in a language like Java, we should make sure we don't pass null around, period.
3) If you work on an API or a library that is designed to be used by others, the null check is more important. Or, if you work in a language like C or C++ where there is pointer allowed, it is more important to check for null explicitly and thoroughly.

So, what's the conclusion? In general, for this course project, since you are developing a standalone application in Java, it is less important to do null check. Instead, we should focus on making sure we don't pass null around.


Otherwise, good job!
Look forward to reviewing more of your domain logic in the next review.

Please approve and merge the PR once the team has read the feedback. Thanks!

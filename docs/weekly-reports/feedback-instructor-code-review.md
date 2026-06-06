
# Instructor Code Review Feedback

**Contact**: Dr. Yiji Zhang (yiji.zhang@northwestern.edu)

**Purpose of This Document**:
The instructor will perform code review with respect to software design, error handling, format and style on the main branch every week starting Week 6 using the letter grade A standards.
The following chapters of the textbook are considered: Chapter 1, 2, 3, 4, 5, 6, 7, 9, and 10. The corresponding lectures are considered, too.

Please note that this feedback does not include evaluation of your progress, the proper use of linters, the quality of your test cases, or your compliance of TDD/BDD workflow.  
You can find the weekly feedback from your dedicated PM/TA for that.

## Week 10 Feedback
The following table contains the _Grading Rubrics for the Team_ that was released on Canvas in the beginning of the quarter (Canvas -> Project Resources -> Grading).
The instructor reviewed all the code in your main branch against every rubric item except "Product Completion."
If any item is not satisfied, your grade will not be penalized as long as your team corrects it between now and the final submission.

Note that the instructor may have granted some teams specific exceptions, which are not reflected in this feedback.
So don't worry if an item is marked "significant issue found" but you've been granted an exception for it.
Just make sure to document that exception in your README.md when you finalize your submission.

After this feedback, make sure any new code you add continues to satisfy the rubric items.

*How to read the rubrics: The evaluation starts with the criteria for F and progresses through D, C, B, and A. The grade advances to the next level if all criteria for the current grade are met or exceeded.*

Status:
- ✅: The rubric item is met or exceeded!
- ⚠️: Attention needed
- ❌: Significant issue found
- ➖: Not evaluated by the instructor in this feedback

| Category                   | Status | Item                                                                                                                                                                            | Notes                 |
|----------------------------|--------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------|
| **F: Not Sufficient Work** |        |                                                                                                                                                                                 |                       |
|                            | ✅      | The project does not meet all the criteria of D.                                                                                                                                |                       |
| **D: Progressing**         |        |                                                                                                                                                                                 |                       |
| **Product Completion**     | ➖      | **_Note: This review does not evaluate anything in the "Product Completion" category._**                                                                                        |                       |
|                            | ➖      | If the game has multiple cards or characters, at least one type is fully implemented.                                                                                           |                       |
|                            | ➖      | The game is playable.                                                                                                                                                           |                       |
| **Product Quality**        |        |                                                                                                                                                                                 |                       |
|                            | ✅      | The team has a "definition of done" (BVA) fully documented for the part of the system that is done.                                                                             |                       |
|                            | ✅      | All the automated tests correctly reflect the BVA analysis, including but not limited to using the correct input values identified by BVA and using the appropriate assertions. |                       |
|                            | ✅      | Automated testing may indicate some failures.                                                                                                                                   |                       |
| **Process Quality**        |        |                                                                                                                                                                                 |                       |
|                            | ✅      | The team uses the project management board very rarely.                                                                                                                         |                       |
|                            | ✅      | GitHub repository branch protection rules are not fully set up so that people can push into main without a pull request approval.                                               |                       |
|                            | ✅      | Continuous Integration (CI) is not fully set up or only set up in the last two weeks.                                                                                           |                       |
| **C: Satisfactory**        |        |                                                                                                                                                                                 |                       |
| **Product Completion**     | ➖      | **_Note: This review does not evaluate anything in the "Product Completion" category._**                                                                                        |                       |
|                            | ➖      | If the game has multiple cards or characters, most of the types are fully implemented.                                                                                          |                       |
|                            | ➖      | The win condition is implemented.                                                                                                                                               |                       |
| **Product Quality**        |        |                                                                                                                                                                                 |                       |
|                            | ✅      | All the automated tests pass.                                                                                                                                                   |                       |
|                            | ✅      | GitHub commit history demonstrates evidence of a TDD/BDD workflow for all the non-UI code.                                                                                      |                       |
|                            | ✅      | There is evidence that the team uses mutation testing and code coverage to improve the quality of the test cases, but not all the issues are solved.                            |                       |
|                            | ✅      | The team has set up some code style standards, but not all the code satisfies the standard.                                                                                     |                       |
| **Process Quality**        |        |                                                                                                                                                                                 |                       |
|                            | ✅      | The team uses the project management board steadily and frequently, but the description of each task is very vague.                                                             |                       |
|                            | ✅      | GitHub repository branch protection rules are fully set up so that people cannot push into main without a pull request approval.                                                |                       |
|                            | ✅      | Continuous Integration (CI) is not fully set up or only set up in the last five weeks.                                                                                          |                       |
|                            | ✅      | The team rarely documents each week's planning and progress.                                                                                                                    |                       |
| **B: Good Work**           |        |                                                                                                                                                                                 |                       |
| **Product Completion**     | ➖      | **_Note: This review does not evaluate anything in the "Product Completion" category._**                                                                                        |                       |
|                            | ➖      | (Applicable only if the chosen game has more than one winning condition) All the winning conditions are implemented.                                                            |                       |
|                            | ➖      | If the game has multiple cards or characters, all the types are fully implemented.                                                                                              |                       |
| **Product Quality**        |        |                                                                                                                                                                                 |                       |
|                            | ✅      | There is evidence that the team uses mutation testing and code coverage as soon as the concepts are introduced and there is no issue revealed by them.                          |                       |
|                            | ❌    | 100% mutants should be killed (except for equivalent mutant).                                                                                                                   | Pitest is not set up. |
|                            | ✅      | 100% cyclomatic coverage for non-GUI and non-enum code.                                                                                                                         |                       |
|                            | ✅      | The code fully satisfies the style standards the team set up.                                                                                                                   |                       |
|                            | ✅      | There is progress on integration testing.                                                                                                                                       |                       |
| **Process Quality**        |        |                                                                                                                                                                                 |                       |
|                            | ✅      | The team uses the project management board steadily and frequently, and the description of each task is detailed.                                                               |                       |
|                            | ✅      | Continuous Integration (CI) is fully set up from the beginning.                                                                                                                 |                       |
|                            | ✅      | The team documents every week's planning and progress evaluation professionally.                                                                                                |                       |
| **A: Excellent**           |        |                                                                                                                                                                                 |                       |
| **Product Completion**     | ➖      | **_Note: This review does not evaluate anything in the "Product Completion" category._**                                                                                        |                       |
|                            | ➖      | Support of locale: the software allows the user to choose between at least two languages at the beginning of the game.                                                          |                       |
|                            | ➖      | The code supports easily adding new locales without changing existing code.                                                                                                     |                       |
| **Product Quality**        |        |                                                                                                                                                                                 |                       |
|                            | ✅      | The code fully satisfied the code standards discussed in Clean Code.                                                                                                            |                       |
|                            | ➖      | Integration testing is done on at least 2 main features.                                                                                                                        |                       |

### Other comments
❌One main issue I just found is that your team doesn't have SpotBugs configured. Please set it up asap.

Otherwise, good work!

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

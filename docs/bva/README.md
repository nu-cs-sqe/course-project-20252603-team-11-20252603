# Overall Rule
This folder should contain the BVA analysis. There should be one .md for each class, and there should be BVA analysis for each public method.

# What to Include in each BVA Analysis File (like `MyVector.md`)

You are encouraged to document your intermediate analysis results for Steps 1-3.
However, you are only required to document Step 4.

For each test case, you may choose **any easy-to-read format** you like. Regardless of the format, you are required to specify the following for each test case:
1. A unique ID
2. The method(s) under test
3. The state of the system under test
4. The expected output
5. Whether it has been implemented

Here are 2 formats as references. You can use any of them or a revised version of them.

## Format 1:

### Method under test: `abc()`

|              | State of the System | Expected output | Implemented?              |
|--------------|---------------------|-----------------|---------------------------|
| Test Case 1  |                     |                 | :x: or :white_check_mark: |
| Test Case 2  |                     |                 | :x: or :white_check_mark: |


### Method under test: `efg()`

|              | State of the System | Expected output | Implemented?              |
|--------------|---------------------|-----------------|---------------------------|
| Test Case 3  |                     |                 | :x: or :white_check_mark: |
| Test Case 4  |                     |                 | implemented in Test Case 1 |

## Format 2:

### Method under test: `abc()`
- **TC1: NAME OF THE TEST CASE** ( :x: or :white_check_mark: )
    - **State of the system**: abc
    - **Expected output**: abc

- **TC2: NAME OF THE TEST CASE** ( :x: or :white_check_mark: )
    - **State of the system**: abc
    - **Expected output**: abc

### Method under test: `efg()`
- **TC3: NAME OF THE TEST CASE** ( :x: or :white_check_mark: )
    - **State of the system**: abc
    - **Expected output**: abc

- **TC4: NAME OF THE TEST CASE** ( implemented in TC1 )
    - **State of the system**: abc
    - **Expected output**: abc

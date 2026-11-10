# ThreadCheckArray_Bas

## Overview

ThreadCheckArray_Bas is a Java console application that searches for a subset
of integers whose sum equals a user-supplied target. The project demonstrates
recursive backtracking, two-thread parallel execution, synchronization of
shared state, Java collections, and source-level Javadoc documentation.

The project contains three main classes:

| Class | Responsibility |
| --- | --- |
| `TestThreadCheckArray` | Reads input, starts the worker threads, waits for them, and prints the result. |
| `SharedData` | Stores the input values, target value, completion flag, and winning-position markers. |
| `ThreadCheckArray` | Implements `Runnable` and performs the recursive subset-sum search. |

## Running the project

The application is interactive. It requests:

1. The number of input values.
2. The input values.
3. The target sum, `b`.

Compile and run the project with a JDK from the repository root:

```text
javac -d out src\*.java
java -cp out TestThreadCheckArray
```

An example input sequence is:

```text
Enter array size
4
Enter numbers for array
2 7 11 15
Enter number
9
```

If a matching subset is found, the program prints the target, input
positions, values, and a marker row indicating which positions belong to the
solution. If no solution is found, it prints `Sorry`.

## Recursive multithreaded Subset Sum algorithm

### Recursive search

For an input list `A` and target `b`, the algorithm examines each value using
two possible decisions:

- Include the current value and subtract it from the remaining target.
- Exclude the current value and leave the remaining target unchanged.

`ThreadCheckArray.rec(n, b)` represents a search over the first `n` input
positions with `b` still required. The base case checks the last position.
When a branch reaches a valid sum, the worker marks the corresponding
positions in its `boolean[] winArray`.

This is a backtracking algorithm. Its worst-case time complexity is
approximately `O(2^n)`, because it may visit both branches for every input
value. The recursive call stack requires `O(n)` auxiliary space, excluding
the input and result collections.

### Parallel search

The application creates two workers using the same `SharedData` instance:

```java
Thread thread1 = new Thread(new ThreadCheckArray(sd), "thread1");
Thread thread2 = new Thread(new ThreadCheckArray(sd), "thread2");
```

The workers begin with complementary branches of the search tree:

- `thread1` starts with the final value included in the remaining sum.
- `thread2` starts with the final value excluded.

This allows the two branches to be explored concurrently. The program stops
searching after a worker reports a solution; it is designed to find a valid
solution rather than enumerate every possible solution.

### Thread synchronization

`SharedData` is used as the monitor for communication between the workers.
The shared completion flag is read and written inside `synchronized (sd)`
blocks:

1. A worker checks the shared flag before continuing a branch. If another
   worker already found a solution, it returns early.
2. A worker that finds a solution sets the shared flag while holding the
   `SharedData` monitor.
3. The winning `boolean[]` is published through `setWinArray` while using
   the same monitor.
4. The main thread calls `join()` for both workers before reading the result,
   ensuring that the worker threads have finished.

Each worker has its own local `flag` and `boolean[] winArray`. The shared
flag is the stop signal visible to both workers, while the shared result
reference publishes the selected positions. The result remains a primitive
`boolean[]`; it is not part of the integer collection refactoring.

## `ArrayList<Integer>` data-structure refactoring

The input collection named `array` was refactored from `int[]` to
`ArrayList<Integer>`. This change preserves the integer values while using
the Java Collections Framework and allowing the collection to grow
dynamically.

The main API differences are:

| Primitive array | `ArrayList<Integer>` |
| --- | --- |
| `array.length` | `array.size()` |
| `array[index]` | `array.get(index)` |
| `array[index] = value` | `array.add(value)` while constructing the list |

The boolean arrays were intentionally not changed. They are initialized
from the new list size so that each input position still has exactly one
result marker:

```java
boolean[] winArray = new boolean[array.size()];
```

`ArrayList<Integer>` stores boxed `Integer` values. Java automatically boxes
primitive values added to the list and unboxes values returned by `get()` when
they are used in arithmetic expressions.

The scope of this refactoring is intentionally limited: only the collection
named `array` changes type. Other arrays, especially `boolean[] winArray`,
retain their original array types and indexing behavior.

## Code documentation and Javadoc

The public classes and their principal constructors and methods are
documented with Javadoc. Documentation describes:

- The responsibility of each class.
- Constructor parameters and shared state.
- Recursive method parameters and search behavior.
- Return values and the meaning of result markers.
- The interactive entry point.

Generic types must be written so Javadoc treats them as code rather than as
unknown HTML tags. Use `{@code ...}` or HTML entities:

```java
{@code ArrayList<Integer>}
```

or:

```html
ArrayList&lt;Integer&gt;
```

Javadoc can be generated with:

```text
javadoc -d doc src\*.java
```

The generated documentation should be checked for warnings, especially
unknown-tag warnings caused by unescaped generic type parameters.

## Authors and Git collaboration roles

### Author

- **Denis Zuravlov** — original author, project owner, and primary
  implementer.

### Collaboration roles

Git is used to manage the project history and coordinate changes. Typical
roles are:

- **Author or maintainer:** owns the project direction, maintains the main
  branch, and integrates accepted changes.
- **Contributor:** works in a focused branch, preserves existing behavior,
  and submits a pull request with a clear description and validation steps.
- **Reviewer:** checks algorithm correctness, synchronization, data-type
  scope, Javadoc quality, and regression risk.
- **Integrator:** resolves review feedback and merges approved work.

Contributors should keep commits focused, document behavior changes, and
avoid modifying unrelated array types when working on the
`ArrayList<Integer>` refactoring.

## License

This project is distributed under the **Braude College Academic License**.
It is intended for educational and academic use in the Braude College
context.

Permission is granted to students, instructors, and academic collaborators
to use, study, compile, modify, and share the project for non-commercial
educational purposes, provided that:

1. Original authorship and project attribution are retained.
2. Modified copies clearly identify the changes made.
3. Distributed copies include this license notice.
4. Commercial distribution or commercial use is not undertaken without
   prior written permission from the copyright holder and Braude College,
   where applicable.

This software is provided for academic use without warranty. The authors and
Braude College are not responsible for loss, damage, or results arising from
its use. If an official Braude College license or course policy is supplied
for this project, that official text takes precedence over this summary.
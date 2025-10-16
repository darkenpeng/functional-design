package net.degoes

/*
 * ADVANCED WORKSHOP - BUILD A SCHEDULING SYSTEM
 *
 * This is the next level after workshop_exercises.scala!
 *
 * You'll build a functional domain for a complex scheduling system that combines:
 * - Declarative encoding patterns (from Module 04)
 * - Complex operator composition (from Module 02)
 * - Type-safe modeling (from Module 06)
 * - Real-world complexity (multiple interpreters, resource management)
 *
 * DIFFICULTY PROGRESSION:
 * - Level 1-3: Similar to basic workshop (warm up)
 * - Level 4-5: Intermediate (like 02_operators.scala)
 * - Level 6-7: Advanced (combining multiple concepts)
 * - Level 8: Expert (design your own patterns)
 */

object scheduling_system:

  /*
   * ============================================================================
   * LEVEL 1: MODELS - Modeling a Task Scheduling System
   * ============================================================================
   *
   * Build a system that can schedule and execute tasks with dependencies,
   * resource constraints, and error handling.
   */

  /** STEP 1.1 - Model Task Priority
    *
    * Tasks can have Low, Medium, High, or Critical priority
    *
    * TODO: Define an enum for Priority
    */
  type Priority = Nothing // Replace this!

  /** STEP 1.2 - Model Resource Requirements
    *
    * A task might need certain resources to run (CPU cores, memory MB, GPU)
    *
    * Hint: Should this have fields for each resource type?
    *
    * TODO: Define a case class for Resources
    */
  type Resources = Nothing // Replace this!

  /** STEP 1.3 - Model Task Status
    *
    * A task can be: Pending, Running, Completed, or Failed(reason: String)
    *
    * Notice that Failed carries additional information - this is a key pattern!
    *
    * TODO: Define an enum for TaskStatus
    */
  type TaskStatus = Nothing // Replace this!

  /** STEP 1.4 - Model a Schedule Action (Declarative Encoding!)
    *
    * This is where it gets interesting! Instead of executing tasks directly, we'll model a
    * DESCRIPTION of what we want to schedule.
    *
    * A ScheduleAction[A] represents a scheduled computation that produces A.
    *
    * Possible actions:
    *   - Execute(name, work: () => A) - Execute a single task
    *   - Sequence(first, second) - Run first, then second
    *   - Parallel(left, right) - Run both in parallel
    *   - Retry(action, maxAttempts) - Retry on failure
    *   - WithResources(resources, action) - Require resources
    *   - Delay(duration, action) - Delay before running
    *
    * This is a DECLARATIVE encoding - we describe WHAT to do, not HOW. Later we'll build
    * interpreters that execute these descriptions.
    *
    * TODO: Define an enum ScheduleAction[A] with the cases above
    */
  enum ScheduleAction[+A]:
    case Execute(name: String, work: () => A)
    // TODO: Add the other cases here!
  end ScheduleAction

  /*
   * ============================================================================
   * LEVEL 2: CONSTRUCTORS - Building Simple Schedules
   * ============================================================================
   */

  object ScheduleAction:
    /** STEP 2.1 - Create a constructor for a task that succeeds immediately
      *
      * This should create an Execute action with a name and work that returns the value.
      *
      * TODO: Implement this constructor
      */
    def succeed[A](name: String, value: A): ScheduleAction[A] =
      ???

    /** STEP 2.2 - Create a constructor for a task that fails immediately
      *
      * Hint: In Scala, you can throw exceptions in the work function, or you could model this with
      * a special Result type (your choice!)
      *
      * TODO: Implement this constructor
      */
    def fail(name: String, error: String): ScheduleAction[Nothing] =
      ???

    /** STEP 2.3 - Create a constructor for a delayed computation
      *
      * This should wrap an action in a Delay.
      *
      * TODO: Implement this constructor
      */
    def delay[A](duration: Int, action: ScheduleAction[A]): ScheduleAction[A] =
      ???

    /** STEP 2.4 - Create a constructor that executes an effect
      *
      * This takes a by-name parameter (lazy evaluation) and wraps it in Execute.
      *
      * TODO: Implement this constructor
      */
    def effect[A](name: String)(work: => A): ScheduleAction[A] =
      ???
  end ScheduleAction

  /*
   * ============================================================================
   * TEST LEVEL 2
   * ============================================================================
   */

  // Uncomment to test:
  // val task1 = ScheduleAction.succeed("quick-task", 42)
  // val task2 = ScheduleAction.fail("doomed-task", "This always fails")
  // val task3 = ScheduleAction.effect("side-effect") { println("Hello!"); 100 }

  /*
   * ============================================================================
   * LEVEL 3: BASIC OPERATORS - Sequential and Parallel Composition
   * ============================================================================
   */

  extension [A](self: ScheduleAction[A])

    /** STEP 3.1 - Implement sequential composition (flatMap/andThen)
      *
      * The `*>` operator (pronounced "and then") should run this action, discard its result, and
      * then run the next action.
      *
      * This should create a Sequence action.
      *
      * TODO: Implement this operator
      */
    def *>[B](that: ScheduleAction[B]): ScheduleAction[B] =
      ???

    /** STEP 3.2 - Implement parallel composition (zipPar)
      *
      * The `<&>` operator should run both actions in parallel and return a tuple of both results.
      *
      * This should create a Parallel action.
      *
      * TODO: Implement this operator
      */
    def <&>[B](that: ScheduleAction[B]): ScheduleAction[(A, B)] =
      ???

    /** STEP 3.3 - Implement retry logic
      *
      * The `retry` operator should wrap this action in a Retry with the specified number of max
      * attempts.
      *
      * TODO: Implement this operator
      */
    def retry(maxAttempts: Int): ScheduleAction[A] =
      ???

    /** STEP 3.4 - Implement resource requirements
      *
      * The `requires` operator should wrap this action in a WithResources that specifies what
      * resources are needed.
      *
      * TODO: Implement this operator
      */
    def requires(resources: Resources): ScheduleAction[A] =
      ???
  end extension

  /*
   * ============================================================================
   * LEVEL 4: ADVANCED OPERATORS - Error Handling and Fallbacks
   * ============================================================================
   *
   * Now we're getting into 02_operators.scala territory!
   */

  extension [A](self: ScheduleAction[A])

    /** STEP 4.1 - Implement fallback/orElse
      *
      * If this action fails, try the fallback action instead.
      *
      * Challenge: How do you model this in your ScheduleAction enum? You might need to add a new
      * case like OrElse(primary, fallback)!
      *
      * TODO: First add OrElse to your enum, then implement this operator
      */
    def orElse[A1 >: A](that: ScheduleAction[A1]): ScheduleAction[A1] =
      ???

    /** STEP 4.2 - Implement timeout
      *
      * The action should fail if it takes longer than the specified duration.
      *
      * Challenge: Add a Timeout case to your enum!
      *
      * TODO: First add Timeout to your enum, then implement this operator
      */
    def timeout(maxDuration: Int): ScheduleAction[A] =
      ???

    /** STEP 4.3 - Implement race
      *
      * Run both actions in parallel, return whichever completes first, and cancel the other.
      *
      * Challenge: Add a Race case to your enum!
      *
      * TODO: First add Race to your enum, then implement this operator
      */
    def race[A1 >: A](that: ScheduleAction[A1]): ScheduleAction[A1] =
      ???
  end extension

  /*
   * ============================================================================
   * LEVEL 5: TRANSFORMATION OPERATORS - map, flatMap, and friends
   * ============================================================================
   */

  extension [A](self: ScheduleAction[A])

    /** STEP 5.1 - Implement map
      *
      * Transform the result of this action using a function.
      *
      * Challenge: Add a Map case to your enum, or implement this using other primitives!
      *
      * Think about: Which approach is better? Direct encoding vs derived?
      *
      * TODO: Implement this operator
      */
    def map[B](f: A => B): ScheduleAction[B] =
      ???

    /** STEP 5.2 - Implement flatMap
      *
      * Run this action, and based on its result, decide what action to run next.
      *
      * This is the most powerful operator - it enables dynamic scheduling!
      *
      * Challenge: Add a FlatMap case to your enum!
      *
      * TODO: First add FlatMap to your enum, then implement this operator
      */
    def flatMap[B](f: A => ScheduleAction[B]): ScheduleAction[B] =
      ???

    /** STEP 5.3 - Implement as
      *
      * Replace the result of this action with a constant value.
      *
      * Hint: Can you implement this using map?
      *
      * TODO: Implement this operator
      */
    def as[B](value: B): ScheduleAction[B] =
      ???
  end extension

  /*
   * ============================================================================
   * LEVEL 6: BUILDING AN INTERPRETER (EXECUTABLE ENCODING)
   * ============================================================================
   *
   * Now for the exciting part! You've built a DECLARATIVE model.
   * Let's build an interpreter that can EXECUTE it!
   *
   * This demonstrates the power of the declarative approach:
   * - One model, multiple interpreters
   * - Easy to test (just inspect the model)
   * - Easy to optimize (transform the model before execution)
   */

  /** STEP 6.1 - Build a simple synchronous interpreter
    *
    * This interpreter will execute schedules synchronously (blocking).
    *
    * Pattern match on the ScheduleAction and execute each case:
    *   - Execute: Run the work function
    *   - Sequence: Run first, then second
    *   - Parallel: For simplicity, run sequentially (we'll improve this later)
    *   - Retry: Try running, catch exceptions, retry up to maxAttempts
    *   - etc.
    *
    * TODO: Implement this interpreter
    */
  def runSync[A](action: ScheduleAction[A]): A =
    action match
      case ScheduleAction.Execute(name, work) =>
        println(s"[SYNC] Executing: $name")
        work()
      // TODO: Handle all other cases!
      case _                                  => ???

  /** STEP 6.2 - Build a "dry run" interpreter that just prints the schedule
    *
    * This interpreter doesn't execute anything - it just prints what WOULD happen. Great for
    * debugging!
    *
    * TODO: Implement this interpreter
    */
  def dryRun[A](action: ScheduleAction[A], indent: Int = 0): Unit =
    val prefix = "  " * indent
    action match
      case ScheduleAction.Execute(name, _) =>
        println(s"$prefix- Execute: $name")
      // TODO: Handle all other cases, recursively printing nested actions!
      case _                               => ???

  /** STEP 6.3 - Build an analyzer that estimates total execution time
    *
    * This interpreter doesn't run anything - it analyzes the schedule to estimate how long it will
    * take.
    *
    * Assume each Execute takes 1 time unit, Delays add their duration, Sequences add their times,
    * Parallels take the max of their branches.
    *
    * TODO: Implement this analyzer
    */
  def estimateTime[A](action: ScheduleAction[A]): Int =
    ???

  /*
   * ============================================================================
   * LEVEL 7: PRACTICAL COMPOSITION - Build Complex Schedules
   * ============================================================================
   */

  /** STEP 7.1 - Build a batch processing pipeline
    *
    * Create a schedule that:
    *   1. Fetches data from 3 sources in parallel 2. Validates the data sequentially 3. Transforms
    *      the data 4. Writes to database 5. If any step fails, retries up to 3 times 6. If it still
    *      fails, falls back to writing to a backup location
    *
    * Use only constructors and operators - no direct enum construction!
    *
    * TODO: Implement this compositionally
    */
  lazy val batchPipeline: ScheduleAction[String] =
    ???

  // Helper tasks you can use:
  lazy val fetchSource1  = ScheduleAction.effect("fetch-source-1")("data1")
  lazy val fetchSource2  = ScheduleAction.effect("fetch-source-2")("data2")
  lazy val fetchSource3  = ScheduleAction.effect("fetch-source-3")("data3")
  lazy val validate      = ScheduleAction.effect("validate")("validated")
  lazy val transform     = ScheduleAction.effect("transform")("transformed")
  lazy val writeToDB     = ScheduleAction.effect("write-db")("success")
  lazy val writeToBackup = ScheduleAction.effect("write-backup")("backup-success")

  /** STEP 7.2 - Build a task dependency graph
    *
    * Model this dependency structure:
    *
    * A / \ B C \ / \ D E \ / F
    *
    * Where:
    *   - A must run first
    *   - B and C can run in parallel after A
    *   - D needs both B and C
    *   - E needs only C
    *   - F needs both D and E
    *
    * Use flatMap, <&>, *>, and other operators to model dependencies!
    *
    * TODO: Implement this schedule
    */
  lazy val dependencyGraph: ScheduleAction[String] =
    ???

  // Task definitions:
  def taskA = ScheduleAction.effect("A")("A-done")
  def taskB = ScheduleAction.effect("B")("B-done")
  def taskC = ScheduleAction.effect("C")("C-done")
  def taskD = ScheduleAction.effect("D")("D-done")
  def taskE = ScheduleAction.effect("E")("E-done")
  def taskF = ScheduleAction.effect("F")("F-done")

  /*
   * ============================================================================
   * LEVEL 8: EXPERT CHALLENGE - Design Your Own Patterns
   * ============================================================================
   */

  /** STEP 8.1 - Implement `forever` (repeat indefinitely)
    *
    * This operator should repeat an action forever.
    *
    * Challenge: How do you model infinite repetition declaratively? Hint: You might need a Forever
    * case, or use recursion cleverly!
    *
    * TODO: Design and implement this pattern
    */
  extension [A](self: ScheduleAction[A])
    def forever: ScheduleAction[Nothing] =
      ???

  /** STEP 8.2 - Implement `repeatN` (repeat N times)
    *
    * This operator should repeat an action exactly N times.
    *
    * TODO: Design and implement this pattern
    */
  extension [A](self: ScheduleAction[A])
    def repeatN(n: Int): ScheduleAction[A] =
      ???

  /** STEP 8.3 - Implement `zipAll` for List[ScheduleAction[A]]
    *
    * Take a list of actions and run them all in parallel, collecting results.
    *
    * This is like <&> but for arbitrary numbers of actions!
    *
    * Hint: Use recursion or fold to build this up from <&>
    *
    * TODO: Implement this combinator
    */
  def zipAll[A](actions: List[ScheduleAction[A]]): ScheduleAction[List[A]] =
    ???

  /** STEP 8.4 - Implement `collectFirst` for List[ScheduleAction[A]]
    *
    * Run all actions in parallel (racing), return the first one that succeeds, cancel all others.
    *
    * TODO: Implement this combinator
    */
  def collectFirst[A](actions: List[ScheduleAction[A]]): ScheduleAction[A] =
    ???

  /** STEP 8.5 - Design your own operator!
    *
    * Think about a scheduling pattern you've encountered in real projects. Design an operator that
    * captures that pattern.
    *
    * Ideas:
    *   - `withDeadline`: Must complete before a specific time
    *   - `throttle`: Limit rate of execution
    *   - `circuit`: Circuit breaker pattern
    *   - `conditional`: Run based on runtime condition
    *   - `resource`: Acquire/release pattern (like try-with-resources)
    *
    * TODO: Design and implement your own operator!
    */
  extension [A](self: ScheduleAction[A])
    def myCustomOperator: ScheduleAction[A] =
      ???

  /*
   * ============================================================================
   * BONUS: Type-Safe Scheduling with GADTs (Module 06)
   * ============================================================================
   *
   * If you want an even bigger challenge, try making your ScheduleAction
   * type-safe by tracking properties in the type system!
   *
   * For example:
   * - Track whether actions can fail: ScheduleAction[E, A]
   * - Track resource requirements: ScheduleAction[R, E, A]
   * - Track timing constraints: Timed[A] vs Instant[A]
   *
   * This is advanced material from Module 06 - only attempt if you're comfortable!
   */

end scheduling_system

/*
 * ============================================================================
 * TESTING YOUR SOLUTION
 * ============================================================================
 */

object scheduling_system_test:
  import scheduling_system.*
  import scheduling_system.ScheduleAction.*

  def testBasicExecution(): Unit =
    println("=== Testing Basic Execution ===")

    val simple = succeed("test", 42)
    println(s"Result: ${runSync(simple)}")

    val composed = succeed("first", 1) *> succeed("second", 2)
    println(s"Composed result: ${runSync(composed)}")

  def testDryRun(): Unit =
    println("\n=== Testing Dry Run ===")

    val complex =
      succeed("start", 1) *>
        (succeed("a", 2) <&> succeed("b", 3)) *>
        succeed("end", 4)

    dryRun(complex)

  def testEstimation(): Unit =
    println("\n=== Testing Time Estimation ===")

    val schedule =
      delay(5, succeed("a", 1)) *>
        delay(3, succeed("b", 2))

    println(s"Estimated time: ${estimateTime(schedule)}")

  // Uncomment to run tests:
  // @main def runTests(): Unit =
  //   testBasicExecution()
  //   testDryRun()
  //   testEstimation()
end scheduling_system_test

/*
 * ============================================================================
 * REFLECTION QUESTIONS (Advanced)
 * ============================================================================
 *
 * After completing these exercises, think deeply about:
 *
 * 1. DECLARATIVE vs EXECUTABLE ENCODING (Module 04):
 *    - Why did we use an enum to MODEL schedules instead of executing directly?
 *    - What are the tradeoffs of declarative encoding?
 *    - How many interpreters could you write for the same model?
 *
 * 2. ORTHOGONALITY (Module 05):
 *    - Which operators are primitive and which are derived?
 *    - Could you implement `map` using `flatMap`?
 *    - Could you implement `<&>` using `race` and other operators?
 *    - What's the MINIMAL set of primitives you really need?
 *
 * 3. COMPOSABILITY:
 *    - Why do all operators return ScheduleAction[_]?
 *    - What would happen if `retry` returned a different type?
 *    - How does this enable building complex schedules from simple ones?
 *
 * 4. TYPE SAFETY (Module 06):
 *    - How could you use GADTs to track effects in the type?
 *    - How could you prevent impossible schedules at compile time?
 *    - What properties would be valuable to track in the type system?
 *
 * 5. REAL-WORLD APPLICATIONS:
 *    - How does this compare to libraries like ZIO, Cats Effect, or Akka?
 *    - What would you need to add for production use?
 *    - What are the performance implications of this approach?
 *
 * 6. PATTERN RECOGNITION (Module 07):
 *    - What patterns did you recognize (Monad, Applicative, etc.)?
 *    - How does this relate to Free Monads?
 *    - What other domains could benefit from this approach?
 */

/*
 * ============================================================================
 * NEXT STEPS
 * ============================================================================
 *
 * After completing this advanced workshop, you should:
 *
 * 1. Go back and study the real implementations:
 *    - 02_operators.scala (compare your operators to theirs)
 *    - 04_encoding.scala (study executable vs declarative)
 *    - 05_practices.scala (orthogonality and minimalism)
 *    - 06_typed.scala (type-safe models with GADTs)
 *
 * 2. Compare to real libraries:
 *    - Look at ZIO's effect type and its operators
 *    - Look at Cats Effect's IO
 *    - Look at Akka Streams
 *    - See how they solve similar problems!
 *
 * 3. Build your own domain:
 *    - Pick a domain from your work
 *    - Model it using functional design principles
 *    - Build constructors and operators
 *    - Create multiple interpreters
 *
 * Congratulations on completing the advanced workshop! 🚀
 */

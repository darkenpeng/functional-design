package net.degoes

/*
 * INTERACTIVE WORKSHOP - BUILD A RECIPE COMPOSER
 *
 * In this exercise, you'll build a functional domain for composing cooking recipes!
 * This will teach you the three pillars of Functional Design:
 *   1. MODELS (immutable data types)
 *   2. CONSTRUCTORS (building simple solutions)
 *   3. OPERATORS (transforming and combining solutions)
 *
 * You'll work through 3 progressive levels, with each level building on the previous one.
 */

object recipe_composer:
  /*
   * ============================================================================
   * LEVEL 1: MODELS - Building Your Domain with ADTs
   * ============================================================================
   *
   * Goal: Model recipes using only `case class` and `enum`
   *
   * Think about: What data do we need to represent a recipe?
   */

  /** STEP 1.1 - Model an Ingredient
    *
    * An ingredient has:
    *   - A name (e.g., "flour", "sugar", "eggs")
    *   - An amount (e.g., 2.5)
    *   - A unit (e.g., "cups", "tablespoons", "grams")
    *
    * TODO: Replace the line below with a proper case class
    */
  case class Ingredient(name: String, amount: Double, unit: String)
end recipe_composer

/** STEP 1.2 - Model a Cooking Step
  *
  * A step has:
  *   - A description (e.g., "Mix flour and sugar")
  *   - A duration in minutes (e.g., 5)
  *
  * TODO: Replace the line below with a proper case class
  */
case class CookingStep(description: String, duration: Int)

/** STEP 1.3 - Model Recipe Difficulty
  *
  * Recipes can be Easy, Medium, or Hard
  *
  * Hint: Should this be a case class or an enum? Think about whether there are multiple distinct
  * variants vs. a single type with fields.
  *
  * TODO: Replace the line below with an enum
  */
enum Difficulty:
  case Easy
  case Medium
  case Hard

/** STEP 1.4 - Model a Complete Recipe
  *
  * A recipe has:
  *   - A name (e.g., "Chocolate Chip Cookies")
  *   - A list of ingredients
  *   - A list of cooking steps
  *   - A difficulty level
  *   - Total preparation time in minutes
  *
  * TODO: Replace the line below with a proper case class
  */
case class Recipe(
  name: String,
  ingredients: List[Ingredient],
  steps: List[CookingStep],
  difficulty: Difficulty,
  prepTimeMinutes: Int
)

/*
 * ============================================================================
 * TEST YOUR LEVEL 1 SOLUTIONS!
 * ============================================================================
 *
 * Once you've defined your types above, uncomment the code below to test them.
 * If they compile, you've completed Level 1! 🎉
 */

// Uncomment these to test:
val flour          = Ingredient("flour", 2.0, "cups")
val mixStep        = CookingStep("Mix ingredients", 5)
val easyDifficulty = Difficulty.Easy
val cookies        = Recipe(
  name = "Chocolate Chip Cookies",
  ingredients = List(flour),
  steps = List(mixStep),
  difficulty = easyDifficulty,
  prepTimeMinutes = 30
)

/*
 * ============================================================================
 * LEVEL 2: CONSTRUCTORS - Building Simple Solutions
 * ============================================================================
 *
 * Goal: Create "constructor" functions that make it easy to build recipes
 *
 * Constructors are smart functions that create simple, common instances
 * of your domain model. They live in companion objects.
 */

/** STEP 2.1 - Create a constructor for an empty recipe
  *
  * This should create a recipe with:
  *   - A given name
  *   - Empty ingredients list
  *   - Empty steps list
  *   - Easy difficulty
  *   - 0 prep time
  *
  * TODO: Implement this constructor
  */
def empty(name: String): Recipe =
  Recipe(name, List.empty[Ingredient], List.empty[CookingStep], Difficulty.Easy, 0)

/** STEP 2.2 - Create a constructor for a single-step recipe
  *
  * This creates a recipe with just one step and one ingredient. Useful for very simple recipes like
  * "Boil Water" or "Toast Bread"
  *
  * TODO: Implement this constructor
  */
def simple(
  name: String,
  ingredient: Ingredient,
  step: CookingStep
): Recipe = Recipe(name, List(ingredient), List(step), Difficulty.Easy, 0)

/** STEP 2.3 - Create a constructor from a list of ingredients
  *
  * This creates a recipe that just lists ingredients (like a shopping list) with no steps yet.
  *
  * TODO: Implement this constructor
  */
def fromIngredients(name: String, ingredients: List[Ingredient]): Recipe =
  Recipe(name, ingredients, List.empty[CookingStep], Difficulty.Easy, 0)
end Recipe

/*
 * ============================================================================
 * TEST YOUR LEVEL 2 SOLUTIONS!
 * ============================================================================
 */

// Uncomment these to test:
val emptyRecipe  = Recipe.empty("My New Recipe")
val toast        = Recipe.simple(
  "Toast",
  Ingredient("bread", 2, "slices"),
  CookingStep("Put in toaster", 3)
)
val shoppingList = Recipe.fromIngredients(
  "Pancakes",
  List(
    Ingredient("flour", 2, "cups"),
    Ingredient("eggs", 2, "whole"),
    Ingredient("milk", 1, "cup")
  )
)

/*
 * ============================================================================
 * LEVEL 3: OPERATORS - The Power of Composition! 🚀
 * ============================================================================
 *
 * Goal: Add operators that transform and combine recipes
 *
 * This is where Functional Design becomes powerful! Operators let you:
 * - Transform recipes (unary operators)
 * - Combine recipes (binary operators)
 * - Build complex solutions from simple ones
 *
 * Key principle: Operators should accept and return the same type (or compatible types)
 * so they can be chained together!
 */

extension (self: Recipe)

  /** STEP 3.1 - Binary Operator: Combine two recipes sequentially
    *
    * The `++` operator should create a new recipe that:
    *   - Has a combined name (e.g., "Cookies ++ Frosting" -> "Cookies then Frosting")
    *   - Includes ingredients from BOTH recipes
    *   - Includes steps from BOTH recipes (in order: self's steps, then that's steps)
    *   - Takes the harder difficulty of the two
    *   - Adds up the prep times
    *
    * Example: val cookies = Recipe.simple(...) val frosting = Recipe.simple(...) val
    * frostiedCookies = cookies ++ frosting
    *
    * TODO: Implement this binary operator
    */
  def ++(that: Recipe): Recipe = self.copy(
    name = s"${self.name} then ${that.name}",
    ingredients = self.ingredients,
    difficulty = (self.difficulty, that.difficulty) match
      case (Difficulty.Hard, _)   => Difficulty.Hard
      case (_, Difficulty.Hard)   => Difficulty.Hard
      case (Difficulty.Medium, _) => Difficulty.Medium
      case (_, Difficulty.Medium) => Difficulty.Medium
      case _                      => Difficulty.Easy
    ,
    steps = self.steps ++ that.steps,
    prepTimeMinutes = self.prepTimeMinutes + self.prepTimeMinutes
  )

  /** STEP 3.2 - Unary Operator: Add an ingredient
    *
    * This operator adds a new ingredient to an existing recipe. Everything else stays the same.
    *
    * Example: val cookies = Recipe.empty("Cookies") val withFlour =
    * cookies.addIngredient(Ingredient("flour", 2, "cups"))
    *
    * TODO: Implement this unary operator
    */
  def addIngredient(ingredient: Ingredient): Recipe =
    self.copy(ingredients = self.ingredients :: ingredient)

  /** STEP 3.3 - Unary Operator: Add a cooking step
    *
    * This operator adds a new step to an existing recipe. Also increases prep time by the step's
    * duration.
    *
    * Example: val recipe = Recipe.empty("Cookies") val withStep = recipe.addStep(CookingStep("Mix
    * ingredients", 5))
    *
    * TODO: Implement this unary operator
    */
  def addStep(step: CookingStep): Recipe =
    self.copy(steps = self.steps :+ step, prepTimeMinutes = self.prepTimeMinutes + step.duration)

  /** STEP 3.4 - Unary Operator: Make it harder
    *
    * This operator increases the difficulty level:
    *   - Easy -> Medium
    *   - Medium -> Hard
    *   - Hard -> Hard (stays the same)
    *
    * TODO: Implement this unary operator
    */
  def makeHarder: Recipe = self.copy(difficulty = self.difficulty match
    case Difficulty.Easy   => Difficulty.Medium
    case Difficulty.Medium => Difficulty.Hard
    case Difficulty.Hard   => Difficulty.Hard
  )

  /** STEP 3.5 - Unary Operator: Scale the recipe
    *
    * This operator multiplies all ingredient amounts by a factor. Useful for "double this recipe"
    * or "halve this recipe"
    *
    * Example: val singleBatch = Recipe.simple(...) val doubleBatch = singleBatch.scale(2.0)
    *
    * TODO: Implement this unary operator
    */
  def scale(factor: Double): Recipe =
    self.copy(ingredients = self.ingredients.map(i => i.copy(amount = i.amount * factor)))

  /** STEP 3.6 - Binary Operator: Choose between recipes
    *
    * The `orElse` operator provides a fallback recipe. If this recipe is "empty" (has no steps),
    * use that recipe instead.
    *
    * This is useful for optional variations: val recipe = fancyVersion.orElse(simpleVersion)
    *
    * TODO: Implement this binary operator
    */
  def orElse(that: Recipe): Recipe = if (self.steps.isEmpty) that else self
end extension

/*
 * ============================================================================
 * LEVEL 3 CHALLENGE: Compose Everything Together!
 * ============================================================================
 *
 * Now use your operators to build a complex recipe compositionally!
 */

/** STEP 3.7 - Build a complete recipe using only constructors and operators
  *
  * Build a "Chocolate Chip Cookies with Frosting" recipe by:
  *   1. Starting with an empty recipe 2. Adding ingredients one by one 3. Adding steps one by one
  *      4. Making it harder (if needed) 5. Combining with a frosting recipe
  *
  * Try to do this WITHOUT directly calling the Recipe(...) constructor! Use only: Recipe.empty,
  * .addIngredient, .addStep, .++, .makeHarder, etc.
  *
  * TODO: Implement this compositionally
  */
lazy val chocolateChipCookiesWithFrosting: Recipe =
  val cookies = Recipe
    .empty("Chocolate Chip Cookies")
    .addIngredient(Ingredient("flour", 2, "cups"))
    .addIngredient(Ingredient("sugar", 1, "cup"))
    .addIngredient(Ingredient("chocolate chips", 2, "cups"))
    .addStep(CookingStep("Mix dry ingredients", 5))
    .addStep(CookingStep("Add chocolate chips", 2))
    .addStep(CookingStep("Bake at 350F", 12))
    .makeHarder

  val frosting = Recipe
    .empty("Frosting")
    .addIngredient(Ingredient("butter", 0.5, "cup"))
    .addIngredient(Ingredient("powdered sugar", 2, "cups"))
    .addStep(CookingStep("Beat butter and sugar", 5))

  cookies ++ frosting
end chocolateChipCookiesWithFrosting

/*
 * ============================================================================
 * BONUS LEVEL: Test Your Understanding! 🎓
 * ============================================================================
 */

/** BONUS STEP 1 - Design your own operator
  *
  * Create a `.servings(n: Int)` operator that:
  *   - Scales the recipe to serve n people (assuming original serves 1)
  *   - Updates the name to include serving size
  *
  * Think about: Is this a unary or binary operator? What should it return?
  */
extension (self: Recipe)
  def servings(n: Int): Recipe        = self.copy(
    name = s"${self.name} for ${n} people",
    ingredients = self.ingredients.map(i => i.copy(amount = i.amount * n))
  )

/** BONUS STEP 2 - Design a combining operator with a twist
  *
  * Create an `alongside` operator that combines two recipes that should be prepared in parallel
  * (not sequentially like `++`).
  *
  * The prep time should be the MAXIMUM of the two (since you're cooking in parallel), not the sum!
  *
  * Think about: How is this different from `++`?
  */
extension (self: Recipe)
  def alongside(that: Recipe): Recipe =
    self.copy(
      name = s"${self.name} alongside ${that.name}",
      ingredients = self.ingredients ++ that.ingredients,
      steps = self.steps ++ that.steps,
      difficulty = (self.difficulty, that.difficulty) match
        case (Difficulty.Hard, _)   => Difficulty.Hard
        case (_, Difficulty.Hard)   => Difficulty.Hard
        case (Difficulty.Medium, _) => Difficulty.Medium
        case (_, Difficulty.Medium) => Difficulty.Medium
        case _                      => Difficulty.Easy
      ,
      prepTimeMinutes = Math.max(self.prepTimeMinutes, that.prepTimeMinutes)
    )

/*
 * ============================================================================
 * REFLECTION QUESTIONS
 * ============================================================================
 *
 * After completing these exercises, think about:
 *
 * 1. COMPOSABILITY:
 *    - Why do operators return the same type (Recipe)?
 *    - What does this let you do?
 *
 * 2. IMMUTABILITY:
 *    - Are you modifying existing recipes or creating new ones?
 *    - What are the benefits of this approach?
 *
 * 3. EXPRESSIVENESS:
 *    - Look at your `chocolateChipCookiesWithFrosting` implementation
 *    - Is it readable? Does it look like a "recipe DSL"?
 *
 * 4. ORTHOGONALITY:
 *    - Do your operators overlap in functionality?
 *    - Could you build `scale` from other operators?
 *    - What's the minimal set of operators you really need?
 *
 * These questions preview the advanced topics in modules 05-07!
 */

end recipe_composer

/*
 * ============================================================================
 * NEXT STEPS AFTER COMPLETING THIS WORKSHOP
 * ============================================================================
 *
 * Once you finish this exercise, you'll have learned:
 * ✅ How to model domains with ADTs (Module 01)
 * ✅ How to design composable operators (Module 02)
 * ✅ How to build functional domains (Module 03)
 *
 * You'll be ready to explore:
 * - Module 04: Executable vs Declarative encodings
 * - Module 05: Orthogonality and minimalism
 * - Module 06: Type-safe models with GADTs
 *
 * Have fun! 🚀
 */

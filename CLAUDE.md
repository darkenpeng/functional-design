# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is an educational Scala 3 workshop repository teaching **Functional Design** principles. The repository contains structured exercise sets that teach developers how to build domain-specific solutions using immutable data types, operators, and composability—without requiring advanced category theory or type class knowledge.

The course focuses on:
- Functional domain modeling with immutable ADTs
- Designing composable operators (unary and binary)
- Measuring orthogonality and minimalism in operator design
- Executable vs declarative encodings
- Type-safe domain models using GADTs
- Patterns from the ZIO library ecosystem
- Internal domain-specific languages

## Build System & Commands

This project uses SBT (Scala Build Tool) with Scala 3.3.0-RC6 and ZIO 2.0.9.

### Essential Commands

```bash
# Start SBT shell
sbt

# Compile the project
sbt compile

# Run tests (uses ZIO Test framework)
sbt test

# Format code (uses Scalafmt)
sbt scalafmtAll

# Clean build artifacts
sbt clean
```

### Development Workflow

The project is designed to work with Visual Studio Code + Metals extension. After opening in VS Code with Metals installed, the project should build automatically.

## Code Architecture

### Module Organization

The codebase is organized as a progressive workshop with numbered modules in `src/main/scala/net/degoes/`:

1. **00_intro.scala** - Tour of functional design with examples (ZIO effects, schedules, optics, parsers)
2. **01_adts.scala** - Functional domain modeling with algebraic data types (ADTs)
3. **02_operators.scala** - Designing composable operators (unary and binary transformations)
4. **03_domains.scala** - Building complete functional domains (models + constructors + operators)
5. **04_encoding.scala** - Executable vs declarative encodings, interpreters/executors
6. **05_practices.scala** - Orthogonality, minimalism, and choosing primitive operators
7. **06_typed.scala** - Type-safe models using parameterized types and GADTs
8. **07_patterns.scala** - Recognizing common patterns in functional design
9. **08_practice.scala** - Extended practice exercises
10. **09_integration.scala** - Integration exercises

### Key Design Patterns

**Functional Domain Structure:**
- **Models**: Immutable data types (case classes, enums) representing solutions
- **Constructors**: Functions to build simple solutions (e.g., `const`, `at`, `succeed`)
- **Operators**: Methods to transform and combine solutions (e.g., `map`, `+`, `orElse`, `&&`)

**Executable Encoding** (04_encoding.scala):
- Uses functions/traits with executable methods
- Open for new operators, closed for new interpreters
- Direct execution of domain logic

**Declarative Encoding** (04_encoding.scala):
- Uses enums/data structures describing solutions
- Closed for new operators, open for new interpreters
- Requires separate executor/interpreter/compiler

**Orthogonality Principles** (05_practices.scala):
- Minimize overlapping capabilities between primitives
- Derive complex operators from minimal orthogonal primitives
- Balance expressiveness with minimalism

### Exercise Pattern

Most files follow this structure:
- Type aliases or enums for domain models (initially incomplete: `type Foo`)
- Companion objects with constructor methods
- Operators as methods on the model types
- Example exercises in domain contexts (e-commerce, banking, spreadsheets, etc.)

Solutions are left as `???` placeholders for learners to implement.

## Scala 3 Features in Use

- **Enums**: For sum types and declarative encodings
- **Extension methods**: Via `extension` syntax
- **Given/using**: For context parameters (e.g., `Numeric[A]`)
- **Significant indentation**: Enabled via `-indent` and `-rewrite` compiler flags
- **Top-level definitions**: Objects and functions at package level
- **Union types**: In some advanced exercises

## Common Development Patterns

### When Adding New Exercises

1. Define the domain model using `enum` or `case class`
2. Add primitive constructors in companion object
3. Add operators as methods on the model
4. Ensure operators are composable (accept and return same/compatible types)
5. Follow the executable vs declarative encoding pattern appropriate to the lesson

### When Working with Operators

- **Unary operators**: Transform a single value (e.g., `def buffered: IStream`)
- **Binary operators**: Combine two values (e.g., `def ++(that: IStream): IStream`)
- Composable operators should return types compatible with their inputs
- Use symbolic names (`+`, `&&`, `||`) when they have clear, conventional meanings

### Type Safety Patterns

- Use type parameters to track static properties: `CalculatedValue[A]`
- Use variance annotations appropriately: `+A` for covariance
- Use context bounds for numeric operations: `[A: Numeric]`
- Prefer smart constructors in companion objects over raw case class constructors

## Testing

The project uses ZIO Test framework (configured in build.sbt):
```scala
testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework"))
```

While the workshop files contain exercises rather than tests, any test files should use ZIO Test syntax.

## Important Notes

- This is a **workshop/educational repository**—most code is intentionally incomplete (`???`)
- Do not "solve" exercises unless explicitly asked
- Maintain the pedagogical structure when adding new content
- Follow the progression: ADTs → Operators → Domains → Encodings → Practices → Typed → Patterns
- Keep examples domain-focused (e-commerce, banking, spreadsheets, etc.) rather than abstract

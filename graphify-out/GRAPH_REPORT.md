# Graph Report - Ecommerce  (2026-08-29)

## Corpus Check
- Corpus is ~3,037 words - fits in a single context window. You may not need a graph.

## Summary
- 35 nodes · 25 edges · 19 communities (17 shown, 2 thin omitted)
- Extraction: 100% EXTRACTED · 0% INFERRED · 0% AMBIGUOUS
- Token cost: 0 input · 0 output

## Community Hubs (Navigation)
- Main UI and Compose Theme
- Gradle Wrapper Script
- Android Instrumented Tests
- Unit Tests

## God Nodes (most connected - your core abstractions)
1. `Greeting()` - 4 edges
2. `EcommerceTheme()` - 4 edges
3. `MainActivity` - 3 edges
4. `GreetingPreview()` - 3 edges
5. `ExampleInstrumentedTest` - 2 edges
6. `ExampleUnitTest` - 2 edges

## Surprising Connections (you probably didn't know these)
- `GreetingPreview()` --calls--> `EcommerceTheme()`  [EXTRACTED]
  app/src/main/java/com/ecommerce/MainActivity.kt → app/src/main/java/com/ecommerce/ui/theme/Theme.kt

## Import Cycles
- None detected.

## Communities (19 total, 2 thin omitted)

### Community 0 - "Main UI and Compose Theme"
Cohesion: 0.36
Nodes (7): Greeting(), GreetingPreview(), MainActivity, EcommerceTheme(), Bundle, ComponentActivity, Modifier

### Community 1 - "Gradle Wrapper Script"
Cohesion: 0.83
Nodes (3): gradlew script, die(), warn()

## Knowledge Gaps
- **2 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Not enough signal to generate questions. This usually means the corpus has no AMBIGUOUS edges, no bridge nodes, no INFERRED relationships, and all communities are tightly cohesive. Add more files or run with --mode deep to extract richer edges._
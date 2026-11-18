# Taste
- Communicates in casual Indonesian and expects replies/summaries in Indonesian. Confidence: 0.7
- Asks for the reasoning behind a fix, not just the fix — e.g. wants the distinction explained ("cleaning warnings isn't only deleting"). Explain the taxonomy/nuance (deleted vs. fixed vs. suppressed vs. deliberately left alone) alongside the change. Confidence: 0.65
- Strongly prefers removing dead code: unused classes, methods, variables, layouts, drawables, duplicate adapters, and leftover template files should be deleted rather than left in place. Confidence: 0.8
- Wants no unused imports; expects code to be tidied with Reformat Code and Optimize Imports so it reads cleanly. Confidence: 0.75
- Treats lint / Android Studio Inspect Code warnings as a quality bar to drive to (near) zero, and expects warnings to be resolved idiomatically: fix real issues (hardcoded strings → string resources, `Uri.parse` → `.toUri()`, obsolete SDK checks), suppress only genuine by-design false positives (`tools:ignore`), and never "fix" by upgrades that break the build (e.g. deps pulling an unsupported kotlin-stdlib). Confidence: 0.75
- Endorses idiomatic Kotlin coroutines practice: Room DAO write methods as `suspend` functions, called from `viewModelScope`/`lifecycleScope` instead of `GlobalScope` or ad-hoc executors. Confidence: 0.65

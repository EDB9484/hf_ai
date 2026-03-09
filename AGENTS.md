# AGENTS.md

## Purpose
This document defines the default rules Codex should follow when generating or modifying code in the `hf_ai` project.

## Project Stack
- Java 21
- Spring Boot
- Gradle
- Thymeleaf
- MyBatis
- PostgreSQL

## Code Generation Rules
- Treat planning documents as implementation references.
- Do not expose documentation-only elements in service screens (for example: IA tables, annotation numbers, Description panels).
- Reconstruct screens for real operational usage (menu, form, table, pagination) instead of replicating planning artifacts.
- In early stages, avoid over-implementing business logic; start with the smallest runnable and verifiable unit.
- For requests asking for an interactive version, include end-to-end server-template behavior:
  - Search parameter binding
  - Filtering
  - Pagination
  - Empty-result handling
- Keep sample data in the service layer and structure code so it can be replaced with MyBatis + PostgreSQL later.

## Architecture and Package Rules
- Keep the package structure below:
  - `com.boauto.backoffice.admin`
  - `com.boauto.backoffice.agent`
  - `com.boauto.backoffice.templates`
  - `com.boauto.backoffice.schema`
- Split screen features by domain package under `admin`.
  - Example: `admin.member`
- Controllers should focus on request/response orchestration; filtering/transformation logic belongs in services.

## Thymeleaf View Rules
- Build templates as operational UI, not as direct planning-document copies.
- Preserve form state with `th:value`, `th:checked`, and `th:selected`.
- Use GET-based search URLs so query state is reproducible and shareable.
- Include minimum responsive behavior for desktop and mobile layouts.

## Quality Rules
- Run `./gradlew test` as the minimum verification after changes.
- Do not mark work complete if build/test is failing.
- Never revert or discard user changes unless explicitly requested.

## Workflows
### 1) Baseline Screen Workflow
1. Create package structure by domain under `com.boauto.backoffice.admin`.
2. Implement controller for route binding and request/response orchestration.
3. Implement service with in-memory sample data and filtering/pagination logic.
4. Implement Thymeleaf template with menu, search form, result table, and pagination.
5. Preserve query/form state and render empty-result message when needed.
6. Verify with `./gradlew test`.

### 2) Interactive Screen Update Workflow
1. Bind all search parameters as GET query parameters.
2. Move transformation/filtering logic to service layer.
3. Keep controller thin and focused on model composition.
4. Confirm pagination edge cases:
   - first page
   - last page
   - empty results
5. Verify with `./gradlew test`.

### 3) DB Migration-Ready Workflow
1. Keep service interfaces stable while using sample data implementations.
2. Separate persistence concerns so MyBatis mapper integration is drop-in.
3. Avoid embedding SQL/data access logic in controllers/templates.
4. Verify existing screen behavior remains unchanged after refactoring.
5. Verify with `./gradlew test`.

## Commands
- Run tests: `./gradlew test`
- Run app locally: `./gradlew bootRun`
- Clean + build: `./gradlew clean build`
- Run a specific test class: `./gradlew test --tests "com.boauto.backoffice.SomeTest"`
- Run a specific test method: `./gradlew test --tests "com.boauto.backoffice.SomeTest.someMethod"`
- Stop Gradle daemon when needed: `./gradlew --stop`

## Command Usage Rules
- Execute at least `./gradlew test` after code changes.
- Prefer focused test runs while iterating, then run full tests before completion.
- Use `clean build` for release-style validation or when build cache issues are suspected.
- If a command fails, fix the issue and rerun the relevant verification command.

## Future Extension Rules
- Implement features incrementally after baseline screen completion (for example: Excel download, detail popup, notices).
- Before DB integration, guarantee feature flow with dummy service data; keep interfaces stable to reduce migration cost during MyBatis/PostgreSQL integration.

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

## Future Extension Rules
- Implement features incrementally after baseline screen completion (for example: Excel download, detail popup, notices).
- Before DB integration, guarantee feature flow with dummy service data; keep interfaces stable to reduce migration cost during MyBatis/PostgreSQL integration.

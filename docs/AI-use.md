# AI Assistance Log

## Branch: feature/json-data

- Date: November 6, 2026
- AI system: ChatGPT
- Model: GPT-6.1
- Prompt summary: Help configure Mockaroo to generate 100 Formula 1
  parts and establish the product and category JSON contracts.
- Assistance: Suggested field types, custom-list values, category
  definitions, and validation checks.
- Corrections and verification: Corrected null custom-list values,
  combined list entries, and mismatched categories. The final file
  was checked for 100 records, unique IDs, required fields, valid
  dates, positive prices, and matching categories.
- Data source: Products generated using Mockaroo. Category
  definitions were entered manually with AI assistance.

  ## Branch: feature/json-dtos

- Date: November 6, 2026
- AI system: ChatGPT
- Model: GPT-6.1
- Prompt summary: Help implement the required DTO records,
  mapping between ProductDto and Product, and mapping tests.
- Assistance: Generated DTO records, ProductMapper
- Verification: Added the code and ran the complete Maven test
  suite. All 30 tests passed. Mapping tests verified every product
  field and date conversion in both directions.

  ## Branch: feature/json-loading

- Date: November 6, 2026
- AI system: ChatGPT
- Model: GPT-6.1
- Prompt summary: Add Jackson JSON loading, validate products and
  categories, and replace hard-coded startup inventory.
- Assistance: Generated FileService and updates to InventoryManager,
  StoreFront, and MilestoneApplication.
- Verification: Maven clean test passed. Ran the application and
  confirmed 100 products loaded with a starting stock of 10 each.
- Design decision: Stock starts at 10 because the required product
  JSON contract does not contain a quantity field.
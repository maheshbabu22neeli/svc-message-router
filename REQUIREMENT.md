# Senior Software Engineer Take-Home Exercise

## SMS Message Router

Build a simple SMS routing service that handles opt-outs and carrier selection.

---

## Requirements

### Core Features
1. **Send Message API** - `POST /messages`
2. **Get Message Status** - `GET /messages/{id}`
3. **Opt-out Management** - `POST /optout/{phoneNumber}`
4. **Carrier Routing** - Route by phone number prefix
5. **Message Status** - Track PENDING → SENT → DELIVERED/BLOCKED

### Message Format
```json
{
   "destination_number": "+61491570156",
   "content": "Hello world",
   "format": "SMS"
}
```

### Routing Rules
- AU (+61): "Telstra" or "Optus" (alternate)
- NZ (+64): "Spark"
- Other: "Global"

### Behavior
- Block messages to opted-out numbers (consider status implications)
- Return message ID and status on send
- Simple in-memory storage (no database needed)
- Phone number validation (AU/NZ formats differ - make reasonable assumptions)

---

## Deliverables

1. **Working Spring Boot app** with the 3 endpoints (use https://start.spring.io/ with Maven or Gradle)
2. **unit tests** covering key scenarios
3. **README** with setup, API examples and any assumptions made

---

## Test Cases
- Send to valid AU number → routes to Telstra/Optus
- Send to opted-out number → blocked (check via status endpoint)
- Send to NZ number → routes to Spark

---

## Evaluation
- Clean, readable code
- Proper error handling
- Understanding of requirements
- Testing approach

Submit as Git(Hub|Lab) repo or zip file. Questions? Just ask!


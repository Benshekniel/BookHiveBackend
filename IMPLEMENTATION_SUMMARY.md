# Organization Dashboard Backend Implementation

## Overview
This implementation adds backend support for **Top Donors** and **Active Competitions** sections in the Organization Dashboard.

## New Endpoints

### 1. Top Donors
**Endpoint:** `GET /api/organization-dashboard/top-donors/{organizationId}`

**Description:** Fetches top donors for a specific organization based on total books donated.

**Response:** List of TopDonorDTO
```json
[
  {
    "donorId": 123,
    "donorName": "John Doe",
    "name": "John Doe",
    "totalBooks": 150,
    "quantity": 150,
    "donationCount": 5,
    "lastDonationDate": "2025-10-15T14:30:00",
    "lastDonation": "2025-10-15T14:30:00"
  }
]
```

**Features:**
- Orders donors by total books donated (descending)
- Only includes donations with status 'RECEIVED' or 'APPROVED'
- Joins with AllUsers table to get donor names
- Groups by donor ID and name
- Includes both primary fields and aliases for frontend compatibility

---

### 2. Active Competitions
**Endpoint:** `GET /api/organization-dashboard/active-competitions`

**Description:** Fetches all currently active competitions (not paused).

**Response:** List of ActiveCompetitionDTO
```json
[
  {
    "competitionId": "COMP-001",
    "activeStatus": true,
    "pauseStatus": false,
    "title": "Reading Challenge 2025",
    "theme": "Summer Reading",
    "description": "Complete description...",
    "prizeTrustScore": 100,
    "entryTrustScore": 10,
    "startDateTime": "2025-10-01T00:00:00",
    "endDateTime": "2025-11-30T23:59:59",
    "votingStartDateTime": "2025-12-01T00:00:00",
    "votingEndDateTime": "2025-12-07T23:59:59",
    "maxParticipants": 100,
    "currentParticipants": 45,
    "bannerImage": "https://...",
    "createdAt": "2025-09-15T10:00:00",
    "activatedAt": "2025-10-01T00:00:00"
  }
]
```

**Features:**
- Only returns competitions where `activeStatus = true` AND `pauseStatus = false`
- Orders by creation date (newest first)
- Includes all relevant competition details for frontend display

---

## Files Created/Modified

### New DTOs:
1. **TopDonorDTO.java** (`model/dto/organization/`)
   - Includes both primary fields and aliases for frontend compatibility
   - Constructor for JPA query projection

2. **ActiveCompetitionDTO.java** (`model/dto/organization/`)
   - Contains all competition fields needed for dashboard display

### Modified Files:

1. **DonationRepository.java** (`model/repo/organization/`)
   - Added `findTopDonorsByOrganization()` query method
   - Uses JPA query with JOIN to AllUsers table
   - Groups and aggregates donation data

2. **CompetitionRepo.java** (`model/repo/`)
   - Added `findActiveCompetitions()` query method
   - Filters by active and not paused status

3. **Competitions.java** (`model/entity/`)
   - Added `getPauseStatus()` getter method
   - Added `setPauseStatus()` setter method

4. **OrganizationDashboardService.java** (`service/organization/`)
   - Added `getTopDonors()` method signature
   - Added `getActiveCompetitions()` method signature

5. **OrganizationDashboardServiceImpl.java** (`service/organization/impl/`)
   - Implemented `getTopDonors()` method
   - Implemented `getActiveCompetitions()` method
   - Added `mapToActiveCompetitionDTO()` helper method
   - Added CompetitionRepo dependency injection

6. **OrganizationDashboardController.java** (`controller/organization/`)
   - Added `/top-donors/{organizationId}` endpoint
   - Added `/active-competitions` endpoint
   - Added proper error handling

---

## Database Schema Requirements

### Donations Table
Required columns:
- `id` (Primary Key)
- `organization_id`
- `donor_id`
- `quantity`
- `status`
- `date_donated`

### Competitions Table
Required columns:
- `competition_id` (Primary Key)
- `active_status` (boolean)
- `pause_status` (boolean)
- `title`
- `theme`
- `description`
- `prize_trust_score`
- `entry_trust_score`
- `start_date_time`
- `end_date_time`
- `voting_start_date_time`
- `voting_end_date_time`
- `max_participants`
- `current_participants`
- `banner_image`
- `created_at`
- `activated_at`

### All_Users Table
Required columns:
- `user_id` (Primary Key)
- `name`
- `email`

---

## Testing the Implementation

### 1. Test Top Donors Endpoint
```bash
curl -X GET http://localhost:9090/api/organization-dashboard/top-donors/1
```

### 2. Test Active Competitions Endpoint
```bash
curl -X GET http://localhost:9090/api/organization-dashboard/active-competitions
```

---

## Frontend Integration

The frontend (`Dashboard.jsx`) already has the API calls implemented:
- `getTopDonors(orgId)` 
- `getActiveCompetitions()`

These will now receive real data from the backend instead of empty arrays.

---

## Error Handling

All endpoints include:
- Organization existence validation (for top donors)
- 404 response for non-existent organizations
- 500 response for server errors
- Detailed error logging for debugging

---

## Notes

1. **Top Donors Query:** Only includes donations with status 'RECEIVED' or 'APPROVED' to ensure data accuracy.

2. **Active Competitions Query:** Filters out paused competitions to show only truly active ones.

3. **Field Aliases:** TopDonorDTO includes both `donorName`/`name` and `totalBooks`/`quantity` aliases to support different frontend naming conventions.

4. **Join Optimization:** The top donors query uses LEFT JOIN to handle cases where donor user data might be missing.

5. **Logging:** Both service methods include console logging for debugging purposes.

---

## Next Steps

1. Restart the Spring Boot backend
2. Test the endpoints using curl or Postman
3. Verify the frontend receives and displays the data correctly
4. Add pagination if needed (currently returns all results)
5. Consider adding caching for frequently accessed data

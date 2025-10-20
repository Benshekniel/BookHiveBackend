# Testing Guide for Organization Dashboard Backend

## Prerequisites
1. Spring Boot backend running on `http://localhost:9090`
2. Database with sample data (donations and competitions)
3. Valid organization ID for testing (e.g., 1, 2, 3, etc.)

---

## Test 1: Top Donors Endpoint

### Request
```bash
GET http://localhost:9090/api/organization-dashboard/top-donors/1
```

### Using curl:
```bash
curl -X GET http://localhost:9090/api/organization-dashboard/top-donors/1
```

### Expected Response (Success):
```json
[
  {
    "id": null,
    "donorId": 5,
    "donorName": "John Doe",
    "name": "John Doe",
    "totalBooks": 150,
    "quantity": 150,
    "donationCount": 5,
    "lastDonationDate": "2025-10-15T14:30:00",
    "lastDonation": "2025-10-15T14:30:00"
  },
  {
    "id": null,
    "donorId": 12,
    "donorName": "Jane Smith",
    "name": "Jane Smith",
    "totalBooks": 100,
    "quantity": 100,
    "donationCount": 3,
    "lastDonationDate": "2025-10-10T09:15:00",
    "lastDonation": "2025-10-10T09:15:00"
  }
]
```

### Expected Response (No Donors):
```json
[]
```

### Expected Response (Organization Not Found):
```json
{
  "message": "Organization not found with orgId: 999"
}
```
**Status Code:** 404

---

## Test 2: Active Competitions Endpoint

### Request
```bash
GET http://localhost:9090/api/organization-dashboard/active-competitions
```

### Using curl:
```bash
curl -X GET http://localhost:9090/api/organization-dashboard/active-competitions
```

### Expected Response (Success):
```json
[
  {
    "competitionId": "COMP-2025-001",
    "activeStatus": true,
    "pauseStatus": false,
    "title": "Reading Challenge 2025",
    "theme": "Summer Reading",
    "description": "Join our summer reading challenge and win amazing prizes!",
    "prizeTrustScore": 100,
    "entryTrustScore": 10,
    "startDateTime": "2025-10-01T00:00:00",
    "endDateTime": "2025-11-30T23:59:59",
    "votingStartDateTime": "2025-12-01T00:00:00",
    "votingEndDateTime": "2025-12-07T23:59:59",
    "maxParticipants": 100,
    "currentParticipants": 45,
    "bannerImage": "https://example.com/banner.jpg",
    "createdAt": "2025-09-15T10:00:00",
    "activatedAt": "2025-10-01T00:00:00"
  }
]
```

### Expected Response (No Active Competitions):
```json
[]
```

---

## Test 3: Integration Test from Frontend

### Open Browser Developer Console (F12)
1. Navigate to Organization Dashboard page
2. Open Network tab
3. Refresh the page
4. Look for these API calls:
   - `/api/organization-dashboard/top-donors/{orgId}`
   - `/api/organization-dashboard/active-competitions`

### Check Console Logs
You should see:
```
API Request: GET http://localhost:9090/api/organization-dashboard/top-donors/1
API Response: /organization-dashboard/top-donors/1 - Success

API Request: GET http://localhost:9090/api/organization-dashboard/active-competitions
API Response: /organization-dashboard/active-competitions - Success
```

---

## Backend Console Logs

When endpoints are called, you should see in IntelliJ console:
```
Found 5 top donors for orgId: 1
Found 3 active competitions
```

---

## Common Issues and Solutions

### Issue 1: Empty Array Response for Top Donors
**Possible Causes:**
- No donations in database for that organization
- No donations with status 'RECEIVED' or 'APPROVED'
- Donor IDs don't match any users in All_Users table

**Solution:**
Check database:
```sql
SELECT * FROM donations WHERE organization_id = 1 AND status IN ('RECEIVED', 'APPROVED');
SELECT * FROM All_Users;
```

---

### Issue 2: Empty Array Response for Active Competitions
**Possible Causes:**
- No competitions in database
- All competitions have `active_status = false`
- All competitions have `pause_status = true`

**Solution:**
Check database:
```sql
SELECT * FROM competitions WHERE active_status = true AND pause_status = false;
```

---

### Issue 3: 500 Internal Server Error
**Possible Causes:**
- Database connection issue
- Missing table/column in database
- Join condition mismatch

**Solution:**
1. Check backend console for detailed error message
2. Verify all required tables exist
3. Verify column names match entity definitions

---

## Sample Database Seed Data

### Insert Sample Donors (if needed):
```sql
-- Insert sample users
INSERT INTO All_Users (name, email, password, role, status) 
VALUES 
  ('John Doe', 'john@example.com', 'password123', 'DONOR', 'active'),
  ('Jane Smith', 'jane@example.com', 'password123', 'DONOR', 'active'),
  ('Bob Wilson', 'bob@example.com', 'password123', 'DONOR', 'active');

-- Insert sample donations
INSERT INTO donations (organization_id, donor_id, book_title, quantity, status, date_donated, priority, created_at)
VALUES
  (1, 1, 'Java Programming', 50, 'RECEIVED', NOW(), 'High', NOW()),
  (1, 1, 'Python Basics', 100, 'RECEIVED', NOW() - INTERVAL '5 days', 'Medium', NOW()),
  (1, 2, 'Web Development', 75, 'RECEIVED', NOW() - INTERVAL '10 days', 'High', NOW()),
  (1, 3, 'Database Design', 25, 'APPROVED', NOW() - INTERVAL '15 days', 'Low', NOW());
```

### Insert Sample Competitions (if needed):
```sql
INSERT INTO competitions (
  competition_id, active_status, pause_status, created_by, title, theme, 
  description, prize_trust_score, entry_trust_score, start_date_time, 
  end_date_time, voting_start_date_time, voting_end_date_time, 
  max_participants, current_participants, banner_image, created_at
)
VALUES (
  'COMP-2025-001', 
  true, 
  false, 
  'moderator@example.com', 
  'Reading Challenge 2025',
  'Summer Reading',
  'Join our summer reading challenge and win amazing prizes!',
  100,
  10,
  '2025-10-01 00:00:00',
  '2025-11-30 23:59:59',
  '2025-12-01 00:00:00',
  '2025-12-07 23:59:59',
  100,
  45,
  'https://example.com/banner.jpg',
  NOW()
);
```

---

## Success Criteria

✅ Top Donors endpoint returns sorted list by total books  
✅ Active Competitions endpoint returns only active, non-paused competitions  
✅ Frontend displays Top Donors leaderboard with proper ranking  
✅ Frontend displays Active Competitions cards with all details  
✅ No 404 or 500 errors in browser console  
✅ Backend logs show successful data fetching  

---

## Next Steps After Testing

1. ✅ Verify data displays correctly in frontend UI
2. ✅ Test with different organization IDs
3. ✅ Test edge cases (no data, invalid IDs)
4. ⚠️ Consider adding pagination for large datasets
5. ⚠️ Consider adding caching for better performance
6. ⚠️ Add unit tests for service methods
7. ⚠️ Add integration tests for controller endpoints

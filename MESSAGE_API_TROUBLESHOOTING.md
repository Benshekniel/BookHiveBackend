# Message API Internal Server Error - Troubleshooting Guide

## Issues Fixed

### 1. ✅ Method Name Mismatch
**Problem**: `dto.setRead()` vs `dto.setIsRead()` causing compilation/runtime errors
**Solution**: Fixed to use `dto.setRead()` to match the DTO definition

### 2. ✅ Missing Field Mapping
**Problem**: `sentAt` field was not being mapped in DTO conversion
**Solution**: Added `dto.setSentAt(message.getSentAt())` in conversion method

### 3. ✅ Null Pointer Exceptions
**Problem**: Missing null checks for user IDs and message fields
**Solution**: Added comprehensive null validation throughout the service

### 4. ✅ Better Error Handling
**Problem**: Generic exceptions without proper logging
**Solution**: Added detailed error logging and specific error messages

### 5. ✅ Database Table Structure
**Problem**: Messages table might not exist or have wrong structure
**Solution**: Created SQL script to ensure proper table creation

## Testing Steps

### 1. Test Basic API Connectivity
```bash
# Test if the API is responding
curl http://localhost:9090/api/test/ping

# Expected response:
{
  "status": "OK",
  "message": "Message API is working",
  "timestamp": "2024-01-XX..."
}
```

### 2. Test Database Connection
```bash
# Check if messages table exists
curl http://localhost:9090/api/test/health

# If this fails, run the SQL script:
# mysql -u your_username -p your_database < create_messages_table.sql
```

### 3. Test Message Sending
```bash
# Test sending a message
curl -X POST http://localhost:9090/api/messages/send \
  -H "Content-Type: application/json" \
  -d '{
    "senderId": 1,
    "receiverId": 2,
    "content": "Test message"
  }'
```

### 4. Test Getting Conversations
```bash
# Test getting conversation
curl http://localhost:9090/api/messages/conversation/1/2
```

## Common Error Scenarios

### Error 1: Table 'messages' doesn't exist
**Solution**: Run the SQL script to create the table
```sql
-- Run this in your database
SOURCE create_messages_table.sql;
```

### Error 2: Column name mismatch
**Symptoms**: SQL errors about unknown columns
**Solution**: Verify table structure matches entity definition
```sql
DESCRIBE messages;
```

### Error 3: User not found errors
**Symptoms**: "Unknown Sender/Receiver" in responses
**Solution**: Ensure users exist in the `allusers` table
```sql
SELECT * FROM allusers WHERE id IN (1, 2);
```

### Error 4: Agent not found for broadcasting
**Symptoms**: "No agents found for hub" message
**Solution**: Ensure agents exist for the hub
```sql
SELECT * FROM agent WHERE hub_id = 1;
```

## Backend Logs to Check

### 1. Application Startup
Look for these messages in the console:
```
Started BookHiveApplication in X.XXX seconds
```

### 2. Message API Calls
Look for these debug messages:
```
Creating message - Sender: X, Receiver: Y
Message saved with ID: Z
Getting conversation between X and Y
Found N messages in conversation
```

### 3. Error Messages
Look for these error patterns:
```
Error in sendMessage: [error details]
Error in getConversation: [error details]
Failed to send message: [error details]
```

## Frontend Testing

### 1. Check Browser Console
Open browser dev tools and look for:
- Network errors (404, 500, CORS)
- JavaScript errors
- API response data

### 2. Test API Endpoints Directly
Use browser or Postman to test:
- `GET http://localhost:9090/api/test/ping`
- `GET http://localhost:9090/api/agents/hub/1`
- `POST http://localhost:9090/api/messages/send`

### 3. Check Network Tab
Verify:
- Requests are being sent to correct URLs
- Response status codes
- Response data format

## Database Setup

### Required Tables
1. **messages** - Main message storage
2. **allusers** - User information
3. **agent** - Agent information
4. **hub** - Hub information

### Sample Data for Testing
```sql
-- Insert test users
INSERT INTO allusers (id, name, email) VALUES 
(1, 'Hub Manager', 'manager@test.com'),
(2, 'Agent One', 'agent1@test.com'),
(3, 'Agent Two', 'agent2@test.com');

-- Insert test hub
INSERT INTO hub (id, name, location) VALUES 
(1, 'Test Hub', 'Test Location');

-- Insert test agents
INSERT INTO agent (agent_id, hub_id, first_name, last_name, availability_status) VALUES 
(2, 1, 'Agent', 'One', 'AVAILABLE'),
(3, 1, 'Agent', 'Two', 'AVAILABLE');
```

## Quick Fix Checklist

- [ ] Backend server is running on port 9090
- [ ] Database is connected and accessible
- [ ] Messages table exists with correct structure
- [ ] Test users and agents exist in database
- [ ] CORS is enabled for frontend origin
- [ ] No compilation errors in backend
- [ ] Frontend is making requests to correct URLs

## If Still Getting Errors

1. **Check the exact error message** in backend logs
2. **Verify database connection** and table structure
3. **Test with simple curl commands** before using frontend
4. **Check if other API endpoints work** (like agents API)
5. **Verify data exists** in required tables

## Contact Points for Further Help

- Check backend console logs for detailed error messages
- Use the test endpoints to isolate the issue
- Verify database schema matches entity definitions
- Test API endpoints individually before integration testing
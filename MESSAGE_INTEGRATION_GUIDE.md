# Message System Integration Guide

## Overview
This guide explains how the messaging system has been configured to connect the frontend with the backend for real-time communication between hub managers and agents.

## Backend Components

### 1. Message Entity (`model/entity/Message.java`)
- Stores message data with sender, receiver, content, timestamps
- Includes read status tracking
- Auto-generates timestamps on creation and updates

### 2. Message Repository (`model/repo/Hubmanager/MessageRepository.java`)
- Provides database operations for messages
- Includes methods for conversations, unread counts, and filtering
- Supports complex queries for conversation management

### 3. Message Service (`service/Hubmanager/impl/MessageService.java`)
- Business logic for message operations
- Handles sending, receiving, and managing messages
- Includes hub-specific features like broadcasting to all agents
- Provides conversation summaries and statistics

### 4. Message Controller (`controller/MessageController.java`)
- REST API endpoints for message operations
- Handles HTTP requests and responses
- Includes CORS configuration for frontend integration

## API Endpoints

### Basic Message Operations
- `POST /api/messages/send` - Send a new message
- `GET /api/messages/conversation/{user1Id}/{user2Id}` - Get conversation between two users
- `GET /api/messages/user/{userId}` - Get all messages for a user
- `PUT /api/messages/mark-read/{messageId}` - Mark message as read
- `DELETE /api/messages/{messageId}` - Delete a message

### Hub Manager Specific
- `GET /api/messages/hub/{hubManagerId}/conversations` - Get all conversations for hub manager
- `GET /api/messages/hub/{hubManagerId}/summary` - Get conversation statistics
- `POST /api/messages/hub/{hubId}/broadcast` - Broadcast message to all agents in hub
- `GET /api/messages/recent/{userId}` - Get recent messages with limit

### Utility Endpoints
- `GET /api/messages/unread-count/{userId}` - Get unread message count
- `PUT /api/messages/mark-conversation-read/{receiverId}/{senderId}` - Mark entire conversation as read

## Frontend Components

### 1. API Service (`services/apiService.js`)
- Added `messageApi` object with all message-related API calls
- Handles HTTP requests to backend endpoints
- Includes error handling and response processing

### 2. Enhanced Messages Component (`pages/hubManager/MessagesEnhanced.jsx`)
- Integrates with real backend APIs
- Supports both real and demo conversations
- Includes broadcast functionality for hub managers
- Real-time message sending and receiving
- Error handling and loading states

## Key Features

### 1. Real-time Messaging
- Send messages between hub managers and agents
- Automatic message persistence in database
- Read status tracking

### 2. Conversation Management
- View all conversations for a hub manager
- Search and filter conversations
- Unread message indicators

### 3. Broadcasting
- Hub managers can broadcast messages to all agents
- Useful for announcements and urgent communications

### 4. Fallback Support
- If backend is unavailable, frontend shows demo data
- Graceful error handling with user feedback

## Usage Instructions

### For Hub Managers:
1. View all conversations in the left sidebar
2. Click on any conversation to view messages
3. Send messages using the input field at the bottom
4. Use the broadcast button to send messages to all agents
5. Unread messages are highlighted with badges

### For Integration:
1. Ensure backend server is running on `http://localhost:9090`
2. Replace `MessagesEnhanced.jsx` with the original `Messages.jsx` or update routing
3. Update `hubId` and `hubManagerId` to come from authentication context
4. Configure proper user authentication and authorization

## Database Schema

The Message entity requires the following database table:

```sql
CREATE TABLE messages (
    message_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sender_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    sent_at DATETIME,
    created_at DATETIME,
    updated_at DATETIME,
    is_read BOOLEAN DEFAULT FALSE
);
```

## Configuration Notes

1. **CORS**: The controller includes `@CrossOrigin(origins = "*")` for development. In production, specify exact origins.

2. **Authentication**: The current implementation uses hardcoded user IDs. Integrate with your authentication system.

3. **Real-time Updates**: For real-time messaging, consider implementing WebSocket connections or Server-Sent Events.

4. **Error Handling**: The frontend includes comprehensive error handling with fallback to demo data.

5. **Performance**: For large message volumes, implement pagination in the message loading endpoints.

## Testing the Integration

1. Start the backend server
2. Use the frontend Messages component
3. Try sending messages between different users
4. Test the broadcast functionality
5. Verify message persistence by refreshing the page

## Future Enhancements

1. **WebSocket Integration**: For real-time message updates
2. **File Attachments**: Support for sending files and images
3. **Message Reactions**: Like, reply, and emoji reactions
4. **Push Notifications**: Browser notifications for new messages
5. **Message Search**: Full-text search across all messages
6. **Message Threading**: Reply to specific messages
7. **Typing Indicators**: Show when someone is typing
8. **Message Encryption**: End-to-end encryption for sensitive communications

## Troubleshooting

### Common Issues:
1. **CORS Errors**: Ensure backend CORS configuration allows frontend origin
2. **404 Errors**: Verify API endpoints are correctly mapped
3. **Database Errors**: Check database connection and table schema
4. **Authentication Issues**: Ensure user IDs are valid and exist in database

### Debug Steps:
1. Check browser console for JavaScript errors
2. Verify network requests in browser dev tools
3. Check backend logs for API errors
4. Test API endpoints directly using tools like Postman
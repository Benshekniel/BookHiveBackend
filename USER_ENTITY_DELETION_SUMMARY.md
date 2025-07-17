# User Entity Deletion Summary

## What Was Deleted
1. **User Entity** (`src/main/java/model/entity/User.java`)
2. **UserRepository** (`src/main/java/model/repo/UserRepository.java`)

## Affected Files (Require Manual Updates)

### Services with User Dependencies:
1. **AgentService** (`src/main/java/service/impl/AgentService.java`)
   - Uses User for agent creation and role management
   - Methods affected: `createAgent()`, `deleteAgent()`, `convertToResponseDto()`, `convertToPerformanceDto()`

2. **HubService** (`src/main/java/service/impl/HubService.java`)
   - Uses User for hub manager assignment
   - Methods affected: `assignManager()`, `deleteHub()`, `convertToHubManagerResponseDto()`

3. **MessageService** (`src/main/java/service/impl/MessageService.java`)
   - Uses User for message sender/receiver details
   - Methods affected: `createMessage()`, `convertToResponseDto()`

4. **DeliveryService** (`src/main/java/service/impl/DeliveryService.java`)
   - Uses User for customer information
   - Methods affected: `convertToResponseDto()`

## Key Differences Between User and AllUsers Entities

### User Entity (Deleted):
- Field: `Long userId`
- Field: `String name`
- Field: `String email`
- Field: `UserRole role` (enum)
- Field: `String phoneNumber`
- Field: `String address`
- Field: `LocalDateTime createdAt`

### AllUsers Entity (Available):
- Field: `int user_id`
- Field: `String name`
- Field: `String email`
- Field: `String password`
- Field: `String phoneNumber`
- Field: `String address`
- Field: `LocalDateTime createdAt`
- Field: `String role` (string, not enum)

## Required Actions

### 1. Update Service Dependencies
Replace UserRepository references with AllUsersRepo in affected services.

### 2. Field Mapping Changes
- Change `Long userId` to `int user_id`
- Change `User.UserRole` enum to `String role`
- Update getter/setter method calls (`getUserId()` vs `getUser_id()`)

### 3. Repository Method Updates
The following repository methods need to be recreated in AllUsersRepo if needed:
- `findByEmail(String email)`
- Role-based queries
- User lookup methods

### 4. Role Management
Update role assignments from enum values to string values:
- `User.UserRole.DELIVERY_AGENT` → `"DELIVERY_AGENT"`
- `User.UserRole.HUB_MANAGER` → `"HUB_MANAGER"`
- `User.UserRole.USER` → `"USER"`

## Next Steps
1. Update all service classes to use AllUsers instead of User
2. Update field mappings and method calls
3. Recreate necessary repository methods in AllUsersRepo
4. Test all functionality after migration
5. Update any remaining references in controllers or other components

## Files Currently Commented Out
The following import statements have been commented out to prevent compilation errors:
- All `import model.entity.User;` statements
- All `import model.repo.UserRepository;` statements

These services will need significant refactoring to work with AllUsers entity.

// Domain events for User aggregate

sealed class DomainEvent {
    data class UserCreated(val userId: String, val email: String, val name: String) : DomainEvent()
    data class UserDeleted(val userId: String) : DomainEvent()
}
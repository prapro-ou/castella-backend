sealed interface Destination {
    class DM(val user: User) : Destination
    class MailingList(val users: List<User>) : Destination
}

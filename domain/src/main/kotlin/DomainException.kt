sealed class DomainException(
    override val message: String,
    override val cause: Throwable? = null,
) : Exception() {
    // サーバー側の予期しない例外
    class SystemException(
        override val message: String,
        override val cause: Throwable?,
    ) : DomainException(message, cause)
}

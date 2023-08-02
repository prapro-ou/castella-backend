sealed class DomainException(
    override val message: String,
    override val cause: Throwable? = null,
) : Exception() {
    // リクエストの方法を間違えている時の例外
    class RequestValidationException(
        override val message: String,
    ) : DomainException(message)

    // サーバー側の予期しない例外
    class SystemException(
        override val message: String,
        override val cause: Throwable?,
    ) : DomainException(message, cause)
}

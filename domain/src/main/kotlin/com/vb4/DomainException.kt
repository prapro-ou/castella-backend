package com.vb4

sealed class DomainException(
    override val message: String,
    override val cause: Throwable? = null,
) : Exception() {
    // 指定したEmailが既にUserとして登録されていた場合の例外
    class AlreadyRegisteredException(
        override val message: String,
    ) : DomainException(message)

    // ログイン失敗
    class AuthException(
        override val message: String,
    ) : DomainException(message)

    // DBに指定されたカラムがない時の例外
    class NoSuchElementException(
        override val message: String,
    ) : DomainException(message)

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

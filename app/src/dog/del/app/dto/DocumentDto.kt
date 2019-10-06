package dog.del.app.dto

import dog.del.commons.formatLong
import dog.del.commons.lineCount
import dog.del.data.base.model.document.XdDocumentType
import dog.del.data.model.Document
import dog.del.data.model.DocumentType

data class FrontendDocumentDto(
    val slug: String,
    val type: DocumentTypeDto,
    val content: String?,
    val owner: UserDto,
    val created: String,
    // Actually get viewcount from SA eventually
    val viewCount: Int
) {
    // Disable for rendered markdown content
    val showLines = true
    val lines = content?.lineCount ?: 0
    // Use frontmatter data for rendered markdown content
    val description = content?.take(100) ?: "The sexiest pastebin and url-shortener ever"
    val title = "dogbin - $slug"
    companion object {
        fun fromDocument(document: Document<XdDocumentType, *>) = FrontendDocumentDto(
            document.slug,
            DocumentTypeDto.fromXdDocumentType(document.type),
            document.stringContent,
            UserDto.fromUser(document.owner),
            document.created.formatLong(),
            document.viewCount
        )
    }
}

data class CreateDocumentDto(
    val content: String,
    val slug: String? = null
)

data class CreateDocumentResponseDto(
    val isUrl: Boolean? = null,
    val key: String? = null,
    val message: String? = null
)

enum class DocumentTypeDto{
    URL, PASTE;

    companion object {
        fun fromXdDocumentType(documentType: XdDocumentType) = when(documentType) {
            XdDocumentType.PASTE -> PASTE
            XdDocumentType.URL -> URL
            else -> throw IllegalStateException()
        }
    }
}
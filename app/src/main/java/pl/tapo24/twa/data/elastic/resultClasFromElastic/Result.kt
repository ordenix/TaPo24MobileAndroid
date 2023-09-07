package pl.tapo24.twa.data.elastic.resultClasFromElastic

data class Result(
    val _meta: MetaX,
    val category: Category,
    val code: Code,
    val id: Id,
    val law: Law,
    val law_sub: LawSub,
    val max_speed: MaxSpeed,
    val min_speed: MinSpeed,
    val name: Name,
    val paragraph: Paragraph,
    val paragraph_sub: ParagraphSub,
    val path: Path,
    val points: Points,
    val sub_name: SubName,
    val tax: Tax,
    val text: Text,
    val visible: Visible
)
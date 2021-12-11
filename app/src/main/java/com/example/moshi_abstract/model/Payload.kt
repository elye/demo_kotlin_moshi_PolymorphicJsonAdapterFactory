package com.example.moshi_abstract.model

class Payload(val items: List<ListItem>)

sealed class ListItem(
    val type: Type,
    val alignment: Align,
    val weight: Float = 0f
)

class Text(
    val message: String,
    val textAlign: Align = Align.CENTER,
    val textFont: FontSize = FontSize.DEFAULT,
    alignment: Align = Align.CENTER,
    weight: Float = 0f
) : ListItem(Type.TEXT, alignment, weight)

class Button(
    val message: String,
    alignment: Align = Align.CENTER,
    weight: Float = 0f
) : ListItem(Type.BUTTON, alignment, weight)

class Image(
    val imageAlign: Align = Align.CENTER,
    alignment: Align = Align.CENTER,
    weight: Float = 0f
) : ListItem(Type.IMAGE, alignment, weight)

class Column(
    val listItems: List<ListItem>,
    alignment: Align = Align.CENTER,
    weight: Float = 0f
) : ListItem(Type.COLUMN, alignment, weight)

class Row(
    val listItems: List<ListItem>,
    alignment: Align = Align.CENTER,
    weight: Float = 0f
) : ListItem(Type.ROW, alignment, weight)

enum class Type {
    TEXT,
    BUTTON,
    IMAGE,
    COLUMN,
    ROW
}

enum class FontSize {
    TINY,
    SMALL,
    DEFAULT,
    BIG,
    HUGE
}

enum class Align {
    NONE,
    START,
    CENTER,
    END,
    FILL
}
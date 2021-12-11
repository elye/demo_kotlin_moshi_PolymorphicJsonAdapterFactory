package com.example.moshi_abstract.model

open class Base(val number: Int)
class SimpleChild(val item: String): Base(1)
class DynamicChild(val item: String, number: Int): Base(1)

abstract class BaseAbstract(val number: Int)
class SimpleAbstractChild(val item: String): BaseAbstract(1)
class DynamicAbstractChild(val item: String, number: Int): BaseAbstract(1)

interface BaseInterface {
    val number: Int
}
class SimpleInterfaceChild(val item: String): BaseInterface {
    override val number: Int = 1
}
class DynamicInterfaceChild(val item: String, override val number: Int): BaseInterface

sealed class BaseSealed(val number: Int)
class SimpleSealedChild(val item: String): BaseSealed(1)
class DynamicSealedChild(val item: String, number: Int): BaseSealed(1)

sealed interface BaseSealedInterface {
    val number: Int
}
class SimpleSealedInterface(val item: String): BaseSealedInterface {
    override val number: Int = 1
}
class DynamicSealedInterface(val item: String, override val number: Int): BaseSealedInterface

enum class BaseType {
    FirstType,
    SecondType,
    ThirdType
}

sealed interface BaseSealedTypeInterface {
    val type: BaseType
}

data class FirstChild (
    val strMsg: String
): BaseSealedTypeInterface {
    override val type: BaseType = BaseType.FirstType
}

data class SecondChild(
    val intMsg: Int,
    override val type: BaseType = BaseType.SecondType
) : BaseSealedTypeInterface

data class ThirdChild (
    val strMsg: ChildEnum
): BaseSealedTypeInterface {
    override val type: BaseType = BaseType.ThirdType
}

abstract class BaseTypeAbstract(val type: BaseType)

data class FirstAbstractChild (val strMsg: String):
    BaseTypeAbstract(BaseType.FirstType)

data class SecondAbstractChild(val intMsg: Int) :
    BaseTypeAbstract(BaseType.SecondType)

data class ThirdAbstractChild (val strMsg: ChildEnum):
    BaseTypeAbstract(BaseType.ThirdType)

enum class ChildEnum {
    JustOneEnum
}
package com.example.moshi_abstract

import com.example.moshi_abstract.model.*
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.lang.reflect.ParameterizedType

fun main() {
    simpleBase()
    simpleAbstract()
    simpleInterface()
    sealedBase()
    sealedInterfaceBase()

    sealedInterfaceTypeBase()
    abstractTypeBase()

    normalMoshiSerialization()
    abstractMoshiSerialization()
    singleAbstractMoshiSerialization()
}

private fun sealedInterfaceTypeBase() {
    val moshi: Moshi = Moshi.Builder()
        .add(
            PolymorphicJsonAdapterFactory.of(BaseSealedTypeInterface::class.java, "type")
                .withSubtype(FirstChild::class.java, BaseType.FirstType.name)
                .withSubtype(SecondChild::class.java, BaseType.SecondType.name)
                .withSubtype(ThirdChild::class.java, BaseType.ThirdType.name))
        .add(KotlinJsonAdapterFactory()).build()
    val adaptor: JsonAdapter<BaseSealedTypeInterface> = moshi.adapter(BaseSealedTypeInterface::class.java)

    // val base = object: BaseSealedTypeInterface { override val type: BaseType = BaseType.FirstType }

    val firstChild = FirstChild("Hi")
    val secondChild = SecondChild(1, BaseType.SecondType)
    val secondChildVariant = SecondChild(2, BaseType.FirstType)
    val thirdChild = ThirdChild(ChildEnum.JustOneEnum)

    val adaptorFirst: JsonAdapter<FirstChild> = moshi.adapter(FirstChild::class.java)
    println("firstChild ${adaptor.toJson(adaptor.fromJson(adaptor.toJson(firstChild)))}")
    println("firstChild adaptor ${adaptorFirst.toJson(adaptorFirst.fromJson(adaptorFirst.toJson(firstChild)))}")

    val adaptorSecond: JsonAdapter<SecondChild> = moshi.adapter(SecondChild::class.java)
    println("secondChild from Json ${adaptorSecond.fromJson("{\"type\":\"SecondType\",\"intMsg\":1}")}")
    println("secondChild from Object ${adaptorSecond.toJson(secondChild)}")

    println("secondChild adaptor from Json ${adaptor.fromJson("{\"type\":\"SecondType\",\"intMsg\":1}")}")
    println("secondChild adaptor from Object ${adaptor.toJson(secondChild)}")
    println("secondChildVariant adaptor from Object ${adaptor.toJson(secondChildVariant)}")
    // println("secondChild crash due to multiple type ${adaptor.toJson(adaptor.fromJson(adaptor.toJson(secondChild)))}")
    // println("secondChildVariant crash due to multiple type ${adaptor.toJson(adaptor.fromJson(adaptor.toJson(secondChildVariant)))}")
    println("secondChild adaptor don't crash due to no multiple type ${adaptorSecond.toJson(adaptorSecond.fromJson(adaptorSecond.toJson(secondChild)))}")
    println("secondChildVariant adaptor don't crash due to no multiple type ${adaptorSecond.toJson(adaptorSecond.fromJson(adaptorSecond.toJson(secondChildVariant)))}")

    val adaptorThird: JsonAdapter<ThirdChild> = moshi.adapter(ThirdChild::class.java)
    println("thirdChild ${adaptor.toJson(adaptor.fromJson(adaptor.toJson(thirdChild)))}")
    println("thirdChild adaptor ${adaptorThird.toJson(adaptorThird.fromJson(adaptorThird.toJson(thirdChild)))}")

    val baseItems = listOf(firstChild, secondChild, thirdChild)

    val type: ParameterizedType = Types.newParameterizedType(
        List::class.java, BaseSealedTypeInterface::class.java)
    val adaptorList: JsonAdapter<List<BaseSealedTypeInterface>> = moshi.adapter(type)

    println("List Children ${adaptorList.toJson(baseItems)}")

    println()
}

private fun abstractTypeBase() {
    val moshi: Moshi = Moshi.Builder()
        .add(
            PolymorphicJsonAdapterFactory.of(BaseTypeAbstract::class.java, "type")
                .withSubtype(FirstAbstractChild::class.java, BaseType.FirstType.name)
                .withSubtype(SecondAbstractChild::class.java, BaseType.SecondType.name)
                .withSubtype(ThirdAbstractChild::class.java, BaseType.ThirdType.name)
                .withDefaultValue(FirstAbstractChild("Unknown")))
        .add(KotlinJsonAdapterFactory()).build()
    val adaptor: JsonAdapter<BaseTypeAbstract> = moshi.adapter(BaseTypeAbstract::class.java)

    val base = object: BaseTypeAbstract(BaseType.FirstType) {  }

    // println("base ${adaptor.toJson(adaptor.fromJson(adaptor.toJson(base)))}")

    val adaptorFirst = moshi.adapter(FirstAbstractChild::class.java)
    val adaptorSecond = moshi.adapter(SecondAbstractChild::class.java)
    val adaptorThird = moshi.adapter(ThirdAbstractChild::class.java)

    val firstChild = FirstAbstractChild("Hi")
    val secondChild = SecondAbstractChild(1)
    val thirdChild = ThirdAbstractChild(ChildEnum.JustOneEnum)

    println("firstAbstractChild ${adaptor.toJson(adaptor.fromJson(adaptor.toJson(firstChild)))}")
    println("firstAbstractChild adaptor ${adaptorFirst.toJson(adaptorFirst.fromJson(adaptorFirst.toJson(firstChild)))}")
    println("secondAbstractChild ${adaptor.toJson(adaptor.fromJson(adaptor.toJson(secondChild)))}")
    println("secondAbstractChild adaptor ${adaptorSecond.toJson(adaptorSecond.fromJson(adaptorSecond.toJson(secondChild)))}")
    println("thirdAbstractChild ${adaptor.toJson(adaptor.fromJson(adaptor.toJson(thirdChild)))}")
    println("thirdAbstractChild adaptor ${adaptorThird.toJson(adaptorThird.fromJson(adaptorThird.toJson(thirdChild)))}")

    val baseItems = listOf(firstChild, secondChild, thirdChild)

    val type: ParameterizedType = Types.newParameterizedType(
        List::class.java, BaseTypeAbstract::class.java)
    val adaptorList: JsonAdapter<List<BaseTypeAbstract>> = moshi.adapter(type)

    println("List Children ${adaptorList.toJson(baseItems)}")

    // Try to get unknown entity triggered with Forth type
    val json = """
    [   
        {"type":"FirstType","strMsg":"Hi"},
        {"type":"SecondType","intMsg":1},
        {"type":"ForthType","strMsg":"JustOneEnum"}
    ]
    """.trimIndent()

    val withDefaulEntityTriggered = adaptorList.fromJson(json)
    println(withDefaulEntityTriggered)
    println()
}

private fun simpleBase() {
    val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    val adaptor: JsonAdapter<Base> = moshi.adapter(Base::class.java)
    val base = Base(1)
    println("Base ${adaptor.toJson(base)}")

    val simpleChild = SimpleChild("Hi")
    val adaptorSimple: JsonAdapter<SimpleChild> = moshi.adapter(SimpleChild::class.java)
    println("SimpleChild ${adaptor.toJson(simpleChild)}")
    println("SimpleChild Adaptor ${adaptorSimple.toJson(simpleChild)}")

    val adaptorDynamic: JsonAdapter<DynamicChild> = moshi.adapter(DynamicChild::class.java)
    val dynamicChild = DynamicChild("Hello", 2)
    println("DynamicChild ${adaptor.toJson(dynamicChild)}")
    println("DynamicChild Adaptor ${adaptorDynamic.toJson(dynamicChild)}")

    val baseItems = listOf(simpleChild, dynamicChild)

    val type: ParameterizedType = Types.newParameterizedType(List::class.java, Base::class.java)
    val adaptorList: JsonAdapter<List<Base>> = moshi.adapter(type)

    println("List Children ${adaptorList.toJson(baseItems)}")

    println()
}

private fun simpleAbstract() {
    val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    // The below will error
    // val adaptor: JsonAdapter<BaseAbstract> = moshi.adapter(BaseAbstract::class.java)
    val baseAbstract = object: BaseAbstract(1) {}

    // println("BaseAbstract ${adaptor.toJson(baseAbstract)}")

    val simpleAbstractChild = SimpleAbstractChild("Hi")
    val adaptorAbstractSimple: JsonAdapter<SimpleAbstractChild> = moshi.adapter(SimpleAbstractChild::class.java)

    // println("SimpleAbstractChild ${adaptor.toJson(simpleAbstractChild)}")
    println("SimpleAbstractChild Adaptor ${adaptorAbstractSimple.toJson(simpleAbstractChild)}")

    val adaptorAbstractDynamic: JsonAdapter<DynamicAbstractChild> = moshi.adapter(DynamicAbstractChild::class.java)
    val dynamicAbstractChild = DynamicAbstractChild("Hello", 2)

    // println("DynamicAbstractChild ${adaptor.toJson(dynamicAbstractChild)}")
    println("DynamicAbstractChild Adaptor ${adaptorAbstractDynamic.toJson(dynamicAbstractChild)}")

    println()
}

private fun simpleInterface() {
    val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    // The below will error
    // val adaptor: JsonAdapter<BaseInterface> = moshi.adapter(BaseInterface::class.java)
    // val baseInterface = object: BaseInterface { override val number: Int = 1 }

    // println("BaseInterface ${adaptor.toJson(baseInterface)}")

    val simpleInterfaceChild = SimpleInterfaceChild("Hi")
    val adaptorInterfaceSimpleChild: JsonAdapter<SimpleInterfaceChild> = moshi.adapter(SimpleInterfaceChild::class.java)

    // println("SimpleInterfaceChild ${adaptor.toJson(simpleInterfaceChild)}")
    println("SimpleAbstractChild Adaptor ${adaptorInterfaceSimpleChild.toJson(simpleInterfaceChild)}")

    val adaptorDynamicInterfaceChild: JsonAdapter<DynamicInterfaceChild> = moshi.adapter(DynamicInterfaceChild::class.java)
    val dynamicInterfaceChild = DynamicInterfaceChild("Hello", 2)

    // println("DynamicAbstractChild ${adaptor.toJson(dynamicInterfaceChild)}")
    println("DynamicAbstractChild Adaptor ${adaptorDynamicInterfaceChild.toJson(dynamicInterfaceChild)}")

    println()
}

private fun sealedBase() {
    val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    // val adaptor: JsonAdapter<BaseSealed> = moshi.adapter(BaseSealed::class.java)
    // val baseSealed = BaseSealed(1)
    // println("Base ${adaptor.toJson(baseSealed)}")

    val simpleSealedChild = SimpleSealedChild("Hi")
    val adaptorSealedSimple: JsonAdapter<SimpleSealedChild> = moshi.adapter(SimpleSealedChild::class.java)
    // println("SimpleSealedChild ${adaptor.toJson(simpleSealedChild)}")
    println("SimpleSealedChild Adaptor ${adaptorSealedSimple.toJson(simpleSealedChild)}")

    val adaptorSealedDynamic: JsonAdapter<DynamicSealedChild> = moshi.adapter(DynamicSealedChild::class.java)
    val dynamicSealedChild = DynamicSealedChild("Hello", 2)
    // println("DynamicSealedChild ${adaptor.toJson(dynamicSealedChild)}")
    println("DynamicChild Adaptor ${adaptorSealedDynamic.toJson(dynamicSealedChild)}")

    println()
}

private fun sealedInterfaceBase() {
    val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    // val adaptor: JsonAdapter<BaseSealedInterface> = moshi.adapter(BaseSealedInterface::class.java)
    // val baseInterfaceSealed = object: BaseSealedInterface { override val number: Int = 1 }
    // println("Base ${adaptor.toJson(baseInterfaceSealed)}")

    val simpleSealedInterfaceChild = SimpleSealedInterface("Hi")
    val adaptorSealedSimpleInterface: JsonAdapter<SimpleSealedInterface> = moshi.adapter(SimpleSealedInterface::class.java)
    // println("SimpleSealedInterfaceChild ${adaptor.toJson(simpleSealedInterfaceChild)}")
    println("SimpleSealedInterfaceChild Adaptor ${adaptorSealedSimpleInterface.toJson(simpleSealedInterfaceChild)}")

    val adaptorSealedInterfaceDynamic: JsonAdapter<DynamicSealedInterface> = moshi.adapter(DynamicSealedInterface::class.java)
    val dynamicSealedInterfaceChild = DynamicSealedInterface("Hello", 2)
    // println("DynamicSealedChild ${adaptor.toJson(dynamicSealedInterfaceChild)}")
    println("DynamicSealedChild Adaptor ${adaptorSealedInterfaceDynamic.toJson(dynamicSealedInterfaceChild)}")

    println()
}

private fun normalMoshiSerialization() {
    val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    val adaptor: JsonAdapter<Movie> = moshi.adapter(Movie::class.java)
    val movie = Movie(
        id = 1,
        title = "Hello",
        imagePath = "http://example.com",
        genre_ids = listOf(1, 2, 3),
        overview = "Nothing"
    )
    println(adaptor.toJson(adaptor.fromJson(adaptor.toJson(movie))))
}

private fun abstractMoshiSerialization() {
    val moshi = Moshi.Builder()
        .add(
            PolymorphicJsonAdapterFactory.of(ListItem::class.java, "type")
                .withSubtype(Text::class.java, Type.TEXT.name)
                .withSubtype(Button::class.java, Type.BUTTON.name)
                .withSubtype(Image::class.java, Type.IMAGE.name)
                .withSubtype(Row::class.java, Type.ROW.name)
                .withSubtype(Column::class.java, Type.COLUMN.name))
        .add(KotlinJsonAdapterFactory())
        .build()

    val payload = Payload(listOf(
        Column(listOf(Text("Testing"), Image())),
        Row(listOf(Text("Testing"), Button("Button"))),
        Column(
            listOf(
                Text("Testing"), Image(),
                Row(listOf(Image(), Text("Good"))
                )
            )
        ),
        Text("Good"),
        Button("")
    ))

    val adaptor: JsonAdapter<Payload> = moshi.adapter(Payload::class.java)
    println(adaptor.toJson(adaptor.fromJson(adaptor.toJson(payload))))
}

private fun singleAbstractMoshiSerialization() {
    val moshi = Moshi.Builder()
        .add(
            PolymorphicJsonAdapterFactory.of(ListItem::class.java, "type")
                .withSubtype(Text::class.java, Type.TEXT.name)
                .withSubtype(Button::class.java, Type.BUTTON.name)
                .withSubtype(Image::class.java, Type.IMAGE.name)
                .withSubtype(Row::class.java, Type.ROW.name)
                .withSubtype(Column::class.java, Type.COLUMN.name))
        .add(KotlinJsonAdapterFactory())
        .build()

    val payload = Text("Testing")

    val adapterText: JsonAdapter<Text> = moshi.adapter(Text::class.java)
    println(adapterText.toJson(adapterText.fromJson(adapterText.toJson(payload))))

    val adapterListItem: JsonAdapter<ListItem> = moshi.adapter(ListItem::class.java)
    println(adapterListItem.toJson(adapterListItem.fromJson(adapterListItem.toJson(payload))))
}
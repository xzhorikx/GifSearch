package alex.zhurkov.gifloader.common

fun <T : Any, R> T.getPrivateProperty(variableName: String): R? {
    return javaClass.getDeclaredField(variableName).let { field ->
        field.isAccessible = true
        return@let field.get(this) as? R
    }
}

fun <T : Any, R> T.setAndReturnPrivateProperty(variableName: String, data: R): R? {
    return javaClass.getDeclaredField(variableName).let { field ->
        field.isAccessible = true
        field.set(this, data)
        return@let field.get(this) as? R
    }
}
package ru.fav.petcare.data.providers

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.fav.petcare.domain.providers.ResourceProvider
import javax.inject.Inject

class AndroidResourceProvider @Inject constructor(
    @ApplicationContext private val context: Context
) : ResourceProvider {

    override fun getString(stringResId: Int): String {
        return context.getString(stringResId)
    }

    override fun getString(stringResId: Int, vararg args: Any): String {
        return context.getString(stringResId, *args)
    }
}
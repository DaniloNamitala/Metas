package dev.namitala.metas

import android.content.Context
import dev.namitala.metas.repository.GoalsRepository
import dev.namitala.metas.repository.GoalsRepositoryImpl
import dev.namitala.metas.viewmodel.GoalsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val goalsModule = module {
    single<GoalsRepository> { GoalsRepositoryImpl(androidContext().getSharedPreferences(androidContext().packageName, Context.MODE_PRIVATE)) }
    viewModel { GoalsViewModel(get())}
}
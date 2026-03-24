package org.delcom.module

import org.delcom.repositories.LayananRepository
import org.delcom.repositories.ILayananRepository
import org.delcom.services.LayananService
import org.delcom.services.ProfileService
import org.koin.dsl.module

val appModule = module {
    // Layanan Repository
    single<ILayananRepository> {
        LayananRepository()
    }

    // Layanan Service
    single {
        LayananService(get())
    }

    // Profile Service
    single {
        ProfileService()
    }
}

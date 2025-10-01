package com.xycz.simplelive.domain.usecase

import com.xycz.simplelive.domain.repository.LiveRepository
import com.xycz.simplelive.domain.repository.SiteModel
import javax.inject.Inject

/**
 * Use case for getting all available sites
 */
class GetAllSitesUseCase @Inject constructor(
    private val liveRepository: LiveRepository
) {
    suspend operator fun invoke(): List<SiteModel> {
        return liveRepository.getAllSites()
    }
}
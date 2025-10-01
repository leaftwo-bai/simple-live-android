package com.xycz.simplelive.domain.usecase

import com.xycz.simplelive.core.model.LiveCategoryResult
import com.xycz.simplelive.domain.repository.LiveRepository
import javax.inject.Inject

/**
 * Use case for getting recommended rooms
 */
class GetRecommendRoomsUseCase @Inject constructor(
    private val liveRepository: LiveRepository
) {
    suspend operator fun invoke(siteId: String, page: Int = 1): Result<LiveCategoryResult> {
        return liveRepository.getRecommendRooms(siteId, page)
    }
}
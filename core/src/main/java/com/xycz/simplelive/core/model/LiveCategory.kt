package com.xycz.simplelive.core.model

import kotlinx.serialization.Serializable

/**
 * Live category model
 * Represents a top-level category with subcategories
 */
@Serializable
data class LiveCategory(
    /** Category ID */
    val id: String,

    /** Category name */
    val name: String,

    /** List of subcategories */
    val children: List<LiveSubCategory>
)

/**
 * Live subcategory model
 * Represents a specific category that can be browsed
 */
@Serializable
data class LiveSubCategory(
    /** Subcategory ID */
    val id: String,

    /** Subcategory name */
    val name: String,

    /** Parent category ID */
    val parentId: String,

    /** Category icon/picture URL (optional) */
    val pic: String? = null
)
package com.example.myopeninapp.data.model.data


abstract class Link {
    abstract val times_ago: String
    abstract val title: String
    abstract val total_clicks: Int
}

data class TopLink(
    val url_id: Int,
    val web_link: String,
    val smart_link: String,
    override val title: String,
    override val total_clicks: Int,
    val original_image: String,
    val thumbnail: Int,
    override val times_ago: String,
    val created_at: String,
    val domain_id: String,
    val url_prefix: String,
    val url_suffix: String,
    val app: String,
    val is_favourite: Boolean
) : Link()


data class RecentLink(
    val url_id: Int,
    val web_link: String,
    val smart_link: String,
    override val title: String,
    override val total_clicks: Int,
    val original_image: String,
    val thumbnail: Int,
    override val times_ago: String,
    val created_at: String,
    val domain_id: String,
    val url_prefix: String,
    val url_suffix: String,
    val app: String,
    val is_favourite: Boolean
) : Link()
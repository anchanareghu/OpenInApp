package com.example.myopeninapp.com.example.myopeninapp.data.model.data


abstract class Link {
    abstract val times_ago: String
    abstract val title: String
    abstract val total_clicks: Int
}

data class TopLink(
    val app: String,
    val created_at: String,
    val domain_id: String,
    val original_image: String,
    val smart_link: String,
    val thumbnail: Any,
    override val times_ago: String,
    override val title: String,
    override val total_clicks: Int,
    val url_id: Int,
    val url_prefix: String,
    val url_suffix: String,
    val web_link: String
) : Link()


data class RecentLink(
    val app: String,
    val created_at: String,
    val domain_id: String,
    val original_image: String,
    val smart_link: String,
    val thumbnail: Any,
    override val times_ago: String,
    override val title: String,
    override val total_clicks: Int,
    val url_id: Int,
    val url_prefix: Any,
    val url_suffix: String,
    val web_link: String
) : Link()
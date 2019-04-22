package me.thanel.webmark.data

import androidx.core.net.toUri

const val TITLE_GUARDIANS =
    "Humor, Charm, and Creativity Save Guardians of the Galaxy from Stock Storytelling"
const val TITLE_BLACK_PANTHER = "Black Panther Makes the MCU a Deeper, Richer Place"
const val TITLE_CAPTAIN_MARVEL =
    "Captain Marvel Is a Throwback to the Earliest MCU Films, Even as It Breaks New Ground"
const val TITLE_ENDGAME_PRESELLS =
    "'Avengers: Endgame' Continues To Outpace 'Avengers: Infinity War's Presells"
const val LINK_GUARDIANS = "http://www.theandrewblog.net/2017/05/03/guardians-of-the-galaxy-2014/"

fun WebmarkQueries.insertSampleData() {
    insert(
        id = 1,
        url = LINK_GUARDIANS,
        title = TITLE_GUARDIANS,
        imageUrl = "http://www.theandrewblog.net/wp-content/uploads/2017/05/gotg1-1024x682.jpg",
        readingTime = 12
    )
    insert(
        id = 2,
        url = "http://www.theandrewblog.net/2018/02/19/black-panther/",
        title = TITLE_BLACK_PANTHER,
        imageUrl = "http://www.theandrewblog.net/wp-content/uploads/2018/02/bp1-1024x583.jpg"
    )
    insert(
        id = 3,
        url = "http://www.theandrewblog.net/2019/03/11/captain-marvel-is-a-throwback-to-the-earliest-mcu-films-even-as-it-breaks-new-ground/",
        title = TITLE_CAPTAIN_MARVEL,
        imageUrl = "http://www.theandrewblog.net/wp-content/uploads/2019/03/cm1.jpg",
        readingTime = 360
    )

    insert(
        id = 4,
        url = "https://mcuexchange.com/avengers-endgame-presells-infinity-war/",
        title = TITLE_ENDGAME_PRESELLS,
        archived = true
    )
}

private fun WebmarkQueries.insert(
    id: Long,
    url: String,
    title: String,
    imageUrl: String? = null,
    readingTime: Int = 0,
    archived: Boolean = false
) {
    insert(id, url.toUri())
    updateById(title, null, readingTime, imageUrl?.toUri(), id)
    if (archived) {
        markAsReadById(id)
    }
}

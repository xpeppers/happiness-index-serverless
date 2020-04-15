package happiness.addvote

import happiness.getvotes.Vote

interface Votes {
    fun add(vote: String)
    fun all(): List<Vote>
}
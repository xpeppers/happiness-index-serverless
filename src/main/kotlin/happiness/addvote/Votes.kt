package happiness.addvote

interface Votes {
    fun add(vote: UserVote)
    fun all(): List<UserVote>
}
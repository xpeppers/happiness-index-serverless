package happiness

interface Votes {
    fun add(vote: UserVote)
    fun all(): List<UserVote>
}
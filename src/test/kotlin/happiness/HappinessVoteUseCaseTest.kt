package happiness

import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class HappinessVoteUseCaseTest {

    private val textAppender = mockk<TextAppender>(relaxed = true)
    private val voteUseCase = HappinessVoteUseCase(textAppender)

    @Test
    internal fun `useCase call properly the textAppender`() {
        voteUseCase.execute(1)
        
        verify { textAppender.append("1") }
        confirmVerified(textAppender)
    }
}
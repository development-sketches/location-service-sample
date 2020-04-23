package ca.softwareadd.domain.resolvers

import ca.softwareadd.domain.commands.Command
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [
    CommandTypeResolver::class
])
internal class CommandTypeResolverTest {

    @Autowired
    private lateinit var resolver: CommandTypeResolver

    @Test
    internal fun `command type is derived from the class name with command suffix`() {
        assertEquals(
                "simple",
                resolver.commandType(SimpleCommand::class)
        )
    }

    @Test
    internal fun `command type derived from the class name without command suffix`() {
        assertEquals(
                "simple-pojo",
                resolver.commandType(SimplePojo::class)
        )
    }

    @Test
    internal fun `command type is taken from the annotation`() {
        assertEquals(
                "annotated-command",
                resolver.commandType(AnnotatedCommand::class)
        )
    }

    @Test
    internal fun `command type derived from the annotated class`() {
        assertEquals(
                "annotated-no-type",
                resolver.commandType(AnnotatedNoTypeCommand::class)
        )
    }

    class SimpleCommand

    class SimplePojo

    @Command("annotated-command")
    class AnnotatedCommand

    @Command
    class AnnotatedNoTypeCommand

}

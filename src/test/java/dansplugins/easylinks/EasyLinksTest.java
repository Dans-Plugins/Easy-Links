package dansplugins.easylinks;

import org.junit.jupiter.api.Test;
import preponderous.ponder.minecraft.bukkit.services.CommandService;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Guards the plugin class against holding a command service of its own.
 *
 * Ponder already owns one, reachable through {@code getPonder().getCommandService()}, and that is
 * the instance {@code initializeCommandService()} hands the plugin's commands to. A second instance
 * constructed on the plugin is never initialized, so dispatching through it fails for every
 * subcommand. The check is structural because neither the plugin nor Ponder's command service can
 * be constructed outside a running server: {@code JavaPlugin} requires a plugin class loader and
 * {@code CommandService}'s constructor dereferences the plugin it is given.
 */
class EasyLinksTest {
    @Test
    void doesNotHoldACommandServiceBesideTheOnePonderOwns() {
        List<String> commandServiceFields = new ArrayList<>();
        for (Field field : EasyLinks.class.getDeclaredFields()) {
            if (CommandService.class.isAssignableFrom(field.getType())) {
                commandServiceFields.add(field.getName());
            }
        }

        assertTrue(commandServiceFields.isEmpty(),
                "EasyLinks holds its own CommandService, which initializeCommandService() never initializes; "
                        + "dispatch through getPonder().getCommandService() instead: " + commandServiceFields);
    }
}

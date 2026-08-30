package dansplugins.easylinks.commands;

import org.bukkit.command.CommandSender;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A stand-in for Bukkit's {@link CommandSender} that records the messages sent to it and answers
 * permission checks from a set of nodes the test has granted.
 *
 * A dynamic proxy is used rather than a hand-written implementation because {@link CommandSender}
 * inherits dozens of permission and server methods that these tests never exercise.
 */
class FakeCommandSender {
    private final List<String> messages = new ArrayList<>();
    private final Set<String> grantedPermissions = new HashSet<>();
    private final CommandSender commandSender;

    FakeCommandSender() {
        commandSender = (CommandSender) Proxy.newProxyInstance(
                CommandSender.class.getClassLoader(),
                new Class<?>[]{CommandSender.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("sendMessage") && args != null && args[0] instanceof String) {
                        messages.add((String) args[0]);
                    }
                    if (method.getName().equals("hasPermission") && args != null && args[0] instanceof String) {
                        return grantedPermissions.contains(args[0]);
                    }
                    return defaultValueFor(method.getReturnType());
                });
    }

    CommandSender asCommandSender() {
        return commandSender;
    }

    void grantPermission(String permission) {
        grantedPermissions.add(permission);
    }

    List<String> getMessages() {
        return messages;
    }

    private static Object defaultValueFor(Class<?> returnType) {
        if (returnType == boolean.class) {
            return false;
        }
        if (returnType == int.class) {
            return 0;
        }
        return null;
    }
}

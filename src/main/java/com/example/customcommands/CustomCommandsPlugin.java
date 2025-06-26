package com.example.customcommands;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;

import java.util.HashMap;
import java.util.Map;

public class CustomCommandsPlugin extends PluginBase {

    private Map<String, CustomCommand> customCommands;

    @Override
    public void onEnable() {
        // Сохраняем дефолтный конфиг
        saveDefaultConfig();
        // Загружаем команды из конфига
        loadCommands();
        // Регистрируем команды
        registerCommands();
        getLogger().info(TextFormat.GREEN + "CustomCommandsPlugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info(TextFormat.RED + "CustomCommandsPlugin disabled!");
    }

    private void loadCommands() {
        customCommands = new HashMap<>();
        Config config = getConfig();
        // Получаем секцию commands как Map<String, Object>
        Map<String, Object> commands = config.get("commands", new HashMap<>());

        for (Map.Entry<String, Object> entry : commands.entrySet()) {
            String name = entry.getKey();
            Object data = entry.getValue();

            // Проверка, что data является Map
            if (!(data instanceof Map)) {
                getLogger().warning("Invalid data for command: " + name + ". Skipping.");
                continue;
            }

            // Приводим Object к Map<String, Object>
            @SuppressWarnings("unchecked")
            Map<String, Object> commandData = (Map<String, Object>) data;

            // Проверка имени команды
            if (name == null || name.trim().isEmpty() || !name.matches("^[a-zA-Z0-9_]+$")) {
                getLogger().warning("Invalid command name: " + name + ". Skipping.");
                continue;
            }

            // Безопасное получение значений с дефолтами
            String command = (String) commandData.getOrDefault("command", "");
            String description = (String) commandData.getOrDefault("description", "Custom command");
            String permission = (String) commandData.getOrDefault("permission", "customcommands." + name);
            String successMessage = (String) commandData.getOrDefault("success_message", "Command executed!");
            String errorMessage = (String) commandData.getOrDefault("error_message", "Error executing command.");

            // Проверка, что команда не пустая
            if (command.trim().isEmpty()) {
                getLogger().warning("No server command specified for " + name + ". Skipping.");
                continue;
            }

            customCommands.put(name, new CustomCommand(name, command, description, permission, successMessage, errorMessage));
        }

        getLogger().info("Loaded " + customCommands.size() + " custom commands.");
    }

    private void registerCommands() {
        for (CustomCommand cmd : customCommands.values()) {
            Command command = new Command(cmd.getName(), cmd.getDescription(), "", new String[]{}) {
                @Override
                public boolean execute(CommandSender sender, String commandLabel, String[] args) {
                    // Проверка прав
                    if (!sender.hasPermission(cmd.getPermission())) {
                        sender.sendMessage(TextFormat.RED + cmd.getErrorMessage());
                        return false;
                    }

                    // Формируем серверную команду, заменяя {player} и другие placeholders
                    String serverCommand = cmd.getCommand()
                        .replace("{player}", sender.getName())
                        .replace("{args}", String.join(" ", args));

                    try {
                        // Выполняем серверную команду
                        boolean success = getServer().dispatchCommand(sender, serverCommand);
                        if (success) {
                            sender.sendMessage(TextFormat.GREEN + cmd.getSuccessMessage());
                        } else {
                            sender.sendMessage(TextFormat.RED + cmd.getErrorMessage());
                        }
                        return success;
                    } catch (Exception e) {
                        getLogger().warning("Error executing command " + cmd.getName() + ": " + e.getMessage());
                        sender.sendMessage(TextFormat.RED + cmd.getErrorMessage());
                        return false;
                    }
                }
            };

            // Регистрируем команду с префиксом "customcommands"
            getServer(). getCommandMap().register("customcommands", command);
        }
    }

    private static class CustomCommand {
        private final String name, command, description, permission, successMessage, errorMessage;

        public CustomCommand(String name, String command, String description, String permission, String successMessage, String errorMessage) {
            this.name = name;
            this.command = command;
            this.description = description != null ? description : "Custom command";
            this.permission = permission != null ? permission : "customcommands." + name;
            this.successMessage = successMessage != null ? successMessage : "Command executed!";
            this.errorMessage = errorMessage != null ? errorMessage : "Error executing command.";
        }

        public String getName() {
            return name;
        }

        public String getCommand() {
            return command;
        }

        public String getDescription() {
            return description;
        }

        public String getPermission() {
            return permission;
        }

        public String getSuccessMessage() {
            return successMessage;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }
}

# CustomCommands
📣 Плагин CustomCommands

На ядро(а): 
Nukkit, nukkit-MOT, и другие форки.

Зависимости: ☺️ отсутствуют 

📄 Описание : 
Хотели ли бы сократить команду с плагина? Или же добавить алиасы для команды /ban? С этим плагином такое легко возможно!

❓ Использование  : 
config.yml

commands:
  hp: \\ основная команда которая будет отображаться в подсказках /
    command: "heal {player}" \\ команда которая будет выполняться
    description: "Вылечить другого игрока" \\ описание команды
    permission: "customcommands.hp" \\ права на использование
    success_message: "Вы были исцелены!" \\ сообщение после успешно выполненной команды
    error_message: "У вас нет права на эту команду" \\ выводимый текст при не выполненной команде
  creative:
    command: "gamemode creative {player}"
    description: "Sets creative mode"
    permission: "customcommands.creative"
    success_message: "Creative mode enabled!"
    error_message: "Error: Unable to set creative mode."
  sayhello:
    command: "say Hello, {args}!"
    description: "Says hello with custom arguments"
    permission: "customcommands.sayhello"
    success_message: "Message sent!"
    error_message: "Error: Failed to send message."

✅Разрешения : 
настраиваете сами в config.yml

⬇️Установка:
1.  Скачайте последнюю версию CustomCommands.jar 
2.  Поместите файл в папку /plugins/ вашего сервера 
3.  Перезагрузите сервер 
4. настройте файл 
config.yml

Телеграм канал где данный плагин : @MCPE_plugin_studio

package sg.edu.nus.iss.PTWServer.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import sg.edu.nus.iss.PTWServer.Repository.RedisRepository;
import sg.edu.nus.iss.PTWServer.Repository.TelegramRepository;

import org.telegram.telegrambots.meta.api.objects.Message;

@Service
public class PTWBot extends TelegramLongPollingBot {

    @Autowired
    RedisRepository redisRepo;
    @Autowired
    TelegramRepository teleRepo;

    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new PTWBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // Enum to define different states of the conversation flow
    private enum BotState {
        START, ASK_EMAIL, ASK_PASSWORD, ASK_TYPE, ASK_EQUIPMENT, ASK_LOCATION, ASK_COMMENTS, ASK_DATETIME, ASK_ENDDATETIME
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();
            String text = update.getMessage().getText();

            String currentState = redisRepo.retrieveState(chatId);
            boolean isCreatingPTW = false; // Flag to indicate PTW creation is ongoing

            // Initialize currentState as START if it's null
            if (currentState == null) {
                currentState = BotState.START.name();
            }

            if (text.equals("/create")) {
                // Start the PTW creation process
                isCreatingPTW = true;
                redisRepo.saveState(chatId, BotState.ASK_EMAIL.name()); // Ask for email first
                sendMessage(chatId, "Please enter your email:");
            } else if (text.equals("/clear")) {
                // Clear all Redis data for the current chatId
                currentState = BotState.START.name();
                isCreatingPTW = false;
                redisRepo.clearData(chatId);
                sendMessage(chatId, "All PTW data has been cleared.");
            } else if (isCreatingPTW = true) {
                // sendMessage(chatId, "received");
            } else {
                // If the received message is not a command,
                SendMessage echoMessage = new SendMessage();
                echoMessage.setChatId(chatId);
                echoMessage.setText("type /create to start permit creation or /submit once you are done");
                try {
                    execute(echoMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            if (isCreatingPTW && currentState != BotState.START.name()) {
                // The message is part of the PTW creation process
                switch (BotState.valueOf(currentState)) {
                    case ASK_EMAIL:
                        // Save the "email" and move to the ASK_PASSWORD state
                        redisRepo.saveEmail(chatId, text);
                        redisRepo.saveState(chatId, BotState.ASK_PASSWORD.name());
                        sendMessage(chatId, "Please enter your password:");
                        break;
                    case ASK_PASSWORD:
                        // Save the chatId in the Redis database and ask for "type"
                        redisRepo.savePassword(chatId, text);
                        redisRepo.saveState(chatId, BotState.ASK_TYPE.name());
                        sendMessage(chatId, "Please enter the PTW type:");
                        break;
                    case ASK_TYPE:
                        // Save the "type" and move to the ASK_EQUIPMENT state
                        redisRepo.saveType(chatId, text);
                        redisRepo.saveState(chatId, BotState.ASK_EQUIPMENT.name());
                        sendMessage(chatId, "Please enter the equipment information:");
                        break;
                    case ASK_EQUIPMENT:
                        // Save the "equipment" and move to the ASK_LOCATION state
                        redisRepo.saveEquipment(chatId, text);
                        System.out.print(text);

                        redisRepo.saveState(chatId, BotState.ASK_LOCATION.name());
                        sendMessage(chatId, "Please enter the location information:");
                        break;
                    case ASK_LOCATION:
                        // Save the "location" and move to the ASK_COMMENTS state
                        redisRepo.saveLocation(chatId, text);
                        System.out.print(text);
                        redisRepo.saveState(chatId, BotState.ASK_COMMENTS.name());
                        sendMessage(chatId, "Please enter any additional comments:");
                        break;
                    case ASK_COMMENTS:
                        // Save the "comments" and complete the conversation
                        redisRepo.saveComments(chatId, text);
                        redisRepo.saveState(chatId, BotState.ASK_DATETIME.name()); // New state for asking datetime
                        sendMessage(chatId, "Please enter the start date and time (yyyy-MM-dd'T'HH:mm):");
                        break;
                    case ASK_DATETIME:
                        // Save the "start date and time" and move to the ASK_ENDDATETIME state
                        redisRepo.saveStartDateTime(chatId, text);
                        redisRepo.saveState(chatId, BotState.ASK_ENDDATETIME.name());
                        sendMessage(chatId, "Please enter the end date and time (yyyy-MM-dd'T'HH:mm):");
                        break;
                    case ASK_ENDDATETIME:
                        // Save the "end date and time" and complete the conversation
                        redisRepo.saveEndDateTime(chatId, text);
                        redisRepo.saveState(chatId, BotState.START.name()); // Move back to START state for next PTW
                        sendMessage(chatId,"Thank you for providing all the information! Your PTW request is now complete. /submit to submit request ");
                        isCreatingPTW = false; // PTW creation completed, reset the flag

                        String type = redisRepo.retrieveType(chatId);
                        String equipment = redisRepo.retrieveEquipment(chatId);
                        String location = redisRepo.retrieveLocation(chatId);
                        String comments = redisRepo.retrieveComments(chatId);
                        String startDateTime = redisRepo.retrieveStartDateTime(chatId);
                        String endDateTime = redisRepo.retrieveEndDateTime(chatId);
                        String email = redisRepo.retrieveEmail(chatId);
                        String password = redisRepo.retrievePassword(chatId);
                        
                        StringBuilder messageText = new StringBuilder("PTW Request Information:\n");
                        messageText.append("Type: ").append(type).append("\n");
                        messageText.append("Equipment: ").append(equipment).append("\n");
                        messageText.append("Location: ").append(location).append("\n");
                        messageText.append("Start Date-Time: ").append(startDateTime).append("\n");
                        messageText.append("End Date-Time: ").append(endDateTime).append("\n");
                        messageText.append("Comments: ").append(comments).append("\n");
                        sendMessage(chatId, messageText.toString());
                        
                        String reply = teleRepo.requestFromTele(type, location, equipment, comments, email, startDateTime, endDateTime, password);
                        sendMessage(chatId, reply);
                        break;
                    default:
                        // default: do not send any msg
                        SendMessage echoMessage = new SendMessage();
                        echoMessage.setChatId(chatId);
                        echoMessage.setText("");
                        try {
                            execute(echoMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "PTW_noti_bot";
    }

    @Override
    public String getBotToken() {
        return "6456602724:AAEcIZlTYa60sHca7cXnD2RMx6Mlk-rM7Rg";
    }

    private void sendMessage(String chatId, String messageText) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(messageText);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
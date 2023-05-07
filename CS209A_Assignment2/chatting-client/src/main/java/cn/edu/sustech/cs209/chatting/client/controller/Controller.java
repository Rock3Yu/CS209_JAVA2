package cn.edu.sustech.cs209.chatting.client.controller;

import cn.edu.sustech.cs209.chatting.common.Message;
import com.vdurmont.emoji.EmojiParser;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

public class Controller implements Initializable {

    @FXML
    TextArea inputArea;
    @FXML
    ListView<String> chatList;
    @FXML
    Label currentUsername;
    @FXML
    Label currentOnlineCnt;
    @FXML
    Font x3;
    @FXML
    Color x4;
    @FXML
    ListView<Message> chatContentList;

    String username;
    String to = "null";
    EventController eventController;
    Alert alert;
    static boolean crash = false;
    static int cnt = 0;

    public Controller() throws Exception {
        eventController = new EventController();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // 提示框
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("提示");
        alert.setHeaderText(null);

        // username输入框
        String normalText = "username:";
        String invalidText = "Invalid username, Please enter a different username:";
        Dialog<String> dialog = new TextInputDialog();
        dialog.setTitle("Login");
        dialog.setHeaderText(null);
        dialog.setContentText(normalText);

        Optional<String> input = dialog.showAndWait();
        // login
        while (true) {
            if (!input.isPresent() || input.get().isEmpty()) {
                System.out.println("Username not present or is empty.");
                dialog.setContentText(invalidText);
                input = dialog.showAndWait();
                continue;
            }
            try {
                if (!eventController.validName(input.get())) {
                    System.out.println("Invalid username " + input.get() + ", exiting");
                    dialog.setContentText(invalidText);
                    input = dialog.showAndWait();
                } else {
                    break;
                }
            } catch (Exception e) {
                System.out.println("判断username出现问题");
            }
        }

        username = input.get();
        currentUsername.setText("Current User: " + username);

        // infinite loop to refresh
        Timeline animation = new Timeline(new KeyFrame(Duration.millis(1500), e1 -> {
            try {
                try {
                    if (cnt % 20 == 0) {
                        eventController.socket.sendUrgentData(0xFF);
                    }
                    cnt++;
                } catch (IOException ex) {
                    System.err.println("server出现错误");
                    alert.setContentText("server出现错误，请您退出。请您放心聊天记录不会消失。");
                    if (!crash) {
                        alert.show();
                        crash = true;
                        return;
                    }
                    return;
                }
                System.out.println(username + " require refresh");
                String[] onlineUsernamesWithState = eventController.getOnlineUsernamesWithState();
                // online num
                int cntOnline = onlineUsernamesWithState.length;
                currentOnlineCnt.setText("Online: " + cntOnline);
                // chat list: user part
                onlineUsernamesWithState = Arrays.stream(onlineUsernamesWithState)
                        .filter(e -> !e.equals(username)).toArray(String[]::new);
                ObservableList<String> strList =
                        FXCollections.observableArrayList(onlineUsernamesWithState);
                // chat list: group part
                List<String> enrolledGroupNames = eventController.getEnrolledGroups();
                strList.addAll(enrolledGroupNames);
                chatList.setItems(strList);
                if (chatList.getSelectionModel().selectedItemProperty().getValue() != null) {
                    to = chatList.getSelectionModel().selectedItemProperty().getValue();
                }
                // chat history content
                List<Message> msgs;
                to = to.replace(" *", "");
                if (!to.toLowerCase().contains("group")) {
                    msgs = eventController.getPrivateChatHistory(username, to);
                } else {
                    msgs = eventController.getGroupChatHistory(to);
                }
                msgs.forEach(e -> e.setData(EmojiParser.parseToUnicode(e.getData())));
                ObservableList<Message> messageObservableList
                        = FXCollections.observableArrayList(msgs);
                chatContentList.setCellFactory(new MessageCellFactory()); // new and clear history
                chatContentList.setItems(messageObservableList);
            } catch (IOException e) {
                System.err.println("更新出现错误。");
            }
        }));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();

        // 打开聊天框框
        chatContentList.setCellFactory(new MessageCellFactory());


    }

    @FXML
    public void createPrivateChat() throws IOException {
        AtomicReference<String> user = new AtomicReference<>();

        Stage stage = new Stage();
        ComboBox<String> userSel = new ComboBox<>();

        // note: get the user list from server, the current user's name should be filtered out
        String[] onlineUsernames = eventController.getOnlineUsernames();
        userSel.getItems().addAll(onlineUsernames);
        userSel.getItems().remove(username);

        Button okBtn = new Button("OK");
        okBtn.setOnAction(e -> {
            user.set(userSel.getSelectionModel().getSelectedItem());
            to = user.toString();
            stage.close();
        });

        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(20, 20, 20, 20));
        box.getChildren().addAll(userSel, okBtn);
        stage.setScene(new Scene(box));
        stage.showAndWait();
    }

    /**
     * A new dialog should contain a multi-select list, showing all user's name. You can select
     * several users that will be joined in the group chat, including yourself.
     * <p>
     * The naming rule for group chats is similar to WeChat: If there are > 3 users: display the
     * first three usernames, sorted in lexicographic order, then use ellipsis with the number of
     * users, for example: UserA, UserB, UserC... (10) If there are <= 3 users: do not display the
     * ellipsis, for example: UserA, UserB (2)
     */
    @FXML
    public void createGroupChat() throws IOException {
        AtomicReference<String> groupMembers = new AtomicReference<>();

        Stage stage = new Stage();
        VBox vBox = new VBox();

        String[] onlineUsernames = eventController.getOnlineUsernames();
        Arrays.stream(onlineUsernames).forEach(txt -> {
            vBox.getChildren().add(new CheckBox(txt));
        });

        Button okBtn = new Button("OK");
        okBtn.setOnAction(e -> {
            List<String> l1 = new ArrayList<>();
            for (Node n : vBox.getChildren()) {
                CheckBox cb = (CheckBox) n;
                if (cb.isSelected()) {
                    l1.add(cb.getText());
                }
            }
            if (l1.size() < 3) {
                alert.setContentText("群聊人数需要 >= 3，请您重试");
                alert.showAndWait();
                stage.close();
                return;
            }
            groupMembers.set(String.join(",", l1));
            try {
                String groupName = eventController.createGroupChat(groupMembers.toString());
                if (!groupName.equals("false")) {
                    to = groupName;
                } else {
                    System.err.println("client: createGroupChat() failed, server error.");
                }
            } catch (IOException ex) {
                System.err.println("client: createGroupChat() failed, network error or else.");
                alert.setContentText("创建失败，发生网络或其他错误");
                alert.showAndWait();
            }
            stage.close();
        });

        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(20, 20, 20, 20));
        box.getChildren().addAll(vBox, okBtn);
        stage.setScene(new Scene(box));
        stage.showAndWait();
    }

    /**
     * Sends the message to the <b>currently selected</b> chat.
     * <p>
     * Blank messages are not allowed. After sending the message, you should clear the text input
     * field.
     */
    @FXML
    public void doSendMessage() {
        String content = inputArea.getText().trim();
        inputArea.clear();
        if ("".equals(content)) {
            alert.setContentText("请输入内容");
            alert.showAndWait();
            return;
        }
        try {
            if (eventController.sendMsg(username, to, content)) {
                System.out.println(username + " send msg ok, content: " + content);
            } else {
                System.err.println("Please select an online user");
                alert.setContentText("请选择私聊对象");
                alert.showAndWait();
            }
        } catch (IOException ignored) {
            System.err.println("doSendMessage() has some error");
        }
    }

    @FXML
    public void doSendFile() {

    }

    /**
     * You may change the cell factory if you changed the design of {@code Message} model. Hint: you
     * may also define a cell factory for the chats displayed in the left panel, or simply override
     * the toString method.
     */
    private class MessageCellFactory implements Callback<ListView<Message>, ListCell<Message>> {

        @Override
        public ListCell<Message> call(ListView<Message> param) {
            return new ListCell<Message>() {

                @Override
                public void updateItem(Message msg, boolean empty) {
                    super.updateItem(msg, empty);
                    if (empty || Objects.isNull(msg)) {
                        return;
                    }

                    HBox wrapper = new HBox();
                    Label nameLabel = new Label(msg.getSentBy());
                    Label msgLabel = new Label(msg.getData());

                    nameLabel.setPrefSize(50, 20);
                    nameLabel.setStyle("-fx-border-color: black; -fx-border-width: 1px;");
                    nameLabel.setWrapText(true);
                    msgLabel.setMaxWidth(300);
                    msgLabel.setWrapText(true);

                    if (username.equals(msg.getSentBy())) {
                        wrapper.setAlignment(Pos.TOP_RIGHT);
                        wrapper.getChildren().addAll(msgLabel, nameLabel);
                        msgLabel.setPadding(new Insets(0, 20, 0, 0));
                    } else {
                        wrapper.setAlignment(Pos.TOP_LEFT);
                        wrapper.getChildren().addAll(nameLabel, msgLabel);
                        msgLabel.setPadding(new Insets(0, 0, 0, 20));
                    }

                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                    setGraphic(wrapper);
                }
            };
        }
    }
}

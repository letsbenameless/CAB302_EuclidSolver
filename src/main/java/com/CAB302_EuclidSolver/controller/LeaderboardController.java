package com.CAB302_EuclidSolver.controller;

import com.CAB302_EuclidSolver.model.user.User;
import com.CAB302_EuclidSolver.model.database.UserDAO;
import com.CAB302_EuclidSolver.model.user.UserSession;
import com.CAB302_EuclidSolver.util.LoadScene;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class LeaderboardController {

    @FXML
    private TableView<User> leaderboardTable;
    @FXML
    private TableColumn<User, String> positionColumn;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, Integer> totalXPColumn;
    @FXML
    private TableColumn<User, Integer> totalQuestionsColumn;
    @FXML
    private TableColumn<User, Integer> hardQuestionsColumn;
    @FXML
    private TableColumn<User, Integer> clockQuestionsColumn;

    private final UserDAO userDAO = new UserDAO();

    // Example: replace with your actual logged-in user instance
    private Optional<User> currentUserOptional = userDAO.getUserByUsername(UserSession.getInstance().getUsername());

    private User currentUser = currentUserOptional.orElse(null);

    @FXML
    public void initialize() {
        setupColumns();
        loadLeaderboardData();
    }

    private void setupColumns() {
        usernameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        totalXPColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getUserXP()).asObject());
        totalQuestionsColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTotalQuestionsAnswered()).asObject());
        hardQuestionsColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTotalHardQuestionsAnswered()).asObject());
        clockQuestionsColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTotalClockQuestionsAnswered()).asObject());

    }

    private void loadLeaderboardData() {
        List<User> users = userDAO.getAll();

        users.sort(Comparator.comparingInt(User::getUserXP).reversed()
                .thenComparing(User::getUsername, String.CASE_INSENSITIVE_ORDER));

        ObservableList<User> observableUsers = FXCollections.observableArrayList(users);
        leaderboardTable.setItems(observableUsers);

        positionColumn.setCellFactory(createPositionCellFactory());
        highlightCurrentUserRow();
    }

    private Callback<TableColumn<User, String>, TableCell<User, String>> createPositionCellFactory() {
        return column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getIndex() >= leaderboardTable.getItems().size()) {
                    setText(null);
                } else {
                    int index = getIndex();
                    switch (index) {
                        case 0 -> setText("🥇");
                        case 1 -> setText("🥈");
                        case 2 -> setText("🥉");
                        default -> setText(String.valueOf(index + 1));
                    }
                }
            }
        };
    }

    private void highlightCurrentUserRow() {
        leaderboardTable.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (user == null || empty) {
                    setStyle("");
                } else if (currentUser != null && user.getUsername().equals(currentUser.getUsername())) {
                    getStyleClass().add("current-user-row");
                } else {
                    getStyleClass().remove("current-user-row");
                }
            }
        });
    }


    @FXML
    private void handleMainPage() throws IOException {
        LoadScene.getInstance().render("scenes/main/main-scene.fxml", "scenes/main/main-styles.css");
    }

    @FXML
    private void handleSignOut() throws IOException {
        System.out.println("Signing out");

        LoadScene.getInstance().render("scenes/signin/signin-scene.fxml", "scenes/signin/signin-styles.css");
        UserSession.getInstance().logout();

    }
}
